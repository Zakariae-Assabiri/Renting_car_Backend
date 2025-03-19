package Car.project.Controllers;

import java.time.LocalDateTime;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Car.project.Entities.Expense;
import Car.project.Services.FinanceService;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    @Autowired
    private FinanceService financeService;

    // Ajouter une dépense
    @PostMapping("/expense")
    public ResponseEntity<Expense> ajouterExpense(@RequestBody Expense expense) {
        if (expense == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Expense nouvelleExpense = financeService.ajouterExpense(expense);
        return ResponseEntity.ok(nouvelleExpense);
    }

    // Générer un rapport financier
    @GetMapping("/rapport")
        public ResponseEntity<String> genererRapportFinancier(
                @RequestParam LocalDateTime startDate,
                @RequestParam LocalDateTime endDate) {
            try {
                // Appeler la méthode du service
                Double[] rapport = financeService.genererRapportFinancier(startDate, endDate);

                // Vérifier que le tableau n'est pas null et a la bonne taille
                if (rapport == null || rapport.length != 3) {
                    return ResponseEntity.badRequest().body("Erreur lors de la génération du rapport.");
                }

                // Extraire les valeurs du tableau
                Double revenuTotal = rapport[0];
                Double depensesTotales = rapport[1];
                Double profit = rapport[2];

                // Formater la réponse
                String response = String.format(
                    "Revenu Total: %.2f, Dépenses Totales: %.2f, Profit: %.2f",
                    revenuTotal, depensesTotales, profit
                );

                // Retourner la réponse
                return ResponseEntity.ok(response);
            } catch (IllegalArgumentException e) {
                // Gérer les erreurs de validation des dates
                return ResponseEntity.badRequest().body("Dates invalides : " + e.getMessage());
            } catch (Exception e) {
                // Gérer les autres erreurs
                return ResponseEntity.internalServerError().body("Erreur interne : " + e.getMessage());
            }
        }
    }