package Car.project.Services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.*;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.Map;

@Service
public class DialogflowService {

    private final SessionsClient sessionsClient;
    private final String projectId = "loc-auto-gosk"; // Remplace avec ton ID de projet Google Cloud

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

    public String detectIntent(String sessionId, String userMessage) throws Exception {
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
}
