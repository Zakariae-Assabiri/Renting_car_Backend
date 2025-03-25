package Car.project.Controllers;

import Car.project.Services.OcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController

public class OcrController {

    @Autowired
    private OcrService ocrService;

    @PostMapping("/ext")
    public ResponseEntity<String> extractText(@RequestParam("file") MultipartFile file) {
        String text = ocrService.extractTextFromImage(file);
        return ResponseEntity.ok(text);
    }

    @PostMapping("/nom-prenom")
    public ResponseEntity<String> extractNomPrenom(@RequestParam("file") MultipartFile file) {
        String text = ocrService.extractTextFromImage(file);
        String nomPrenom = ocrService.getNomPrenom(text);
        return ResponseEntity.ok(nomPrenom);
    }
}
