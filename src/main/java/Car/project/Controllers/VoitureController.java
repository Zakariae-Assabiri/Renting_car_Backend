package Car.project.Controllers;

import Car.project.Entities.Voiture;
import Car.project.Services.VoitureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Voiture> createVoiture(@RequestBody Voiture voiture) {
        Voiture newVoiture = voitureService.createVoiture(voiture);
        return new ResponseEntity<>(newVoiture, HttpStatus.CREATED);
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
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Voiture> updateVoiture(@PathVariable Long id,@RequestBody Voiture voiture) {
    	voiture.setId(id); 
    	Voiture updatedVoiture = voitureService.updateVoiture(voiture);
        return ResponseEntity.ok(updatedVoiture);
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