package Car.project.Services;

import Car.project.Entities.User;
import Car.project.Entities.Role;
import Car.project.Entities.RoleNom;
import Car.project.Entities.Client;
import Car.project.Entities.Token;
import Car.project.Repositories.UserRepository;
import Car.project.Repositories.RoleRepository;
import Car.project.Repositories.TokenRepository;
import Car.project.Repositories.ClientRepository;
import Car.project.dto.LoginRequest;
import Car.project.dto.RegisterRequest;
import Car.project.dto.TokenType;
import Car.project.Services.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ClientRepository clientRepository;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       TokenRepository tokenRepository,
                       EmailService emailService,
                       ClientRepository clientRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.clientRepository = clientRepository;
    }

    // Méthode pour s'enregistrer (avec création de Client)
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
        user.setEnabled(false); // Inactif jusqu'à la vérification email

        Set<Role> roles = new HashSet<>();
        if (registerRequest.getRoles() == null || registerRequest.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByNom(RoleNom.ROLE_CLIENT)
                    .orElseThrow(() -> new RuntimeException("Erreur : ROLE_CLIENT non trouvé."));
            roles.add(defaultRole);
        } else {
            for (String roleName : registerRequest.getRoles()) {
                Role role = roleRepository.findByNom(RoleNom.valueOf(roleName))
                        .orElseThrow(() -> new RuntimeException("Erreur : rôle non trouvé."));
                roles.add(role);
            }
        }
        user.setRoles(roles);

        userRepository.save(user);

        //  Maintenant, création automatique du Client lié au User
        Client client = new Client();
        client.setCname(registerRequest.getUsername()); // Ou demande dans RegisterRequest le vrai nom complet
        client.setAdresse("Adresse par défaut"); // Ou une vraie adresse récupérée
        client.setTel("0000000000"); // Ou un champ dans ton RegisterRequest

        client.setUser(user); // très important
        clientRepository.save(client);

        //  Génération du token d'activation
        String tokenValue = UUID.randomUUID().toString();
        Token token = new Token(
                tokenValue,
                TokenType.EMAIL_VERIFICATION,
                user,
                LocalDateTime.now().plus(Duration.ofHours(2))
        );
        tokenRepository.save(token);

        //  Envoi de l'email de vérification
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
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Username ou mot de passe incorrect.");
        }

        if (!user.isEnabled()) {
            throw new IllegalStateException("Veuillez d'abord vérifier votre adresse email avant de vous connecter.");
        }

        return user;
    }

    // Méthode pour vérifier l'email
    public void verifyEmail(String tokenValue) {
        Token token = tokenRepository.findByValueAndType(tokenValue, TokenType.EMAIL_VERIFICATION)
                .orElseThrow(() -> new RuntimeException("Token de vérification invalide"));

        if (token.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expiré");
        }

        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        tokenRepository.delete(token); // Supprime le token après validation
    }
}
