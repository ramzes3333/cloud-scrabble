package com.aryzko.scrabble.scrabbledictionary.adapters.db.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "dictionary", uniqueConstraints = {
        @UniqueConstraint(name = "uc_dictionary_language", columnNames = {"language"})
})
public class DictionaryDb {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "language", nullable = false, unique = true)
    private String language;

    @Column(name = "default_dictionary", nullable = false)
    private boolean defaultDictionary;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dictionary")
    private Collection<DictionaryEntryDb> entries = new java.util.ArrayList<>();
}
