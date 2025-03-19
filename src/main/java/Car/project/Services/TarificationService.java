package Car.project.Services;

import Car.project.Entities.Reservation;
import Car.project.Entities.Voiture;
import Car.project.Repositories.ReservationRepository;
import Car.project.Repositories.VoitureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TarificationService {

    private static final Logger logger = LoggerFactory.getLogger(TarificationService.class);

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private VoitureRepository voitureRepository;

    // Facteurs de tarification configurables depuis application.properties
    @Value("${tarification.facteur.demande.elevee:1.2}")
    private double facteurDemandeElevee;

    @Value("${tarification.facteur.saison.vacances:1.3}")
    private double facteurSaisonVacances;

    @Value("${tarification.facteur.disponibilite.faible:1.5}")
    private double facteurDisponibiliteFaible;

    @Value("${tarification.seuil.demande.elevee:10}")
    private int seuilDemandeElevee;

    @Value("${tarification.seuil.disponibilite.faible:5}")
    private int seuilDisponibiliteFaible;

    @Value("${tarification.tarif.minimum.vacances:400.0}")
    private double tarifMinimumVacances;

    // Méthode pour calculer le tarif dynamique
    public double calculerTarifDynamique(Long voitureId, LocalDateTime dateDebut, LocalDateTime dateFin) {
        logger.info("Calcul du tarif dynamique pour voitureId={}, dateDebut={}, dateFin={}", voitureId, dateDebut, dateFin);

        try {
            // Vérifier si la voiture existe
            Voiture voiture = voitureRepository.findById(voitureId)
                    .orElseThrow(() -> {
                        logger.error("Voiture non trouvée avec ID: {}", voitureId);
                        return new IllegalArgumentException("Voiture non trouvée avec ID: " + voitureId);
                    });

            // Vérification du prix de base
            if (voiture.getPrixDeBase() <= 0) {
                logger.error("Prix de base invalide : {}", voiture.getPrixDeBase());
                throw new IllegalArgumentException("Le prix de base de la voiture doit être positif.");
            }

            double tarifBase = voiture.getPrixDeBase();
            logger.info("Tarif de base : {}", tarifBase);

            // Application des facteurs d'ajustement
            double facteurDemande = ajustementParDemande(dateDebut, dateFin);
            double facteurSaison = ajustementParSaison(dateDebut.toLocalDate());
            double facteurDisponibilite = ajustementParDisponibilite(voitureId, dateDebut, dateFin);

            // Calcul du tarif final
            double tarifDynamique = tarifBase * facteurDemande * facteurSaison * facteurDisponibilite;

            // Vérification du tarif minimum pour les vacances
            if (estEnVacances(dateDebut.toLocalDate()) && tarifDynamique < tarifMinimumVacances) {
                logger.info("Application du tarif minimum vacances : {}", tarifMinimumVacances);
                tarifDynamique = tarifMinimumVacances;
            }

            logger.info("Tarif dynamique final : {}", tarifDynamique);
            return tarifDynamique;

        } catch (Exception e) {
            logger.error("Erreur lors du calcul du tarif : {}", e.getMessage(), e);
            throw new RuntimeException("Erreur lors du calcul du tarif : " + e.getMessage(), e);
        }
    }

    // Ajustement en fonction de la demande
    private double ajustementParDemande(LocalDateTime dateDebut, LocalDateTime dateFin) {
        int reservationsActuelles = reservationRepository.countReservationsInPeriod(dateDebut, dateFin);
        return reservationsActuelles > seuilDemandeElevee ? facteurDemandeElevee : 1.0;
    }

    // Ajustement en fonction de la saison (vacances d'été)
    private double ajustementParSaison(LocalDate dateDebut) {
        return estEnVacances(dateDebut) ? facteurSaisonVacances : 1.0;
    }

    // Vérifier si une date est en période de vacances
    private boolean estEnVacances(LocalDate date) {
        int mois = date.getMonthValue();
        return mois == 7 || mois == 8; // Juillet ou août
    }

    // Ajustement en fonction de la disponibilité des voitures
    private double ajustementParDisponibilite(Long voitureId, LocalDateTime dateDebut, LocalDateTime dateFin) {
        int voituresDisponibles = obtenirNombreVoituresDisponibles(dateDebut, dateFin);
        return voituresDisponibles < seuilDisponibiliteFaible ? facteurDisponibiliteFaible : 1.0;
    }

    // Obtenir le nombre de voitures disponibles pour une période donnée
    private int obtenirNombreVoituresDisponibles(LocalDateTime dateDebut, LocalDateTime dateFin) {
        List<Reservation> reservations = reservationRepository.findReservationsOverlapping(dateDebut, dateFin);
        Set<Long> voituresReserveesIds = reservations.stream()
                .map(reservation -> reservation.getVoiture().getId())
                .collect(Collectors.toSet());

        return (int) voitureRepository.findAll().stream()
                .filter(voiture -> !voituresReserveesIds.contains(voiture.getId()))
                .count();
    }
}
