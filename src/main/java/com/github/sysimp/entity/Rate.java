package com.github.sysimp.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rate")
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rate_seq")
    @SequenceGenerator(name = "rate_seq", sequenceName = "rate_seq", allocationSize = 1)
    private long id;
    @Column(name = "value", nullable = false)
    private int value;
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "from_currency")
    Currency fromCurrency;
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "to_currency")
    Currency toCurrency;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdate;

    protected Rate() {}

    public Rate(Currency fromCurrency, Currency toCurrency, int value, LocalDateTime lastUpdate) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.value = value;
        this.lastUpdate = lastUpdate;
    }

    public long getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public Currency getFromCurrency() {
        return fromCurrency;
    }

    public Currency getToCurrency() {
        return toCurrency;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
