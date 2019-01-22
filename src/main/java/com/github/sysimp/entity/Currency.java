package com.github.sysimp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "currency")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_seq")
    @SequenceGenerator(name = "currency_seq", sequenceName = "currency_seq", allocationSize = 1)
    private long id;
    private double value;
    private String name;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdate;

    protected Currency() {}

    public Currency(String name, String description, double value, LocalDateTime lastUpdate) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.lastUpdate = lastUpdate;
    }

    public long getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setValue(double value) {
        this.value = value;
        this.lastUpdate = LocalDateTime.now();
    }
}
