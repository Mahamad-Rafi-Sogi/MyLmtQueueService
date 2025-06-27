package com.rafi.lmt.repository;

import com.rafi.lmt.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LmtQueueRepository extends JpaRepository<LmtQueue, String> {

    Optional<LmtQueue> findByLniata(String lniata);
    void deleteByLniata(String lniata); // Optional: use this if you want direct delete

}