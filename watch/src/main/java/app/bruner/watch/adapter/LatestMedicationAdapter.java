package app.bruner.watch.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.bruner.library.models.Medication;
import app.bruner.library.utils.DateTimeParseUtils;
import app.bruner.watch.databinding.MedicationRowBinding;
import app.bruner.watch.ui.MedicationActivity;

public class LatestMedicationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Medication> medications = new ArrayList<>();
    private Context context;

    public LatestMedicationAdapter(Context context) {
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

            // Show the last whenTook date if available
            Date lastTaken = null;
            if (medication.getSchedule() != null && medication.getSchedule().getWhenTook() != null) {
                ArrayList<Date> whenTook = medication.getSchedule().getWhenTook();
                if (!whenTook.isEmpty()) {
                    lastTaken = whenTook.get(whenTook.size() - 1);
                }
            }
            String dateText = lastTaken != null
                    ? DateTimeParseUtils.formatDateTime(context, lastTaken)
                    : "-";
            binding.txtTime.setText(dateText);

            // set click listener to open MedicationActivity
            itemView.setOnClickListener(v -> {
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