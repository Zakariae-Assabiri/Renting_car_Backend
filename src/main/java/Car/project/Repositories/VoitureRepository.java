package Car.project.Repositories;

import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository;
import Car.project.Entities.Voiture;

@Repository
public interface VoitureRepository extends JpaRepository<Voiture, Long>{

}
