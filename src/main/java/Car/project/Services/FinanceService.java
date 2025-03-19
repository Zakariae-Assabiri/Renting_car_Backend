package Car.project.Services;

import java.time.LocalDateTime; 


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Car.project.Entities.Expense;
import Car.project.Repositories.ExpenseRepository;
import Car.project.Repositories.ReservationRepository;

@Service
public class FinanceService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    // Ajouter une dépense
    public Expense ajouterExpense(Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException("La dépense ne peut pas être nulle.");
        }
        return expenseRepository.save(expense);
    }

    // Calculer le revenu total des réservations confirmées sur une période
    public Double calculerRevenuTotalConfirmeParPeriode(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Les dates fournies sont invalides.");
        }
        Double revenuTotal = reservationRepository.sumMontantTotalConfirmedByPeriod(startDate, endDate);
        return (revenuTotal != null) ? revenuTotal : 0.0;
    }

    // Calculer les dépenses totales sur une période
    public Double calculerDepensesTotales(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Les dates fournies sont invalides.");
        }
        Double depensesTotales = expenseRepository.sumMontantByDateBetween(startDate, endDate);
        return (depensesTotales != null) ? depensesTotales : 0.0;
    }

    // Générer un rapport financier
    public Double[] genererRapportFinancier(LocalDateTime startDate, LocalDateTime endDate) {
        Double revenuTotal = calculerRevenuTotalConfirmeParPeriode(startDate, endDate);
        Double depensesTotales = calculerDepensesTotales(startDate, endDate);
        Double profit = revenuTotal - depensesTotales;

        // Retourner un tableau de Double
        return new Double[]{revenuTotal, depensesTotales, profit};
    }
}