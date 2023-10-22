package sit.project.projectv1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import sit.project.projectv1.models.User;
import sit.project.projectv1.exceptions.ItemNotFoundException;
import sit.project.projectv1.exceptions.ResponseStatusValidationException;
import sit.project.projectv1.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Argon2PasswordEncoder argon2PasswordEncoder;

    public UserDetails loadUserByUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            User user = userRepository.findByUsername(username);

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(), user.getPassword(), getUserRoles(user));
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

    private List<GrantedAuthority> getUserRoles(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString())); // Form is ROLE_role
        return authorities;
    }
}