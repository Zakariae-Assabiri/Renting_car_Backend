package Car.project.Controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import Car.project.Entities.Depense;
import Car.project.Services.FinanceService;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    @Autowired
    private FinanceService financeService;

    //  Ajouter une dépense
    @PostMapping("/expense")
    public ResponseEntity<Depense> ajouterDepense(@RequestBody Depense expense) {
        if (expense == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Depense nouvelleDepense = financeService.createDepense(expense);
        return ResponseEntity.ok(nouvelleDepense);
    }

    // 📄 Obtenir une dépense par ID
    @GetMapping("/expense/{id}")
    public ResponseEntity<Depense> getDepenseById(@PathVariable Long id) {
        Optional<Depense> depense = financeService.getDepenseById(id);
        return depense.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    //  Obtenir toutes les dépenses
    @GetMapping("/expenses")
    public ResponseEntity<List<Depense>> getAllDepenses() {
        List<Depense> depenses = financeService.getAllDepenses();
        return ResponseEntity.ok(depenses);
    }

    //  Mettre à jour une dépense
    @PutMapping("/expense")
    public ResponseEntity<Depense> updateDepense(@RequestBody Depense depense) {
        if (depense == null || depense.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Depense updated = financeService.updateDepense(depense);
        return ResponseEntity.ok(updated);
    }

    //  Supprimer une dépense
    @DeleteMapping("/expense/{id}")
    public ResponseEntity<Void> deleteDepense(@PathVariable Long id) {
        financeService.deleteDepense(id);
        return ResponseEntity.noContent().build();
    }

    //  Générer un rapport financier complet
    @GetMapping("/rapport")
    public ResponseEntity<String> genererRapportFinancier(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            Double[] rapport = financeService.genererRapportFinancier(startDate, endDate);
            if (rapport == null || rapport.length != 3) {
                return ResponseEntity.badRequest().body("Erreur lors de la génération du rapport.");
            }
            String response = String.format(
                "📊 Rapport Financier :\nRevenu Total: %.2f €\nDépenses Totales: %.2f €\nProfit: %.2f €",
                rapport[0], rapport[1], rapport[2]
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("⛔ Dates invalides : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("❌ Erreur interne : " + e.getMessage());
        }
    }

    //  Calculer seulement le revenu total confirmé
    @GetMapping("/revenu")
    public ResponseEntity<Double> getRevenuTotal(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            return ResponseEntity.ok(financeService.calculerRevenuTotalConfirmeParPeriode(startDate, endDate));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(0.0);
        }
    }

    // calculer seulement les dépenses totales
    @GetMapping("/depenses-total")
    public ResponseEntity<Double> getDepensesTotales(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            return ResponseEntity.ok(financeService.calculerDepensesTotales(startDate, endDate));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(0.0);
        }
    }
}
