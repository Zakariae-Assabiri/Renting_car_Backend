package Car.project.Controllers;


import Car.project.Services.VisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/ocr")
public class VisionController {

    @Autowired
    private VisionService visionservice;

    @PostMapping("/extract")
    public Map<String, String> extractText(@RequestParam("file") MultipartFile file) throws IOException {
        // 1) Convertir le MultipartFile en fichier temporaire
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convFile);

        // 2) Extraire le texte via l’OCR
        String fullText = visionservice.extractFullText(convFile.getAbsolutePath());

        // 3) Parser le texte pour récupérer Nom, Prénom, CIN
        Map<String, String> extractedFields = visionservice.extractFields(fullText);

        return extractedFields;
    }

}
