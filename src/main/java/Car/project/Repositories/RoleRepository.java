package Car.project.Repositories;

import Car.project.Entities.Role;
import Car.project.Entities.RoleNom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNom(RoleNom nom);
    boolean existsByNom(RoleNom Nom);
}
