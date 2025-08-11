package app.bruner.pillguin.ui.medication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.bruner.library.models.Medication;
import app.bruner.pillguin.R;

public class MedicationReportAdapter extends RecyclerView.Adapter<MedicationReportAdapter.ViewHolder> {

    private final List<Medication> medicationList;
    private final int expectedDoses = 30; // Or get dynamically from schedule
    private final Context context;

    public MedicationReportAdapter(List<Medication> meds, Context context) {
        this.medicationList = meds;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_medication_report, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medication med = medicationList.get(position);

        int totalTaken = med.getSchedule().getWhenTook() != null ? med.getSchedule().getWhenTook().size() : 0;
        holder.textMedicationName.setText(med.getName());
        holder.textDosesTaken.setText(context.getString(R.string.doses_taken, totalTaken));
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
        }
    }
}