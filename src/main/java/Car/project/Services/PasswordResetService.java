package Car.project.Services;

import Car.project.Entities.Token;
import Car.project.Entities.User;
import Car.project.Repositories.TokenRepository;
import Car.project.Repositories.UserRepository;
import Car.project.dto.TokenType;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public void initiatePasswordReset(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable avec cet email"));

        String token = UUID.randomUUID().toString();
        LocalDateTime expirationDate = LocalDateTime.now().plusHours(1); // Token valable 1 heure
        Token resetToken = new Token(
                token, TokenType.PASSWORD_RESET, user, expirationDate
        );
        tokenRepository.save(resetToken);

        String subject = "Réinitialisation de mot de passe";
        String body = "Cliquez sur le lien suivant pour réinitialiser votre mot de passe :\n" +
                "http://localhost:4200/reset-password?token=" + token;

        emailService.sendEmail(user.getEmail(), subject, body);
    }

    public void verifyEmail(String token) {
        Token resetToken = tokenRepository.findByValueAndType(token, TokenType.EMAIL_VERIFICATION)
                .orElseThrow(() -> new IllegalArgumentException("Token de vérification invalide"));

        if (resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Le token de vérification a expiré.");
        }

        User user = resetToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        tokenRepository.delete(resetToken);
    }

    public void resetPassword(String token, String newPassword) {
        Token resetToken = tokenRepository.findByValueAndType(token, TokenType.PASSWORD_RESET)
                .orElseThrow(() -> new IllegalArgumentException("Token de réinitialisation invalide"));

        if (resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Le token a expiré.");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenRepository.delete(resetToken);
    }
}
