package pl.edu.wat.wcy.pz.project.server;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.wat.wcy.pz.project.server.entity.Role;
import pl.edu.wat.wcy.pz.project.server.entity.RoleName;
import pl.edu.wat.wcy.pz.project.server.entity.User;
import pl.edu.wat.wcy.pz.project.server.repository.RoleRepository;
import pl.edu.wat.wcy.pz.project.server.repository.UserRepository;

import java.util.*;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    ApplicationRunner init(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        return args -> {
            List<User> allUsers = userRepository.findAll();
            allUsers.forEach(user -> user.setPassword(encoder.encode(user.getUsername())));
            userRepository.saveAll(allUsers);
        };
    }
}
