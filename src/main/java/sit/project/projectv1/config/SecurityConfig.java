package sit.project.projectv1.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sit.project.projectv1.exceptions.JwtAccessDeniedHandler;
import sit.project.projectv1.exceptions.JwtAuthenticationEntryPoint;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    private final AuthenticationConfiguration authConfiguration;

    @Autowired
    private JwtAuthenticationEntryPoint entryPoint;

    @Autowired
    private JwtAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private JwtRequestFilter requestFilter;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.cors();
        http
                .csrf().disable()
                .authorizeHttpRequests()

//                .requestMatchers("/api/**").permitAll()

                .requestMatchers("/api/token", "/api/sendmail/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/announcements/**", "/api/announcements/{announcementId}", "/api/users/username/{username}").permitAll()

                .requestMatchers(HttpMethod.POST, "/api/announcements/**").hasAnyRole("admin", "announcer")
                .requestMatchers(HttpMethod.PUT, "/api/announcements/**").hasAnyRole("admin", "announcer")
                .requestMatchers(HttpMethod.DELETE, "/api/announcements/**").hasAnyRole("admin", "announcer")
                .requestMatchers(HttpMethod.GET, "/api/users/username").hasAnyRole("admin", "announcer")

                .requestMatchers("/api/users/**").hasRole("admin")
//                .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("admin")
//                .requestMatchers(HttpMethod.POST, "/api/users/**").hasRole("admin")
//                .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("admin")
//                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("admin")

                .anyRequest().authenticated()
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(entryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

