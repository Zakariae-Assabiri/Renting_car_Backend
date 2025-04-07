package Car.project.Entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import Car.project.dto.TokenType;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    private LocalDateTime expirationDate;

    @Enumerated(EnumType.STRING)
    private TokenType type;

    @ManyToOne
    private User user;

    public Token(String value, TokenType type, User user, LocalDateTime expirationDate) {
        this.value = value;
        this.type = type;
        this.user = user;
        this.expirationDate = expirationDate;
    }
}
