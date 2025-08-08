package app.bruner.pillguin.ui.medication.frequencies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Date;

import app.bruner.library.models.Schedule;
import app.bruner.pillguin.R;
import app.bruner.pillguin.databinding.CardCustomBinding;
import app.bruner.pillguin.models.ScheduleProvider;
import app.bruner.pillguin.utils.DateTimePickerUtils;

public class CustomCard extends Fragment implements ScheduleProvider {

    CardCustomBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = CardCustomBinding.inflate(inflater, container, false);
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
            if (isChecked) {
                binding.txtEndDate.setVisibility(View.GONE);
                binding.txtEndDate.setText("");
            } else {
                binding.txtEndDate.setVisibility(View.VISIBLE);
            }
        });
        validadeSwiches();
    }


    private void validadeSwiches() {
        // when any switch change
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
            // Show or hide error message
            if (!anyChecked) {
                binding.txtWeekdaysError.setVisibility(View.VISIBLE);
            } else {
                binding.txtWeekdaysError.setVisibility(View.GONE);
            }
        };
        // Set the listener
        binding.swhWeekSunday.setOnCheckedChangeListener(listener);
        binding.swhWeekModay.setOnCheckedChangeListener(listener);
        binding.swhWeekTuesday.setOnCheckedChangeListener(listener);
        binding.swhWeekWednesday.setOnCheckedChangeListener(listener);
        binding.swhWeekThursday.setOnCheckedChangeListener(listener);
        binding.swhWeekFriday.setOnCheckedChangeListener(listener);
        binding.swhWeekSaturday.setOnCheckedChangeListener(listener);

        // Initial validation
        listener.onCheckedChanged(null, false);
    }

    @Override
    public Schedule getSchedule() {
        try {
            // Start date
            String startDateStr = binding.txtStartDate.getText().toString();
            Date startDate = DateTimePickerUtils.parseDateTime(getContext(), startDateStr);
            binding.txtStartDate.setError(null);
            if (startDateStr == null || startDate == null) {
                binding.txtStartDate.setError(getString(R.string.msg_error_start_date_required));
                return null;
            }

            // End date or indefinite
            boolean isIndefinite = binding.swhNoEndDate.isChecked();
            binding.txtEndDate.setError(null);
            Date endDate = null;
            if (!isIndefinite) {
                String endDateStr = binding.txtEndDate.getText().toString();
                if (!endDateStr.isEmpty()) {
                    endDate = DateTimePickerUtils.parseDateTime(getContext(), endDateStr);
                } else {
                    binding.txtEndDate.setError(getString(R.string.msg_error_end_date_required));
                    return null;
                }
            }

            // Frequency
            String frequency = Schedule.FREQUENCY_WEEKLY;
            int interval = 1; // every week

            // days of the week
            ArrayList<Integer> daysOfWeek = new ArrayList<>();

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

            return new Schedule(
                    startDate,
                    endDate,
                    isIndefinite,
                    frequency,
                    interval,
                    daysOfWeek
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
