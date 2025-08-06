package app.bruner.library.moc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import app.bruner.library.models.Medication;
import app.bruner.library.models.Schedule;

public class MedicationsMoc {

    public static Medication getMedication() {
        Schedule schedule = new Schedule(
                new Date(),
                null,
                true,
                Schedule.FREQUENCY_DAILY,
                1,
                new ArrayList<>(),
                new ArrayList<>(Arrays.asList("08:00", "20:00"))
        );
        return new Medication(1, "Aspirin", "100mg", "Tablet", schedule);
    }

    public static ArrayList<Medication> getMedications() {
        ArrayList<Medication> medications = new ArrayList<>();
        medications.add(getMedication());
        medications.add(new Medication(
                2,
                "Ibuprofen",
                "200mg",
                "Capsule",
                new Schedule(
                        new Date(),
                        null,
                        true,
                        Schedule.FREQUENCY_DAILY,
                        1,
                        new ArrayList<>(),
                        new ArrayList<>(Arrays.asList("09:00"))
                )
        ));
        return medications;
    }
}