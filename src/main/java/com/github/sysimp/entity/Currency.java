package com.github.sysimp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    public double getValueReverse() {
        return 1 / value;
    }

    @Override
    public String toString() {
        return String.format("Currency{id:%d; value:%f; name:%s; description:%s}", id, value, name, description);
    }
}
