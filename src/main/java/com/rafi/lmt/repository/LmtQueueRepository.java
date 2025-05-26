package com.rafi.lmt.repository;

import com.rafi.lmt.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface LmtQueueRepository extends JpaRepository<LmtQueue, Long> {}