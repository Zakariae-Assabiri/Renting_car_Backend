package Car.project.dto;

import Car.project.Entities.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class LoginRequest {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    } 
    public String getPassword() {
        return password;
    }
}
