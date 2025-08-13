package app.bruner.pillguin.ui.medication.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Date;

import app.bruner.library.models.Schedule;
import app.bruner.library.utils.DateTimeParseUtils;
import app.bruner.pillguin.R;
import app.bruner.pillguin.databinding.CardAddMedicationCustomBinding;
import app.bruner.pillguin.models.ScheduleProvider;
import app.bruner.pillguin.utils.DateTimePickerUtils;

/**
 * Fragment to add medication schedules with custom days of the week.
 */
public class AddMedicationCustomCard extends Fragment implements ScheduleProvider {

    CardAddMedicationCustomBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = CardAddMedicationCustomBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        setUi();
    }

    private void setUi(){

        //set up date pickers
        DateTimePickerUtils.showDateTimePicker(getContext(), binding.txtStartDate);
        DateTimePickerUtils.showDateTimePicker(getContext(), binding.txtEndDate);

        // set up switch no end date
        binding.swhNoEndDate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) { // if switch is checked, hide end date
                binding.txtEndDate.setVisibility(View.GONE);
                binding.txtEndDate.setText("");
            } else {
                binding.txtEndDate.setVisibility(View.VISIBLE);
            }
        });
        validadeSwiches();
    }


    // validate switches, at least one day selected
    private void validadeSwiches() {
        // criate a listener for all switches
        CompoundButton.OnCheckedChangeListener listener = (buttonView, isChecked) -> {
            boolean anyChecked = false;
            if (binding.swhWeekSunday.isChecked() ||
                    binding.swhWeekModay.isChecked() ||
                    binding.swhWeekTuesday.isChecked() ||
                    binding.swhWeekWednesday.isChecked() ||
                    binding.swhWeekThursday.isChecked() ||
                    binding.swhWeekFriday.isChecked() ||
                    binding.swhWeekSaturday.isChecked()) {
                anyChecked = true;
            }

            // show or hide error message
            if (!anyChecked) {
                binding.txtWeekdaysError.setVisibility(View.VISIBLE);
            } else {
                binding.txtWeekdaysError.setVisibility(View.GONE);
            }
        };

        // set the listener
        binding.swhWeekSunday.setOnCheckedChangeListener(listener);
        binding.swhWeekModay.setOnCheckedChangeListener(listener);
        binding.swhWeekTuesday.setOnCheckedChangeListener(listener);
        binding.swhWeekWednesday.setOnCheckedChangeListener(listener);
        binding.swhWeekThursday.setOnCheckedChangeListener(listener);
        binding.swhWeekFriday.setOnCheckedChangeListener(listener);
        binding.swhWeekSaturday.setOnCheckedChangeListener(listener);

        // initia validation
        listener.onCheckedChanged(null, false);
    }


    @Override
    public Schedule getSchedule() { // contract method to get the schedule
        try {

            // start date
            String startDateStr = binding.txtStartDate.getText().toString();
            Date startDate = DateTimeParseUtils.parseDateTime(getContext(), startDateStr);

            // check if is valid
            binding.txtStartDate.setError(null);
            if (startDateStr == null || startDate == null) {
                binding.txtStartDate.setError(getString(R.string.msg_error_start_date_required));
                return null;
            }

            // end date or indefinite
            boolean isIndefinite = binding.swhNoEndDate.isChecked();
            binding.txtEndDate.setError(null); // reset error
            Date endDate = null;
            if (!isIndefinite) { // if switch is not checked, get end date
                String endDateStr = binding.txtEndDate.getText().toString();
                if (!endDateStr.isEmpty()) {
                    endDate = DateTimeParseUtils.parseDateTime(getContext(), endDateStr);
                } else {
                    binding.txtEndDate.setError(getString(R.string.msg_error_end_date_required));
                    return null;
                }
            }

            // set frequency
            String frequency = Schedule.FREQUENCY_WEEKLY;
            int interval = 1; // every week

            // days of the week
            ArrayList<Integer> daysOfWeek = new ArrayList<>();

            // add days of week
            binding.txtWeekdaysError.setVisibility(View.GONE);
            if (binding.swhWeekSunday.isChecked()){
                daysOfWeek.add(Schedule.WEEKDAY_SUNDAY);
            }
            if (binding.swhWeekModay.isChecked()){
                daysOfWeek.add(Schedule.WEEKDAY_MONDAY);
            }
            if (binding.swhWeekTuesday.isChecked()){
                daysOfWeek.add(Schedule.WEEKDAY_TUESDAY);
            }
            if (binding.swhWeekWednesday.isChecked()){
                daysOfWeek.add(Schedule.WEEKDAY_WEDNESDAY);
            }
            if (binding.swhWeekThursday.isChecked()){
                daysOfWeek.add(Schedule.WEEKDAY_THURSDAY);
            }
            if (binding.swhWeekFriday.isChecked()){
                daysOfWeek.add(Schedule.WEEKDAY_FRIDAY);
            }
            if (binding.swhWeekSaturday.isChecked()){
                daysOfWeek.add(Schedule.WEEKDAY_SATURDAY);
            }

            // adjust frequency if all are selected
            if(daysOfWeek.size() == 7){
                frequency = Schedule.FREQUENCY_DAILY;
            }

            // create and return the schedule
            return new Schedule(
                    startDate,
                    endDate,
                    isIndefinite,
                    frequency,
                    interval,
                    daysOfWeek
            );
        } catch (Exception e) {
            e.printStackTrace(); // log the error
            return null;
        }
    }
}
