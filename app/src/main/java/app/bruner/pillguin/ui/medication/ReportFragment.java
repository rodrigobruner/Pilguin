package app.bruner.pillguin.ui.medication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import app.bruner.library.viewModels.MedicationViewModel;
import app.bruner.pillguin.R;
import app.bruner.pillguin.databinding.FragmentReportBinding;
import app.bruner.pillguin.ui.medication.MedicationReportAdapter;

public class ReportFragment extends Fragment {

    private MedicationViewModel viewModel;
    private FragmentReportBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentReportBinding.inflate(inflater, container, false);
        binding.recyclerMedicationReports.setLayoutManager(new LinearLayoutManager(requireContext()));
        viewModel = new ViewModelProvider(requireActivity()).get(MedicationViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }


    private void init(){
        // get medications from viewModel
        viewModel.getMedications().observe(getViewLifecycleOwner(), medications -> {
            if (medications == null || medications.isEmpty()) { // if is empty
                binding.recyclerMedicationReports.setVisibility(View.GONE);
                binding.textNoData.setVisibility(View.VISIBLE);
            } else {
                //set up recycler view
                binding.recyclerMedicationReports.setVisibility(View.VISIBLE);
                binding.textNoData.setVisibility(View.GONE);
                binding.recyclerMedicationReports.setAdapter(
                        new MedicationReportAdapter(medications, requireContext())
                );
            }
        });
    }
}