package Car.project.Repositories;

import Car.project.Entities.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VoitureRepository extends JpaRepository<Voiture, Long> {


    boolean existsByMatricule(String matricule);
    @Query("SELECT v FROM Voiture v WHERE v.id NOT IN " +
           "(SELECT r.voiture.id FROM Reservation r WHERE r.dateDebut < :dateFin AND r.dateFin > :dateDebut)")
    List<Voiture> findVoituresDisponibles(@Param("dateDebut") LocalDateTime dateDebut, @Param("dateFin") LocalDateTime dateFin);

}