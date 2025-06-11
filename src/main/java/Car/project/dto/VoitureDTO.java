package Car.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class VoitureDTO {
    
    @NotBlank(message = "error.voiture.vname.notblank")
    private String vname;
    
    @NotBlank(message = "error.voiture.marque.notblank")
    private String marque;
    
    @NotBlank(message = "error.voiture.matricule.notblank")
    private String matricule;
    
    @NotBlank(message = "error.voiture.modele.notblank")
    private String modele;
    
    @NotNull(message = "error.voiture.prix.notnull")
    private Double prixDeBase;
    
    private String couleur;
    private String carburant;
    private Integer capacite;
    private String type;
    private Boolean estAutomate;
    private MultipartFile photo;
}