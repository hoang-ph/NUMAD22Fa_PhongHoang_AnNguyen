package edu.northeastern.nowornever.habit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import edu.northeastern.nowornever.R;

public class HabitActivity extends AppCompatActivity {

    private static final String USERNAME_KEY = "usernameKey", SHARED_PREF = "userPref";

    private String username;
    private SharedPreferences sharedPreferences;
    private TextView testHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        testHabit = findViewById(R.id.testHabit);

        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);

        username = sharedPreferences.getString(USERNAME_KEY, null);
        if (username != null) {
            testHabit.setText(username);
        }
    }

}