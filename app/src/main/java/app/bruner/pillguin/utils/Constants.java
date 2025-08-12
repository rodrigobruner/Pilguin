package app.bruner.pillguin.utils;

/**
 * Constants used throughout in the app
 **/
public class Constants {
    // date and time formatters
    public static final String DATE_FORMATTER = "%04d-%02d-%02d";
    public static final String TIME_FORMATTER = "%02d:%02d";


    // minutes before due time to remind user
    public static final int TIME_TO_REMIND_MINUTES = 2;

    // minutes before take to ask user about side effects
    public static final int TIME_TO_ASK_SIDE_EFFECT_MINUTES = 2;

    // permission request code
    public static final int PERMISSION_CODE = 100;

    // every X hours default frequency
    public static final int SEEK_BAR_DEFAULT_FREQUENCY = 8;
}