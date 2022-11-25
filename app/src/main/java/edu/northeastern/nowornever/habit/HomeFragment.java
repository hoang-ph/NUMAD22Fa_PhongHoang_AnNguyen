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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.northeastern.nowornever.R;

public class HomeFragment extends Fragment {

    private SimpleDateFormat CALENDAR_TITLE_FORMAT = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

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
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        TextView calendarTitle = view.findViewById(R.id.calendarTitle);
        calendarTitle.setText(CALENDAR_TITLE_FORMAT.format(new Date()));

        habitUuid = getArguments().getString(HABIT_ID_KEY);
        username = getArguments().getString(USERNAME_KEY);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(ROOT_HABIT).child(username).child(CHILD_HABIT).child(habitUuid);
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    DataSnapshot ds = task.getResult();
                    habitName = "Habit: " + ds.child("habitName").getValue();
                    habitNameView.setText(habitName);
                    String tempCreatedDate = String.valueOf(ds.child("createdDate").getValue());
                    createdDate = Long.parseLong(tempCreatedDate);
                    Event event = new Event(Color.RED, createdDate);
                    compactCalendarView.addEvent(event);
                }
            }
        });

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                calendarTitle.setText(CALENDAR_TITLE_FORMAT.format(firstDayOfNewMonth));
            }
        });

        return view;
    }
}