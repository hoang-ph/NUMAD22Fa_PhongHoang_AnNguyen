package edu.northeastern.nowornever.habit;

import static edu.northeastern.nowornever.utils.Constants.HABIT_ID_KEY;
import static edu.northeastern.nowornever.utils.Constants.SHARED_PREF;
import static edu.northeastern.nowornever.utils.Constants.USERNAME_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import edu.northeastern.nowornever.R;
import edu.northeastern.nowornever.databinding.ActivityHabitBinding;

public class HabitActivity extends AppCompatActivity {

    private ActivityHabitBinding binding;
    private Bundle storage;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHabitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Store habit uuid and username in fragment manager
        String habitUuid = getIntent().getStringExtra(HABIT_ID_KEY);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        String username = sharedPreferences.getString(USERNAME_KEY, null);
        storage = new Bundle();
        storage.putString(HABIT_ID_KEY, habitUuid);
        storage.putString(USERNAME_KEY, username);


        displaySelectedFrag(new HomeFragment());

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
        fragment.setArguments(storage);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.habitFrameLayout, fragment);
        fragmentTransaction.commit();
    }

}