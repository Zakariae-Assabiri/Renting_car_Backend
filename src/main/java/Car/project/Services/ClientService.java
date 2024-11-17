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

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

 
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }


    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client updateClient(Client client) { 
            return clientRepository.save(client);    
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}
