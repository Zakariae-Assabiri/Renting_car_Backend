package Car.project.Controllers;

import Car.project.Entities.Client;
import Car.project.Entities.Voiture;
import Car.project.Services.VoitureService;
import Car.project.dto.VoitureDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/voitures")
public class VoitureController {

    @Autowired
    private VoitureService voitureService;

    // Créer une nouvelle voiture
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Voiture> createVoiture(@ModelAttribute VoitureDTO voitureDTO) {
        try {
            Voiture voiture = new Voiture();
            voiture.setVname(voitureDTO.getVname());
            voiture.setCouleur(voitureDTO.getCouleur());
            voiture.setMarque(voitureDTO.getMarque());
            voiture.setMatricule(voitureDTO.getMatricule());
            voiture.setModele(voitureDTO.getModele());
            voiture.setCarburant(voitureDTO.getCarburant());
            voiture.setCapacite(voitureDTO.getCapacite());
            voiture.setType(voitureDTO.getType());
            voiture.setPrixDeBase(voitureDTO.getPrixDeBase());
            voiture.setEstAutomate(voitureDTO.getEstAutomate());

            if (voitureDTO.getPhoto() != null && !voitureDTO.getPhoto().isEmpty()) {
                voiture.setPhoto(voitureDTO.getPhoto().getBytes());
            }

            Voiture savedVoiture = voitureService.createVoiture(voiture);
            return new ResponseEntity<>(savedVoiture, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getVoiturePhoto(@PathVariable Long id) {
        Optional<Voiture> voiture = voitureService.getVoitureById(id);
        if (voiture.isPresent() && voiture.get().getPhoto() != null) {
            return ResponseEntity
                    .ok()
                    .header("Content-Type", "image/jpeg")  // tu peux adapter selon le type réel
                    .body(voiture.get().getPhoto());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Obtenir une voiture par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Voiture> getVoitureById(@PathVariable Long id) {
        Optional<Voiture> voiture = voitureService.getVoitureById(id);
        return voiture.map(ResponseEntity::ok)
                      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Obtenir toutes les voitures
    @GetMapping
    public List<Voiture> getAllVoitures() {
        return voitureService.getAllVoitures();
    }

    // Mettre à jour une voiture
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Voiture> updateVoiture(@PathVariable Long id, @ModelAttribute VoitureDTO voitureDTO) {
        try {
            Optional<Voiture> optionalVoiture = voitureService.getVoitureById(id);
            if (optionalVoiture.isPresent()) {
                Voiture voiture = optionalVoiture.get();
                voiture.setVname(voitureDTO.getVname());
                voiture.setCouleur(voitureDTO.getCouleur());
                voiture.setMarque(voitureDTO.getMarque());
                voiture.setMatricule(voitureDTO.getMatricule());
                voiture.setModele(voitureDTO.getModele());
                voiture.setCarburant(voitureDTO.getCarburant());
                voiture.setCapacite(voitureDTO.getCapacite());
                voiture.setType(voitureDTO.getType());
                voiture.setPrixDeBase(voitureDTO.getPrixDeBase());
                voiture.setEstAutomate(voitureDTO.getEstAutomate());

                if (voitureDTO.getPhoto() != null && !voitureDTO.getPhoto().isEmpty()) {
                    voiture.setPhoto(voitureDTO.getPhoto().getBytes());
                }

                Voiture updatedVoiture = voitureService.updateVoiture(voiture);
                return new ResponseEntity<>(updatedVoiture, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Supprimer une voiture
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteVoiture(@PathVariable Long id) {
        voitureService.deleteVoiture(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Trouver les voitures disponibles entre deux dates
    @GetMapping("/disponibles")
    public ResponseEntity<List<Voiture>> getVoituresDisponibles(
            @RequestParam("dateDebut") LocalDateTime dateDebut,
            @RequestParam("dateFin") LocalDateTime dateFin) {
        
        List<Voiture> voituresDisponibles = voitureService.trouverVoituresDisponibles(dateDebut, dateFin);
        return ResponseEntity.ok(voituresDisponibles);
    }
}