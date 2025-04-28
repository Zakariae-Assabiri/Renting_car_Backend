package Car.project.Controllers;

import Car.project.Services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/dashboard")

public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/revenu-total")
    public ResponseEntity<Double> getRevenuTotal() {
        return ResponseEntity.ok(dashboardService.calculerRevenuTotal());
    }

    @GetMapping("/taux-disponibilite")
    public ResponseEntity<Double> getTauxDisponibilite(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        return ResponseEntity.ok(dashboardService.calculerTauxDisponibilite(dateDebut, dateFin));
    }

    @GetMapping("/revenu-moyen-par-voiture/{voitureId}")
    public ResponseEntity<Double> getRevenuMoyenParVoiture(@PathVariable Long voitureId) {
        return ResponseEntity.ok(dashboardService.calculerRevenuMoyenParVoiture(voitureId));
    }

    @GetMapping("/nombre-reservations-par-voiture")
    public ResponseEntity<Long> getNombreReservationsParVoiture(@RequestParam Long voitureId) {
        return ResponseEntity.ok(dashboardService.calculerNombreReservationsParVoiture(voitureId));
    }

    @GetMapping("/taux-annulation")
    public ResponseEntity<Double> getTauxAnnulation() {
        return ResponseEntity.ok(dashboardService.calculerTauxAnnulation());
    }

    @GetMapping("/duree-moyenne-reservations")
    public ResponseEntity<Double> getDureeMoyenneReservations() {
        return ResponseEntity.ok(dashboardService.calculerDureeMoyenneReservations());
    }

    @GetMapping("/revenu-par-periode")
    public ResponseEntity<Double> getRevenuParPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        return ResponseEntity.ok(dashboardService.calculerRevenuParPeriode(dateDebut, dateFin));
    }
}