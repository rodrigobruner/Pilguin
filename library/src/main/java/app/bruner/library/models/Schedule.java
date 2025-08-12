package app.bruner.library.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Schedule model
 */
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

        // prevent null pointer exception
        this.daysOfWeek = daysOfWeek != null ? daysOfWeek : new ArrayList<>();

        this.isExpired = false; // not expired by default

        this.whenTook = new ArrayList<>(); // init empty
        this.whenTook.add(startDate); // add start date as first taken
        setNextTime(); // calculate the next time
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

    // get days of the week as strings
    public ArrayList<String> getDaysOfWeekAsString(){
        ArrayList<String> days = new ArrayList<>();
        for (Integer day : daysOfWeek) {
            switch (day) {
                case WEEKDAY_MONDAY:
                    days.add("Monday");
                    break;
                case WEEKDAY_TUESDAY:
                    days.add("Tuesday");
                    break;
                case WEEKDAY_WEDNESDAY:
                    days.add("Wednesday");
                    break;
                case WEEKDAY_THURSDAY:
                    days.add("Thursday");
                    break;
                case WEEKDAY_FRIDAY:
                    days.add("Friday");
                    break;
                case WEEKDAY_SATURDAY:
                    days.add("Saturday");
                    break;
                case WEEKDAY_SUNDAY:
                    days.add("Sunday");
                    break;
            }
        }
        return days;
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

    // add a date to the list of the medication was taken
    public void addWhenTook(Date date) {
        if (this.whenTook == null) {
            this.whenTook = new ArrayList<>();
        }
        this.whenTook.add(date);
        setNextTime();
    }

    // get the last time when took the medication
    public Date getLastTaken() {
        if (this.whenTook != null && !this.whenTook.isEmpty()) {
            return this.whenTook.get(this.whenTook.size() - 1);
        }
        return null;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    // set the next time
    public void setNextTime() {

        if( isExpired ){ // already expired
            return;
        }

        // check if null and init
        if (this.whenTook == null) {
            this.whenTook = new ArrayList<>();
            return;
        }

        // get last time
        Date lastTaken = whenTook.get(whenTook.size() - 1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastTaken);

        // add the interval by frequency
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
                    // calculate the next week time
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

        // get the next time
        this.nextTime = calendar.getTime();

        // check if expired
        checkIfExpired();
    }

    // calculate the next time for week frequency
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

    // check if the schedule is expired
    private void checkIfExpired() {

        // if period is indefinite not check expiration
        if (!isIndefinitePeriod && endDate != null) {
            Date now = new Date(); // current time
            if (nextTime.after(endDate) || now.after(endDate)) { // if next time is after end date
                //set expired
                this.isExpired = true;
                this.nextTime = null;
            }
        }
    }
}
