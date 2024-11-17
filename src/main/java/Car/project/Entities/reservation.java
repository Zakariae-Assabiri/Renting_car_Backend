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
    private Long id;

    @ManyToOne
    @NotNull(message = "La voiture est obligatoire")
    @JoinColumn(name = "voiture_id", nullable = false)
    private Voiture voiture;

    @ManyToOne
    @NotNull(message = "Le client est obligatoire")
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "conducteur_secondaire_id")
    private Client conducteurSecondaire;

    @NotNull(message = "La date et l'heure de début sont obligatoires")
    private LocalDateTime dateDebut;

    @NotNull(message = "La date et l'heure de retour sont obligatoires")
    private LocalDateTime dateFin;

    @NotNull(message = "Le montant total est obligatoire")
    private float montantTotal;

    private float acompte;

    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("La date et l'heure de fin ne peuvent pas être antérieures à la date et l'heure de début.");
        }
    }
}
