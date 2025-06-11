package Car.project.Controllers;

import Car.project.dto.ClientDetailDTO;
import Car.project.dto.ClientDTO;
import Car.project.Services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    public ResponseEntity<ClientDetailDTO> createClient(@Valid @ModelAttribute ClientDTO clientRequestDto) throws IOException {
        // Le contrôleur passe directement le DTO au service
        ClientDetailDTO createdClientDto = clientService.createClient(clientRequestDto);
        return new ResponseEntity<>(createdClientDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientDetailDTO> updateClient(@PathVariable Long id, @Valid @ModelAttribute ClientDTO clientRequestDto) throws IOException {
        // Le contrôleur passe l'ID et le DTO au service
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