package Car.project.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import Car.project.Entities.Reservation;
import Car.project.Services.ReservationService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final ReservationService reservationService;

    @Autowired
    public PaymentController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/create-payment-intent/{reservationId}")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@PathVariable Long reservationId) throws StripeException {
        Reservation reservation = reservationService.findEntityById(reservationId);
        if (reservation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Réservation introuvable"));
        }

        float amount = reservation.getMontantTotal() ; 

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
            .setAmount((long) amount)
            .setCurrency("eur")
            .build();

        PaymentIntent intent = PaymentIntent.create(params);

        return ResponseEntity.ok(Map.of("clientSecret", intent.getClientSecret()));
    }


	}

