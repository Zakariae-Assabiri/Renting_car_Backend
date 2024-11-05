package Car.project.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Car.project.Entities.client;

@Repository
public interface clientRepository  extends JpaRepository<client, Long> {
}

