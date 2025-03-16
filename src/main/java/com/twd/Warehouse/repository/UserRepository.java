package com.twd.Warehouse.repository;

import com.twd.Warehouse.entity.OurUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<OurUsers, Integer> {
    Optional<OurUsers> findByUsername(String username);
    Optional<OurUsers> findByEmail(String email);
}
