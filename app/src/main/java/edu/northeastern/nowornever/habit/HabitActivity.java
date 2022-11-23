package edu.northeastern.nowornever.habit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import edu.northeastern.nowornever.R;
import edu.northeastern.nowornever.databinding.ActivityHabitBinding;

public class HabitActivity extends AppCompatActivity {

    private ActivityHabitBinding binding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHabitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.habitFrameLayout, fragment);
        fragmentTransaction.commit();
    }

}