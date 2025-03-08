package Car.project.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Car.project.Entities.Client;
import Car.project.Repositories.ClientRepository;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    // Créer un nouveau client et l'enregistrer dans la base de données
    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    // Récupérer un client par son identifiant
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    // Récupérer la liste de tous les clients
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // Mettre à jour un client existant dans la base de données
    public Client updateClient(Client client) { 
        return clientRepository.save(client);    
    }

    // Supprimer un client de la base de données par son ID
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}
