package Car.project.dto;

import lombok.Data;

@Data
public class VoitureDetailDTO {
    private Long id;
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
    // On n'inclut PAS le champ photo
}