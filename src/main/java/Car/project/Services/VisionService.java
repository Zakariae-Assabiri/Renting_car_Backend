package Car.project.Services;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service
public class VisionService {

    private final ImageAnnotatorClient visionClient;

    // Constructeur qui initialise le client via la clé JSON
    public VisionService() throws Exception {
        InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("vision-key.json");
        if (serviceAccount == null) {
            throw new Exception("❌ Fichier de clé 'vision-key.json' introuvable !");
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

        ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        visionClient = ImageAnnotatorClient.create(settings);
    }

    // Méthode pour extraire le texte d’une image
    public String extractFullText(String imagePath) throws IOException {
        // Charger le fichier image
        ByteString imgBytes = ByteString.copyFrom(Files.readAllBytes(Paths.get(imagePath)));
        Image image = Image.newBuilder().setContent(imgBytes).build();

        Feature feature = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feature)
                .setImage(image)
                .build();

        // Appeler l’API Vision
        List<AnnotateImageResponse> responses = visionClient.batchAnnotateImages(List.of(request)).getResponsesList();
        if (!responses.isEmpty()) {
            AnnotateImageResponse res = responses.get(0);
            if (res.hasError()) {
                return "Erreur Vision API : " + res.getError().getMessage();
            }
            // getTextAnnotationsList().get(0) contient généralement tout le texte détecté
            return res.getTextAnnotationsList().get(0).getDescription();
        }
        return "";
    }
    public Map<String, String> extractFields(String fullText) {
        Map<String, String> fields = new HashMap<>();

        // Exemple de pattern pour CIN (à adapter selon le format)
        Pattern cinPattern = Pattern.compile("\\b([A-Z]{1,2}\\d{5,7})\\b");
        Matcher cinMatcher = cinPattern.matcher(fullText);
        if (cinMatcher.find()) {
            fields.put("CIN", cinMatcher.group(1));
        }

        // Exemple de pattern pour "Nom"
        Pattern nomPattern = Pattern.compile("(?i)Nom[:\\s]+([^\\n]+)");
        Matcher nomMatcher = nomPattern.matcher(fullText);
        if (nomMatcher.find()) {
            fields.put("Nom", nomMatcher.group(1).trim());
        }

        // Exemple de pattern pour "Prénom"
        Pattern prenomPattern = Pattern.compile("(?i)Prénom[:\\s]+([^\\n]+)");
        Matcher prenomMatcher = prenomPattern.matcher(fullText);
        if (prenomMatcher.find()) {
            fields.put("Prenom", prenomMatcher.group(1).trim());
        }

        return fields;
    }
}
