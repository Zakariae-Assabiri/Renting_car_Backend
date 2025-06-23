package Car.project.Controllers;

import Car.project.dto.ReservationRequestDTO;
import Car.project.dto.ReservationResponseDTO;
// Ne pas importer l'entité Reservation ici, car le contrôleur ne doit pas la manipuler directement.
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
        List<ReservationResponseDTO> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@securiteService.isAdminOrOwner(#id)")
    public ResponseEntity<ReservationResponseDTO> getReservationById(@PathVariable Long id) {
        
        ReservationResponseDTO reservationDto = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservationDto);
    }
    
    @PostMapping
    @PreAuthorize("isAuthenticated()") 
    public ResponseEntity<ReservationResponseDTO> createReservation(@Valid @RequestBody ReservationRequestDTO reservationRequestDto) {
        ReservationResponseDTO createdReservation = reservationService.createReservation(reservationRequestDto);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@securiteService.isAdminOrOwner(#id)")
    public ResponseEntity<ReservationResponseDTO> updateReservation(
            @PathVariable Long id,
            @RequestBody @Valid ReservationRequestDTO dto) {
        ReservationResponseDTO response = reservationService.updateReservation(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@securiteService.isAdminOrOwner(#id)")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/client/{clientId}")
    @PreAuthorize("@securiteService.isAdminOrOwner(#clientId)")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByClientId(@PathVariable Long clientId) {
        List<ReservationResponseDTO> reservations = reservationService.findReservationsByClientId(clientId);
        return ResponseEntity.ok(reservations);
    }
}