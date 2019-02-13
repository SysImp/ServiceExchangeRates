package com.github.sysimp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Rate {

    @JsonIgnore
    private int id;
    @JsonProperty(value = "val")
    private double value;
    @JsonProperty(value = "fr")
    private String fromCurrency;
    @JsonProperty(value = "to")
    private String toCurrency;
    private LocalDateTime lastUpdate;

    public Rate() {}

    public Rate(double value, String fromCurrency, String toCurrency, LocalDateTime lastUpdate) {
        this.value = value;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.lastUpdate = lastUpdate;
    }

    public int getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return"Rate{" +
                "id=" + id +
                ", value=" + value +
                ", fromCurrency='" + fromCurrency + '\'' +
                ", toCurrency='" + toCurrency + '\'' +
                '}';
    }
}
