package Car.project.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Car.project.Entities.Reservation;
import Car.project.Entities.Voiture;
import Car.project.Repositories.ReservationRepository;
import Car.project.Repositories.VoitureRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VoitureService {

    @Autowired
    private VoitureRepository voitureRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    // Créer une voiture
    public Voiture createVoiture(Voiture voiture) {
        return voitureRepository.save(voiture);
    }

    // Obtenir une voiture par son ID
    public Optional<Voiture> getVoitureById(Long id) {
        return voitureRepository.findById(id);
    }

    // Obtenir toutes les voitures
    public List<Voiture> getAllVoitures() {
        return voitureRepository.findAll();
    }

    // Mettre à jour une voiture
    public Voiture updateVoiture(Voiture voiture) {
        return voitureRepository.save(voiture);
    }

    // Supprimer une voiture
    public void deleteVoiture(Long id) {
        voitureRepository.deleteById(id);
    }

    // Trouver les voitures disponibles entre deux dates
    public List<Voiture> trouverVoituresDisponibles(LocalDateTime dateDebut, LocalDateTime dateFin) {
        // Vérifier que la date de début est avant la date de fin
        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin.");
        }

        // Récupérer les IDs des voitures réservées pendant la période
        Set<Long> voituresReserveesIds = reservationRepository.findVoituresReserveesIds(dateDebut, dateFin);

        // Récupérer toutes les voitures non réservées
        return voitureRepository.findAll()
            .stream()
            .filter(voiture -> !voituresReserveesIds.contains(voiture.getId()))
            .collect(Collectors.toList());
    }
}