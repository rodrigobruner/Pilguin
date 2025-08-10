package app.bruner.pillguin.ui.medication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Date;

import app.bruner.library.models.Medication;
import app.bruner.library.models.SideEffect;
import app.bruner.library.utils.MedicationUtils;
import app.bruner.library.viewModels.MedicationViewModel;
import app.bruner.pillguin.R;
import app.bruner.pillguin.databinding.DialogReportSideEffectBinding;

public class ReportSideEffectDialog extends DialogFragment {

    private Medication medication;
    private MedicationViewModel medicationViewModel;
    DialogReportSideEffectBinding binding;


    public static ReportSideEffectDialog newInstance(Medication medication) {
        ReportSideEffectDialog dialog = new ReportSideEffectDialog();
        Bundle args = new Bundle();
        args.putSerializable("medication", medication);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DialogReportSideEffectBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();

    }

    private void init(){
        if (getArguments() != null) {
            medication = getArguments().getSerializable("medication", Medication.class);
        }

        medicationViewModel = new ViewModelProvider(requireActivity()).get(MedicationViewModel.class);

        binding.buttonDialogSubmit.setOnClickListener(v -> {
            String description = binding.editDialogSideEffect.getText().toString().trim();
            if (description.isEmpty()) {
                Toast.makeText(getContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create side effect
            SideEffect newEffect = new SideEffect(description, new Date());

            // Add to medication's side effects list
            if (medication != null && medication.getSideEffects() != null) {
                medication.getSideEffects().add(newEffect);
                MedicationUtils.update(getContext(), medication);
            }

            Toast.makeText(getContext(), "Side effect reported", Toast.LENGTH_SHORT).show();
            dismiss();
        });
    }

}


