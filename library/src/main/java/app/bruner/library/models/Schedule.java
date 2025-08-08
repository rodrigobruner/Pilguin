package app.bruner.library.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Schedule implements Serializable {

    public final static String FREQUENCY_HOURLY = "hourly";
    public final static String FREQUENCY_DAILY = "daily";
    public final static String FREQUENCY_WEEKLY = "weekly";
    public final static String FREQUENCY_MONTHLY = "monthly";

    public final static int WEEKDAY_MONDAY = Calendar.MONDAY;
    public final static int WEEKDAY_TUESDAY = Calendar.TUESDAY;
    public final static int WEEKDAY_WEDNESDAY = Calendar.WEDNESDAY;
    public final static int WEEKDAY_THURSDAY = Calendar.THURSDAY;
    public final static int WEEKDAY_FRIDAY = Calendar.FRIDAY;
    public final static int WEEKDAY_SATURDAY = Calendar.SATURDAY;
    public final static int WEEKDAY_SUNDAY = Calendar.SUNDAY;


    //daily, weekly, monthly
    private String frequency;
    //each x hour(s)
    private int interval;
    //specific days of the week
    private ArrayList<Integer> daysOfWeek;

    // period
    private Date startDate;
    private Date endDate;
    private boolean isIndefinitePeriod;

    // times
    private Date nextTime;
    private ArrayList<Date> whenTook;
    private boolean isExpired;


    public Schedule(Date startDate,
                    Date endDate,
                    boolean isIndefinitePeriod,
                    String frequency,
                    int interval,
                    ArrayList<Integer> daysOfWeek
                    ) {

        this.startDate = startDate;
        this.endDate = endDate;
        this.isIndefinitePeriod = isIndefinitePeriod;


        this.frequency = frequency;
        this.interval = interval;
        this.daysOfWeek = daysOfWeek != null ? daysOfWeek : new ArrayList<>();

        this.isExpired = false;

        this.whenTook = new ArrayList<>();
        this.whenTook.add(startDate);
        setNextTime();
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

    public ArrayList<Integer> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(ArrayList<Integer> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
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

    public Date getNextTime() {
        return nextTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
    }

    public ArrayList<Date> getWhenTook() {
        return whenTook;
    }

    public void setWhenTook(ArrayList<Date> whenTook) {
        this.whenTook = whenTook;
    }

    public void addWhenTook(Date date) {
        this.whenTook.add(date);
        setNextTime();
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public void setNextTime() {

        if( isExpired ){ // already expired
            return;
        }

        // get last time
        Date lastTaken = whenTook.get(whenTook.size() - 1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastTaken);

        switch (frequency) {

            case FREQUENCY_HOURLY:
                // add the interval in hours
                calendar.add(Calendar.HOUR_OF_DAY, interval);
                break;

            case FREQUENCY_DAILY:
                // add the interval in days
                calendar.add(Calendar.DAY_OF_MONTH, interval);
                break;

            case FREQUENCY_WEEKLY:
                if (daysOfWeek != null && !daysOfWeek.isEmpty()) {
                    //
                    calculateNextWeekTime(calendar);
                } else {
                    // add the interval in weeks
                    calendar.add(Calendar.WEEK_OF_YEAR, interval);
                }
                break;

            case FREQUENCY_MONTHLY:
                // add the interval in months
                calendar.add(Calendar.MONTH, interval);
                break;
        }

        this.nextTime = calendar.getTime();

        // check if expired
        checkIfExpired();
    }

    private void calculateNextWeekTime(Calendar calendar) {

        // get the current day
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);

        for (int i = currentDay; i <= 7; i++) { // week days - 1 to 7

            // get next day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            int nextDay = calendar.get(Calendar.DAY_OF_WEEK);

            // check if the next day is in the list
            if (daysOfWeek.contains(nextDay)) {

                // next day is greater than current day, same week
                if (nextDay > currentDay) {
                    return; // find the next
                }
            }

            if(i == 7){ // reached the end of the week, start over
                i = 1; // reset loop
                currentDay = 0; // reset current day to find the next
            }
        }

        this.nextTime = calendar.getTime();
    }

    private void checkIfExpired() {
        if (!isIndefinitePeriod && endDate != null) {
            Date now = new Date();
            if (nextTime.after(endDate) || now.after(endDate)) {
                this.isExpired = true;
                this.nextTime = null;
            }
        }
    }
}
