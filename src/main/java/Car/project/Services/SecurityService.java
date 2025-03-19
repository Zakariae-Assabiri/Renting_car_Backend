package Car.project.Services;

import Car.project.Entities.Client;
import Car.project.Entities.Reservation;
import Car.project.Repositories.ClientRepository;
import Car.project.Repositories.ReservationRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;

    public SecurityService(ReservationRepository reservationRepository, ClientRepository clientRepository) {
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
    }

    // Vérifier si l'utilisateur est propriétaire de la réservation
    public boolean isReservationOwner(Long reservationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Supposons que c'est l'email du client

        return reservationRepository.findById(reservationId)
                .map(reservation -> reservation.getClient().getAdresse().equals(username))
                .orElse(false);
    }

    // Vérifier si l'utilisateur est propriétaire du client
    public boolean isClientOwner(Long clientId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Supposons que c'est l'email du client

        return clientRepository.findById(clientId)
                .map(client -> client.getAdresse().equals(username))
                .orElse(false);
    }
}
