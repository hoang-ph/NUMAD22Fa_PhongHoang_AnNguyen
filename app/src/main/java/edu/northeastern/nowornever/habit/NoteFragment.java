package edu.northeastern.nowornever.habit;

import static edu.northeastern.nowornever.utils.Constants.BLANK_NOTI;
import static edu.northeastern.nowornever.utils.Constants.CHILD_HABIT;
import static edu.northeastern.nowornever.utils.Constants.CHILD_NOTE;
import static edu.northeastern.nowornever.utils.Constants.HABIT_ID_KEY;
import static edu.northeastern.nowornever.utils.Constants.ROOT_HABIT;
import static edu.northeastern.nowornever.utils.Constants.SERVER_ERROR;
import static edu.northeastern.nowornever.utils.Constants.SUCCESS_ADD;
import static edu.northeastern.nowornever.utils.Constants.USERNAME_KEY;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.nowornever.R;

public class NoteFragment extends Fragment {

    private static final String TAG = NoteFragment.class.getSimpleName();

    private DatabaseReference databaseReference;
    private RecyclerView notesRecyclerView;
    private List<Note> notes;
    private String username;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        String habitUuid = getArguments().getString(HABIT_ID_KEY);
        this.username = getArguments().getString(USERNAME_KEY);

        notes = new ArrayList<>();
        notesRecyclerView = view.findViewById(R.id.noteRecyclerView);
        NoteRecyclerViewAdapter adapter = new NoteRecyclerViewAdapter(getContext(), notes);
        notesRecyclerView.setAdapter(adapter);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseReference = FirebaseDatabase.getInstance().getReference().child(ROOT_HABIT).child(username).child(CHILD_HABIT).child(habitUuid).child(CHILD_NOTE);
        loadNotesData();

        // Add a note with fab
        FloatingActionButton fab = view.findViewById(R.id.addNoteBtn);
        fab.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.note_fab);
            final EditText inputNoteTitle = dialog.findViewById(R.id.inputNoteName);
            final EditText inputNoteDescription = dialog.findViewById(R.id.inputNoteDescription);
            Button noteSaveBtn = dialog.findViewById(R.id.noteSaveButton);
            Button noteCancelBtn = dialog.findViewById(R.id.noteCancelButton);
            noteSaveBtn.setOnClickListener(view1 -> {
                String noteTitle = inputNoteTitle.getText().toString();
                String noteDescription = inputNoteDescription.getText().toString();
                if (noteDescription.isEmpty() || noteTitle.isEmpty()) {
                    Toast.makeText(getContext(),BLANK_NOTI, Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    Note note = new Note(noteTitle, noteDescription);
                    databaseReference.child(note.getUuid()).setValue(note).addOnCompleteListener(task -> {
                       loadNotesData();
                       Toast.makeText(getContext(), SUCCESS_ADD, Toast.LENGTH_SHORT).show();
                    });
                }

            });

            noteCancelBtn.setOnClickListener(view1 -> dialog.dismiss());
            dialog.show();
        });

        return view;
    }

    private void loadNotesData() {
        notes.clear();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Note tempNote = ds.getValue(Note.class);
                        notes.add(0, tempNote);
                    }
                }
                if (notes.size() != 0) {
                    notesRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }
}