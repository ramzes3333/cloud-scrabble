package com.aryzko.scrabble.scrabbledictionary.adapters.db;

import com.aryzko.scrabble.scrabbledictionary.adapters.db.model.DictionaryDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataDictionaryRepository extends JpaRepository<DictionaryDb, Long> {

    @Query("select d from DictionaryDb d where d.defaultDictionary = true")
    Optional<DictionaryDb> findDefaultDictionary();
}
