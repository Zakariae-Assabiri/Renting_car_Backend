package Car.project.Services;

import Car.project.Repositories.ReservationRepository;
import Car.project.Repositories.VoitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import Car.project.Entities.Reservation;

@Service
public class DashboardService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private VoitureRepository voitureRepository;

    // 1. Calculer le revenu total généré par toutes les réservations
    public Double calculerRevenuTotal() {
        Double revenuTotal = reservationRepository.sumMontantTotal();
        return (revenuTotal != null) ? revenuTotal : 0.0;
    }

    // 2. Calculer le taux de disponibilité des véhicules sur une période donnée
    public Double calculerTauxDisponibilite(LocalDateTime dateDebut, LocalDateTime dateFin) {
        long totalVoitures = voitureRepository.count();
        if (totalVoitures == 0) {
            return 0.0; // Éviter la division par zéro
        }

        int voituresReservees = reservationRepository.countReservedCarsInPeriod(dateDebut, dateFin);
        return ((double) (totalVoitures - voituresReservees) / totalVoitures) * 100;
    }

    // 3. Calculer le taux d'occupation des véhicules sur une période donnée
    public Double calculerTauxOccupation(LocalDateTime dateDebut, LocalDateTime dateFin) {
        long totalVoitures = voitureRepository.count();
        if (totalVoitures == 0) {
            return 0.0; // Éviter la division par zéro
        }

        int voituresReservees = reservationRepository.countReservedCarsInPeriod(dateDebut, dateFin);
        return ((double) voituresReservees / totalVoitures) * 100;
    }

    // 4. Calculer le revenu moyen par véhicule
    public Double calculerRevenuMoyenParVoiture(Long voitureId) {
        Double revenuVoiture = reservationRepository.sumMontantTotalByVoitureId(voitureId);
        Long nombreReservations = reservationRepository.countByVoitureId(voitureId);

        if (nombreReservations == 0 || revenuVoiture == null) {
            return 0.0; // Éviter la division par zéro et gérer les valeurs nulles
        } 

        return revenuVoiture / nombreReservations;
    }


    // 5. Calculer le nombre de réservations effectuées pour un véhicule spécifique
    public Long calculerNombreReservationsParVoiture(Long voitureId) {
        return reservationRepository.countByVoitureId(voitureId);
    }

    // 6. Calculer le taux d'annulation des réservations
    public Double calculerTauxAnnulation() {
        Long totalReservations = reservationRepository.count();
        Long annulations = reservationRepository.countByStatut("Annulée");

        if (totalReservations == 0) {
            return 0.0; // Éviter la division par zéro
        }

        return ((double) annulations / totalReservations) * 100;
    }

    // 7. Calculer la durée moyenne des réservations en heures
    public Double calculerDureeMoyenneReservations() {
        List<Reservation> reservations = reservationRepository.findAllReservations();
        if (reservations.isEmpty()) {
            return 0.0;
        }

        long totalDuree = reservations.stream()
                .mapToLong(reservation -> ChronoUnit.HOURS.between(reservation.getDateDebut(), reservation.getDateFin()))
                .sum();

        return (double) totalDuree / reservations.size();
    }

    // 8. Calculer le revenu généré sur une période spécifique
    public Double calculerRevenuParPeriode(LocalDateTime dateDebut, LocalDateTime dateFin) {
        Double revenuPeriode = reservationRepository.sumMontantTotalByPeriod(dateDebut, dateFin);
        return (revenuPeriode != null) ? revenuPeriode : 0.0;
    }
}
