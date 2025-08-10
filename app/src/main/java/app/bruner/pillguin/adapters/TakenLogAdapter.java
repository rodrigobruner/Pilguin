package app.bruner.pillguin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

import app.bruner.library.utils.DateTimeParseUtils;

public class TakenLogAdapter extends RecyclerView.Adapter<TakenLogAdapter.ViewHolder> {

    private List<Date> logs;

    public TakenLogAdapter(List<Date> logs) {
        this.logs = logs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Date dateLog = logs.get(position);
        String formattedDateTime = DateTimeParseUtils.formatDateTime(holder.itemView.getContext(), dateLog);
        holder.textView.setText(formattedDateTime);
    }

    @Override
    public int getItemCount() {
        return logs == null ? 0 : logs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }

    public void setData(List<Date> newLogs) {
        this.logs = newLogs;
        notifyDataSetChanged();
    }
}