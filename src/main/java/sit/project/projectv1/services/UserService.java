package sit.project.projectv1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import sit.project.projectv1.dtos.JwtRequest;
import sit.project.projectv1.exceptions.ItemNotFoundException;
import sit.project.projectv1.exceptions.ResponseStatusValidationException;
import sit.project.projectv1.models.Announcement;
import sit.project.projectv1.models.User;
import sit.project.projectv1.repositories.AnnouncementRepository;
import sit.project.projectv1.repositories.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Argon2PasswordEncoder argon2PasswordEncoder;

    @Autowired
    private AnnouncementRepository announcementRepository;

    public List<User> getAllUser() {
        return userRepository.findAllUser();
    }

    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ItemNotFoundException("This user does not exits!!!"));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(User user) {
        String hashedPassword = argon2PasswordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        User saveUser = userRepository.saveAndFlush(user);
        userRepository.refresh(saveUser);
        return saveUser;
    }

    public User updateUser(Integer userId, User user) {
        User storedUser = userRepository.findById(userId).orElseThrow(
                () -> new ItemNotFoundException("This user does not exits!!!"));
        storedUser.setUsername(user.getUsername());
        storedUser.setName(user.getName());
        storedUser.setEmail(user.getEmail());
        storedUser.setRole(user.getRole());
        User saveUser = userRepository.saveAndFlush(storedUser);
        userRepository.refresh(saveUser);
        return saveUser;
    }

    public User getUserFromToken(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            User user = userRepository.findByUsername(username);
            return user;
        }
        // No token = guest
        return null;
    }

    public void deleteUser(Integer userId, User user) {
        User userToDelete = userRepository.findById(userId).orElseThrow(
                () -> new ItemNotFoundException("This user does not exits!!!"));

        // Retrieve announcements owned by the user being deleted
        List<Announcement> announcementsToDelete = announcementRepository.findAllByAnnouncementOwner(userToDelete);

        // Set the admin user as the new owner for these announcements
        if (user == null) {
            throw new ItemNotFoundException("Admin user not found!!!");
        }
        for (Announcement announcement : announcementsToDelete) {
            announcement.setAnnouncementOwner(user);
        }

        // Save the updated announcements
        announcementRepository.saveAll(announcementsToDelete);

        // Delete the user
        userRepository.deleteById(userId);
    }

    public boolean checkPassword(JwtRequest jwtRequest) {
        if (userRepository.existsByUsername(jwtRequest.getUsername())) {
            String providedPassword = jwtRequest.getPassword();
            String storedPassword = userRepository.findByUsername(jwtRequest.getUsername()).getPassword();
            if (argon2PasswordEncoder.matches(providedPassword, storedPassword)) {
                return argon2PasswordEncoder.matches(providedPassword, storedPassword);
            }
            throw new ResponseStatusValidationException(HttpStatus.UNAUTHORIZED, "password", "Password NOT Matched");
        }
        throw new ItemNotFoundException("This username does not exist!!!");
    }
}
