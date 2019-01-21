package com.github.sysimp.model;


import javax.persistence.*;

@Entity
@Table(name = "rate")
public class Rate {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    @Column(name = "value", nullable = false)
    private int value;

    protected Rate() {}

    public Rate(int value) {
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

}
