package pl.edu.wat.wcy.pz.project.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.wcy.pz.project.server.configuration.security.jwt.JwtProvider;
import pl.edu.wat.wcy.pz.project.server.dto.EmailDTO;
import pl.edu.wat.wcy.pz.project.server.dto.LoginForm;
import pl.edu.wat.wcy.pz.project.server.dto.SignUpForm;
import pl.edu.wat.wcy.pz.project.server.dto.response.JwtResponse;
import pl.edu.wat.wcy.pz.project.server.entity.EmailVerificationToken;
import pl.edu.wat.wcy.pz.project.server.entity.Role;
import pl.edu.wat.wcy.pz.project.server.entity.User;
import pl.edu.wat.wcy.pz.project.server.entity.enumeration.RoleName;
import pl.edu.wat.wcy.pz.project.server.rabbit.RabbitProducer;
import pl.edu.wat.wcy.pz.project.server.repository.EmailVerificationTokenRepository;
import pl.edu.wat.wcy.pz.project.server.repository.RoleRepository;
import pl.edu.wat.wcy.pz.project.server.repository.UserRepository;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    private PasswordEncoder encoder;
    private JwtProvider jwtProvider;

    private RabbitProducer rabbitProducer;

    private SimpUserRegistry userRegistry;

    @Value("${email.verification.url}")
    private String verificationUrl;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, EmailVerificationTokenRepository emailVerificationTokenRepository, PasswordEncoder encoder, JwtProvider jwtProvider, RabbitProducer rabbitProducer, SimpUserRegistry userRegistry) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
        this.encoder = encoder;
        this.jwtProvider = jwtProvider;
        this.rabbitProducer = rabbitProducer;
        this.userRegistry = userRegistry;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginForm) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword()));
        LOGGER.trace("User: " + loginForm.getUsername() + " is trying to log in.");
        if (userRegistry.getUser(loginForm.getUsername()) != null) {
            LOGGER.warn("Logged user " + loginForm.getUsername() + " was trying to log in second time");
            throw new RuntimeException("User is already logged!");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Optional<User> userOptional = userRepository.findByUsername(userDetails.getUsername());
        User user = userOptional.get();
        user.setLastLogonDate(Calendar.getInstance().getTime());
        userRepository.save(user);

        LOGGER.info("Logged user: " + userDetails.getUsername() + ". Authorities: " + userDetails.getAuthorities().toString());

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@Valid @RequestBody SignUpForm signUpForm) {
        LOGGER.trace("New user is trying to sign up");
        if (userRepository.existsByUsername(signUpForm.getUsername())) {
            LOGGER.trace("User with this name exist: " + signUpForm.getUsername());
            return new ResponseEntity<>("User with this name already exist", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signUpForm.getEmail())) {
            LOGGER.trace("User with this email exist: " + signUpForm.getEmail());
            return new ResponseEntity<>("User with this e-mail already exist", HttpStatus.BAD_REQUEST);
        }

        Set<Role> roles = new HashSet<>();
        signUpForm.getRole().forEach(roleName -> {
            if (roleName.equalsIgnoreCase("ADMIN")) {
                Role adminRole = roleRepository.findByRoleName(RoleName.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Role not found: ADMIN"));
                roles.add(adminRole);
            }
            if (roleName.equalsIgnoreCase("USER")) {
                Role userRole = roleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new RuntimeException("Role not found: USER"));
                roles.add(userRole);
            }
        });

        User user = User.builder()
                .username(signUpForm.getUsername())
                .email(signUpForm.getEmail())
                .isEmailVerified("N")
                .password(encoder.encode(signUpForm.getPassword()))
                .roles(roles)
                .registrationDate(Calendar.getInstance().getTime())
                .build();

        userRepository.save(user);

        EmailVerificationToken emailVerificationToken = new EmailVerificationToken(user);
        emailVerificationTokenRepository.save(emailVerificationToken);
        System.out.println(emailVerificationToken.getToken());

        EmailDTO emailDTO = EmailDTO.builder()
                .emailAddress(user.getEmail())
                .username(user.getUsername())
                .url(verificationUrl + emailVerificationToken.getToken())
                .emailType(EmailDTO.EmailType.VERIFICATION_EMAIL)
                .build();

        rabbitProducer.sendToQueue(emailDTO);

        LOGGER.info("User created" + user.getUsername());
        return new ResponseEntity<>("User created!", HttpStatus.CREATED);
    }
}
