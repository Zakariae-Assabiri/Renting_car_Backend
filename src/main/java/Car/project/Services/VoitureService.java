package Car.project.Services;

import Car.project.Entities.Voiture;
import Car.project.Repositories.ReservationRepository;
import Car.project.Repositories.VoitureRepository;
import Car.project.dto.VoitureDetailDTO;
import Car.project.dto.VoitureDTO;
import Car.project.exception.ResourceAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoitureService {

    @Autowired
    private VoitureRepository voitureRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    // --- Méthodes publiques pour le contrôleur ---

    public List<VoitureDetailDTO> getAllVoituresDto() {
        return voitureRepository.findAll().stream()
                .map(this::mapToVoitureDetailDTO)
                .collect(Collectors.toList());
    }

    public VoitureDetailDTO getVoitureDtoById(Long id) {
        Voiture voiture = findVoitureEntityById(id);
        return mapToVoitureDetailDTO(voiture);
    }

    public VoitureDetailDTO createVoiture(VoitureDTO dto) throws IOException {
        if (voitureRepository.existsByMatricule(dto.getMatricule())) {
            throw new ResourceAlreadyExistsException("error.voiture.matricule.exists");
        }

        Voiture voiture = new Voiture();
        mapDtoToEntity(dto, voiture);

        Voiture savedVoiture = voitureRepository.save(voiture);
        return mapToVoitureDetailDTO(savedVoiture);
    }

    public VoitureDetailDTO updateVoiture(Long id, VoitureDTO dto) throws IOException {
        Voiture existingVoiture = findVoitureEntityById(id);
        mapDtoToEntity(dto, existingVoiture);

        Voiture updatedVoiture = voitureRepository.save(existingVoiture);
        return mapToVoitureDetailDTO(updatedVoiture);
    }

    public void deleteVoiture(Long id) {
        Voiture voiture = findVoitureEntityById(id);
        
        // Règle métier : On ne peut pas supprimer une voiture si elle a des réservations futures
        if (reservationRepository.existsByVoitureAndDateFinAfter(voiture, LocalDateTime.now())) {
            throw new IllegalStateException("error.voiture.delete.hasreservations");
        }

        voitureRepository.delete(voiture);
    }

    public List<VoitureDetailDTO> findVoituresDisponiblesDto(LocalDateTime dateDebut, LocalDateTime dateFin) {
        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("error.dates.invalid");
        }
        List<Voiture> voituresDisponibles = voitureRepository.findVoituresDisponibles(dateDebut, dateFin);
        return voituresDisponibles.stream()
                .map(this::mapToVoitureDetailDTO)
                .collect(Collectors.toList());
    }
    
    // --- Méthodes internes et de mapping ---

    public Voiture findVoitureEntityById(Long id) {
        return voitureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("error.voiture.notfound"));
    }

    private void mapDtoToEntity(VoitureDTO dto, Voiture voiture) throws IOException {
        voiture.setVname(dto.getVname());
        voiture.setMarque(dto.getMarque());
        voiture.setMatricule(dto.getMatricule());
        voiture.setModele(dto.getModele());
        voiture.setPrixDeBase(dto.getPrixDeBase());
        voiture.setCouleur(dto.getCouleur());
        voiture.setCarburant(dto.getCarburant());
        voiture.setCapacite(dto.getCapacite());
        voiture.setType(dto.getType());
        voiture.setEstAutomate(dto.getEstAutomate());

        if (dto.getPhoto() != null && !dto.getPhoto().isEmpty()) {
            voiture.setPhoto(dto.getPhoto().getBytes());
        }
    }

    private VoitureDetailDTO mapToVoitureDetailDTO(Voiture voiture) {
        VoitureDetailDTO dto = new VoitureDetailDTO();
        dto.setId(voiture.getId());
        dto.setVname(voiture.getVname());
        dto.setMarque(voiture.getMarque());
        dto.setMatricule(voiture.getMatricule());
        dto.setModele(voiture.getModele());
        dto.setPrixDeBase(voiture.getPrixDeBase());
        dto.setCouleur(voiture.getCouleur());
        dto.setCarburant(voiture.getCarburant());
        dto.setCapacite(voiture.getCapacite());
        dto.setType(voiture.getType());
        dto.setEstAutomate(voiture.getEstAutomate());
        return dto;
    }
}