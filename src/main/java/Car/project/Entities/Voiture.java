package Car.project.Entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
    private Long id; // Identifiant unique de la voiture

    @NotBlank(message = "Le nom est obligatoire")
    private String vname; // Nom de la voiture

    private String couleur; // Couleur de la voiture

    @NotBlank(message = "La marque est obligatoire")
    private String marque; // Marque de la voiture

    @NotBlank(message = "La matricule est obligatoire")
    @Size(max = 10, message = "La matricule ne peut pas dépasser 10 caractères")
    private String matricule; // Matricule de la voiture

    @NotBlank(message = "Le modèle est obligatoire")
    private String modele; // Modèle de la voiture

    @NotBlank(message = "Le type de carburant est obligatoire")
    private String carburant; // Type de carburant (ex: Essence, Diesel)

    private Integer capacite; // Capacité de la voiture (nombre de places)

    @NotBlank(message = "Le type de véhicule est obligatoire")
    private String type; // Type de véhicule (ex: Berline, SUV)

    @NotNull(message = "Le prix de base est obligatoire")
    private Double prixDeBase; // Prix de base de la location

    private Boolean estAutomate; // Indique si la voiture est automatique
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] photo;

}