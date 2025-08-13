package app.bruner.watch.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.bruner.library.models.Medication;
import app.bruner.library.utils.DateTimeParseUtils;
import app.bruner.library.utils.MedicineTypeIconMapper;
import app.bruner.watch.R;
import app.bruner.watch.databinding.MedicationRowBinding;
import app.bruner.watch.ui.MedicationActivity;

/**
 * RecyclerView adapter to medications
 */
public class MedicationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Medication> medications = new ArrayList<>();
    private Context context;

    private boolean showNextTime = true;

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

    public void setShowNextTime(boolean showNextTime) {
        this.showNextTime = showNextTime;
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
            binding.imgType.setImageResource(MedicineTypeIconMapper.getIconByType(context, medication.getType()));

            if(showNextTime) { // is to show  next time info
                binding.txtTime.setText(DateTimeParseUtils.formatDateTime(context, medication.getSchedule().getNextTime()));
                Date now = new Date();
                if (medication.getSchedule().getNextTime().before(now)) { // set red if is late
                    binding.imgTime.setColorFilter(ContextCompat.getColor(context, app.bruner.library.R.color.light_red));
                } else { // yellow if is not
                    binding.imgTime.setColorFilter(ContextCompat.getColor(context, app.bruner.library.R.color.light_yellow));
                }
            } else { // else hide
                binding.txtTime.setVisibility(View.GONE);
                binding.imgTime.setVisibility(View.GONE);
            }

            if(medication.getSchedule().getLastTaken() == null) { // never taken, set gray
                binding.imgTimeLastTaken.setColorFilter(ContextCompat.getColor(context, app.bruner.library.R.color.gray_400));
                binding.txtTimeLastTaken.setText(context.getString(R.string.txt_never_taken));
            } else { // set last taken date, green
                binding.imgTimeLastTaken.setColorFilter(ContextCompat.getColor(context, app.bruner.library.R.color.light_green));
                binding.txtTimeLastTaken.setText(DateTimeParseUtils.formatDateTime(context, medication.getSchedule().getLastTaken()));
            }


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