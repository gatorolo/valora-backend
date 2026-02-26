package com.valora.gestion.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
public class Medication {
    private String name;
    private String schedule;

    public Medication() {
    }

    public Medication(String name, String schedule) {
        this.name = name;
        this.schedule = schedule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
