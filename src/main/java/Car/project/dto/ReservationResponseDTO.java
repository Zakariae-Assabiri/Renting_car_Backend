package Car.project.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservationResponseDTO {
    private Long id;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private float montantTotal; // <-- CHAMP AJOUTÉ
    private float acompte;
    private String statut;
    
    private VoitureDetailDTO voiture;
    private ClientDetailDTO client;
    private ClientDetailDTO conducteurSecondaire;
}