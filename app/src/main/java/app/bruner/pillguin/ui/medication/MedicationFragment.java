package app.bruner.pillguin.ui.medication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.bruner.library.models.Medication;
import app.bruner.library.utils.MedicationUtils;
import app.bruner.pillguin.adapters.MedicationAdapter;
import app.bruner.pillguin.databinding.FragmentMedicationBinding;

public class MedicationFragment extends Fragment {
    FragmentMedicationBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentMedicationBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        setRecyclerView();
    }

    private void setRecyclerView() {

        // get the medication list
        ArrayList<Medication> medicationList = MedicationUtils.getAll(getContext());
        // set layout to recycler view
        binding.recyclerViewMedicine.setLayoutManager(new LinearLayoutManager(getContext()));

        // create the adapter
        MedicationAdapter adapter = new MedicationAdapter(medicationList, new MedicationAdapter.OnMedicineActionListener() {

            // took medicine button
            @Override
            public void onTookMedicine(Medication medication) {
                Toast.makeText(getContext(), "Took " + medication.getName(), Toast.LENGTH_SHORT).show();
            }

            // report side effects button
            @Override
            public void onReportSideEffects(Medication medication) {
                Toast.makeText(getContext(), "Report sideEffects " + medication.getName(), Toast.LENGTH_SHORT).show();
            }

            // swipe to delete
            @Override
            public void onSwipeToDelete(Medication medication) {
                MedicationUtils.delete(getContext(), medication.getId());
                Toast.makeText(getContext(), "Deleted " + medication.getName(), Toast.LENGTH_SHORT).show();
                setRecyclerView();
            }

            // click on medication card
            @Override
            public void onClick(Medication medication) {
                Toast.makeText(getContext(), "Clicked on " + medication.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        // add the adapter
        binding.recyclerViewMedicine.setAdapter(adapter);

        // set up swipe to delete
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Medication medication = medicationList.get(position);
                adapter.listener.onSwipeToDelete(medication);
            }
        };

        new ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.recyclerViewMedicine);
    }
}