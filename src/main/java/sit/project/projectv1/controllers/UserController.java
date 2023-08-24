package sit.project.projectv1.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.project.projectv1.dtos.InputUserDTO;
import sit.project.projectv1.entities.User;
import sit.project.projectv1.services.UserService;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<User> getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Integer userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public User createUser(@RequestBody InputUserDTO inputUserDTO) {
//        inputUserDTO.setUsername(inputUserDTO.getUsername().trim());
//        inputUserDTO.setName(inputUserDTO.getName().trim());
//        inputUserDTO.setEmail(inputUserDTO.getEmail().trim());
//        inputUserDTO.setRole(inputUserDTO.getRole().trim());
        User user = modelMapper.map(inputUserDTO, User.class);
        user.setId(null);
        return userService.createUser(user);
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable Integer userId, @RequestBody InputUserDTO inputUserDTO) {
//        inputUserDTO.setUsername(inputUserDTO.getUsername().trim());
//        inputUserDTO.setName(inputUserDTO.getName().trim());
//        inputUserDTO.setEmail(inputUserDTO.getEmail().trim());
//        inputUserDTO.setRole(inputUserDTO.getRole().trim());
        User user = modelMapper.map(inputUserDTO, User.class);
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
    }
}
