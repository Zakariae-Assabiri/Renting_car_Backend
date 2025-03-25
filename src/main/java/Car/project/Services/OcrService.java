package Car.project.Services;

import net.sourceforge.tess4j.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Service
public class OcrService {

    public String extractTextFromImage(MultipartFile file) {
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata"); // Chemin vers Tesseract
        tesseract.setLanguage("fra"); // Français pour les CIN

        try (InputStream inputStream = file.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream); // Convertir en BufferedImage
            return tesseract.doOCR(image);
        } catch (IOException | TesseractException e) {
            e.printStackTrace();
            return "Erreur OCR : " + e.getMessage();
        }
    }

    public String getNomPrenom(String texteOCR) {
        String[] lignes = texteOCR.split("\n");
        for (String ligne : lignes) {
            if (ligne.matches(".*[A-Z]+ [A-Z]+.*")) { // Détecter un nom/prénom en majuscules
                return ligne.trim();
            }
        }
        return "Nom/Prénom introuvables";
    }
}
