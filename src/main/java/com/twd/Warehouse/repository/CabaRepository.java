package com.twd.Warehouse.repository;
import com.twd.Warehouse.entity.Caba;
import com.twd.Warehouse.entity.CabaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CabaRepository extends JpaRepository<Caba, Long> {
    Optional<Caba> findByBarcode(String barcode);

    Optional<Caba> findByLastScannedByAndStatus(String lastScannedBy, CabaStatus status);

    List<Caba> findByWagon_WagonId(String wagonId);

    @Query("SELECT c FROM Caba c WHERE c.wagon.wagonId = :wagonId AND c.status = :status")
    List<Caba> findByWagonIdAndStatus(@Param("wagonId") String wagonId, @Param("status") CabaStatus status);

    @Query("SELECT COUNT(c) FROM Caba c WHERE c.wagon.wagonId = :wagonId AND c.status = :status")
    long countByWagonIdAndStatus(@Param("wagonId") String wagonId, @Param("status") CabaStatus status);

    @Query("SELECT c FROM Caba c WHERE c.lastScanTime BETWEEN :startDate AND :endDate")
    List<Caba> findByLastScanTimeBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}