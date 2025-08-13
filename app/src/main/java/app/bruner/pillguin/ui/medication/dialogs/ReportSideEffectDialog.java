package app.bruner.pillguin.ui.medication.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Dialog to report side effecta
 */
public class ReportSideEffectDialog extends DialogFragment {

    private Medication medication;
    private MedicationViewModel medicationViewModel;
    DialogReportSideEffectBinding binding;

    // factory to create a new instance of this dialog
    // I use factory method to pass the medication object as parameter
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
        // set medication
        if (getArguments() != null) {
            medication = getArguments().getSerializable("medication", Medication.class);
        }

        // set view model
        medicationViewModel = new ViewModelProvider(requireActivity()).get(MedicationViewModel.class);

        // button tosubmit
        binding.buttonDialogSubmit.setOnClickListener(v -> {
            String description = binding.editDialogSideEffect.getText().toString().trim();
            if (description.isEmpty()) {
                Toast.makeText(getContext(), getString(R.string.txt_description_empty), Toast.LENGTH_SHORT).show();
                return;
            }

            // create side effect
            SideEffect newEffect = new SideEffect(description, new Date());

            // add to medication's side effects list
            if (medication != null && medication.getSideEffects() != null) {
                medication.getSideEffects().add(newEffect);
                MedicationUtils.update(getContext(), medication);
            }

            Toast.makeText(getContext(), getString(R.string.msg_side_effect_reported), Toast.LENGTH_SHORT).show();
            dismiss();
        });
    }

}


