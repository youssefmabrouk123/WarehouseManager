package com.twd.Warehouse.repository;

import com.twd.Warehouse.entity.OurUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OurUserRepo extends JpaRepository<OurUsers, Integer> {
    Optional<OurUsers> findByEmail(String email);
    Optional<OurUsers> findByUsername(String username);
    boolean existsByUsername(String username);
}
