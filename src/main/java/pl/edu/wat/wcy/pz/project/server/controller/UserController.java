package pl.edu.wat.wcy.pz.project.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.wcy.pz.project.server.entity.User;
import pl.edu.wat.wcy.pz.project.server.service.UserService;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@CrossOrigin
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private UserService userService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable(name = "id") Long id) {
        Optional<User> userById = userService.getUserById(id);

        if (!userById.isPresent())
            throw new RuntimeException("User not found");

        return userById.orElse(null);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }
}
