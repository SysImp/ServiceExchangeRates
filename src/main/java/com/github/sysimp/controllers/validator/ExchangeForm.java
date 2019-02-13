package com.github.sysimp.controllers.validator;

public class ExchangeForm {

    private String fromCurrency;
    private String toCurrency;
    private String factor;

    private double value;

    public ExchangeForm() {
        this.factor = "1";
        this.value = 1;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ExchangeForm{" +
                "fromCurrency='" + fromCurrency + '\'' +
                ", toCurrency='" + toCurrency + '\'' +
                ", factor=" + factor +
                ", value=" + value +
                '}';
    }
}
