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

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    ApplicationRunner init(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        return args -> {
//            Role role = new Role(1L, RoleName.ROLE_ADMIN);
//            Role role2 = new Role(2L, RoleName.ROLE_USER);
//            roleRepository.save(role);
//            roleRepository.save(role2);
//
//            Set<Role> roles = new HashSet<>();
//            roles.add(roleRepository.findByRoleName(RoleName.ROLE_ADMIN).get());
//            roles.add(roleRepository.findByRoleName(RoleName.ROLE_USER).get());
//
//            User user = User.builder()
//                    .username("Mateusz")
//                    .email("mateusz@mateusz.pl")
//                    .password(encoder.encode("Mateusz"))
//                    .registrationDate(new Date())
//                    .isEmailVerified("T")
//                    .lastLogonDate(new Date())
//                    .roles(roles).build();
//
//            userRepository.save(user);
//
//            roles.removeIf(role1 -> role1.getRoleName().equals(RoleName.ROLE_ADMIN));
//
//            user = User.builder()
//                    .username("Marcin")
//                    .email("Marcin@Marcin.pl")
//                    .password(encoder.encode("Marcin"))
//                    .registrationDate(new Date())
//                    .isEmailVerified("T")
//                    .lastLogonDate(new Date())
//                    .roles(roles).build();
//            userRepository.save(user);
//            Optional<User> mateusz = userRepository.findByUsername("Mateusz");
//            mateusz.get().setPassword(encoder.encode("Mateusz"));
//            userRepository.save(mateusz.get());
//
//            Optional<User> marcin = userRepository.findByUsername("Marcin");
//            marcin.get().setPassword(encoder.encode("Marcin"));
//            userRepository.save(marcin.get());
        };
    }
}
