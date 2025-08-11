package app.bruner.watch.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.bruner.library.models.Medication;
import app.bruner.library.utils.DateTimeParseUtils;
import app.bruner.watch.databinding.MedicationRowBinding;
import app.bruner.watch.ui.MedicationActivity;

/**
 * RecyclerView adapter to medications
 */
public class MedicationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Medication> medications = new ArrayList<>();
    private Context context;

    public MedicationAdapter(Context context) {
        super();
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MedicationRowBinding rowBinding = MedicationRowBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(rowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bindView(medications.get(position), position);
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

    class ViewHolder extends RecyclerView.ViewHolder {
        MedicationRowBinding binding;

        public ViewHolder(MedicationRowBinding rowBinding) {
            super(rowBinding.getRoot());
            this.binding = rowBinding;
        }

        void bindView(final Medication medication, final int position) {
            binding.txtMedicationName.setText(medication.getName());

            binding.txtTime.setText(DateTimeParseUtils.formatDateTime(context, medication.getSchedule().getNextTime()));

            // set click listener to open MedicationActivity
            itemView.setOnClickListener(v -> {
                // redirect to MedicationActivity with medication as parameter
                Intent intent = new Intent(context, MedicationActivity.class);
                intent.putExtra(MedicationActivity.MEDICATION_PARAM, medication);
                if (!(context instanceof Activity)) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent);
            });
        }
    }
}