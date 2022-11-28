package edu.northeastern.nowornever.habit;

import static edu.northeastern.nowornever.utils.Constants.CALENDAR_TITLE;
import static edu.northeastern.nowornever.utils.Constants.CHILD_HABIT;
import static edu.northeastern.nowornever.utils.Constants.HABIT_ID_KEY;
import static edu.northeastern.nowornever.utils.Constants.MONTHLY_PERCENTAGE;
import static edu.northeastern.nowornever.utils.Constants.ONE_DAY_IN_EPOCH;
import static edu.northeastern.nowornever.utils.Constants.ROOT_HABIT;
import static edu.northeastern.nowornever.utils.Constants.USERNAME_KEY;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.northeastern.nowornever.R;

public class HomeFragment extends Fragment {

    private final SimpleDateFormat CALENDAR_TITLE_FORMAT = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

    private String habitUuid, habitName, username;
    private List<String> dailyHabitCompletions;
    TextView currStreakView;
    CompactCalendarView compactCalendarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView habitNameView = view.findViewById(R.id.habitNameView);
        compactCalendarView = (CompactCalendarView) view.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        TextView calendarTitle = view.findViewById(R.id.calendarTitle);
        calendarTitle.setText(CALENDAR_TITLE_FORMAT.format(new Date()));
        CheckBox completionCheckBox = view.findViewById(R.id.comletionCheckBox);
        currStreakView = view.findViewById(R.id.currStreakView);

        habitUuid = getArguments().getString(HABIT_ID_KEY);
        username = getArguments().getString(USERNAME_KEY);
        dailyHabitCompletions = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(ROOT_HABIT).child(username).child(CHILD_HABIT).child(habitUuid);
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    DataSnapshot ds = task.getResult();
                    habitName = "Habit: " + ds.child("habitName").getValue();
                    habitNameView.setText(habitName);

                    dailyHabitCompletions = (List<String>) ds.child("dailyCompletion").getValue();
                    if (dailyHabitCompletions != null) {
                        for (String eachCompletedDate : dailyHabitCompletions) {
                            long createdDate = Long.parseLong(eachCompletedDate);
                            Event event = new Event(Color.RED, createdDate);
                            compactCalendarView.addEvent(event);
                        }
                        updateStreakView();
                        sendMonthlyCompletionToStatFragment(new Date());
                    }
                }
            }
        });


        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                if (compactCalendarView.getEvents(dateClicked).isEmpty()) {
                    completionCheckBox.setChecked(false);
                    completionCheckBox.setEnabled(true);
                    completionCheckBox.setOnClickListener(v -> {
                        long epochDateClicked = dateClicked.toInstant().toEpochMilli();
                        compactCalendarView.addEvent(new Event(Color.RED, epochDateClicked));
                        completionCheckBox.setChecked(true);
                        completionCheckBox.setEnabled(false);
                        if (dailyHabitCompletions == null) {
                            dailyHabitCompletions = new ArrayList<>();
                        }
                        dailyHabitCompletions.add(String.valueOf(epochDateClicked));
                        ref.child("dailyCompletion").setValue(dailyHabitCompletions);
                        updateStreakView();
                        sendMonthlyCompletionToStatFragment(dateClicked);
                    });
                } else {
                    completionCheckBox.setChecked(true);
                    completionCheckBox.setEnabled(false);
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                calendarTitle.setText(CALENDAR_TITLE_FORMAT.format(firstDayOfNewMonth));
                sendMonthlyCompletionToStatFragment(firstDayOfNewMonth);
            }
        });

        return view;
    }

    private void updateStreakView() {
        String streak = "Current streak: " + getCurrStreak() + " days";
        currStreakView.setText(streak);
    }

    private int getCurrStreak() {
        if (dailyHabitCompletions == null) {
            return 0;
        }
        dailyHabitCompletions.sort(Collections.reverseOrder());
        int streak = 0;
        long dateEpoch = Instant.now().toEpochMilli();
        for (String item : dailyHabitCompletions) {
            long itemEpoch = Long.parseLong(item);
            if ((dateEpoch - itemEpoch) > ONE_DAY_IN_EPOCH) {
                break;
            } else {
                streak += 1;
                dateEpoch = itemEpoch;
            }
        }
        return streak;
    }

    private void sendMonthlyCompletionToStatFragment(Date date) {
        int completedDays = compactCalendarView.getEventsForMonth(date).size();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int numDaysOfMonth = localDate.lengthOfMonth();
        int percentage = (completedDays / numDaysOfMonth) * 100;
        getArguments().putInt(MONTHLY_PERCENTAGE, percentage);

        String calendarTitle = CALENDAR_TITLE_FORMAT.format(date);
        getArguments().putString(CALENDAR_TITLE, calendarTitle);
    }
}