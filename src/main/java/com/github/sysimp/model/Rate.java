package com.github.sysimp.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rate")
public class Rate {

    @Id
    private long id;
    @Column(name = "value", nullable = false)
    private int value;

    protected Rate() {}

    public Rate(int id, int value) {
        this.id = id;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

}
