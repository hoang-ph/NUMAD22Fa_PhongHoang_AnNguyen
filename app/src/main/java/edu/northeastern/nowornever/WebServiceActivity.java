package edu.northeastern.nowornever;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.nowornever.model.Game;
import edu.northeastern.nowornever.model.IFreeToGame;
import edu.northeastern.nowornever.webservice.RecyclerViewAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebServiceActivity extends AppCompatActivity {
    private static final String TAG = "WebServiceActivity";

    private Retrofit retrofit;
    private IFreeToGame api;
    private Button searchButton;
    private RecyclerView recyclerView;

    private List<Game> games;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "_onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service);
        searchButton = findViewById(R.id.searchButton);

        // Recycler View for the list of games
        games = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, games);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchButton.setOnClickListener(view -> {
            search();
        });

        // Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.freetogame.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(IFreeToGame.class);
    }

    private void search() {
        Call<List<Game>> call = api.getGames();
        call.enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "_Call failed!" + response.code());
                    return;
                }

                Log.d(TAG, "_Call Successed!");
                List<Game> result = response.body();
                for (Game game : result) {
                    StringBuffer str = new StringBuffer();
                    str.append("_Code: ")
                            .append(response.code())
                            .append("\n")
                            .append("ID : ")
                            .append(game.getId())
                            .append("\n")
                            .append("Title: ")
                            .append(game.getTitle())
                            .append("\n")
                            .append("shortDescription :")
                            .append(game.getShortDescription())
                            .append("\n")
                            .append("Genre: ")
                            .append(game.getGenre())
                            .append("\n")
                            .append("Platform: ")
                            .append(game.getPlatform())
                            .append("\n")
                            .append("Publisher: ")
                            .append(game.getPublisher())
                            .append("\n")
                            .append("Developer: ")
                            .append(game.getDeveloper())
                            .append("\n")
                            .append("releaseDate: ")
                            .append(game.getReleaseDate())
                            .append("\n");
                    Log.d(TAG, str.toString());
                    games.add(new Game(game.getId(), game.getTitle(), game.getShortDescription(),
                            game.getGenre(), game.getPlatform(), game.getPublisher(),
                            game.getDeveloper(), game.getReleaseDate()));
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t) {
                Log.d(TAG, "_onFailure: " + t.getMessage());
            }
        });
    }
}
