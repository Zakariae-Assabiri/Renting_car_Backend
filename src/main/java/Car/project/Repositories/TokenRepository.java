package Car.project.Repositories;

import Car.project.Entities.Token;
import Car.project.dto.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByValueAndType(String value, TokenType type);
}
