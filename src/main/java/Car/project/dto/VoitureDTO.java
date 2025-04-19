package Car.project.dto;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class VoitureDTO {

    private String vname;
    private String couleur;
    private String marque;
    private String matricule;
    private String modele;
    private String carburant;
    private Integer capacite;
    private String type;
    private Double prixDeBase;
    private Boolean estAutomate;
    private MultipartFile photo;  // Représente la photo à uploader
}
