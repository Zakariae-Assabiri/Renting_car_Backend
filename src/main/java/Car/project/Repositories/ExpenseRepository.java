package Car.project.Repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Car.project.Entities.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	    List<Expense> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
	    @Query("SELECT SUM(e.montant) FROM Expense e WHERE e.date BETWEEN :startDate AND :endDate")
	    Double sumMontantByDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

	}

