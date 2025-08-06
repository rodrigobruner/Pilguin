package app.bruner.library.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Schedule implements Serializable {

    public final static String FREQUENCY_DAILY = "daily";
    public final static String FREQUENCY_WEEKLY = "weekly";
    public final static String FREQUENCY_MONTHLY = "monthly";

    public final static String WEEKDAY_MONDAY = "monday";
    public final static String WEEKDAY_TUESDAY = "tuesday";
    public final static String WEEKDAY_WEDNESDAY = "wednesday";
    public final static String WEEKDAY_THURSDAY = "thursday";
    public final static String WEEKDAY_FRIDAY = "friday";
    public final static String WEEKDAY_SATURDAY = "saturday";
    public final static String WEEKDAY_SUNDAY = "sunday";

    private Date startDate;
    private Date endDate;
    private boolean isIndefinitePeriod;
    //daily, weekly, monthly
    private String frequency;
    //each x hour(s)
    private int interval;
    //specific days of the week
    private ArrayList<String> daysOfWeek;
    // times
    private ArrayList<String> times;

    public Schedule(Date startDate,
                    Date endDate,
                    boolean isIndefinitePeriod,
                    String frequency,
                    int interval,
                    ArrayList<String> daysOfWeek,
                    ArrayList<String> times) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.isIndefinitePeriod = isIndefinitePeriod;
        this.frequency = frequency;
        this.interval = interval;
        this.daysOfWeek = daysOfWeek != null ? daysOfWeek : new ArrayList<>();
        this.times = times;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isIndefinitePeriod() {
        return isIndefinitePeriod;
    }

    public void setIndefinitePeriod(boolean indefinitePeriod) {
        isIndefinitePeriod = indefinitePeriod;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public ArrayList<String> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(ArrayList<String> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public ArrayList<String> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<String> times) {
        this.times = times;
    }
}
