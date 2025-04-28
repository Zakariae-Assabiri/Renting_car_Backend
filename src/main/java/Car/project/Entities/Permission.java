package Car.project.Entities;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String endpoint; // ex: "/api/clients", "/api/reservations", "/api/dashboard"
    private String method;  // ex: "GET", "POST"
    public Permission(String endpoint, String method) {
        this.endpoint = endpoint;
        this.method = method;
    }
}
