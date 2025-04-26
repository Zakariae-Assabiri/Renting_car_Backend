package Car.project.Controllers;

import Car.project.Entities.Client;
import Car.project.Entities.Voiture;
import Car.project.Services.ClientService;
import Car.project.dto.ClientDTO;
import Car.project.dto.VoitureDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    // Créer un nouveau client
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Client> createClient(@ModelAttribute ClientDTO clientDTO) {
        try {
            Client client = new Client();
            client.setCname(clientDTO.getCname());
            client.setAdresse(clientDTO.getAdresse());
            client.setNationalite(clientDTO.getNationalite());
            client.setAdresseEtranger(clientDTO.getAdresseEtranger());
            client.setPasseport(clientDTO.getPasseport());
            client.setDelivreLePasseport(clientDTO.getDelivreLePasseport());
            client.setCin(clientDTO.getCin());
            client.setCinDelivreLe(clientDTO.getCinDelivreLe());
            client.setTel(clientDTO.getTel());
            client.setPermis(clientDTO.getPermis());
            client.setPermisDelivreLe(clientDTO.getPermisDelivreLe());
            client.setPermisDelivreAu(clientDTO.getPermisDelivreAu());

            if (clientDTO.getPhotoCIN() != null && !clientDTO.getPhotoCIN().isEmpty()) {
                client.setPhotoCIN(clientDTO.getPhotoCIN().getBytes());
            }
            if (clientDTO.getPhotoPermis() != null && !clientDTO.getPhotoPermis().isEmpty()) {
                client.setPhotoPermis(clientDTO.getPhotoPermis().getBytes());
            }

            Client savedClient = clientService.createClient(client); 
            return new ResponseEntity<>(savedClient, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Obtenir un client par ID
    @PreAuthorize("@securityService.isClientOwner(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Optional<Client> client = clientService.getClientById(id);
        return client.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Obtenir la liste de tous les clients
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    // Mettre à jour un client 
    
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("@securityService.isClientOwner(#id)")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @ModelAttribute ClientDTO clientDTO) {
        try {
            Optional<Client> optionalClient = clientService.getClientById(id);
            if (optionalClient.isPresent()) {
                Client existingClient = optionalClient.get();
                existingClient.setCname(clientDTO.getCname());
                existingClient.setAdresse(clientDTO.getAdresse());
                existingClient.setNationalite(clientDTO.getNationalite());
                existingClient.setAdresseEtranger(clientDTO.getAdresseEtranger());
                existingClient.setPasseport(clientDTO.getPasseport());
                existingClient.setDelivreLePasseport(clientDTO.getDelivreLePasseport());
                existingClient.setCin(clientDTO.getCin());
                existingClient.setCinDelivreLe(clientDTO.getCinDelivreLe());
                existingClient.setTel(clientDTO.getTel());
                existingClient.setPermis(clientDTO.getPermis());
                existingClient.setPermisDelivreLe(clientDTO.getPermisDelivreLe());
                existingClient.setPermisDelivreAu(clientDTO.getPermisDelivreAu());

                if (clientDTO.getPhotoCIN() != null && !clientDTO.getPhotoCIN().isEmpty()) {
                    existingClient.setPhotoCIN(clientDTO.getPhotoCIN().getBytes());
                }
                if (clientDTO.getPhotoPermis() != null && !clientDTO.getPhotoPermis().isEmpty()) {
                    existingClient.setPhotoPermis(clientDTO.getPhotoPermis().getBytes());
                }

                Client updatedClient = clientService.createClient(existingClient);
                return new ResponseEntity<>(updatedClient, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Supprimer un client
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        Optional<Client> clientOptional = clientService.getClientById(id);

        if (clientOptional.isPresent()) {
            clientService.deleteClient(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
