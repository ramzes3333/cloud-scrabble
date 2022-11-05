package com.aryzko.scrabble.scrabbledictionary.adapters.db;

import com.aryzko.scrabble.scrabbledictionary.adapters.db.model.DictionaryDb;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataDictionaryRepository extends JpaRepository<DictionaryDb, Long> {

    @Query("select d from DictionaryDb d where d.defaultDictionary = true")
    Optional<DictionaryDb> findDefaultDictionary();

    @Cacheable("findInDefaultDictionary")
    @Query("""
            select (count(d) > 0) from DictionaryDb d inner join d.entries entries
            where d.defaultDictionary = true and entries.value = lower(?1)
            """)
    boolean findInDefaultDictionary(String value);

    @Cacheable("findInDictionary")
    @Query("""
            select (count(d) > 0) from DictionaryDb d inner join d.entries entries
            where d.language = lower(?1) and entries.value = lower(?2)
            """)
    boolean findInDictionary(String language, String value);
}
