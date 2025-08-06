package app.bruner.watch.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.bruner.library.models.Medication;
import app.bruner.watch.databinding.MedicationRowBinding;
import app.bruner.watch.ui.MedicationActivity;

public class MedicationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Medication> medicationList;
    private Context context;

    public MedicationAdapter(Context context, ArrayList<Medication> medicationList) {
        super();
        this.context = context;
        this.medicationList = medicationList;
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
        ((ViewHolder) holder).bindView(medicationList.get(position), position);
    }

    @Override
    public int getItemCount() {
        int count = (medicationList == null) ? 0 : medicationList.size();
        return count;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MedicationRowBinding recyclerRowBinding;

        public ViewHolder(MedicationRowBinding rowBinding) {
            super(rowBinding.getRoot());
            this.recyclerRowBinding = rowBinding;
        }

        void bindView(final Medication medication, final int position) {
            recyclerRowBinding.txtMedicationName.setText(medication.getName());
            if (medication.getSchedule() != null && medication.getSchedule().getTimes() != null && !medication.getSchedule().getTimes().isEmpty()) {
                recyclerRowBinding.txtTime.setText(medication.getSchedule().getTimes().get(0));
            } else {
                recyclerRowBinding.txtTime.setText("");
            }

            itemView.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(context, app.bruner.watch.ui.MedicationActivity.class);
                intent.putExtra(MedicationActivity.MEDICATION_PARAM, medication);
                if (!(context instanceof android.app.Activity)) {
                    intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent);
            });
        }
    }
}