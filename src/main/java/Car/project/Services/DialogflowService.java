package Car.project.Services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.*;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import jakarta.annotation.PreDestroy;

import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class DialogflowService {

    private final SessionsClient sessionsClient;
    private final String projectId = "rentingcar-chat-csmh"; // Remplace avec ton ID de projet Google Cloud
    private static final Logger logger = LoggerFactory.getLogger(DialogflowService.class);

    public String detectIntent(String sessionId, String userMessage) throws Exception {
        SessionName session = SessionName.of(projectId, sessionId);
        TextInput.Builder textInput = TextInput.newBuilder()
                .setText(userMessage)
                .setLanguageCode("fr");  // Vérifie que la langue est bien "fr"

        QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
        DetectIntentRequest request = DetectIntentRequest.newBuilder()
                .setSession(session.toString())
                .setQueryInput(queryInput)
                .build();

        DetectIntentResponse response = sessionsClient.detectIntent(request);
        
        // 🔍 Log toutes les infos utiles
        System.out.println("🟢 Message envoyé : " + userMessage);
        System.out.println("🟢 Intent détecté : " + response.getQueryResult().getIntent().getDisplayName());
        System.out.println("🟢 Réponse Dialogflow : " + response.getQueryResult().getFulfillmentText());
        System.out.println("🟢 Paramètres détectés : " + response.getQueryResult().getParameters());

        return response.getQueryResult().getFulfillmentText();
    }

    
    public DialogflowService() throws Exception {
        InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("dialogflow-key.json");

        if (serviceAccount == null) {
            throw new Exception("❌ Fichier de clé introuvable !");
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        sessionsClient = SessionsClient.create(SessionsSettings.newBuilder()
                .setCredentialsProvider(() -> credentials)
                .build());
    }

    public String detectIntent1(String sessionId, String userMessage) throws Exception {
        SessionName session = SessionName.of(projectId, sessionId);
        TextInput.Builder textInput = TextInput.newBuilder().setText(userMessage).setLanguageCode("fr");

        QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
        DetectIntentRequest request = DetectIntentRequest.newBuilder()
                .setSession(session.toString())
                .setQueryInput(queryInput)
                .build();

        DetectIntentResponse response = sessionsClient.detectIntent(request);
        return response.getQueryResult().getFulfillmentText();
    }
    @PreDestroy
    public void shutdown() {
        if (sessionsClient != null) {
            sessionsClient.close(); // ferme correctement le canal
        }
    }
}
