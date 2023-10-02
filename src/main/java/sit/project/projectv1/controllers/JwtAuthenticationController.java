package sit.project.projectv1.controllers;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import sit.project.projectv1.config.JwtTokenUtil;
import sit.project.projectv1.dtos.JwtRefreshResponse;
import sit.project.projectv1.dtos.JwtRequest;
import sit.project.projectv1.dtos.JwtResponse;
import sit.project.projectv1.exceptions.ItemNotFoundException;
import sit.project.projectv1.exceptions.ResponseStatusValidationException;
import sit.project.projectv1.repositories.UserRepository;
import sit.project.projectv1.services.JwtUserDetailsService;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/token")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest request) throws Exception {
        if (userRepository.existsByUsername(request.getUsername())) {
            if (userDetailsService.checkPassword(request.getUsername(), request.getPassword())) {
                authenticate(request.getUsername(), request.getPassword());
                final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
                final String accessToken = jwtTokenUtil.generateToken(userDetails);
                final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
                return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
            }
            throw new ResponseStatusValidationException(HttpStatus.UNAUTHORIZED, "password", "Password NOT Matched");
        }
        throw new ItemNotFoundException("User not found with username: " + request.getUsername());
    }

    @GetMapping
    public ResponseEntity<?> createAuthenticationTokenByRefreshToken(HttpServletRequest request) throws Exception {
        String username = checkUsername(request);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String accessToken = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtRefreshResponse(accessToken));
    }

    // bring username and password check verify
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException exception) {
            throw new Exception("USER_DISABLED", exception);
        } catch (BadCredentialsException exception) {
            throw new Exception("INVALID_CREDENTIALS", exception);
        }
    }

    private String checkUsername(HttpServletRequest request) throws Exception {
        String requestTokenHeader = request.getHeader("Authorization");
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String jwtToken = requestTokenHeader.substring(7);
            try {
                return jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException exception) {
                throw new Exception("Unable to get JWT Token", exception);
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unable to get JWT Token");
            } catch (ExpiredJwtException exception) {
                throw new Exception("Refresh JWT Token has expired", exception);
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT Token has expired");
            }
        } else {
            throw new Exception("JWT Token does not begin with Bearer String");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT Token does not begin with Bearer String");
        }
    }
}
