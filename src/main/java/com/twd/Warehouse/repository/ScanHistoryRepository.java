package com.twd.Warehouse.repository;

import com.twd.Warehouse.entity.ScanHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScanHistoryRepository extends JpaRepository<ScanHistory, Long> {
    List<ScanHistory> findByWagonId(String wagonId);

    List<ScanHistory> findByBarcode(String barcode);

    @Query("SELECT s FROM ScanHistory s WHERE s.scanTime BETWEEN :startDate AND :endDate")
    List<ScanHistory> findByScanTimeBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT s FROM ScanHistory s WHERE s.user.id = :userId")
    List<ScanHistory> findByUserId(@Param("userId") Integer userId);
}