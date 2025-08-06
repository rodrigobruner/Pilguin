package app.bruner.library.models;

import java.io.Serializable;
import java.util.Date;

public class SideEffect implements Serializable {
    private long id;
    private long medicationId;
    private String description;
    private Date datetimeReported;

    public SideEffect(long id, long medicationId, String description, String severity, Date datetimeReported) {
        this.id = id; //System.currentTimeMillis();
        this.medicationId = medicationId;
        this.description = description;
        this.datetimeReported = datetimeReported;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDatetimeReported() {
        return datetimeReported;
    }

    public void setDatetimeReported(Date datetimeReported) {
        this.datetimeReported = datetimeReported;
    }
}
