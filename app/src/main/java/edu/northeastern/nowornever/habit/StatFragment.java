package edu.northeastern.nowornever.habit;

import static edu.northeastern.nowornever.utils.Constants.CALENDAR_TITLE;
import static edu.northeastern.nowornever.utils.Constants.MONTHLY_PERCENTAGE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import edu.northeastern.nowornever.R;

public class StatFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stat, container, false);

        int monthCompletionPercentage = getArguments().getInt(MONTHLY_PERCENTAGE);
        String currentMonthYear = getArguments().getString(CALENDAR_TITLE);

        ProgressBar progressBar = view.findViewById(R.id.monthlyCompletionProgress);
        TextView monthlyCompletionView = view.findViewById(R.id.monthlyCompletionView);
        TextView calendarTitle = view.findViewById(R.id.calendarTitle);

        progressBar.setProgress(monthCompletionPercentage);
        String percentage = monthCompletionPercentage + "%";
        monthlyCompletionView.setText(percentage);
        calendarTitle.setText(currentMonthYear);

        return view;
    }
}