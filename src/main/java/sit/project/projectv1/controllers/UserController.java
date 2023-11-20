package sit.project.projectv1.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sit.project.projectv1.dtos.InputCreateUserDTO;
import sit.project.projectv1.dtos.InputUpdateUserDTO;
import sit.project.projectv1.dtos.JwtRequest;
import sit.project.projectv1.enums.Role;
import sit.project.projectv1.models.User;
import sit.project.projectv1.services.UserService;

import java.util.List;

@CrossOrigin
@RestController
//@Secured("ROLE_admin")
@PreAuthorize("hasRole('admin')")
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
    public User createUser(@Valid @RequestBody InputCreateUserDTO inputCreateUserDTO) {
        User user = modelMapper.map(inputCreateUserDTO, User.class);
        user.setId(null);
        return userService.createUser(user);
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable Integer userId, @Valid @RequestBody InputUpdateUserDTO inputUpdateUserDTO) {
        User user = modelMapper.map(inputUpdateUserDTO, User.class);
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserFromToken(authentication);

        if (user.getRole() == Role.admin && userService.getUserById(userId) != user) {
            userService.deleteUser(userId, user);
        } else {
            throw new AccessDeniedException("Access denied!!!");
        }
    }

    @PostMapping("/match")
    public boolean checkPassword(@Valid @RequestBody JwtRequest jwtRequest) {
            return userService.checkPassword(jwtRequest);
    }
}
