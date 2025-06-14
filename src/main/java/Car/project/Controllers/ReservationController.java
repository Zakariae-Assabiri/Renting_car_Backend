package Car.project.Controllers;

import Car.project.dto.ReservationRequestDTO;
import Car.project.dto.ReservationResponseDTO;
import Car.project.Services.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        List<ReservationResponseDTO> reservations = reservationService.getAllReservationsDto();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@securiteService.isAdminOrOwner(#id)")
    public ResponseEntity<ReservationResponseDTO> getReservationById(@PathVariable Long id) {
        ReservationResponseDTO reservationDto = reservationService.getReservationDtoById(id);
        return ResponseEntity.ok(reservationDto);
    }
    
    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasRole('ADMIN') or @securiteService.isOwner(#clientId)")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByClient(@PathVariable Long clientId) {
        List<ReservationResponseDTO> reservations = reservationService.findReservationsByClientId(clientId);
        return ResponseEntity.ok(reservations);
    }
    @GetMapping("/client/{UserId}")
    @PreAuthorize("hasRole('ADMIN') or @securiteService.isOwner(#UserId)")
    public ResponseEntity<List<ReservationResponseDTO>> findReservationsByUserId(@PathVariable Long userId) {
        List<ReservationResponseDTO> reservations = reservationService.findReservationsByClientId(userId);
        return ResponseEntity.ok(reservations);
    }


    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ReservationResponseDTO> createReservation(@Valid @RequestBody ReservationRequestDTO reservationRequestDto) {
        ReservationResponseDTO createdReservation = reservationService.createReservation(reservationRequestDto);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@securiteService.isAdminOrOwner(#id)")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}