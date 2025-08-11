package app.bruner.pillguin.ui.medication.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.bruner.library.models.Medication;
import app.bruner.library.viewModels.MedicationViewModel;
import app.bruner.pillguin.adapters.MedicationDetailTakenLogAdapter;
import app.bruner.pillguin.databinding.FragmentMedicationDetailTakenLogBinding;

/**
 * Fragment to display the taken log of a medication
 */
public class MedicationDetailTakenLogFragment extends Fragment {

    private static final String ARG_MEDICATION = "medication";
    private Medication medication;

    private FragmentMedicationDetailTakenLogBinding binding;
    private MedicationDetailTakenLogAdapter adapter;
    private MedicationViewModel viewModel;

    // factory to create a new instance of this fragment
    // use factory method to pass the medication object as parameter
    public static MedicationDetailTakenLogFragment newInstance(Medication medicationParam) {
        MedicationDetailTakenLogFragment fragment = new MedicationDetailTakenLogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MEDICATION, medicationParam);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) { //get medication from arguments
            medication = getArguments().getSerializable(ARG_MEDICATION, Medication.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMedicationDetailTakenLogBinding.inflate(inflater, container, false);
        initRecycleView();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initRecycleView() {
        adapter = new MedicationDetailTakenLogAdapter(new ArrayList<>());
        binding.recyclerTakenLog.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerTakenLog.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(MedicationViewModel.class);

        List<Date> rawTakenTimes = medication.getSchedule().getWhenTook();

        // check if the list is not null or empty
        if (rawTakenTimes != null && !rawTakenTimes.isEmpty()) {
            adapter.setData(rawTakenTimes);
            binding.recyclerTakenLog.setVisibility(View.VISIBLE);
            binding.textEmptyState.setVisibility(View.GONE);
        } else {
            adapter.setData(new ArrayList<>());
            binding.recyclerTakenLog.setVisibility(View.GONE);
            binding.textEmptyState.setVisibility(View.VISIBLE);
        }
    }
}
