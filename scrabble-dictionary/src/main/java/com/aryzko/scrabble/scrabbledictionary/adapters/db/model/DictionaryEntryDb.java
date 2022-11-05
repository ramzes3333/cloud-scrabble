package com.aryzko.scrabble.scrabbledictionary.adapters.db.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Getter
@Setter
@Entity
@Table(name = "dictionary_entry", indexes = {
        @Index(name = "idx_dictionaryentry", columnList = "dictionary_id, value")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uc_dictionaryentry", columnNames = {"dictionary_id", "value"})
})
public class DictionaryEntryDb {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "value", nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "dictionary_id")
    private DictionaryDb dictionary;
}