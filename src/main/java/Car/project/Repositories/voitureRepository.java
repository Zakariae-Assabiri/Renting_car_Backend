package Car.project.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Car.project.Entities.reservation;

@Repository
public interface voitureRepository extends JpaRepository<reservation, Long>{

}
