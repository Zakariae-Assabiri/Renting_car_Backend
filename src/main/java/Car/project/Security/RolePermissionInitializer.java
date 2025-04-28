package Car.project.Security;

import Car.project.Entities.Permission;
import Car.project.Entities.Role;
import Car.project.Entities.RoleNom;
import Car.project.Repositories.PermissionRepository;
import Car.project.Repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RolePermissionInitializer {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @PostConstruct
    public void initializeRolesAndPermissions() {
        // 1. Créer les permissions si elles n'existent pas
        if (permissionRepository.count() == 0) {
        	permissionRepository.save(new Permission(null, "/api/clients", "POST")); // createClient
        	permissionRepository.save(new Permission(null, "/api/clients", "GET")); // getAllClients
        	permissionRepository.save(new Permission(null, "/api/clients/{id}", "DELETE")); // deleteClient
        	permissionRepository.save(new Permission(null, "/api/clients/{id}", "GET")); // getClientById
        	permissionRepository.save(new Permission(null, "/api/clients/{id}", "PUT")); // updateClient
        	permissionRepository.save(new Permission(null, "/api/contracts/{reservationId}/pdf", "GET")); // getContrat
        	permissionRepository.save(new Permission(null, "/api/dashboard/**", "GET")); // getDashboard
        	permissionRepository.save(new Permission(null, "/api/finance/expense", "POST")); // ajouterDepense
        	permissionRepository.save(new Permission(null, "/api/finance/expense/{id}", "GET")); // getDepenseById
        	permissionRepository.save(new Permission(null, "/api/finance/expenses", "GET")); // getAllDepenses
        	permissionRepository.save(new Permission(null, "/api/finance/expense", "PUT")); // updateDepense
        	permissionRepository.save(new Permission(null, "/api/finance/expense/{id}", "DELETE")); // deleteDepense
        	permissionRepository.save(new Permission(null, "/api/finance/rapport", "GET")); // genererRapportFinancier
        	permissionRepository.save(new Permission(null, "/api/finance/revenu", "GET")); // getRevenuTotal
        	permissionRepository.save(new Permission(null, "/api/finance/depenses-total", "GET")); // getDepensesTotales
        	permissionRepository.save(new Permission(null, "/api/reservations", "GET")); // getAllReservations
        	permissionRepository.save(new Permission(null, "/api/reservations/{id}", "DELETE")); // deleteReservation
        	permissionRepository.save(new Permission(null, "/api/reservations", "POST")); // createReservation
        	permissionRepository.save(new Permission(null, "/api/reservations/{id}", "GET")); // getReservationById
        	permissionRepository.save(new Permission(null, "/api/reservations/{id}", "PUT")); // updateReservation
        	permissionRepository.save(new Permission(null, "/api/voitures", "POST")); // createVoiture
        	permissionRepository.save(new Permission(null, "/api/voitures/{id}", "PUT")); // updateVoiture
        	permissionRepository.save(new Permission(null, "/api/voitures/{id}", "DELETE")); // deleteVoiture

        }

        // 2. Créer ROLE_ADMIN si pas encore présent
        if (roleRepository.count() == 0) {
            // Création du rôle ADMIN avec toutes les permissions
            Role adminRole = new Role();
            adminRole.setNom(RoleNom.ROLE_ADMIN);
            List<Permission> allPermissions = permissionRepository.findAll();
            adminRole.setPermissions(new HashSet<>(allPermissions));
            roleRepository.save(adminRole);

            // Création du rôle CLIENT
            Role clientRole = new Role();
            clientRole.setNom(RoleNom.ROLE_CLIENT);

            // Liste des permissions autorisées pour le rôle CLIENT
            List<Permission> clientPermissions = permissionRepository.findAll().stream()
            	    .filter(p ->
            	        (p.getEndpoint().equals("/api/voitures") && (p.getMethod().equals("GET") ))
            	        || (p.getEndpoint().equals("/api/reservations") &&  p.getMethod().equals("POST"))
            	        // || (p.getEndpoint().equals("/api/reservations") && (p.getMethod().equals("GET") || p.getMethod().equals("POST")))
            	    )
            	    .toList();


            // Association des permissions au rôle CLIENT
            clientRole.setPermissions(new HashSet<>(clientPermissions));

            // Sauvegarde du rôle CLIENT avec ses permissions
            roleRepository.save(clientRole);
        }
    }
}
