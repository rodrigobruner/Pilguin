package app.bruner.pillguin.ui.medication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.bruner.library.models.Medication;
import app.bruner.pillguin.adapters.MedicationAdapter;
import app.bruner.pillguin.databinding.FragmentMedicationBinding;

public class MedicationFragment extends Fragment {
    FragmentMedicationBinding binding;
    MedicationViewModel viewModel;
    MedicationAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentMedicationBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(MedicationViewModel.class);
        init();
        return binding.getRoot();
    }

    private void init() {
        setRecyclerView();
        observeMedications();
    }

    private void setRecyclerView() {
        // set layout manager
        binding.recyclerViewMedicine.setLayoutManager(new LinearLayoutManager(getContext()));

        // new adapter with listeners
        adapter = new MedicationAdapter(new MedicationAdapter.OnMedicineActionListener() {

            // buttons to inform that took medication
            @Override
            public void onTookMedication(Medication medication) {
                Toast.makeText(getContext(), "Took " + medication.getName(), Toast.LENGTH_SHORT).show();
            }

            // button to report side effects
            @Override
            public void onReportSideEffects(Medication medication) {
                Toast.makeText(getContext(), "Report sideEffects " + medication.getName(), Toast.LENGTH_SHORT).show();
            }

            // swipe to delete
            @Override
            public void onSwipeToDelete(Medication medication) {
                viewModel.deleteMedication(medication.getId());
                Toast.makeText(getContext(), "Deleted " + medication.getName(), Toast.LENGTH_SHORT).show();
            }

            // click to show medication details
            @Override
            public void onClick(Medication medication) {
                Toast.makeText(getContext(), "Clicked on " + medication.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        // add adapter
        binding.recyclerViewMedicine.setAdapter(adapter);

        // set up swipe to delete callback
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Medication medication = adapter.getMedications().get(position);
                adapter.listener.onSwipeToDelete(medication);
            }
        };
        new ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.recyclerViewMedicine);
    }

    // Observe medications changes on ViewModel
    private void observeMedications() {
        viewModel.getMedications().observe(getViewLifecycleOwner(), medications -> {
            adapter.setMedications(medications);
        });
    }
}