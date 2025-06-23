package Car.project.Controllers;

import Car.project.dto.ClientDetailDTO;
import Car.project.dto.ClientDTO;
import Car.project.Services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClientDetailDTO>> getAllClients() {
        List<ClientDetailDTO> clients = clientService.getAllClientsDto();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@securiteService.isAdminOrOwner(#id)")
    public ResponseEntity<ClientDetailDTO> getClientById(@PathVariable Long id) {
        ClientDetailDTO clientDto = clientService.getClientDtoById(id);
        return ResponseEntity.ok(clientDto);
    }
    @GetMapping("/{id}/photo-cin")
    public ResponseEntity<byte[]> getClientCinPhoto(@PathVariable Long id) {
        byte[] photoBytes = clientService.getClientCinPhotoById(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(photoBytes);
    }

    @GetMapping("/{id}/photo-permis")
    public ResponseEntity<byte[]> getClientPermisPhoto(@PathVariable Long id) {
        byte[] photoBytes = clientService.getClientPermisPhotoById(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(photoBytes);
    }

    @PostMapping
    public ResponseEntity<ClientDetailDTO> createClient(@Valid @ModelAttribute ClientDTO clientRequestDto) throws IOException {
        // Le contrôleur passe directement le DTO au service
        ClientDetailDTO createdClientDto = clientService.createClient(clientRequestDto);
        return new ResponseEntity<>(createdClientDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securiteService.isClientOwner(#id)")
    public ResponseEntity<ClientDetailDTO> updateClient(@PathVariable Long id, @Valid @ModelAttribute ClientDTO clientRequestDto) throws IOException {

        ClientDetailDTO updatedClientDto = clientService.updateClient(id, clientRequestDto);
        return ResponseEntity.ok(updatedClientDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}