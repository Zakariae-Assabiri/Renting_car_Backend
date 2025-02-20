package Car.project.Services;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import Car.project.Entities.Reservation;
import Car.project.Repositories.ReservationRepository;

import java.io.ByteArrayOutputStream;


@Service
public class ContractService {

    private final TemplateEngine templateEngine;
    private final ReservationRepository reservationRepository;

    public ContractService(TemplateEngine templateEngine, ReservationRepository reservationRepository) {
        this.templateEngine = templateEngine;
        this.reservationRepository = reservationRepository;
    }

    public byte[] generatePdf(Long reservationId) {
        // Récupérer la réservation
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée avec l'ID : " + reservationId));

        // Préparer les variables pour le template
        Context context = new Context();
        context.setVariable("id", reservation.getId());
        context.setVariable("cnom", reservation.getClient().getCname());
        context.setVariable("cadresse", reservation.getClient().getAdresse());
        context.setVariable("cnationalite", reservation.getClient().getNationalite());
        context.setVariable("ccin", reservation.getClient().getCin());
        context.setVariable("vmarque", reservation.getVoiture().getMarque());
        context.setVariable("vmatricule", reservation.getVoiture().getMatricule());
        context.setVariable("vdepart", reservation.getDateDebut());
        context.setVariable("vrendre", reservation.getDateFin());

        // Générer le contenu HTML
        String htmlContent = templateEngine.process("Contract", context);

        // Générer le PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        try {
            renderer.createPDF(outputStream);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }

        return outputStream.toByteArray();
    }
}
