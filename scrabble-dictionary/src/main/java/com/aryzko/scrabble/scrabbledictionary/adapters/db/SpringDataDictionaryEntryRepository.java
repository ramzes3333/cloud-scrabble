package com.aryzko.scrabble.scrabbledictionary.adapters.db;

import com.aryzko.scrabble.scrabbledictionary.adapters.db.model.DictionaryEntryDb;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.Collection;

public interface SpringDataDictionaryEntryRepository extends JpaRepository<DictionaryEntryDb, Long> {

    @Cacheable("findInDefaultDictionary")
    @Query("""
            select (count(d) > 0) from DictionaryDb d inner join d.entries entries
            where d.defaultDictionary = true and entries.value = lower(?1)""")
    boolean findInDefaultDictionary(@NonNull String value);

    @Query("select d from DictionaryEntryDb d where d.dictionary.defaultDictionary = true and d.value in ?1")
    Collection<DictionaryEntryDb> findInDefaultDictionary(@NonNull Collection<String> values);


    @Cacheable("findInDictionary")
    @Query("""
            select (count(d) > 0) from DictionaryDb d inner join d.entries entries
            where d.language = lower(?1) and entries.value = lower(?2)""")
    boolean findInDictionary(@NonNull String language, @NonNull String value);
}