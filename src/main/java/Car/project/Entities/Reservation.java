package Car.project.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identifiant unique de la réservation

    @ManyToOne
    @NotNull(message = "La voiture est obligatoire")
    @JoinColumn(name = "voiture_id", nullable = false)
    private Voiture voiture; // Voiture réservée

    @ManyToOne
    @NotNull(message = "Le client est obligatoire")
    @JoinColumn(name = "client_id", nullable = false)
    private Client client; // Client qui effectue la réservation

    @ManyToOne
    @JoinColumn(name = "conducteur_secondaire_id")
    private Client conducteurSecondaire; // Conducteur secondaire (optionnel)

    @NotNull(message = "La date et l'heure de début sont obligatoires")
    private LocalDateTime dateDebut; // Date et heure de début de la réservation

    @NotNull(message = "La date et l'heure de retour sont obligatoires")
    private LocalDateTime dateFin; // Date et heure de fin de la réservation

    @NotNull(message = "Le montant total est obligatoire")
    private float montantTotal; // Montant total de la réservation

    private float acompte; // Acompte payé (optionnel)
    private String statut; // Statut de la réservation (ex: "Confirmée", "Annulée")

    /**
     * Validation des dates avant la persistance ou la mise à jour.
     * Cette méthode vérifie que la date de fin n'est pas antérieure à la date de début.
     */
    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("La date et l'heure de fin ne peuvent pas être antérieures à la date et l'heure de début.");
        }
    }
}