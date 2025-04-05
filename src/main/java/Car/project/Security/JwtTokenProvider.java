package Car.project.Security;


import Car.project.Entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;
import java.util.Map;


import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String SECRET_KEY = "Oq3F9WzKQ4ZjJsN7qV4m6P2Tb/XkGJ0H2n8VYx6o4TQ=";  // Utilisez la clé secrète générée précédemment
    private final long EXPIRATION_TIME = 86400000;  // 1 jour

    public String generateToken(User user) {
    	 Map<String, Object> claims = Map.of(
    		        "authorities", user.getRoles().stream()
    		            .map(role -> role.getNom().name()) // Exemple : "ROLE_ADMIN"
    		            .collect(Collectors.toList())
    		    );
        return Jwts.builder()
        		.setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
