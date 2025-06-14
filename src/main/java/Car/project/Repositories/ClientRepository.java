package Car.project.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import Car.project.Entities.Client;
import java.util.Optional;

@Repository
public interface ClientRepository  extends JpaRepository<Client, Long> {
	boolean existsByCin(String cin);
	Optional<Client> findByUserId(Long UserId);

}

