package app.bruner.pillguin.ui.medication.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Date;

import app.bruner.library.models.Schedule;
import app.bruner.library.utils.DateTimeParseUtils;
import app.bruner.pillguin.R;
import app.bruner.pillguin.databinding.CardAddMedicationIntervalsBinding;
import app.bruner.pillguin.models.ScheduleProvider;
import app.bruner.pillguin.utils.DateTimePickerUtils;

/**
 * Fragment to add medication schedules with intervals
 */
public class AddMedicationIntervalCard extends Fragment implements ScheduleProvider {

    CardAddMedicationIntervalsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = CardAddMedicationIntervalsBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }


    private void init() {
        setUi();
    }

    // set up the UI
    private void setUi() {
        // set up spinner for frequency
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.list_interval,
                android.R.layout.simple_spinner_item
        );
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.sprFrequency.setAdapter(monthAdapter);

        // set up date pickers to start date
        DateTimePickerUtils.showDateTimePicker(
                getContext(),
                binding.txtStartDate
        );

        // set up date pickers to end date
        DateTimePickerUtils.showDateTimePicker(
                getContext(),
                binding.txtEndDate
        );

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
    public Schedule getSchedule() { // contract method to get the schedule

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

        // interval
        if(binding.txtInterval.getText().toString().isEmpty()) {
            binding.txtEndDate.setError(getString(R.string.msg_error_interval_required));
            return null;
        }
        int interval = Integer.parseInt(binding.txtInterval.getText().toString());

        // frequency
        String frequency;
        switch (binding.sprFrequency.getSelectedItemPosition()) {
            case 0: // "days"
                frequency = Schedule.FREQUENCY_DAILY;
                break;
            case 1: // "weeks"
                frequency = Schedule.FREQUENCY_WEEKLY;
                break;
            case 2: // "months"
                frequency = Schedule.FREQUENCY_MONTHLY;
                break;
            default:
                frequency = Schedule.FREQUENCY_DAILY;
                break;
        }

        // initialize days of the week
        ArrayList<Integer> daysOfWeek = new ArrayList<>();

        return new Schedule(
                startDate,
                endDate,
                isIndefinite,
                frequency,
                interval,
                daysOfWeek
        );
    }
}
