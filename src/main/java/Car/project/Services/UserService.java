package Car.project.Services;

import Car.project.Entities.User; 
import Car.project.Entities.Role;
import Car.project.Entities.RoleNom;
import Car.project.Repositories.UserRepository;
import Car.project.Repositories.RoleRepository;
import Car.project.Repositories.TokenRepository;
import Car.project.dto.LoginRequest;
import Car.project.dto.RegisterRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import Car.project.Entities.Token;
import Car.project.dto.TokenType;
import jakarta.mail.MessagingException;
import Car.project.Repositories.TokenRepository;
import Car.project.Services.EmailService;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            TokenRepository tokenRepository,
            EmailService emailService) {
			this.userRepository = userRepository;
			this.roleRepository = roleRepository;
			this.passwordEncoder = passwordEncoder;
			this.tokenRepository = tokenRepository;
			this.emailService = emailService;
}

    // Méthode pour s'enregistrer
    public User register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé !");
        }
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Ce nom d'utilisateur est déjà pris !");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setEnabled(false); // Désactivé jusqu’à vérification email

        Set<Role> roles = new HashSet<>();
        
        if (registerRequest.getRoles() == null || registerRequest.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByNom(RoleNom.ROLE_CLIENT)
                    .orElseThrow(() -> new RuntimeException("Erreur : ROLE_CLIENT non trouvé."));
            roles.add(defaultRole);
        } else {
            for (String roleName : registerRequest.getRoles()) {
            	 System.out.println("Role reçu : >" + roleName + "<");
                Role role = roleRepository.findByNom(RoleNom.valueOf(roleName))
                        .orElseThrow(() -> new RuntimeException("Erreur : rôle non trouvé."));
                roles.add(role);
            }
        }

        user.setRoles(roles);
        userRepository.save(user);

        // Génère et enregistre le token
        String tokenValue = UUID.randomUUID().toString();
        Token token = new Token(
        	    tokenValue,
        	    TokenType.EMAIL_VERIFICATION,
        	    user,
        	    LocalDateTime.now().plus(Duration.ofHours(2))
        	);

        tokenRepository.save(token);

        // Envoie l’email de vérification
        String verificationUrl = "http://localhost:4200/verify-email?token=" + tokenValue;
        String subject = "Vérification de ton adresse email";
        String body = "<p>Bonjour " + user.getUsername() + ",</p>" +
                "<p>Clique ici pour activer ton compte :</p>" +
                "<a href=\"" + verificationUrl + "\">Activer mon compte</a>";


        try {
            emailService.sendEmail(user.getEmail(), subject, body);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
        }
        return user;
    }

    // Méthode pour s'authentifier
    public User authenticate(LoginRequest loginRequest) {
        // Récupérer l'utilisateur par email
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Username ou mot de passe incorrect.");
        }
     // Log du statut de l'utilisateur
        System.out.println("User enabled status: " + user.isEnabled());

        // Vérifier si le compte est activé
        if (!user.isEnabled()) {
            throw new IllegalStateException("Veuillez d'abord vérifier votre adresse email avant de vous connecter.");
        }

        // Vérifier le mot de passe
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Email ou mot de passe incorrect.");
        }

        return user;
    }

    
    public void verifyEmail(String tokenValue) {
        Token token = tokenRepository.findByValueAndType(tokenValue, TokenType.EMAIL_VERIFICATION)
                .orElseThrow(() -> new RuntimeException("Token de vérification invalide"));

        if (token.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expiré");
        }

        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        tokenRepository.delete(token); // Supprime le token après utilisation
    }

}
