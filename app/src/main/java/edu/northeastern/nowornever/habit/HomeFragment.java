package edu.northeastern.nowornever.habit;

import static edu.northeastern.nowornever.utils.Constants.CHILD_HABIT;
import static edu.northeastern.nowornever.utils.Constants.HABIT_ID_KEY;
import static edu.northeastern.nowornever.utils.Constants.ROOT_HABIT;
import static edu.northeastern.nowornever.utils.Constants.USERNAME_KEY;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import edu.northeastern.nowornever.R;

public class HomeFragment extends Fragment {

    private String habitUuid, habitName, username;
    private long createdDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView habitNameView = view.findViewById(R.id.habitNameView);
        CompactCalendarView compactCalendarView = (CompactCalendarView) view.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);

        habitUuid = getArguments().getString(HABIT_ID_KEY);
        username = getArguments().getString(USERNAME_KEY);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(ROOT_HABIT).child(username).child(CHILD_HABIT).child(habitUuid);
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    DataSnapshot ds = task.getResult();
                    habitName = String.valueOf(ds.child("habitName").getValue());
                    habitNameView.setText(habitName);
                    String tempCreatedDate = String.valueOf(ds.child("createdDate").getValue());
                    createdDate = Long.parseLong(tempCreatedDate);
                    Event event = new Event(Color.GREEN, createdDate);
                    compactCalendarView.addEvent(event);
                }
            }
        });

        return view;
    }
}