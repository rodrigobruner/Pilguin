package app.bruner.pillguin.ui.medication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayoutMediator;

import app.bruner.library.models.Medication;
import app.bruner.pillguin.adapters.MedicationPagerAdapter;
import app.bruner.pillguin.databinding.FragmentMedicationDetailBinding;

public class MedicationDetailFragment extends Fragment {

    private FragmentMedicationDetailBinding binding;
    private Medication medication;

    public static MedicationDetailFragment newInstance(Medication medicationParam) {
        MedicationDetailFragment fragment = new MedicationDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("medication", medicationParam);
        fragment.setArguments(args);
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
            medication = getArguments().getSerializable("medication", Medication.class);
        }

        MedicationPagerAdapter adapter = new MedicationPagerAdapter(this, medication);
        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Details");
                            break;
                        case 1:
                            tab.setText("Taken Log");
                            break;
                        case 2:
                            tab.setText("Side Effects");
                            break;
                    }
                }).attach();
    }
}