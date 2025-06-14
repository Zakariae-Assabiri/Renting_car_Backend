package Car.project.dto;


import lombok.Data;

@Data
public class ClientDetailDTO {
    private Long id;
    private String cname;
    private String adresse;
    private String nationalite;
    private String adresseEtranger;
    private String passeport;
    private String delivreLePasseport;
    private String cin;
    private String CinDelivreLe;
    private String tel;
    private String permis;
    private String PermisDelivreLe;
    private String PermisDelivreAu;
    // On n'inclut PAS les champs photoCIN et photoPermis
}