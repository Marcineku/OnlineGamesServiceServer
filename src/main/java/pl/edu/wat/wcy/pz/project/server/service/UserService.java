package pl.edu.wat.wcy.pz.project.server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.pz.project.server.entity.User;
import pl.edu.wat.wcy.pz.project.server.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;

    public User createUser(User user) {
        List<User> userList = userRepository.findAll();

        if (userList.stream().map(User::getUsername).anyMatch(user.getUsername()::equals))
            throw new RuntimeException("A user with this username already exist");

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User user) {
        Optional<User> oldUser = userRepository.findById(id);
        if (!oldUser.isPresent())
            throw new RuntimeException("User with id " + id + "not exist");
        return null;
    }
}
