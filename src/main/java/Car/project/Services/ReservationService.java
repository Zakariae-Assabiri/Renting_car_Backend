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


    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }


    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }


    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }


    public Reservation updateReservation(Reservation reservation) {
            return reservationRepository.save(reservation);
    }


    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
