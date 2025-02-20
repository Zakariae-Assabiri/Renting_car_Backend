package Car.project.Repositories;

import java.time.LocalDate;
import java.time.LocalDateTime; 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import Car.project.Entities.Reservation;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	 @Query("SELECT r FROM Reservation r WHERE r.dateDebut < :dateFin AND r.dateFin > :dateDebut")
	    List<Reservation> findReservationsOverlapping(LocalDateTime dateDebut, LocalDateTime dateFin);

	 @Query("SELECT SUM(r.montantTotal) FROM Reservation r")
	    Double sumMontantTotal();
	 
	 @Query("SELECT COUNT(r.voiture) FROM Reservation r " +
		       "WHERE r.dateDebut < :dateFin AND r.dateFin > :dateDebut")
		int countReservedCarsInPeriod(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);
	 
	    // Compter les réservations dans une période donnée
	    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.dateDebut <= :dateFin AND r.dateFin >= :dateDebut")
	    int countReservationsInPeriod(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

	    // Récupérer les réservations qui chevauchent une période donnée
	    @Query("SELECT r FROM Reservation r WHERE r.dateDebut <= :dateFin AND r.dateFin >= :dateDebut")
	    List<Reservation> findReservationsOverlapping(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);
	}

