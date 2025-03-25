package Car.project.Controllers;

import Car.project.Services.DialogflowService;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/dialogflow")
public class DialogflowController {

    private final DialogflowService dialogflowService;

    public DialogflowController(DialogflowService dialogflowService) {
        this.dialogflowService = dialogflowService;
    }
    @GetMapping("/send")
    public String sendMessage(@RequestParam String sessionId, @RequestParam String message) {
        try {
            return dialogflowService.detectIntent(sessionId, message);
        } catch (Exception e) {
            return "❌ Erreur Dialogflow : " + e.getMessage();
        }
    }


    @GetMapping("/message")
    public String getResponse(@RequestParam String sessionId, @RequestParam String message) {
        try {
            return dialogflowService.detectIntent(sessionId, message);
        } catch (Exception e) {
            return "❌ Erreur Dialogflow : " + e.getMessage();
        }
    }
}
