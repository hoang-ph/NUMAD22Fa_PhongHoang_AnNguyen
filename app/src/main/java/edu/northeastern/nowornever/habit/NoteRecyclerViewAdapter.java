package edu.northeastern.nowornever.habit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
        if (description.length() > 10) {
            description = description.subSequence(0, 10) + "...";
        }
        holder.noteDescriptionView.setText(description);
        holder.noteItemLayout.setOnClickListener(view -> launchNoteSummary(notes.get(position).getUuid()));
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

    private void launchNoteSummary(String noteUuid) {

    }
}
