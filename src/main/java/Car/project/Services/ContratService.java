package Car.project.Services;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import Car.project.Entities.Reservation;
import Car.project.Repositories.ReservationRepository;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
@Service
public class ContratService {

    private final TemplateEngine templateEngine;
    private final ReservationRepository reservationRepository;

    public ContratService(TemplateEngine templateEngine, ReservationRepository reservationRepository) {
        this.templateEngine = templateEngine;
        this.reservationRepository = reservationRepository;
    }

    // Génère un contrat de réservation sous forme de PDF
    public byte[] generatePdf(Long reservationId) {
        // Récupérer la réservation associée à l'ID donné
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée avec l'ID : " + reservationId));

        // Création d'un contexte Thymeleaf avec les données de la réservation
        Context context = new Context();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String dateDebutStr = reservation.getDateDebut().format(formatter);
        String dateFinStr = reservation.getDateFin().format(formatter);
        context.setVariable("id", reservation.getId());
        context.setVariable("cnom", reservation.getClient().getCname());
        context.setVariable("cadresse", reservation.getClient().getAdresse());
        context.setVariable("cnationalite", reservation.getClient().getNationalite());
        context.setVariable("ccin", reservation.getClient().getCin());
        context.setVariable("vmarque", reservation.getVoiture().getMarque());
        context.setVariable("vmatricule", reservation.getVoiture().getMatricule());
        context.setVariable("vdepart",dateDebutStr);
        context.setVariable("vrendre", dateFinStr);

        // Générer le contenu HTML du contrat à partir du modèle Thymeleaf
        String htmlContent = templateEngine.process("Contract", context);

        // Générer un fichier PDF à partir du contenu HTML
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();

        try {
            renderer.createPDF(outputStream);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }

        // Retourner le PDF sous forme de tableau de bytes
        return outputStream.toByteArray();
    }
}
