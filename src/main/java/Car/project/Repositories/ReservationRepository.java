package Car.project.Repositories;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Set;
import Car.project.Entities.Reservation;
import Car.project.Entities.Voiture;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	List<Reservation> findByClientId(Long clientId);
	
    // Récupère toutes les réservations qui chevauchent une période donnée pour un véhicule spécifique.
    @Query("SELECT r FROM Reservation r WHERE r.voiture.id = :voitureId AND r.dateDebut < :dateFin AND r.dateFin > :dateDebut AND r.statut <> 'Annulée'")
    List<Reservation> findReservationsOverlappingForVoiture(
        @Param("voitureId") Long voitureId, 
        @Param("dateDebut") LocalDateTime dateDebut, 
        @Param("dateFin") LocalDateTime dateFin);

    // Récupère toutes les réservations qui chevauchent une période donnée.
    @Query("SELECT r FROM Reservation r WHERE r.dateDebut < :dateFin AND r.dateFin > :dateDebut AND r.statut <> 'Annulée'")
    List<Reservation> findReservationsOverlapping(
        @Param("dateDebut") LocalDateTime dateDebut, 
        @Param("dateFin") LocalDateTime dateFin);

    // Calcule la somme des montants totaux de toutes les réservations (exclut les annulées).
    @Query("SELECT SUM(r.montantTotal) FROM Reservation r WHERE r.statut <> 'Annulée'")
    Double sumMontantTotal();
    
    // Calcule la somme des montants totaux des réservations pour une voiture spécifique (exclut les annulées).
    @Query("SELECT SUM(r.montantTotal) FROM Reservation r WHERE r.voiture.id = :voitureId AND r.statut <> 'Annulée'")
    Double sumMontantTotalByVoitureId(@Param("voitureId") Long voitureId);
    
    // Calcule la somme des montants totaux des réservations pour une voiture spécifique (Confirmée).
    @Query("SELECT SUM(r.montantTotal) FROM Reservation r WHERE r.statut = 'Confirmée'")
    Double sumMontantTotalConfirmed();
    
    // Compte le nombre de voitures réservées dans une période donnée (exclut les annulées).
    @Query("SELECT COUNT(DISTINCT r.voiture) FROM Reservation r WHERE r.dateDebut < :dateFin AND r.dateFin > :dateDebut AND r.statut <> 'Annulée'")
    int countReservedCarsInPeriod(
        @Param("dateDebut") LocalDateTime dateDebut, 
        @Param("dateFin") LocalDateTime dateFin);
    boolean existsByVoitureAndDateFinAfter(Voiture voiture, LocalDateTime date);
    // Compte le nombre de voitures réservées dans une période donnée (les Confirmée).
    @Query("SELECT SUM(r.montantTotal) FROM Reservation r WHERE r.statut = 'Confirmée' AND r.dateDebut >= :dateDebut AND r.dateFin <= :dateFin")
    Double sumMontantTotalConfirmedByPeriod(
        @Param("dateDebut") LocalDateTime dateDebut, 
        @Param("dateFin") LocalDateTime dateFin);
    

    // Compte le nombre total de réservations dans une période donnée (exclut les annulées).
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.dateDebut <= :dateFin AND r.dateFin >= :dateDebut AND r.statut <> 'Annulée'")
    int countReservationsInPeriod(
        @Param("dateDebut") LocalDateTime dateDebut, 
        @Param("dateFin") LocalDateTime dateFin);

    // Compte le nombre de réservations pour un véhicule spécifique (exclut les annulées).
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.voiture.id = :voitureId AND r.statut <> 'Annulée'")
    Long countByVoitureId(@Param("voitureId") Long voitureId);
    
 // Compte le nombre de réservations pour un véhicule spécifique (les Confirmée).
    @Query("SELECT SUM(r.montantTotal) FROM Reservation r WHERE r.voiture.id = :voitureId AND r.statut = 'Confirmée'")
    Double sumMontantTotalConfirmedByVoitureId(@Param("voitureId") Long voitureId);

    // Compte le nombre de réservations ayant un statut spécifique.
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.statut = :statut")
    Long countByStatut(@Param("statut") String statut);

    // Calcule la somme des montants totaux des réservations dans une période donnée (exclut les annulées).
    @Query("SELECT SUM(r.montantTotal) FROM Reservation r WHERE r.dateDebut >= :dateDebut AND r.dateFin <= :dateFin AND r.statut <> 'Annulée'")
    Double sumMontantTotalByPeriod(
        @Param("dateDebut") LocalDateTime dateDebut, 
        @Param("dateFin") LocalDateTime dateFin);

    // Récupère toutes les réservations (exclut les annulées).
    @Query("SELECT r FROM Reservation r WHERE r.statut <> 'Annulée'")
    List<Reservation> findAllReservations();

    // Récupère les IDs des voitures réservées pendant une période donnée (exclut les annulées).
    @Query("SELECT r.voiture.id FROM Reservation r WHERE r.dateDebut < :dateFin AND r.dateFin > :dateDebut AND r.statut <> 'Annulée'")
    Set<Long> findVoituresReserveesIds(
        @Param("dateDebut") LocalDateTime dateDebut, 
        @Param("dateFin") LocalDateTime dateFin);
    
    @Query(value = "SELECT AVG(DATEDIFF(date_fin, date_debut)) FROM reservation", nativeQuery = true)
    Double findDureeMoyenne();

    
    @Query("SELECT r.dateDebut, r.dateFin FROM Reservation r")
    List<Object[]> findDatesDebutEtFin();
    
    @Query("SELECT r FROM Reservation r WHERE r.client.user.id = :userId")
    List<Reservation> findReservationsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Reservation r " +
            "WHERE r.voiture.id = :voitureId " +
            "AND r.id <> :currentReservationId " +
            "AND (r.dateDebut < :dateFin AND r.dateFin > :dateDebut)")
     boolean isVoitureAlreadyReservedForOther(Long voitureId, LocalDateTime dateDebut, LocalDateTime dateFin, Long currentReservationId);
}