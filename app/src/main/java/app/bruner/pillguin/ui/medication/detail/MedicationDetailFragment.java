package app.bruner.pillguin.ui.medication.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayoutMediator;

import app.bruner.library.models.Medication;
import app.bruner.pillguin.R;
import app.bruner.pillguin.adapters.MedicationDetailTabAdapter;
import app.bruner.pillguin.databinding.FragmentMedicationDetailBinding;

/**
 * Fragment to show information about a medication
 */
public class MedicationDetailFragment extends Fragment {

    private final static String ARG_MEDICATION = "medication";

    private FragmentMedicationDetailBinding binding;
    private Medication medication;

    // factory to create a new instance of this fragment
    // I use factory method to pass the medication object as parameter
    public static MedicationDetailFragment newInstance(Medication medicationParam) {
        // get parameter
        MedicationDetailFragment fragment = new MedicationDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MEDICATION, medicationParam);
        fragment.setArguments(args); // set arguments to fragment
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMedicationDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            medication = getArguments().getSerializable(ARG_MEDICATION, Medication.class);
        }

        MedicationDetailTabAdapter adapter = new MedicationDetailTabAdapter(this, medication);
        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(getString(R.string.txt_tab_medicine_details));
                            break;
                        case 1:
                            tab.setText(getString(R.string.txt_tab_medicine_taken_log));
                            break;
                        case 2:
                            tab.setText(getString(R.string.txt_tab_medicine_reported_side_effects));
                            break;
                    }
                }).attach();
    }
}