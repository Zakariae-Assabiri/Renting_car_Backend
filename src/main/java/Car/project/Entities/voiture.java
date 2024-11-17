package Car.project.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Voiture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String vname;

    private String couleur;

    @NotBlank(message = "La marque est obligatoire")
    private String marque;

    @NotBlank(message = "La matricule est obligatoire")
    @Size(max = 10, message = "La matricule ne peut pas dépasser 10 caractères")
    private String matricule;

    @NotBlank(message = "Le modèle est obligatoire")
    private String modele;

    @NotBlank(message = "Le type de carburant est obligatoire")
    private String carburant;

    private Integer capacite;

    @NotBlank(message = "Le type de véhicule est obligatoire")
    private String type;
    
    @NotNull(message = "Le prix de base est obligatoire")
    private float prixDeBase;
    
    private Boolean estAutomate;
}
