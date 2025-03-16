package com.twd.Warehouse.repository;
import com.twd.Warehouse.entity.Wagon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WagonRepository extends JpaRepository<Wagon, Long> {

    Optional<Wagon> findByWagonId(String wagonId);

    @Query("SELECT DISTINCT w.wagonId FROM Wagon w")
    List<String> findAllWagonIds();
}