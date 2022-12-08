package edu.northeastern.nowornever.habit;

import static edu.northeastern.nowornever.utils.Constants.CHILD_HABIT;
import static edu.northeastern.nowornever.utils.Constants.CHILD_NOTE;
import static edu.northeastern.nowornever.utils.Constants.NOTE_DESCRIPTION_KEY;
import static edu.northeastern.nowornever.utils.Constants.NOTE_HABIT_UUID_KEY;
import static edu.northeastern.nowornever.utils.Constants.NOTE_TITLE_KEY;
import static edu.northeastern.nowornever.utils.Constants.NOTE_USERNAME_KEY;
import static edu.northeastern.nowornever.utils.Constants.NOTE_UUID_KEY;
import static edu.northeastern.nowornever.utils.Constants.ROOT_HABIT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.nowornever.R;

public class NoteSummaryFragment extends Fragment {

    private String username, habitUuid, noteUuid;
    private EditText noteTitle, noteDescription;
    private DatabaseReference ref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_summary, container, false);
        noteTitle = view.findViewById(R.id.noteTitleSummary);
        noteDescription = view.findViewById(R.id.noteDescriptionSummary);
        noteTitle.setText(getArguments().getString(NOTE_TITLE_KEY));
        noteDescription.setText(getArguments().getString(NOTE_DESCRIPTION_KEY));
        username = getArguments().getString(NOTE_USERNAME_KEY);
        habitUuid = getArguments().getString(NOTE_HABIT_UUID_KEY);
        noteUuid = getArguments().getString(NOTE_UUID_KEY);
        ref = FirebaseDatabase.getInstance().getReference().child(ROOT_HABIT).child(username).child(CHILD_HABIT).child(habitUuid).child(CHILD_NOTE);
        return view;
    }

    @Override
    public void onDestroyView() {
        Note note = new Note(noteTitle.getText().toString(), noteDescription.getText().toString(), username, habitUuid);
        note.setUuid(noteUuid);
        ref.child(noteUuid).setValue(note);
        Toast.makeText(getContext(), "Successfully editing the note", Toast.LENGTH_SHORT).show();
        super.onDestroyView();
    }
}
