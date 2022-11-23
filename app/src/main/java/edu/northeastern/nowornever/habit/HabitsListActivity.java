package edu.northeastern.nowornever.habit;

import static edu.northeastern.nowornever.utils.Constants.CHILD_HABIT;
import static edu.northeastern.nowornever.utils.Constants.ROOT_HABIT;
import static edu.northeastern.nowornever.utils.Constants.SHARED_PREF;
import static edu.northeastern.nowornever.utils.Constants.SUCCESS_LOGOUT;
import static edu.northeastern.nowornever.utils.Constants.USERNAME_KEY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    private String username;

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

        username = sharedPreferences.getString(USERNAME_KEY, null);
        if (username != null) {
            String title = "Username: " + username;
            habitUsernameDisplay.setText(title);
            loadHabitsData();
        }

    }

    private void loadHabitsData() {
        ref = FirebaseDatabase.getInstance().getReference().child(ROOT_HABIT).child(username).child(CHILD_HABIT);
        ref.addValueEventListener(new ValueEventListener() {
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
                Toast.makeText(getApplicationContext()
                        , "Server error - fail to get list of habits", Toast.LENGTH_SHORT).show();
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