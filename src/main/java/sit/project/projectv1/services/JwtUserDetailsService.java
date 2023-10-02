package sit.project.projectv1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import sit.project.projectv1.entities.User;
import sit.project.projectv1.exceptions.ItemNotFoundException;
import sit.project.projectv1.exceptions.ResponseStatusValidationException;
import sit.project.projectv1.repositories.UserRepository;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Argon2PasswordEncoder argon2PasswordEncoder;

    public UserDetails loadUserByUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            User user = userRepository.findByUsername(username);
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
        }
        throw new ItemNotFoundException("The specified username DOES NOT exist");
    }

    public boolean checkPassword(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            String storedPassword = userRepository.findByUsername(username).getPassword();
            if (argon2PasswordEncoder.matches(password, storedPassword)) {
                return argon2PasswordEncoder.matches(password, storedPassword);
            }
            throw new ResponseStatusValidationException(HttpStatus.UNAUTHORIZED, "password", "Password NOT Matched");
        }
        throw new ItemNotFoundException("User not found with username: " + username);
    }
}