package app.bruner.pillguin.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.bruner.library.models.Medication;
import app.bruner.library.viewModels.MedicationViewModel;
import app.bruner.pillguin.R;

public class ReportFragment extends Fragment {

    private MedicationViewModel viewModel;
    private RecyclerView recyclerView;
    private TextView textNoData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_report, container, false);
        recyclerView = root.findViewById(R.id.recyclerMedicationReports);
        textNoData = root.findViewById(R.id.textNoData);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MedicationViewModel.class);

        viewModel.getMedications().observe(getViewLifecycleOwner(), medications -> {
            if (medications == null || medications.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                textNoData.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                textNoData.setVisibility(View.GONE);
                recyclerView.setAdapter(new MedicationReportAdapter(medications));
            }
        });
    }

    private static class MedicationReportAdapter extends RecyclerView.Adapter<MedicationReportAdapter.ViewHolder> {

        private final List<Medication> medicationList;
        private final int expectedDoses = 30; // Or get dynamically from schedule

        MedicationReportAdapter(List<Medication> meds) {
            this.medicationList = meds;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_medication_report, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Medication med = medicationList.get(position);

            int totalTaken = med.getSchedule().getWhenTook() != null ? med.getSchedule().getWhenTook().size() : 0;
            int missedDoses = expectedDoses - totalTaken;
            if (missedDoses < 0) missedDoses = 0;
            double adherence = expectedDoses > 0 ? (totalTaken * 100.0 / expectedDoses) : 0;

            holder.textMedicationName.setText(med.getName());
            holder.textDosesTaken.setText("Doses Taken: " + totalTaken);
            holder.textExpectedDoses.setText("Expected Doses: " + expectedDoses);
            holder.textMissedDoses.setText("Missed Doses: " + missedDoses);
            holder.textAdherence.setText(String.format("Adherence: %.1f%%", adherence));
        }

        @Override
        public int getItemCount() {
            return medicationList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            TextView textMedicationName, textDosesTaken, textExpectedDoses, textMissedDoses, textAdherence;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                textMedicationName = itemView.findViewById(R.id.textMedicationName);
                textDosesTaken = itemView.findViewById(R.id.textDosesTaken);
                textExpectedDoses = itemView.findViewById(R.id.textExpectedDoses);
                textMissedDoses = itemView.findViewById(R.id.textMissedDoses);
                textAdherence = itemView.findViewById(R.id.textAdherence);
            }
        }
    }
}
