package com.aryzko.scrabble.scrabbledictionary.adapters.db;

import com.aryzko.scrabble.scrabbledictionary.adapters.db.model.DictionaryEntryDb;
import liquibase.pro.packaged.T;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.stream.Stream;

public interface SpringDataDictionaryEntryRepository extends JpaRepository<DictionaryEntryDb, Long> {

    @Cacheable("findInDefaultDictionary")
    @Query("""
            select (count(d) > 0) from DictionaryDb d inner join d.entries entries
            where d.defaultDictionary = true and entries.entry = lower(?1)""")
    boolean findInDefaultDictionary(@NonNull String entry);

    @Query("select d from DictionaryEntryDb d where d.dictionary.defaultDictionary = true and d.entry in ?1")
    Collection<DictionaryEntryDb> findInDefaultDictionary(@NonNull Collection<String> entries);


    @Cacheable("findInDictionary")
    @Query("""
            select (count(d) > 0) from DictionaryDb d inner join d.entries entries
            where d.language = lower(?1) and entries.entry = lower(?2)""")
    boolean findInDictionary(@NonNull String language, @NonNull String entry);


    Stream<DictionaryEntryDb> findAllByDictionaryId(Integer dictionaryId);
}