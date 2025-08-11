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

import java.util.Date;

import app.bruner.library.models.Medication;
import app.bruner.library.utils.MedicationUtils;
import app.bruner.library.viewModels.MedicationViewModel;
import app.bruner.pillguin.R;
import app.bruner.pillguin.adapters.MedicationListAdapter;
import app.bruner.pillguin.databinding.FragmentMedicationListBinding;
import app.bruner.pillguin.ui.medication.detail.MedicationDetailFragment;
import app.bruner.pillguin.ui.medication.dialogs.ReportSideEffectDialog;

/**
 * Fragment to display a list of medications
 */
public class MedicationListFragment extends Fragment {
    FragmentMedicationListBinding binding;
    MedicationViewModel viewModel;
    MedicationListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentMedicationListBinding.inflate(inflater, container, false);
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
        adapter = new MedicationListAdapter(new MedicationListAdapter.OnMedicineActionListener() {

            // buttons to inform that took medication
            @Override
            public void onTookMedication(Medication medication) {
                medication.getSchedule().addWhenTook(new Date());
                MedicationUtils.update(getContext(), medication);
                Toast.makeText(getContext(), getString(R.string.msg_you_took, medication.getName()) + medication.getName(), Toast.LENGTH_SHORT).show();
            }

            // button to report side effects
            @Override
            public void onReportSideEffects(Medication medication) {

                ReportSideEffectDialog dialog = ReportSideEffectDialog.newInstance(medication);
                dialog.show(getParentFragmentManager(), "ReportSideEffectDialog");

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
                MedicationDetailFragment detailFragment = MedicationDetailFragment.newInstance(medication);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, detailFragment)
                        .addToBackStack(null)
                        .commit();
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