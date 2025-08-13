package app.bruner.pillguin.ui.medication.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Date;

import app.bruner.library.models.Medication;
import app.bruner.library.models.Schedule;
import app.bruner.library.utils.DateTimeParseUtils;
import app.bruner.library.utils.MedicationUtils;
import app.bruner.library.viewModels.MedicationViewModel;
import app.bruner.pillguin.R;
import app.bruner.pillguin.databinding.FragmentMedicationDetailInfoBinding;
import app.bruner.pillguin.ui.medication.dialogs.ReportSideEffectDialog;
import app.bruner.pillguin.utils.ScheduleAlarmUtils;

/**
 * Fragment to display medication information
 */
public class MedicationDetailInfoFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_MEDICATION = "medication";
    private FragmentMedicationDetailInfoBinding binding;
    private Medication medication;
    private MedicationViewModel viewModel;

    // factory to create a new instance of this fragment
    // I use factory method to pass the medication object as parameter
    public static MedicationDetailInfoFragment newInstance(Medication medicationParam) {
        MedicationDetailInfoFragment fragment = new MedicationDetailInfoFragment();
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
        binding = FragmentMedicationDetailInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init(){
        if (getArguments() != null) {
            medication = getArguments().getSerializable(ARG_MEDICATION, Medication.class);
        }

        viewModel = new ViewModelProvider(this).get(MedicationViewModel.class);
        observeMedicationById(medication.getId());
    }

    private void observeMedicationById(long medicationId) {
        viewModel.getMedicationById(medicationId).observe(getViewLifecycleOwner(), medication -> {
            if (medication != null) {
                this.medication = medication;
                updateUI();
            }
        });
    }

    private void updateUI() {
        if (medication == null) return;

        binding.btnTookMedicine.setOnClickListener(this);
        binding.btnReportSideEffects.setOnClickListener(this);
        binding.textName.setText(medication.getName());
        binding.textDose.setText(medication.getDosage());
        binding.textNotes.setText(medication.getType());
        Schedule schedule = medication.getSchedule();

        if (schedule != null) {
            StringBuilder sb = new StringBuilder();

            if (schedule.getInterval() > 0) {
                sb.append(getString(R.string.txt_hour_frequency, schedule.getInterval()));
            }

            ArrayList<String> days = schedule.getDaysOfWeekAsString();
            if (days != null && !days.isEmpty()) {
                sb.append("\n").append(getString(R.string.txt_days_frequency, days));
            }

            // set schedule
            binding.textSchedule.setText(sb.toString());

            // next time
            String nextTime = DateTimeParseUtils.formatDateTime(getContext(), schedule.getNextTime());
            binding.textNextTime.setText(nextTime);

        } else {
            binding.textSchedule.setText(getString(R.string.txt_no_schedule));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onClick(View v) { // button click
        if(v.getId() == binding.btnReportSideEffects.getId()){ // Report side effects
            ReportSideEffectDialog dialog = ReportSideEffectDialog.newInstance(medication);
            dialog.show(getParentFragmentManager(), "ReportSideEffectDialog");
        }

        if(v.getId() == binding.btnTookMedicine.getId()){ // Took medication
            medication.getSchedule().addWhenTook(new Date());
            MedicationUtils.update(getContext(), medication);
            ScheduleAlarmUtils.scheduleTaskAlarm(getContext(), medication, ScheduleAlarmUtils.TYPE_SIDE_EFFECT);
            Toast.makeText(getContext(), getString(R.string.msg_you_took, medication.getName()) + medication.getName(), Toast.LENGTH_SHORT).show();
        }
    }
}