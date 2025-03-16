package Car.project.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Car.project.Entities.Client;

@Repository
public interface ClientRepository  extends JpaRepository<Client, Long> {
}

