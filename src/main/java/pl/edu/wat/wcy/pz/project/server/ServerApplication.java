package pl.edu.wat.wcy.pz.project.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.edu.wat.wcy.pz.project.server.entity.Role;
import pl.edu.wat.wcy.pz.project.server.entity.User;
import pl.edu.wat.wcy.pz.project.server.repository.UserRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Autowired
    public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository repository) throws Exception {

        if (repository.count() == 0)
            repository.save(new User(0L, "user2", "user2", "examplemail", new Date(), new Date(), "T",
                    Arrays.asList(new Role(0L, "USER"), new Role(1L, "ACTUATOR"))));

        List<User> list = repository.findAll();
        for (User user : list) {
            System.out.println(user.getUserName());
        }

        builder.userDetailsService(s -> new CustomUserDetails(repository.findByUserName(s)));
    }
}
