package Car.project.Services;

import Car.project.Entities.Client;
import Car.project.Entities.Reservation;
import Car.project.Entities.Voiture;
import Car.project.Repositories.ReservationRepository;
import Car.project.Repositories.VoitureRepository;
import Car.project.dto.ClientDetailDTO;
import Car.project.dto.ReservationRequestDTO;
import Car.project.dto.ReservationResponseDTO;
import Car.project.dto.VoitureDetailDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private VoitureRepository voitureRepository; // <-- Injection nécessaire
    @Autowired
    private ClientService clientService; // <-- Injection nécessaire

    // --- Méthodes publiques pour le contrôleur ---
    @Transactional
    public List<ReservationResponseDTO> getAllReservationsDto() {
        return reservationRepository.findAll().stream()
                .map(this::mapToReservationResponseDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public ReservationResponseDTO getReservationDtoById(Long id) {
        Reservation reservation = findEntityById(id);
        return mapToReservationResponseDTO(reservation);
    }
    @Transactional
    public ReservationResponseDTO createReservation(ReservationRequestDTO dto) {
        // Règle métier 1 : Valider les dates
        if (dto.getDateDebut().isAfter(dto.getDateFin())) {
            throw new IllegalArgumentException("error.dates.invalid");
        }

        // Règle métier 2 : Vérifier la disponibilité de la voiture
        List<Voiture> voituresDisponibles = voitureRepository.findVoituresDisponibles(dto.getDateDebut(), dto.getDateFin());
        boolean isVoitureDisponible = voituresDisponibles.stream().anyMatch(v -> v.getId().equals(dto.getVoitureId()));
        if (!isVoitureDisponible) {
            throw new IllegalStateException("error.reservation.voiture.notavailable");
        }

        // Récupérer les entités
        Voiture voiture = voitureRepository.findById(dto.getVoitureId()).orElseThrow(() -> new EntityNotFoundException("error.voiture.notfound"));
        Client client = clientService.findClientEntityById(dto.getClientId());
        Client conducteurSecondaire = null;
        if (dto.getConducteurSecondaireId() != null) {
            conducteurSecondaire = clientService.findClientEntityById(dto.getConducteurSecondaireId());
        }

        // Règle métier 3 : Calculer le prix total
        long nombreJours = ChronoUnit.DAYS.between(dto.getDateDebut(), dto.getDateFin());
        if (nombreJours == 0) nombreJours = 1; // Minimum 1 jour de location
        float montantTotal = (float) (nombreJours * voiture.getPrixDeBase());

        // Créer et sauvegarder l'entité
        Reservation reservation = new Reservation();
        reservation.setVoiture(voiture);
        reservation.setClient(client);
        reservation.setConducteurSecondaire(conducteurSecondaire);
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());
        reservation.setAcompte(dto.getAcompte());
        reservation.setStatut(dto.getStatut() != null ? dto.getStatut() : "Confirmée");
        reservation.setMontantTotal(montantTotal);

        Reservation savedReservation = reservationRepository.save(reservation);
        return mapToReservationResponseDTO(savedReservation);
    }
    /**
     * Trouve toutes les réservations pour un client donné.
     * @param clientId L'identifiant du client.
     * @return Une liste de ReservationResponseDTO.
     * @throws EntityNotFoundException si le client n'existe pas.
     */
    @Transactional
    public List<ReservationResponseDTO> findReservationsByClientId(Long clientId) {
        // Règle métier : On vérifie d'abord que le client existe.
        // Cela lance une erreur 404 propre si l'ID est invalide.
        clientService.findClientEntityById(clientId);

        // On récupère les entités de la base de données
        List<Reservation> reservations = reservationRepository.findByClientId(clientId);

        // On convertit la liste d'entités en liste de DTOs et on la retourne
        return reservations.stream()
                .map(this::mapToReservationResponseDTO)
                .collect(Collectors.toList());
    }
    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new EntityNotFoundException("error.reservation.notfound");
        }
        reservationRepository.deleteById(id);
    }
    
    // La mise à jour d'une réservation peut être complexe (recalcul du prix, etc.)
    // Pour l'instant, nous la laissons de côté pour simplifier.

    // --- Méthodes internes et de mapping ---

    private Reservation findEntityById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("error.reservation.notfound"));
    }
    
    // Le mapping reste ici, car ce service est le seul à connaître la structure d'une ReservationResponseDTO
    private ReservationResponseDTO mapToReservationResponseDTO(Reservation reservation) {
        // ... (logique de mapping vers DTO de réponse) ...
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(reservation.getId());
        dto.setDateDebut(reservation.getDateDebut());
        dto.setDateFin(reservation.getDateFin());
        dto.setMontantTotal(reservation.getMontantTotal());
        dto.setAcompte(reservation.getAcompte());
        dto.setStatut(reservation.getStatut());
        // ... etc ...
        return dto;
    }
    @Transactional
    public List<Reservation> getReservationsByUserId(Long userId) {
        return reservationRepository.findReservationsByUserId(userId);
    }
}