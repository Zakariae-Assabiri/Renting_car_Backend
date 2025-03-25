package Car.project.Services;

import Car.project.Entities.User;
import Car.project.Entities.Role;
import Car.project.Entities.RoleNom;
import Car.project.Repositories.UserRepository;
import Car.project.Repositories.RoleRepository;
import Car.project.dto.LoginRequest;
import Car.project.dto.RegisterRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Méthode pour s'enregistrer
    public User register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé !");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());

        // Gérer les rôles
        Set<Role> roles = new HashSet<>();
        if (registerRequest.getRoles() == null || registerRequest.getRoles().isEmpty()) {
            // Si aucun rôle n'est défini, on attribue `ROLE_CLIENT` par défaut
            Role defaultRole = roleRepository.findByNom(RoleNom.ROLE_CLIENT)
                    .orElseThrow(() -> new RuntimeException("Erreur : ROLE_CLIENT non trouvé."));
            roles.add(defaultRole);
        } else {
            // Assigner les rôles définis dans la requête
            for (String roleName : registerRequest.getRoles()) {
                Role role = roleRepository.findByNom(RoleNom.valueOf(roleName))
                        .orElseThrow(() -> new RuntimeException("Erreur : rôle non trouvé."));
                roles.add(role);
            }
        }

        user.setRoles(roles);
        return userRepository.save(user);
    }

    // Méthode pour s'authentifier
    public User authenticate(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Identifiants invalides");
        }
        return user;
    }
}
