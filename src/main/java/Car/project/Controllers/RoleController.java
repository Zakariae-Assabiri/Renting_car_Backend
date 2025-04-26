package Car.project.Controllers;

import Car.project.Entities.Role;
import Car.project.Repositories.RoleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Créer un nouveau rôle
    @PostMapping
    public Role createRole(@RequestBody Role role) {
        return roleRepository.save(role);
    }

    // Liste des rôles (optionnel pour tester)
    @GetMapping
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
