package app.bruner.library.moc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import app.bruner.library.models.Medication;
import app.bruner.library.models.Schedule;

public class MedicationsMoc {
    public static ArrayList<Medication> getMedications() {
        ArrayList<Medication> medications = new ArrayList<>();

        Date today = new Date();
        Date nextWeek = new Date(today.getTime() + 7 * 24 * 60 * 60 * 1000);
        Date nextMonth = new Date(today.getTime() + 30 * 24 * 60 * 60 * 1000);

        Medication ibuprofen = new Medication(
                1,
                "Ibuprofen",
                "200mg",
                "Capsule",
                new Schedule(
                        today,
                        null,
                        true,
                        Schedule.FREQUENCY_DAILY,
                        1,
                        new ArrayList<>()
                )
        );

        medications.add(ibuprofen);

        Medication amoxicillin = new Medication(
                2,
                "Amoxicillin",
                "500mg",
                "Tablet",
                new Schedule(
                        today,
                        nextWeek,
                        false,
                        Schedule.FREQUENCY_HOURLY,
                        12,
                        new ArrayList<>()

                )
        );

        medications.add(amoxicillin);
        Medication metformin = new Medication(
                3,
                "Metformin",
                "850mg",
                "Tablet",
                new Schedule(
                        today,
                        nextMonth,
                        false,
                        Schedule.FREQUENCY_DAILY,
                        1,
                        new ArrayList<>(Arrays.asList(Schedule.WEEKDAY_MONDAY, Schedule.WEEKDAY_WEDNESDAY, Schedule.WEEKDAY_FRIDAY))
                )
        );
        medications.add(metformin);

        return medications;
    }


    public Medication getMedication() {
        ArrayList<Medication> medications = getMedications();
        if (medications.isEmpty()) {
            return null;
        }
        return medications.get(0);
    }
}