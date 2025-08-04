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
import app.bruner.pillguin.utils.DatePickerUtil;
import app.bruner.pillguin.utils.TimePickerUtils;

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

        // month spinner
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.list_frequency_months,
                android.R.layout.simple_spinner_item
        );
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnMonth.setAdapter(monthAdapter);

        //configure interface options, show or hide week and weekdays
        binding.spnMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                binding.spnWeek.setVisibility(View.VISIBLE);
                binding.lnlWeekdays.setVisibility(View.VISIBLE);
                if (position != 0) {
                    binding.spnWeek.setVisibility(View.GONE);
                    binding.lnlWeekdays.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {

            }
        });

        // week spinner
        ArrayAdapter<CharSequence> weekAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.list_frequency_week,
                android.R.layout.simple_spinner_item
        );
        weekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnWeek.setAdapter(weekAdapter);

        //configure interface options, show or hide weekdays
        binding.spnWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                binding.lnlWeekdays.setVisibility(View.VISIBLE);
                if (position != 0) {
                    binding.lnlWeekdays.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {

            }
        });



        //set up date pickers
        DatePickerUtil.showDatePicker(getContext(), binding.txtStartDate);
        DatePickerUtil.showDatePicker(getContext(), binding.txtEndDate);
        // set up time picker
        TimePickerUtils.showTimePicker(requireContext(), binding.edtTime);

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
            Date startDate = DatePickerUtil.parseDate(getContext(), startDateStr);
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
                    endDate = DatePickerUtil.parseDate(getContext(), endDateStr);
                } else {
                    binding.txtEndDate.setError(getString(R.string.msg_error_end_date_required));
                    return null;
                }
            }

            // Frequency
            String frequency;
            int interval = 0;
            ArrayList<String> daysOfWeek = new ArrayList<>();

            int monthPos = binding.spnMonth.getSelectedItemPosition();
            if (monthPos == 0) { // Weekly
                frequency = Schedule.FREQUENCY_WEEKLY;

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
            } else { // Monthly
                frequency = Schedule.FREQUENCY_MONTHLY;
            }

            // Time(s)
            String time = binding.edtTime.getText().toString();
            binding.edtTime.setError(null);
            if (time.isEmpty()) {
                binding.edtTime.setError(getString(R.string.msg_error_time_required));
                return null;
            }
            ArrayList<String> times = new ArrayList<>();

            return new Schedule(
                    startDate,
                    endDate,
                    isIndefinite,
                    frequency,
                    interval,
                    daysOfWeek,
                    times
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
