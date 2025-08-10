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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.bruner.library.models.Medication;
import app.bruner.library.viewModels.MedicationViewModel;
import app.bruner.pillguin.adapters.TakenLogAdapter;
import app.bruner.pillguin.databinding.FragmentTakenLogBinding;

public class TakenLogFragment extends Fragment {

    private static final String ARG_MEDICATION = "medication";
    private Medication medication;

    private FragmentTakenLogBinding binding;
    private TakenLogAdapter adapter;
    private MedicationViewModel viewModel;

    public static TakenLogFragment newInstance(Medication medicationParam) {
        TakenLogFragment fragment = new TakenLogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MEDICATION, medicationParam);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            medication = getArguments().getSerializable(ARG_MEDICATION, Medication.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTakenLogBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void init() {
        adapter = new TakenLogAdapter(new ArrayList<>());
        binding.recyclerTakenLog.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerTakenLog.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(MedicationViewModel.class);

        List<Date> rawTakenTimes = medication.getSchedule().getWhenTook();

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
