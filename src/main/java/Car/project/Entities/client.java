package Car.project.Entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 50, message = "Le nom ne peut pas dépasser 50 caractères")
    private String cname;

    @NotBlank(message = "L'adresse est obligatoire")
    @Size(max = 100, message = "L'adresse ne peut pas dépasser 100 caractères")
    private String adresse;

    @NotBlank(message = "La nationalité est obligatoire")
    private String nationalite;

    @Size(max = 100, message = "L'adresse à l'étranger ne peut pas dépasser 100 caractères")
    private String adresseEtranger;

    @Size(max = 20, message = "Le passeport ne peut pas dépasser 20 caractères")
    private String passeport;

    private String delivreLePasseport;

    @NotBlank(message = "Le CIN est obligatoire")
    @Size(max = 15, message = "Le CIN ne peut pas dépasser 15 caractères")
    private String cin;

    private String CinDelivreLe;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Le numéro de téléphone doit être valide")
    private String tel;

    @NotBlank(message = "Le numéro de permis est obligatoire")
    private String permis;

    private String PermisDelivreLe;

    private String PermisDelivreAu; 
}
