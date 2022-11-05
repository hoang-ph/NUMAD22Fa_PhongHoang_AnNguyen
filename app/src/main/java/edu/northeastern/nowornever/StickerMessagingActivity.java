package edu.northeastern.nowornever;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.northeastern.nowornever.model.sticker.Sticker;
import edu.northeastern.nowornever.model.sticker.User;

public class StickerMessagingActivity extends AppCompatActivity {
    private static final String TAG = StickerMessagingActivity.class.getSimpleName(), ROOT = "users";

    private DatabaseReference ref;
    private TextView recentReceivedStickerView;
    private String username;
    private EditText receiverUsername;
    private RadioButton sticker;
    private List<Sticker> receiverList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_messaging);

        TextView usernameView = findViewById(R.id.usernameView);
        username = getIntent().getStringExtra("usernameKey");
        usernameView.setText(username);

        recentReceivedStickerView = findViewById(R.id.recentReceivedSticker);
        receiverUsername = findViewById(R.id.receiverUsername);
        sticker = findViewById(R.id.sticker1);

        ref = FirebaseDatabase.getInstance().getReference();

        // Check if user already exists - avoid override
        ref.child(ROOT).child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    User user = new User(StickerMessagingActivity.this.username);
                    ref.child(ROOT).child(user.username).setValue(user);
                    Log.d(TAG, "Create new user: " + user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "checkUser onCancelled:" + error);
            }
        });

        // Update view
        ref.child(ROOT).child(username).child("receivedStickers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showMostRecent(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "receivedStickers onCancelled:" + error);
                Toast.makeText(getApplicationContext()
                        , "DBError: " + error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void sendSticker(View view) {
        String receiver = receiverUsername.getText().toString();

        if (receiver.matches("") || receiver.equals(this.username)) {
            Toast.makeText(getApplicationContext(),
                            "Receiver can't be null nor be the same as current username",
                            Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Check if receiver exists? Might not be necessary

        // Retrieve existing data first
        ref.child(ROOT).child(receiver).child("receivedStickers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<List<Sticker>> t = new GenericTypeIndicator<List<Sticker>>() {};
                receiverList = snapshot.getValue(t);
                Log.d(TAG, "receivedStickers onDataChange: snapshot = " + Objects.requireNonNull(snapshot.getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "receivedStickers onCancelled:" + error);
                Toast.makeText(getApplicationContext()
                        , "DBError: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        // Add to list
        Sticker tempSticker = new Sticker(checkStickerType(), this.username);
        receiverList.add(tempSticker);

        // Then write
        ref.child(ROOT).child(receiver).child("receivedStickers")
                .setValue(receiverList)
                .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(),
                        "Sticker sent successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext()
                        , "Failed to send sticker!", Toast.LENGTH_SHORT).show());
    }

    // TODO: 2 tasks:
    //  1 - change sticker type to actually sticker instead of String
    // 2 - Change these buttons to spinner?
    private String checkStickerType() {
        if (sticker.isChecked()) {
            return "Type 1";
        }
        return "Type 2";
    }


    private void showMostRecent(DataSnapshot snapshot) {
        if (snapshot != null) {
            GenericTypeIndicator<List<Sticker>> t = new GenericTypeIndicator<List<Sticker>>() {};
            List<Sticker> list = snapshot.getValue(t);
            Log.d(TAG, "receivedStickers onDataChange: snapshot = " + (snapshot.getValue(t)));
            if (list != null) {
                if (list.size() != 0) {
                    String displayValue = String.valueOf(list.get(list.size() -1));
                    recentReceivedStickerView.setText(displayValue);
                }
            }
        }
    }


}
