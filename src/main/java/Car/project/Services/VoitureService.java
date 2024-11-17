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
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;


	@Service
	public class VoitureService {

	    @Autowired
	    private VoitureRepository voitureRepository;

	    @Autowired
	    private ReservationRepository reservationRepository;
	    public Voiture createVoiture(Voiture voiture) {
	        return voitureRepository.save(voiture);
	    }


	    public Optional<Voiture> getVoitureById(Long id) {
	        return voitureRepository.findById(id);
	    }


	    public List<Voiture> getAllVoitures() {
	        return voitureRepository.findAll();
	    }


	    public Voiture updateVoiture(Voiture voiture) {
	            return voitureRepository.save(voiture);
	    }

	    public void deleteVoiture(Long id) {
	        voitureRepository.deleteById(id);
	    }
	    
	    public List<Voiture> trouverVoituresDisponibles(LocalDateTime dateDebut, LocalDateTime dateFin) {
	        List<Voiture> voituresDisponibles = new ArrayList<>();

	        // Récupérer les réservations qui chevauchent la période demandée
	        List<Reservation> reservationsChevauchantes = reservationRepository.findReservationsOverlapping(dateDebut, dateFin);

	        // Obtenir tous les véhicules
	        List<Voiture> toutesLesVoitures = voitureRepository.findAll();

	        // Créer un ensemble d'IDs de voitures réservées
	        Set<Long> voituresReserveesIds = reservationsChevauchantes.stream()
	            .map(reservation -> reservation.getVoiture().getId())
	            .collect(Collectors.toSet());

	        // Filtrer les voitures disponibles
	        for (Voiture voiture : toutesLesVoitures) {
	            if (!voituresReserveesIds.contains(voiture.getId())) {
	                voituresDisponibles.add(voiture);
	            }
	        }

	        // Retourner la liste des voitures disponibles
	        return voituresDisponibles;
	    }
	}


