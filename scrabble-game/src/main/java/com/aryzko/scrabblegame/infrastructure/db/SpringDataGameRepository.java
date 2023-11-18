package com.aryzko.scrabblegame.infrastructure.db;

import com.aryzko.scrabblegame.infrastructure.db.model.GameEntryDb;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataGameRepository extends JpaRepository<GameEntryDb, Long> {

    @Query(nativeQuery = true, value = "select * from game where (data ->> 'id') = ?1")
    Optional<GameEntryDb> getGameById(String id);

    @Query(nativeQuery = true, value = "SELECT * FROM game ORDER BY data ->> 'creationDate' DESC")
    Page<GameEntryDb> findAllGamesWithSorting(Pageable pageable);
}
