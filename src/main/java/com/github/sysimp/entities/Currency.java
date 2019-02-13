package com.github.sysimp.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "currency")
public class Currency {

    @Id
    private long id;

    private double value;
    private String name;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdate;

    protected Currency() {}

    public Currency(long id, String name, String description, double value, LocalDateTime lastUpdate) {
        this.id = id;
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

    public double getValueReverse() {
        return 1 / value;
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

    public void setId(long id) {
        this.id = id;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setValue(double value) {
        this.value = value;
        this.lastUpdate = LocalDateTime.now();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", value=" + value +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", lastUpdate=" + lastUpdate +
                ", hashCode=" + hashCode() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Currency currency = (Currency)o;
        return getId() == currency.getId() && getName().equals(currency.getName()) && getDescription().equals(currency.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription());
    }
}
