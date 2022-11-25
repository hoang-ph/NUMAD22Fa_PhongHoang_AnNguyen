package edu.northeastern.nowornever.habit;

import static edu.northeastern.nowornever.utils.Constants.HABIT_ID_KEY;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.nowornever.R;

public class HabitRecyclerViewAdapter extends RecyclerView.Adapter<HabitRecyclerViewAdapter.HabitViewHolder> {

    private final List<Habit> habits;
    private final Context context;

    public HabitRecyclerViewAdapter(Context context, List<Habit> habits) {
        this.context = context;
        this.habits = habits;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.habit_item, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        holder.habitTitleView.setText(habits.get(position).getHabitName());
        String createdDate = "Created on " + habits.get(position).getCreatedDate();
        holder.createdDateView.setText(createdDate);
        String frequency = habits.get(position).getFrequency() + " minutes per day";
        holder.frequencyView.setText(frequency);
        holder.habitItemLayout.setOnClickListener(view -> launchHabitCentralAct(habits.get(position).getUuid()));
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    public static class HabitViewHolder extends RecyclerView.ViewHolder {

        private final ConstraintLayout habitItemLayout;
        private final TextView habitTitleView, createdDateView, frequencyView;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            habitItemLayout = itemView.findViewById(R.id.habitItemLayout);
            habitTitleView = itemView.findViewById(R.id.habitTitleView);
            createdDateView = itemView.findViewById(R.id.createdDateView);
            frequencyView = itemView.findViewById(R.id.frequencyView);
        }
    }

    private void launchHabitCentralAct(String habitUuid) {
        Intent intent = new Intent(context, HabitActivity.class);
        intent.putExtra(HABIT_ID_KEY, habitUuid);
        context.startActivity(intent);
    }

}
