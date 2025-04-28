package Car.project.Entities;

import jakarta.persistence.Entity; 
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
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


    @Size(max = 100, message = "Le nom ne peut pas dépasser 50 caractères")
    private String cname; // Nom du client


    @Size(max = 100, message = "L'adresse ne peut pas dépasser 100 caractères")
    private String adresse; // Adresse du client


    private String nationalite; // Nationalité du client

    @Size(max = 100, message = "L'adresse à l'étranger ne peut pas dépasser 100 caractères")
    private String adresseEtranger; // Adresse à l'étranger (optionnelle)

    @Size(max = 100, message = "Le passeport ne peut pas dépasser 20 caractères")
    private String passeport; // Numéro de passeport (optionnel)

    private String delivreLePasseport; // Date de délivrance du passeport (optionnelle)


    @Size(max = 100, message = "Le CIN ne peut pas dépasser 15 caractères")
    private String cin; // Numéro de CIN du client

    private String CinDelivreLe; // Date de délivrance du CIN (optionnelle)


    private String tel; // Numéro de téléphone du client


    private String permis; // Numéro de permis de conduire

    private String PermisDelivreLe; // Date de délivrance du permis (optionnelle)
    private String PermisDelivreAu; // Lieu de délivrance du permis (optionnelle)
    @OneToOne
    @JoinColumn(name = "user_id") // colonne dans la table client qui va référencer l'id de user
    private User user;
    @Lob
    private byte[] photoCIN;

    @Lob
    private byte[] photoPermis;

    /**
     * Chiffrement des données sensibles avant la persistance.
     * Cette méthode est appelée automatiquement avant que l'entité ne soit sauvegardée ou mise à jour.
     */
    @PrePersist
    @PreUpdate
    private void encryptData() {
        try {
            this.cname = this.cname != null ? EncryptionUtil.encrypt(this.cname) : null;
            this.adresse = this.adresse != null ? EncryptionUtil.encrypt(this.adresse) : null;
            this.adresseEtranger = this.adresseEtranger != null ? EncryptionUtil.encrypt(this.adresseEtranger) : null;
            this.passeport = this.passeport != null ? EncryptionUtil.encrypt(this.passeport) : null;
            this.cin = this.cin != null ? EncryptionUtil.encrypt(this.cin) : null;
            this.tel = this.tel != null ? EncryptionUtil.encrypt(this.tel) : null;
            this.permis = this.permis != null ? EncryptionUtil.encrypt(this.permis) : null;
            this.photoCIN =  this.photoCIN != null ? EncryptionUtil.encryptBytes(this.photoCIN) : null;
            this.photoPermis = this.photoPermis != null ? EncryptionUtil.encryptBytes(this.photoPermis) : null;
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
    	        this.cname = this.cname != null ? EncryptionUtil.decrypt(this.cname) : null;
    	        this.adresse = this.adresse != null ? EncryptionUtil.decrypt(this.adresse) : null;
    	        this.adresseEtranger = this.adresseEtranger != null ? EncryptionUtil.decrypt(this.adresseEtranger) : null;
    	        this.passeport = this.passeport != null ? EncryptionUtil.decrypt(this.passeport) : null;
    	        this.cin = this.cin != null ? EncryptionUtil.decrypt(this.cin) : null;
    	        this.tel = this.tel != null ? EncryptionUtil.decrypt(this.tel) : null;
    	        this.permis = this.permis != null ? EncryptionUtil.decrypt(this.permis) : null;
    	        this.photoCIN = this.photoCIN != null ? EncryptionUtil.decryptBytes(this.photoCIN) : null;
    	        this.photoPermis =    this.photoPermis != null ? EncryptionUtil.decryptBytes(this.photoPermis) : null;
    	    } catch (Exception e) {
    	        throw new RuntimeException("Erreur lors du déchiffrement des données", e);
    	    }
    	}

    }