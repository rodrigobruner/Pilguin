package app.bruner.pillguin.ui.medication.frequencies;

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
import app.bruner.pillguin.R;
import app.bruner.pillguin.databinding.CardCustomBinding;
import app.bruner.pillguin.databinding.CardIntervalsBinding;
import app.bruner.pillguin.models.ScheduleProvider;
import app.bruner.pillguin.utils.DateTimePickerUtils;

public class IntervalCard extends Fragment implements ScheduleProvider {


    CardIntervalsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = CardIntervalsBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }


    private void init() {
        setUi();
    }

    private void setUi() {
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.list_interval,
                android.R.layout.simple_spinner_item
        );
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.sprFrequency.setAdapter(monthAdapter);

        DateTimePickerUtils.showDateTimePicker(
                getContext(),
                binding.txtStartDate
        );

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
    public Schedule getSchedule() {

        // Start date
        String startDateStr = binding.txtStartDate.getText().toString();
        Date startDate = DateTimePickerUtils.parseDateTime(getContext(), startDateStr);
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
                endDate = DateTimePickerUtils.parseDateTime(getContext(), endDateStr);
            } else {
                binding.txtEndDate.setError(getString(R.string.msg_error_end_date_required));
                return null;
            }
        }

        // Interval
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
