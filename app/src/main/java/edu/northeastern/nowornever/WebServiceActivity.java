package edu.northeastern.nowornever;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    private static final String TAG = "WebServiceActivity", STATE_LIST = "state_list";

    private Retrofit retrofit;
    private IFreeToGame api;

    private FrameLayout searchButton;
    private ProgressBar progressBar;
    private TextView loadBtnText;

    private RecyclerView recyclerView;
    private Spinner platformSpinner;
    private Spinner sortBySpinner;
    private Spinner genreSpinner;

    private String genre, sortBy, platform;
    ArrayList<Game> games;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "_onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service);
        searchButton = findViewById(R.id.searchButton);
        progressBar = findViewById(R.id.progressBar);
        loadBtnText = findViewById(R.id.loadBtnText);

        // Platform Spinner
        platformSpinner = findViewById(R.id.platformSpinner);
        ArrayAdapter<CharSequence> platformAdapter = ArrayAdapter.createFromResource(this,R.array.platform_array, android.R.layout.simple_spinner_dropdown_item);
        platformAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        platformSpinner.setAdapter(platformAdapter);
        platformSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                platform = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "Platform onItemSelected: " + platform);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Sort_by Spinner
        sortBySpinner = findViewById(R.id.sortBySpinner);
        ArrayAdapter<CharSequence> sortByAdapter = ArrayAdapter.createFromResource(this,R.array.sort_by_array, android.R.layout.simple_spinner_dropdown_item);
        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBySpinner.setAdapter(sortByAdapter);
        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortBy = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "Sort-by onItemSelected: " + sortBy);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Genre Spinner
        genreSpinner = findViewById(R.id.genreSpinner);
        ArrayAdapter<CharSequence> genreAdapter = ArrayAdapter.createFromResource(this,R.array.genre_array, android.R.layout.simple_spinner_dropdown_item);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(genreAdapter);
        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                genre = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "Genre onItemSelected: " + genre);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Recycler View for the list of games
        games = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, games);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchButton.setOnClickListener(view -> search());

        // Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.freetogame.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(IFreeToGame.class);

        if (savedInstanceState != null) {
            ArrayList<Game> tempList = savedInstanceState.getParcelableArrayList(STATE_LIST);
            games.addAll(tempList);
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    private void search() {
        progressBar.setVisibility(View.VISIBLE);
        loadBtnText.setVisibility(View.GONE);

        // clear the recycler view before each call
        games.clear();

        Call<List<Game>> call;
        if (platform.equals("All") && genre.equals("All")) {
            // get all the games
            call = api.getGames(sortBy);
        } else if (platform.equals("All")) {
            // get games by genre
            call = api.getGamesByGenre(genre, sortBy);
        } else if (genre.equals("All")) {
            // get games by platform
            call = api.getGamesByPlatform(platform, sortBy);
        } else {
            // get games by platform & genre
            call = api.getGamesByPlatformAndGenre(platform, genre, sortBy);
        }
        Log.d(TAG, "_onCall: " + call.request().url());
        call.enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                progressBar.setVisibility(View.INVISIBLE);
                loadBtnText.setVisibility(View.VISIBLE);

                if (!response.isSuccessful()) {
                    Log.d(TAG, "_Call failed!" + response.code());
                    return;
                }

                Log.d(TAG, "_Call Succeed!");
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
                Toast.makeText(WebServiceActivity.this, "Failed to fetch the data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_LIST, games);
    }
}
