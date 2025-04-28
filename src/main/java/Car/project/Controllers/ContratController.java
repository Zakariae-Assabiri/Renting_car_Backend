package Car.project.Controllers;


import Car.project.Services.ContratService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contracts")

public class ContratController {

    private final ContratService contractService;

    public ContratController(ContratService contractService) {
        this.contractService = contractService;
    }
    @PreAuthorize("@securiteService.isAdminOrOwner(#reservationId)")
    @GetMapping("/{reservationId}/pdf")
    public ResponseEntity<byte[]> generateContractPdf(@PathVariable Long reservationId) {
    	
        byte[] pdfContent = contractService.generatePdf(reservationId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "contrat_reservation_" + reservationId + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfContent);
    }
}
