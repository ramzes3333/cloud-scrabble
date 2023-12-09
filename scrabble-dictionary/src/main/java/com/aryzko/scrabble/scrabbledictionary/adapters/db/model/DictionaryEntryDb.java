package com.aryzko.scrabble.scrabbledictionary.adapters.db.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Getter
@Setter
@Entity
@Table(name = "dictionary_entry", indexes = {
        @Index(name = "idx_dictionaryentry", columnList = "dictionary_id, entry")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uc_dictionaryentry", columnNames = {"dictionary_id", "entry"})
})
public class DictionaryEntryDb {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "entry", nullable = false)
    private String entry;

    @ManyToOne
    @JoinColumn(name = "dictionary_id")
    private DictionaryDb dictionary;
}