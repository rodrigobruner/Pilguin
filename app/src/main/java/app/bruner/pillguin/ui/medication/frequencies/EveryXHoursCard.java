package app.bruner.pillguin.ui.medication.frequencies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Date;

import app.bruner.library.models.Schedule;
import app.bruner.library.utils.DateTimeParseUtils;
import app.bruner.pillguin.R;
import app.bruner.pillguin.databinding.CardEveryXHourBinding;
import app.bruner.pillguin.models.ScheduleProvider;
import app.bruner.pillguin.utils.Constants;
import app.bruner.pillguin.utils.DateTimePickerUtils;

public class EveryXHoursCard extends Fragment implements ScheduleProvider {

    private CardEveryXHourBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = CardEveryXHourBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        setupSeekBar();
        setUi();
    }

    private void setupSeekBar() {
        binding.sekFrequency.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) progress = Constants.SEEK_BAR_DEFAULT_FREQUENCY;
                updateDisplay(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Configuração inicial
        updateDisplay(Constants.SEEK_BAR_DEFAULT_FREQUENCY);
    }

    private void updateDisplay(int hours) {
        String hoursText = getString(R.string.txt_hour_frequency, hours);
        binding.txtFrequency.setText(hoursText);
    }

    private void setUi(){

        //set up date pickers
        DateTimePickerUtils.showDateTimePicker(getContext(), binding.txtStartDate);
        DateTimePickerUtils.showDateTimePicker(getContext(), binding.txtEndDate);

        // set up switch for no end date
        binding.swhNoEndDate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.txtEndDate.setVisibility(View.GONE);
                binding.txtEndDate.setText("");
            } else {
                binding.txtEndDate.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public Schedule getSchedule() {
        try {
            // Start date
            String startDateStr = binding.txtStartDate.getText().toString();
            Date startDate = DateTimeParseUtils.parseDateTime(getContext(), startDateStr);
            binding.txtStartDate.setError(null);
            if (startDate == null) {
                binding.txtStartDate.setError(getString(R.string.msg_error_start_date_required));
                return null;
            }

            // End date or indefinite
            boolean isIndefinite = binding.swhNoEndDate.isChecked();
            Date endDate = null;
            binding.txtEndDate.setError(null);
            if (!isIndefinite) {
                String endDateStr = binding.txtEndDate.getText().toString();
                if (!endDateStr.isEmpty()) {
                    endDate = DateTimeParseUtils.parseDateTime(getContext(), endDateStr);
                } else {
                    binding.txtEndDate.setError(getString(R.string.msg_error_end_date_required));
                    return null;
                }
            }

            // Interval
            int interval = binding.sekFrequency.getProgress();

            // Frequency
            String frequency = Schedule.FREQUENCY_HOURLY;

            ArrayList<Integer> daysOfWeek = new ArrayList<>();
            daysOfWeek.add(Schedule.WEEKDAY_SUNDAY);
            daysOfWeek.add(Schedule.WEEKDAY_MONDAY);
            daysOfWeek.add(Schedule.WEEKDAY_TUESDAY);
            daysOfWeek.add(Schedule.WEEKDAY_WEDNESDAY);
            daysOfWeek.add(Schedule.WEEKDAY_THURSDAY);
            daysOfWeek.add(Schedule.WEEKDAY_FRIDAY);
            daysOfWeek.add(Schedule.WEEKDAY_SATURDAY);

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