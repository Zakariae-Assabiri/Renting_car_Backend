package Car.project.Controllers;

import Car.project.Security.JwtTokenProvider;
import Car.project.Services.UserService;
import Car.project.Entities.User;
import Car.project.dto.LoginRequest;
import Car.project.dto.RegisterRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Route pour s'enregistrer
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            userService.register(registerRequest);
            return ResponseEntity.ok(new ApiResponse(true, "User registered successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    // Route pour se connecter
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.authenticate(loginRequest);
            String jwtToken = jwtTokenProvider.generateToken(user);
            return ResponseEntity.ok(new AuthResponse(jwtToken, "Bearer", "Authentication successful"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    // Classe interne pour la réponse API
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ApiResponse {
        private boolean success;
        private String message;
    }

    // Classe interne pour la réponse d'authentification
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class AuthResponse {
        private String token;
        private String type;
        private String message;
    }
}
