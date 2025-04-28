package Car.project.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import Car.project.Entities.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsByEndpoint(String endpoint);
}