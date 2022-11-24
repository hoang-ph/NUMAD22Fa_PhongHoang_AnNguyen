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

    private List<Habit> habits;
    private Context context;

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
        String currStreak = "Current streak: " + habits.get(position).getCurrStreak() + " days"; // TODO: Update the unit
        holder.streakView.setText(currStreak);
        holder.frequencyView.setText(habits.get(position).getFrequency());
        holder.habitItemLayout.setOnClickListener(view -> launchHabitCentralAct(habits.get(position).getUuid()));
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    public static class HabitViewHolder extends RecyclerView.ViewHolder {

        private final ConstraintLayout habitItemLayout;
        private final TextView habitTitleView, streakView, frequencyView;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            habitItemLayout = itemView.findViewById(R.id.habitItemLayout);
            habitTitleView = itemView.findViewById(R.id.habitTitleView);
            streakView = itemView.findViewById(R.id.streakView);
            frequencyView = itemView.findViewById(R.id.frequencyView);
        }
    }

    private void launchHabitCentralAct(String habitUuid) {
        Intent intent = new Intent(context, HabitActivity.class);
        intent.putExtra(HABIT_ID_KEY, habitUuid);
        context.startActivity(intent);
    }

}
