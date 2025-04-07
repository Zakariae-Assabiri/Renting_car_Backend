package Car.project.Security;

import Car.project.Security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableMethodSecurity // Active @PreAuthorize et @Secured
@RequiredArgsConstructor
public class SecuriteConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors() 
            .and()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll() // Autorise les routes d'authentification
                .requestMatchers("/api/admin/**").hasRole("ADMIN") // Protège les routes admin
                .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN") // Accessible aux utilisateurs et admins
                .requestMatchers("/api/dialogflow/**").permitAll() 
                .requestMatchers("/api/ocr/**").permitAll() 
                .requestMatchers("/api/").hasAnyRole("USER", "ADMIN")
                .requestMatchers(
                		"/swagger-ui/**",
                		 "/v3/api-docs/**", 
                		 "/swagger-ui.html"
                		).permitAll() 
                .anyRequest().authenticated() // Toutes les autres routes nécessitent une authentification
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Mode JWT
            .authenticationProvider(authenticationProvider()) // Définit le provider d'authentification
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Ajoute le filtre JWT

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder()); // Encodeur de mots de passe
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(authenticationProvider()));
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Encode les mots de passe
    }
}
