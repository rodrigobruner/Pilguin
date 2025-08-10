package app.bruner.pillguin.ui.medication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import app.bruner.library.models.Medication;
import app.bruner.library.models.Schedule;
import app.bruner.library.viewModels.MedicationViewModel;
import app.bruner.pillguin.databinding.FragmentMedicationDetailsBinding;

public class MedicationDetailsFragment extends Fragment {

    private static final String ARG_MEDICATION = "medication";
    private FragmentMedicationDetailsBinding binding;
    private Medication medication;
    private MedicationViewModel medicationViewModel;

    public static MedicationDetailsFragment newInstance(Medication medicationParam) {
        MedicationDetailsFragment fragment = new MedicationDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MEDICATION, medicationParam);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMedicationDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init(){
        if (getArguments() != null) {
            medication = getArguments().getSerializable(ARG_MEDICATION, Medication.class);
        }

        medicationViewModel = new ViewModelProvider(this).get(MedicationViewModel.class);
        updateUI();
    }

    private void updateUI() {
        if (medication == null) return;

        binding.textName.setText(medication.getName());
        binding.textDose.setText(medication.getDosage());
        binding.textNotes.setText(medication.getType());
        Schedule schedule = medication.getSchedule();
        if (schedule != null) {
            StringBuilder sb = new StringBuilder();

            if (schedule.getInterval() > 0) {
                sb.append("Every ").append(schedule.getInterval()).append(" hour(s)");
            }


            ArrayList<String> days = schedule.getDaysOfWeekAsString();
            if (days != null && !days.isEmpty()) {
                sb.append("\nDays: ").append(", "+ days);
            }

            // TODO: refectory here
//            if (schedule.getTimes() != null && !schedule.getTimes().isEmpty()) {
//                sb.append("\nTimes: ").append(String.join(", "), schedule.getNextTime());
//            }

            binding.textSchedule.setText(sb.toString());
        } else {
            binding.textSchedule.setText("No schedule info");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}