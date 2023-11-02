package com.aryzko.scrabblegame.infrastructure.db.model;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Getter
@Setter
@Entity
@Table(name = "game")
public class GameEntryDb {

    @Id
    @GeneratedValue(generator = "game_sequence", strategy= GenerationType.SEQUENCE)
    @SequenceGenerator(name = "game_sequence", sequenceName = "game_sequence", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Version
    private Integer version;

    @Type(JsonType.class)
    @Column(name = "data", columnDefinition = "jsonb")
    private String data;
}
