package Car.project.Controllers;

import Car.project.dto.ReservationRequestDTO;
import Car.project.dto.ReservationResponseDTO;
import Car.project.Entities.Reservation;
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
    
    @PutMapping("/{id}")
    public ReservationResponseDTO updateReservation(@PathVariable Long id, @Valid @RequestBody ReservationRequestDTO dto) {
        return reservationService.updateReservation(id, dto);
    }
    
    @GetMapping("/client/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUserId(@PathVariable Long userId) {
        List<Reservation> reservations = reservationService.getReservationsByUserId(userId);
        if (reservations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
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