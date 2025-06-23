package Car.project.Services;

import Car.project.Entities.Client;
import Car.project.Entities.Reservation;
import Car.project.Repositories.ClientRepository;
import Car.project.Repositories.ReservationRepository;
import Car.project.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
@Service("securiteService")
public class SecuriteService {
	@Autowired
    private  ReservationRepository reservationRepository;
	@Autowired
    private  ClientRepository clientRepository;
	@Autowired
    private  UserRepository userRepository;


    // Vérifier si l'utilisateur est propriétaire de la réservation
    public boolean isReservationOwner(Long reservationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // C'est le username du User

        return reservationRepository.findById(reservationId)
                .map(reservation -> {
                    Client client = reservation.getClient();
                    if (client != null && client.getUser() != null) {
                        return client.getUser().getUsername().equals(username);
                    }
                    return false;
                })
                .orElse(false);
    }

    // Vérifier si l'utilisateur est propriétaire du client
    public boolean isClientOwner(Long clientId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // username du User

        return clientRepository.findById(clientId)
                .map(client -> {
                    if (client.getUser() != null) {
                        return client.getUser().getUsername().equals(username);
                    }
                    return false;
                })
                .orElse(false);
    }



    // Vérifier si l'utilisateur est ADMIN ou propriétaire de la réservation
    public boolean isAdminOrOwner(Long reservationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        return isReservationOwner(reservationId);
    }
   // Vérifier si l'utilisateur est ADMIN
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
