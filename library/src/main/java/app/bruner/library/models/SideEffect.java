package app.bruner.library.models;

import java.io.Serializable;
import java.util.Date;

public class SideEffect implements Serializable {
    private long id;
    private String description;
    private Date datetimeReported;

    public SideEffect(String description, Date datetimeReported) {
        this.id = System.currentTimeMillis();
        this.description = description;
        this.datetimeReported = datetimeReported;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
