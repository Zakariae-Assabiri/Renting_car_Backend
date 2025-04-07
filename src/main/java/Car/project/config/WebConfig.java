package Car.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Appliquer à toutes les routes de l'application
                .allowedOrigins("http://localhost:4200")  // Frontend Angular
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Méthodes autorisées
                .allowedHeaders("*")  // Autoriser tous les en-têtes
                .allowCredentials(true)  // Permettre l'utilisation des cookies (si nécessaire)
                .maxAge(3600);  // Durée du cache de CORS
    }
}
