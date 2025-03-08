package Car.project.Repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Car.project.Entities.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	    List<Expense> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
	    Double sumMontantByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
	}

