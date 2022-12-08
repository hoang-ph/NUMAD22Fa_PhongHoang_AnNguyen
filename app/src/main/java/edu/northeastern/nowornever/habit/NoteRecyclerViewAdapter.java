package edu.northeastern.nowornever.habit;

import static edu.northeastern.nowornever.utils.Constants.NOTE_DESCRIPTION_KEY;
import static edu.northeastern.nowornever.utils.Constants.NOTE_HABIT_UUID_KEY;
import static edu.northeastern.nowornever.utils.Constants.NOTE_TITLE_KEY;
import static edu.northeastern.nowornever.utils.Constants.NOTE_USERNAME_KEY;
import static edu.northeastern.nowornever.utils.Constants.NOTE_UUID_KEY;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.nowornever.R;

public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.NoteViewHolder> {

    private final List<Note> notes;
    private final Context context;

    public NoteRecyclerViewAdapter(Context context, List<Note> notes) {
        this.notes = notes;
        this.context = context;
    }

    @NonNull
    @Override
    public NoteRecyclerViewAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteRecyclerViewAdapter.NoteViewHolder holder, int position) {
        holder.noteTitleView.setText(notes.get(position).getNoteName());
        String createdDate = "Created On " + notes.get(position).getCreatedDate();
        holder.noteCreatedDateView.setText(createdDate);
        String description = notes.get(position).getNoteDescription();
        if (description.length() > 30) {
            description = description.subSequence(0, 30) + "...";
        }
        holder.noteDescriptionView.setText(description);
        holder.noteItemLayout.setOnClickListener(view -> launchNoteSummary(notes.get(position)));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {

        private final ConstraintLayout noteItemLayout;
        private final TextView noteTitleView, noteCreatedDateView, noteDescriptionView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteItemLayout = itemView.findViewById(R.id.noteItemLayout);
            noteTitleView = itemView.findViewById(R.id.noteTitleView);
            noteDescriptionView = itemView.findViewById(R.id.noteDescriptionView);
            noteCreatedDateView = itemView.findViewById(R.id.noteCreatedDateView);
        }
    }

    private void launchNoteSummary(Note note) {
        NoteSummaryFragment fragment = new NoteSummaryFragment();
        Bundle storage = new Bundle();
        storage.putString(NOTE_TITLE_KEY, note.getNoteName());
        storage.putString(NOTE_DESCRIPTION_KEY, note.getNoteDescription());
        storage.putString(NOTE_USERNAME_KEY, note.getUsername());
        storage.putString(NOTE_HABIT_UUID_KEY, note.getHabitUuid());
        storage.putString(NOTE_UUID_KEY, note.getUuid());
        fragment.setArguments(storage);
        switchContent(fragment);
    }

    public void switchContent(Fragment fragment) {
        if (context == null) return;
        if (context instanceof HabitActivity) {
            HabitActivity habitActivity = (HabitActivity) context;
            habitActivity.switchContent(fragment);
        }
    }
}
