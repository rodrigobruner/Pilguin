package app.bruner.library.models;

import java.io.Serializable;
import java.util.Date;

public class MedicationDiary implements Serializable {
    private long id;
    private long medicationId;
    private Date datetime;

    public MedicationDiary(long id, long medicationId, Date datetime) {
        this.id = id;
        this.medicationId = medicationId;
        this.datetime = datetime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(long medicationId) {
        this.medicationId = medicationId;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
