package Car.project.Controllers;

import Car.project.Entities.Permission;
import Car.project.Entities.Role;
import Car.project.Repositories.PermissionRepository;
import Car.project.Repositories.RoleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
	

	    private final RoleRepository roleRepository;
	    private final PermissionRepository permissionRepository;

	    public PermissionController(RoleRepository roleRepository, PermissionRepository permissionRepository) {
	        this.roleRepository = roleRepository;
	        this.permissionRepository = permissionRepository;
	    }

	    // Ajouter une nouvelle permission (ex: "/clients", "/dashboard")
	    @PostMapping
	    public Permission createPermission(@RequestBody Permission permission) {
	        if (permissionRepository.existsByEndpoint(permission.getEndpoint())) {
	            throw new RuntimeException("Cette permission existe déjà.");
	        }
	        return permissionRepository.save(permission);
	    }

	    // Associer une permission à un rôle
	    @PostMapping("/assign")
	    public void assignPermissionToRole(@RequestParam Long roleId, @RequestParam Long permissionId) {
	        Optional<Role> optionalRole = roleRepository.findById(roleId);
	        Optional<Permission> optionalPermission = permissionRepository.findById(permissionId);

	        if (optionalRole.isPresent() && optionalPermission.isPresent()) {
	            Role role = optionalRole.get();
	            Permission permission = optionalPermission.get();
	            role.getPermissions().add(permission);
	            roleRepository.save(role);
	        } else {
	            throw new RuntimeException("Role ou Permission introuvable");
	        }
	    }
	}

