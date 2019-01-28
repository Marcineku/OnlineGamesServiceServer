package pl.edu.wat.wcy.pz.project.server.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.pz.project.server.entity.User;
import pl.edu.wat.wcy.pz.project.server.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;

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
}
