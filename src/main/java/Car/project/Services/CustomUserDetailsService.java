package Car.project.Services;


import Car.project.Entities.User;
import Car.project.Repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Rechercher l'utilisateur dans la base de données
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(username));

        // Utiliser orElseThrow pour lever une exception si l'utilisateur n'est pas trouvé
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec ce nom d'utilisateur"));

        // Retourner l'objet UserDetails (peut être une implémentation personnalisée si nécessaire)
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}
