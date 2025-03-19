package Car.project.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import Car.project.Entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
