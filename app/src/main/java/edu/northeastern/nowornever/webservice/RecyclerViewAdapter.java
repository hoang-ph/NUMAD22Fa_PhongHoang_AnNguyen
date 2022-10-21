package edu.northeastern.nowornever.webservice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.nowornever.R;
import edu.northeastern.nowornever.model.Game;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.GameViewHolder> {

    private List<Game> games;
    private Context context;
    public RecyclerViewAdapter(Context context, List<Game> games) {
        this.context = context;
        this.games = games;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.game_row, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        holder.titleText.setText(games.get(position).getTitle());
        holder.descriptionText.setText(games.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public class GameViewHolder extends RecyclerView.ViewHolder {

        private TextView titleText, descriptionText;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleTextView);
            descriptionText = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
