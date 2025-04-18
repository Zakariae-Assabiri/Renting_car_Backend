package Car.project.Controllers;

import Car.project.Security.JwtTokenProvider;
import Car.project.Services.PasswordResetService;
import Car.project.Services.UserService;
import Car.project.Entities.User;
import Car.project.dto.ForgotPasswordRequest;
import Car.project.dto.LoginRequest;
import Car.project.dto.RegisterRequest;
import Car.project.dto.ResetPasswordRequest;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import Car.project.dto.TokenType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordResetService passwordResetService;

    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider, PasswordResetService passwordResetService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
        	 System.out.println("Received: " + registerRequest);
            userService.register(registerRequest);
            return ResponseEntity.ok(new ApiResponse(true, "Utilisateur enregistré. Veuillez vérifier votre email."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        try {
            userService.verifyEmail(token);
            return ResponseEntity.ok(new ApiResponse(true, "Email vérifié avec succès. Vous pouvez maintenant vous connecter."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
        	User user = userService.authenticate(loginRequest);

            
            String jwtToken = jwtTokenProvider.generateToken(user);
            return ResponseEntity.ok(new AuthResponse(jwtToken, "Bearer", "Connexion réussie."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/reset-password-request")
    public ResponseEntity<?> requestPasswordReset(@RequestBody ForgotPasswordRequest request) {
        try {
            passwordResetService.initiatePasswordReset(request.getEmail());
            return ResponseEntity.ok(new ApiResponse(true, "Un email de réinitialisation a été envoyé."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok(new ApiResponse(true, "Mot de passe réinitialisé avec succès."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Le token de réinitialisation est invalide ou expiré."));
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ApiResponse {
        private boolean success;
        private String message;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class AuthResponse {
        private String token;
        private String type;
        private String message;
    }
}
