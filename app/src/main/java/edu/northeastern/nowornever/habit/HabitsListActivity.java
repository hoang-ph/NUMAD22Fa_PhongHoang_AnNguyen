package edu.northeastern.nowornever.habit;

import static edu.northeastern.nowornever.utils.Constants.BLANK_NOTI;
import static edu.northeastern.nowornever.utils.Constants.CHILD_HABIT;
import static edu.northeastern.nowornever.utils.Constants.ROOT_HABIT;
import static edu.northeastern.nowornever.utils.Constants.SERVER_ERROR;
import static edu.northeastern.nowornever.utils.Constants.SHARED_PREF;
import static edu.northeastern.nowornever.utils.Constants.SUCCESS_ADD;
import static edu.northeastern.nowornever.utils.Constants.SUCCESS_LOGOUT;
import static edu.northeastern.nowornever.utils.Constants.USERNAME_KEY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.nowornever.R;

public class HabitsListActivity extends AppCompatActivity {

    private static final String TAG = HabitsListActivity.class.getSimpleName();

    private RecyclerView habitsRecyclerView;
    private List<Habit> habits;
    private SharedPreferences sharedPreferences;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits_list);

        TextView habitUsernameDisplay = findViewById(R.id.habitUsernameDisplay);
        habits = new ArrayList<>();
        habitsRecyclerView = findViewById(R.id.habitRecyclerView);
        HabitRecyclerViewAdapter adapter = new HabitRecyclerViewAdapter(this, habits);
        habitsRecyclerView.setAdapter(adapter);
        habitsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        String username = sharedPreferences.getString(USERNAME_KEY, null);
        ref = FirebaseDatabase.getInstance().getReference().child(ROOT_HABIT).child(username).child(CHILD_HABIT);
        String title = "Username: " + username;
        habitUsernameDisplay.setText(title);
        loadHabitsData();

        FloatingActionButton fab = findViewById(R.id.addHabitBtn);
        fab.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(HabitsListActivity.this);
            dialog.setContentView(R.layout.habit_fab);
            final EditText inputHabitName = dialog.findViewById(R.id.inputHabitName);
            final EditText inputFreqNum = dialog.findViewById(R.id.inputFreqNum);
            Button habitSaveBtn = dialog.findViewById(R.id.habitSaveBtn);
            Button cancelBtn = dialog.findViewById(R.id.cancelBtn);

            habitSaveBtn.setOnClickListener(view -> {
                String habitName = inputHabitName.getText().toString();
                String freqNum = inputFreqNum.getText().toString();

                if (habitName.isEmpty() || freqNum.isEmpty()) {
                    Toast.makeText(HabitsListActivity.this, BLANK_NOTI, Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    Habit habit = new Habit(habitName, freqNum);
                    ref.child(habit.getUuid()).setValue(habit).addOnCompleteListener(task -> {
                        loadHabitsData();
                        Toast.makeText(HabitsListActivity.this, SUCCESS_ADD, Toast.LENGTH_SHORT).show();
                    });
                }
            });

            cancelBtn.setOnClickListener(view -> dialog.dismiss());

            dialog.show();
        });

    }

    private void loadHabitsData() {
        habits.clear();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Habit tempHabit = ds.getValue(Habit.class);
                        habits.add(0, tempHabit);
                    }
                }
                if (habits.size() != 0) {
                    habitsRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "listOfHabits onCancelled:" + error);
                Toast.makeText(getApplicationContext(), SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void LogOut(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Toast.makeText(getApplicationContext(), SUCCESS_LOGOUT, Toast.LENGTH_SHORT).show();
        finish();
    }
}