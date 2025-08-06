package app.bruner.pillguin.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import app.bruner.library.models.Medication;
import app.bruner.library.utils.MedicineTypeIconMapper;
import app.bruner.pillguin.databinding.CardMedicationBinding;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.MedicationViewHolder> {

    private final List<Medication> medications = new ArrayList<>();

    // public for swipe access
    public final OnMedicineActionListener listener;

    // define interface for listeners
    public interface OnMedicineActionListener {
        void onTookMedication(Medication medication);
        void onReportSideEffects(Medication medication);
        void onClick(Medication medication);
        void onSwipeToDelete(Medication medication);
    }

    public MedicationAdapter(OnMedicineActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardMedicationBinding binding = CardMedicationBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MedicationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationViewHolder holder, int position) {
        Medication medication = medications.get(position);
        holder.bind(medication);
    }

    @Override
    public int getItemCount() {
        return medications.size();
    }

    public List<Medication> getMedications() {
        return medications;
    }

    public void setMedications(List<Medication> newMedications) {
        medications.clear();
        medications.addAll(newMedications);
        notifyDataSetChanged();
    }

    class MedicationViewHolder extends RecyclerView.ViewHolder {
        private final CardMedicationBinding binding;

        MedicationViewHolder(CardMedicationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Medication medication) {
            binding.txtMedicinaName.setText(medication.getName());
            binding.txtType.setText(medication.getType());
            binding.txtDozage.setText(medication.getDosage());

            // get array of strings
            String[] medicationTypesArray = binding.getRoot().getContext().getResources().getStringArray(app.bruner.library.R.array.list_medication_types);
            ArrayList<String> medicationTypesList = new ArrayList<>(Arrays.asList(medicationTypesArray));

            // find the position
            int typePosition = medicationTypesList.indexOf(medication.getType());
            if (typePosition == -1) typePosition = 99; // defaul 99 to go to other

            // set icon
            int iconResource = MedicineTypeIconMapper.getIconByPosition(typePosition);
            binding.txtMedicinaName.setCompoundDrawablesWithIntrinsicBounds(iconResource, 0, 0, 0);

            // set click listeners
            binding.btnTookMedicine.setOnClickListener(v -> {
                if (listener != null) listener.onTookMedication(medication);
            });
            binding.btnReportSideEffects.setOnClickListener(v -> {
                if (listener != null) listener.onReportSideEffects(medication);
            });

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onClick(medication);
            });
        }
    }
}