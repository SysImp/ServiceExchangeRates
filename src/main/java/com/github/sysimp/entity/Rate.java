package com.github.sysimp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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

    protected Rate() {}

    public Rate(double value, String fromCurrnecy, String toCurrency, LocalDateTime lastUpdate) {
        this.value = value;
        this.fromCurrency = fromCurrnecy;
        this.toCurrency = toCurrency;
        this.lastUpdate = lastUpdate;
    }

    public int getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public String getFromCurrnecy() {
        return fromCurrency;
    }

    public String getToCurrnecy() {
        return toCurrency;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }
}
