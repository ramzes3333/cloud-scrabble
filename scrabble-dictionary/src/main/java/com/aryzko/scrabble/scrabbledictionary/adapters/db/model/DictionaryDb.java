package com.aryzko.scrabble.scrabbledictionary.adapters.db.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "dictionary")
    private Collection<DictionaryEntryDb> entries = new java.util.ArrayList<>();
}
