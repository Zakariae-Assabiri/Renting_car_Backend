package Car.project.Controllers;

import Car.project.Services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    // Endpoint pour obtenir les KPIs
    
    @GetMapping("/kpis")
    public Map<String, Object> getKpis(
            @RequestParam("dateDebut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam("dateFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        
        Map<String, Object> kpis = new HashMap<>();
        kpis.put("revenuTotal", dashboardService.calculerRevenuTotal());
        //kpis.put("coutsTotaux", dashboardService.calculerCoutsTotaux());
        kpis.put("tauxDisponibilite", dashboardService.calculerTauxDisponibilite(dateDebut, dateFin));
        return kpis;
    }
}
