package edu.northeastern.nowornever.habit;

import static edu.northeastern.nowornever.utils.Constants.HABIT_ID_KEY;
import static edu.northeastern.nowornever.utils.Constants.USERNAME_KEY;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.northeastern.nowornever.R;

public class MediaFragment extends Fragment {

    private String habitUuid, habitName, username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media, container, false);
        habitUuid = getArguments().getString(HABIT_ID_KEY);
        username = getArguments().getString(USERNAME_KEY);

        return view;
    }
}