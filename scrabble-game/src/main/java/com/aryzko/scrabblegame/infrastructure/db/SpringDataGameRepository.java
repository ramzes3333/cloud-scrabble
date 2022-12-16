package com.aryzko.scrabblegame.infrastructure.db;

import com.aryzko.scrabblegame.infrastructure.db.model.GameEntryDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataGameRepository extends JpaRepository<GameEntryDb, Long> {
}
