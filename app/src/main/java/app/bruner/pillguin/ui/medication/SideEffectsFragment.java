package app.bruner.pillguin.ui.medication;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.bruner.library.models.Medication;
import app.bruner.library.models.SideEffect;
import app.bruner.library.viewModels.MedicationViewModel;
import app.bruner.pillguin.R;

public class SideEffectsFragment extends Fragment {
    private static final String ARG_MEDICATION = "medication";
    private ListView listView;
    private Medication medication;
    private MedicationViewModel medicationViewModel;

    public static SideEffectsFragment newInstance(Medication medicationParam) {
        SideEffectsFragment fragment = new SideEffectsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MEDICATION, medicationParam);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_side_effects, container, false);

        listView = root.findViewById(R.id.listViewSideEffects);
        medicationViewModel = new ViewModelProvider(this).get(MedicationViewModel.class);

        if (getArguments() != null) {
            medication = getArguments().getSerializable(ARG_MEDICATION, Medication.class);
        }

        observeMedications();

        return root;
    }

    private void observeMedications() {
        loadSideEffects(medication);
    }

    private void loadSideEffects(Medication medication) {
        List<String> displayList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());

        if (medication != null && medication.getSideEffects() != null) {
            for (SideEffect se : medication.getSideEffects()) {
                displayList.add(
                        "Description: " + se.getDescription() + "\n" +
                                "Date: " + dateFormat.format(se.getDatetimeReported())
                );
            }
        } else {
            displayList.add("No side effects reported.");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                displayList
        );
        listView.setAdapter(adapter);
    }
}