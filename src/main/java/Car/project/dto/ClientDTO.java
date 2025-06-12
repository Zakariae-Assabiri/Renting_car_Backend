package Car.project.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClientDTO {
	@NotBlank(message = "error.client.cname.notblank") 
    private String cname;
	@NotBlank(message = "error.client.adresse.notblank") 
    private String adresse;
	@NotBlank(message = "error.client.nationalite.notblank") 
    private String nationalite;
    private String adresseEtranger;
    private String passeport;
    private String delivreLePasseport;
    @NotBlank(message = "error.client.cin.notblank") 
    private String cin;
    private String cinDelivreLe;
    @NotBlank(message = "error.client.tel.notblank") 
    private String tel;
    @NotBlank(message = "error.client.permis.notblank") 
    private String permis;
    private String permisDelivreLe;
    private String permisDelivreAu; 
    private MultipartFile photoCIN;
    private MultipartFile photoPermis;

}
