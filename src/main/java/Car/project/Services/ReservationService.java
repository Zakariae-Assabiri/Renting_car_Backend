package Car.project.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Car.project.Entities.Reservation;
import Car.project.Repositories.ReservationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    // Créer une nouvelle réservation
    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    // Obtenir une réservation par son ID
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    // Obtenir toutes les réservations
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Mettre à jour une réservation existante
    public Reservation updateReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    // Supprimer une réservation par ID
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
