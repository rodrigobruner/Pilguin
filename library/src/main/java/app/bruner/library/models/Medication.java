package app.bruner.library.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Medication model
 */
public class Medication implements Serializable {
    private long id;
    private String name;
    private String dosage;
    private String type;
    private Schedule schedule;
    private ArrayList<SideEffect> sideEffects;

    public Medication(long id, String name, String dosage, String type, Schedule schedule) {
        this.id = id;
        this.name = name;
        this.dosage = dosage;
        this.type = type;
        this.schedule = schedule;
        this.sideEffects = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public ArrayList<SideEffect> getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(ArrayList<SideEffect> sideEffects) {
        this.sideEffects = sideEffects;
    }
}
