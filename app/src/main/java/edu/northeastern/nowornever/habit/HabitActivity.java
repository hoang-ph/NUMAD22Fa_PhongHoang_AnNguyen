package edu.northeastern.nowornever.habit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import edu.northeastern.nowornever.R;
import edu.northeastern.nowornever.databinding.ActivityHabitBinding;

public class HabitActivity extends AppCompatActivity {

    private static final String USERNAME_KEY = "usernameKey", SHARED_PREF = "userPref";

    private String username;
    private SharedPreferences sharedPreferences;
    private TextView testHabit;
    private ActivityHabitBinding binding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHabitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displaySelectedFrag(new HomeFragment());

        testHabit = findViewById(R.id.testHabit);

        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);

        username = sharedPreferences.getString(USERNAME_KEY, null);
        if (username != null) {
            String title = "Username: " + username; // Replace/ stop using this later
            testHabit.setText(title);
        }

        binding.habitBotNavBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.homeFrag:
                    displaySelectedFrag(new HomeFragment());
                    break;
                case R.id.noteFrag:
                    displaySelectedFrag(new NoteFragment());
                    break;
                case R.id.statFrag:
                    displaySelectedFrag(new StatFragment());
                    break;
                case R.id.mediaFrag:
                    displaySelectedFrag(new MediaFragment());
                    break;
                case R.id.friendFrag:
                    displaySelectedFrag(new FriendFragment());
                    break;
            }
            return true;
        });
    }

    private void displaySelectedFrag(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.habitFrameLayout, fragment);
        fragmentTransaction.commit();
    }

}