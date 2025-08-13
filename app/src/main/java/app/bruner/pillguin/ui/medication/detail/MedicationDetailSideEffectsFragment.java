package app.bruner.pillguin.ui.medication.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import app.bruner.library.models.Medication;
import app.bruner.library.models.SideEffect;
import app.bruner.library.utils.DateTimeParseUtils;
import app.bruner.library.viewModels.MedicationViewModel;
import app.bruner.pillguin.R;
import app.bruner.pillguin.databinding.FragmentMedicationDetailSideEffectsBinding;

/**
 * Fragment to display the side effects of a medication
 */
public class MedicationDetailSideEffectsFragment extends Fragment {
    private static final String ARG_MEDICATION = "medication";
    private ListView listView;
    private Medication medication;

    private FragmentMedicationDetailSideEffectsBinding binding;

    private MedicationViewModel viewModel;

    // factory to create a new instance of this fragment
    // I use factory method to pass the medication object as parameter
    public static MedicationDetailSideEffectsFragment newInstance(Medication medicationParam) {
        MedicationDetailSideEffectsFragment fragment = new MedicationDetailSideEffectsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MEDICATION, medicationParam);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMedicationDetailSideEffectsBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init(){

        viewModel = new ViewModelProvider(this).get(MedicationViewModel.class);

        if (getArguments() != null) { // get medication from arguments
            medication = getArguments().getSerializable(ARG_MEDICATION, Medication.class);
        }

        observeMedicationById(medication.getId());
    }

    private void observeMedicationById(long medicationId) {
        viewModel.getMedicationById(medicationId).observe(getViewLifecycleOwner(), medication -> {
            if (medication != null) {
                this.medication = medication;
                loadSideEffects(); // load side effects
            } else {
                // if is null, set empty list
                binding.listViewSideEffects.setAdapter(new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        new ArrayList<>()
                ));
            }
        });
    }

    // add a side effects
    // TODO: refectory here to use a adapter
    private void loadSideEffects() {
        List<String> displayList = new ArrayList<>();

        if (medication != null && medication.getSideEffects() != null && !medication.getSideEffects().isEmpty()) {
            for (SideEffect se : medication.getSideEffects()) {
                StringBuilder sb = new StringBuilder();
                sb.append(getString(R.string.txt_reported_side_effects_description, se.getDescription()))
                        .append("\n")
                        .append(getString(R.string.txt_reported_side_effects_date,
                                DateTimeParseUtils.formatDateTime(getContext(), se.getDatetimeReported())));

                displayList.add(sb.toString());
            }
        } else {
            displayList.add(getString(R.string.txt_reported_side_effects_no_side_effects));
        }

        binding.listViewSideEffects.setAdapter(new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                displayList
        ));
    }
}