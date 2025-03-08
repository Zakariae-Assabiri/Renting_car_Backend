package Car.project.Controllers;

import Car.project.Services.TarificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/tarification")
public class TarificationController {

    @Autowired
    private TarificationService tarificationService;

    @GetMapping
    public ResponseEntity<?> getTarifDynamique(
            @RequestParam("voitureId") Long voitureId,
            @RequestParam("dateDebut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam("dateFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {

        // Validation des paramètres
        if (voitureId == null || voitureId <= 0) {
            return ResponseEntity.badRequest().body("L'ID de la voiture est invalide.");
        }

        if (dateDebut == null || dateFin == null || dateDebut.isAfter(dateFin)) {
            return ResponseEntity.badRequest().body("La date de début doit être antérieure à la date de fin.");
        }

        try {
            // Calcul du tarif dynamique
            double tarif = tarificationService.calculerTarifDynamique(voitureId, dateDebut, dateFin);
            return ResponseEntity.ok(tarif);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite lors du calcul du tarif.");
        }
    }
}