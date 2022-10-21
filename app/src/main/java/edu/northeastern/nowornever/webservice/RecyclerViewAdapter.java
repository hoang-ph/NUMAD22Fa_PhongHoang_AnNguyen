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
        String description = context.getString(R.string.description) + " " + games.get(position).getShortDescription();
        holder.descriptionText.setText(description);
        String genre = context.getString(R.string.genre) + " " + games.get(position).getGenre();
        holder.genreText.setText(genre);
        String platform = context.getString(R.string.platform) + " " + games.get(position).getPlatform();
        holder.platformText.setText(platform);
        String developer = context.getString(R.string.developer) + " " + games.get(position).getDeveloper();
        holder.developerText.setText(developer);
        String publisher = context.getString(R.string.publisher) + " " + games.get(position).getPublisher();
        holder.publisherText.setText(publisher);
        String releaseDate = context.getString(R.string.release_date) + " " + games.get(position).getReleaseDate();
        holder.releaseDateText.setText(releaseDate);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public class GameViewHolder extends RecyclerView.ViewHolder {

        private TextView titleText, descriptionText, genreText, platformText, developerText, publisherText, releaseDateText;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleTextView);
            descriptionText = itemView.findViewById(R.id.descriptionTextView);
            genreText = itemView.findViewById(R.id.genreTextView);
            platformText = itemView.findViewById(R.id.platformTextView);
            developerText = itemView.findViewById(R.id.developerTextView);
            publisherText = itemView.findViewById(R.id.publisherTextView);
            releaseDateText = itemView.findViewById(R.id.releaseDateTextView);
        }
    }
}
