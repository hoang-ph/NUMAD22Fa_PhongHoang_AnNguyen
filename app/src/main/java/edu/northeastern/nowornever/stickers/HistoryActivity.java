package edu.northeastern.nowornever.stickers;

import static edu.northeastern.nowornever.utils.Constants.ROOT;
import static edu.northeastern.nowornever.utils.Constants.USERNAME_KEY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.nowornever.R;

public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = HistoryActivity.class.getSimpleName();

    private RecyclerView stickerRecyclerView;
    private List<Sticker> stickers;
    private TextView numStickers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        String username = getIntent().getStringExtra(USERNAME_KEY);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        numStickers = findViewById(R.id.numStickersView);

        // Recycler View for the list of games
        stickers = new ArrayList<>();
        stickerRecyclerView = findViewById(R.id.historyRecyclerView);
        HistoryRecyclerViewAdapter adapter = new HistoryRecyclerViewAdapter(this, stickers);
        stickerRecyclerView.setAdapter(adapter);
        stickerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ref.child(ROOT).child(username).child("receivedStickers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Sticker tempSticker = ds.getValue(Sticker.class);
                        stickers.add(0, tempSticker);
                    }
                }
                if (stickers.size() != 0) {
                    stickerRecyclerView.getAdapter().notifyDataSetChanged();
                    numStickers.setText(String.valueOf(stickers.size()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "receivedStickers onCancelled:" + error);
                Toast.makeText(getApplicationContext()
                        , "DBError: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}