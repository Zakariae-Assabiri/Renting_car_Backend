package Car.project.Repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Car.project.Entities.Depense;



public interface DepenseRepository extends JpaRepository<Depense, Long> {
    
    List<Depense> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT SUM(d.montant) FROM Depense d WHERE d.date >= :startDate AND d.date <= :endDate")
    Double sumMontantByDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
