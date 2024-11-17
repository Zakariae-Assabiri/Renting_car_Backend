package Car.project.Controllers;

import Car.project.Services.TarificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/tarification")
public class TarificationController {

    @Autowired
    private TarificationService tarificationService;

    @GetMapping
    public double getTarifDynamique(
            @RequestParam("voitureId") Long voitureId,
            @RequestParam("dateDebut") LocalDate dateDebut,
            @RequestParam("dateFin") LocalDate dateFin) {
        
        return tarificationService.calculerTarifDynamique(voitureId, dateDebut, dateFin);
    }
}
