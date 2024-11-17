package Car.project.Services;

import Car.project.Repositories.ReservationRepository;
import Car.project.Repositories.VoitureRepository;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private VoitureRepository voitureRepository;

    // Méthode pour calculer le revenu total basé sur le montantTotal des réservations
    public Double calculerRevenuTotal() {
        // Calcul de la somme de tous les montants totaux des réservations
        return reservationRepository.sumMontantTotal();
    }

    // Méthode pour calculer les coûts totaux (fonctionnalité en attente)
    // public Double calculerCoutsTotaux() {
    //     // Exemple de coût : Maintenance ou autres dépenses liées aux véhicules
    //     return voitureRepository.sumMaintenanceCosts();
    // }

    // Méthode pour calculer le taux de disponibilité des véhicules
    public Double calculerTauxDisponibilite(LocalDate dateDebut, LocalDate dateFin) {
        int totalVoitures = (int) voitureRepository.count();
        
        // Compter les voitures réservées pendant la période
        int voituresReservees = reservationRepository.countReservedCarsInPeriod(dateDebut, dateFin);
        
        return ((double) (totalVoitures - voituresReservees) / totalVoitures) * 100;
    }
}
