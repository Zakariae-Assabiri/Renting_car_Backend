package Car.project.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ClientDTO {
    private String cname;
    private String adresse;
    private String nationalite;
    private String adresseEtranger;
    private String passeport;
    private String delivreLePasseport;
    private String cin;
    private String cinDelivreLe;
    private String tel;
    private String permis;
    private String permisDelivreLe;
    private String permisDelivreAu;

    private MultipartFile photoCIN;
    private MultipartFile photoPermis;

}
