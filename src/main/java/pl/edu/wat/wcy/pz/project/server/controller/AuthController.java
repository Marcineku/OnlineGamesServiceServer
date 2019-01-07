package pl.edu.wat.wcy.pz.project.server.controller;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.wcy.pz.project.server.configuration.security.jwt.JwtProvider;
import pl.edu.wat.wcy.pz.project.server.entity.Role;
import pl.edu.wat.wcy.pz.project.server.entity.RoleName;
import pl.edu.wat.wcy.pz.project.server.entity.User;
import pl.edu.wat.wcy.pz.project.server.form.LoginForm;
import pl.edu.wat.wcy.pz.project.server.form.SignUpForm;
import pl.edu.wat.wcy.pz.project.server.form.response.JwtResponse;
import pl.edu.wat.wcy.pz.project.server.repository.RoleRepository;
import pl.edu.wat.wcy.pz.project.server.repository.UserRepository;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private PasswordEncoder encoder;
    private JwtProvider jwtProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginForm) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@Valid @RequestBody SignUpForm signUpForm) {
        if (userRepository.existsByUsername(signUpForm.getUsername())) {
            return new ResponseEntity<>("User with this name already exist", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signUpForm.getEmail())) {
            return new ResponseEntity<>("User with this email already exist", HttpStatus.BAD_REQUEST);
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
                .password(encoder.encode(signUpForm.getPassword())) //todo
                .roles(roles)
                .registrationDate(Calendar.getInstance().getTime())
                .build();

        userRepository.save(user);

        return new ResponseEntity<>("User created!", HttpStatus.CREATED);
    }
}
