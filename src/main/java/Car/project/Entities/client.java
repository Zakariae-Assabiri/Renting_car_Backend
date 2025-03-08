package Car.project.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.PostLoad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import Car.project.util.EncryptionUtil;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identifiant unique du client

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 50, message = "Le nom ne peut pas dépasser 50 caractères")
    private String cname; // Nom du client

    @NotBlank(message = "L'adresse est obligatoire")
    @Size(max = 100, message = "L'adresse ne peut pas dépasser 100 caractères")
    private String adresse; // Adresse du client

    @NotBlank(message = "La nationalité est obligatoire")
    private String nationalite; // Nationalité du client

    @Size(max = 100, message = "L'adresse à l'étranger ne peut pas dépasser 100 caractères")
    private String adresseEtranger; // Adresse à l'étranger (optionnelle)

    @Size(max = 20, message = "Le passeport ne peut pas dépasser 20 caractères")
    private String passeport; // Numéro de passeport (optionnel)

    private String delivreLePasseport; // Date de délivrance du passeport (optionnelle)

    @NotBlank(message = "Le CIN est obligatoire")
    @Size(max = 15, message = "Le CIN ne peut pas dépasser 15 caractères")
    private String cin; // Numéro de CIN du client

    private String CinDelivreLe; // Date de délivrance du CIN (optionnelle)

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Le numéro de téléphone doit être valide")
    private String tel; // Numéro de téléphone du client

    @NotBlank(message = "Le numéro de permis est obligatoire")
    private String permis; // Numéro de permis de conduire

    private String PermisDelivreLe; // Date de délivrance du permis (optionnelle)
    private String PermisDelivreAu; // Lieu de délivrance du permis (optionnelle)

    /**
     * Chiffrement des données sensibles avant la persistance.
     * Cette méthode est appelée automatiquement avant que l'entité ne soit sauvegardée ou mise à jour.
     */
    @PrePersist
    @PreUpdate
    private void encryptData() {
        try {
            this.cname = EncryptionUtil.encrypt(this.cname);
            this.adresse = EncryptionUtil.encrypt(this.adresse);
            this.adresseEtranger = EncryptionUtil.encrypt(this.adresseEtranger);
            this.passeport = EncryptionUtil.encrypt(this.passeport);
            this.cin = EncryptionUtil.encrypt(this.cin);
            this.tel = EncryptionUtil.encrypt(this.tel);
            this.permis = EncryptionUtil.encrypt(this.permis);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du chiffrement des données", e);
        }
    }

    /**
     * Déchiffrement des données sensibles après le chargement.
     * Cette méthode est appelée automatiquement après que l'entité est chargée depuis la base de données.
     */
    @PostLoad
    private void decryptData() {
        try {
            this.cname = EncryptionUtil.decrypt(this.cname);
            this.adresse = EncryptionUtil.decrypt(this.adresse);
            this.adresseEtranger = EncryptionUtil.decrypt(this.adresseEtranger);
            this.passeport = EncryptionUtil.decrypt(this.passeport);
            this.cin = EncryptionUtil.decrypt(this.cin);
            this.tel = EncryptionUtil.decrypt(this.tel);
            this.permis = EncryptionUtil.decrypt(this.permis);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du déchiffrement des données", e);
        }
    }
}