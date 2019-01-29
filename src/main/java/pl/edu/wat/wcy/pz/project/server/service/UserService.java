package pl.edu.wat.wcy.pz.project.server.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.pz.project.server.dto.EmailDTO;
import pl.edu.wat.wcy.pz.project.server.entity.EmailVerificationToken;
import pl.edu.wat.wcy.pz.project.server.entity.User;
import pl.edu.wat.wcy.pz.project.server.rabbit.RabbitProducer;
import pl.edu.wat.wcy.pz.project.server.repository.EmailVerificationTokenRepository;
import pl.edu.wat.wcy.pz.project.server.repository.UserRepository;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    private PasswordEncoder encoder;

    private RabbitProducer rabbitProducer;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User user) {
        LOGGER.info("User to update. Id: " + id);
        Optional<User> oldUserOptional = userRepository.findById(id);
        if (!oldUserOptional.isPresent())
            throw new RuntimeException("User with id " + id + "not exist");
        User oldUser = oldUserOptional.get();
        oldUser.setEmail(user.getEmail());
        oldUser.setRoles(user.getRoles());
        userRepository.save(oldUser);
        LOGGER.info("User updated. Id: " + oldUser.getUserId());
        return oldUser;
    }

    public String validateEmail(String token) {
        LOGGER.info("Token to verification: " + token);
        Optional<EmailVerificationToken> tokenOptional = emailVerificationTokenRepository.findFirstByToken(token);
        if (!tokenOptional.isPresent()) {
            LOGGER.info("Token not found in database");
            return "Invalid token. Please ask for another verification email";
        }
        EmailVerificationToken emailVerificationToken = tokenOptional.get();
        if (Calendar.getInstance().getTime().after(emailVerificationToken.getExpiraionDate())) {
            emailVerificationToken.setExpired("T");
            emailVerificationTokenRepository.save(emailVerificationToken);
            LOGGER.info("Token expired. TokenId: " + emailVerificationToken.getTokenId());
            return "Token is expired. Please ask for another verification email";
        }
        emailVerificationToken.setExpired("T");
        emailVerificationTokenRepository.save(emailVerificationToken);

        User user = emailVerificationToken.getUser();
        user.setIsEmailVerified("T");
        userRepository.save(user);
        LOGGER.info("User: " + user.getUsername() + ". Email verified.");
        return user.getUsername() + ", your email is now verified. Have fun playing our games!";
    }

    public String resetPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            LOGGER.info("User with email " + email + " not exist");
            return "User with this email not exist";
        }
        User user = userOptional.get();
        String pass = UUID.randomUUID().toString().split("-")[0];
        user.setPassword(encoder.encode(pass));
        userRepository.save(user);

        EmailDTO emailDTO = EmailDTO.builder()
                .emailAddress(user.getEmail())
                .emailType(EmailDTO.EmailType.PASSWORD_RESET)
                .username(user.getUsername())
                .emailText(pass)
                .build();

        rabbitProducer.sendToQueue(emailDTO);

        LOGGER.info("User: " + user.getUsername() + ". Generated temporary password.");
        return "Temporary password sent";
    }
}
