package edu.northeastern.nowornever.habit;

import static edu.northeastern.nowornever.utils.Constants.NOTE_DESCRIPTION_KEY;
import static edu.northeastern.nowornever.utils.Constants.NOTE_TITLE_KEY;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import androidx.fragment.app.Fragment;

import edu.northeastern.nowornever.R;

public class NoteSummaryFragment extends Fragment {

    private EditText noteTitle, noteDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_summary, container, false);
        noteTitle = view.findViewById(R.id.noteTitleSummary);
        noteDescription = view.findViewById(R.id.noteDescriptionSummary);
        noteTitle.setText(getArguments().getString(NOTE_TITLE_KEY));
        noteDescription.setText(getArguments().getString(NOTE_DESCRIPTION_KEY));
        return view;
    }
}
