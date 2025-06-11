package Car.project.Controllers;

import Car.project.dto.VoitureDetailDTO;
import Car.project.dto.VoitureDTO;
import Car.project.Services.VoitureService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // Gardez cette ligne si vous en avez besoin pour votre frontend
@RequestMapping("/api/voitures")
public class VoitureController {

    @Autowired
    private VoitureService voitureService;

    /**
     * Obtenir toutes les voitures. Retourne une liste de DTOs.
     */
    @GetMapping
    public ResponseEntity<List<VoitureDetailDTO>> getAllVoitures() {
        List<VoitureDetailDTO> voitures = voitureService.getAllVoituresDto();
        return ResponseEntity.ok(voitures);
    }

    /**
     * Obtenir une voiture par son ID. Retourne un DTO de détail.
     * La gestion de l'erreur "Not Found" est déléguée au service et au GlobalExceptionHandler.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VoitureDetailDTO> getVoitureById(@PathVariable Long id) {
        VoitureDetailDTO voitureDto = voitureService.getVoitureDtoById(id);
        return ResponseEntity.ok(voitureDto);
    }

    /**
     * Créer une nouvelle voiture.
     * Accepte un formulaire multipart (données + fichier), valide les données,
     * et délègue la création au service.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VoitureDetailDTO> createVoiture(@Valid @ModelAttribute VoitureDTO voitureDto) throws IOException {
        VoitureDetailDTO createdVoitureDto = voitureService.createVoiture(voitureDto);

        return new ResponseEntity<VoitureDetailDTO>(createdVoitureDto, HttpStatus.CREATED);
    }

    /**
     * Mettre à jour une voiture existante.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VoitureDetailDTO> updateVoiture(@PathVariable Long id, @Valid @ModelAttribute VoitureDTO voitureRequestDto) throws IOException {
        VoitureDetailDTO updatedVoitureDto = voitureService.updateVoiture(id, voitureRequestDto);
        return ResponseEntity.ok(updatedVoitureDto);
    }

    /**
     * Supprimer une voiture.
     * La gestion des erreurs (Not Found, voiture avec réservations) est déléguée au service.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVoiture(@PathVariable Long id) {
        voitureService.deleteVoiture(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint spécial pour récupérer uniquement la photo d'une voiture.
     * Utile pour afficher des images sans charger toutes les autres données.
     */
    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getVoiturePhoto(@PathVariable Long id) {
        // Il faut ajouter la méthode getVoiturePhotoById au service
        byte[] photoBytes = voitureService.findVoitureEntityById(id).getPhoto();
        if (photoBytes != null) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(photoBytes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Trouver les voitures disponibles entre deux dates.
     * L'annotation @DateTimeFormat aide Spring à comprendre le format des dates envoyées par le client.
     */
    @GetMapping("/disponibles")
    public ResponseEntity<List<VoitureDetailDTO>> getVoituresDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        List<VoitureDetailDTO> voituresDisponibles = voitureService.findVoituresDisponiblesDto(dateDebut, dateFin);
        return ResponseEntity.ok(voituresDisponibles);
    }
}