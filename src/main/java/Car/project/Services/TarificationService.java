package Car.project.Services;

import Car.project.Entities.Reservation;
import Car.project.Entities.Voiture;
import Car.project.Repositories.ReservationRepository;
import Car.project.Repositories.VoitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class TarificationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private VoitureRepository voitureRepository;

    // Définir un tarif minimum pour les mois de vacances
    private static final double TARIF_MINIMUM_VACANCES = 400.0;

    // Calculer le tarif dynamique
    public double calculerTarifDynamique(Long voitureId, LocalDate dateDebut, LocalDate dateFin) {
        Voiture voiture = voitureRepository.findById(voitureId)
                .orElseThrow(() -> new IllegalArgumentException("Voiture non trouvée avec ID: " + voitureId));

        double tarifBase = voiture.getPrixDeBase();
        double facteurDemande = ajustementParDemande(dateDebut, dateFin);
        double facteurSaison = ajustementParSaison(dateDebut);
        double facteurDisponibilite = ajustementParDisponibilite(voitureId, dateDebut, dateFin);

        // Calculer le tarif dynamique
        double tarifDynamique = tarifBase * facteurDemande * facteurSaison * facteurDisponibilite;

        // Appliquer le tarif minimum pour les mois de vacances, si nécessaire
        if ((dateDebut.getMonthValue() == 7 || dateDebut.getMonthValue() == 8) && tarifDynamique < TARIF_MINIMUM_VACANCES) {
            tarifDynamique = TARIF_MINIMUM_VACANCES;
        }

        return tarifDynamique;
    }

    private double ajustementParDemande(LocalDate dateDebut, LocalDate dateFin) {
        int reservationsActuelles = reservationRepository.countReservationsInPeriod(dateDebut, dateFin);
        return reservationsActuelles > 10 ? 1.2 : 1.0;
    }

    private double ajustementParSaison(LocalDate dateDebut) {
        return (dateDebut.getMonthValue() == 7 || dateDebut.getMonthValue() == 8) ? 1.3 : 1.0;
    }

    private double ajustementParDisponibilite(Long voitureId, LocalDate dateDebut, LocalDate dateFin) {
        int voituresDisponibles = obtenirNombreVoituresDisponibles(dateDebut, dateFin);
        return voituresDisponibles < 5 ? 1.5 : 1.0;
    }

    private int obtenirNombreVoituresDisponibles(LocalDate dateDebut, LocalDate dateFin) {
        List<Reservation> reservations = reservationRepository.findReservationsOverlapping(dateDebut, dateFin);
        Set<Long> voituresReserveesIds = reservations.stream()
            .map(reservation -> reservation.getVoiture().getId())
            .collect(Collectors.toSet());

        return (int) voitureRepository.findAll().stream()
                .filter(voiture -> !voituresReserveesIds.contains(voiture.getId()))
                .count();
    }
}
