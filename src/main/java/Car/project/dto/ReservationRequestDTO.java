package Car.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservationRequestDTO {

    @NotNull(message = "error.reservation.voitureid.notnull")
    private Long voitureId;

    @NotNull(message = "error.reservation.clientid.notnull")
    private Long clientId;

    private Long conducteurSecondaireId; // Optionnel

    @NotNull(message = "error.reservation.datedebut.notnull")
    private LocalDateTime dateDebut;

    @NotNull(message = "error.reservation.datefin.notnull")
    private LocalDateTime dateFin;

    private float acompte;
    private String statut;
}