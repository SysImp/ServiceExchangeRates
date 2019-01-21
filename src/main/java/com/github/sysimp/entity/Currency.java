package com.github.sysimp.entity;

import javax.persistence.*;

@Entity
@Table(name = "currency")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_seq")
    @SequenceGenerator(name = "currency_seq", sequenceName = "currency_seq", allocationSize = 1)
    private long id;

    private String name;
    private String description;

    protected Currency() {}

    public Currency(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
