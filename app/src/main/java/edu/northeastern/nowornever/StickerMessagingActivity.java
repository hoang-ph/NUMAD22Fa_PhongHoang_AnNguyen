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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;

import edu.northeastern.nowornever.model.sticker.Sticker;
import edu.northeastern.nowornever.model.sticker.User;

public class StickerMessagingActivity extends AppCompatActivity {
    private static final String TAG = StickerMessagingActivity.class.getSimpleName();

    private FirebaseDatabase database;
    private TextView usernameView, recentReceivedStickerView;
    private String username;
    private EditText receiverUsername;
    private RadioButton sticker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_messaging);

        usernameView = findViewById(R.id.usernameView);
        username = getIntent().getStringExtra("usernameKey");
        usernameView.setText(username);

        recentReceivedStickerView = findViewById(R.id.recentReceivedSticker);
        receiverUsername = findViewById(R.id.receiverUsername);
        sticker = findViewById(R.id.sticker1);

        database = FirebaseDatabase.getInstance();

        database.getReference().child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                showMostRecent(snapshot);
                Log.e(TAG, "onChildAdded: snapshot = " + snapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                showMostRecent(snapshot);
                Log.e(TAG, "onChildChanged: snapshot = " + snapshot.getValue().toString());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled:" + error);
                Toast.makeText(getApplicationContext()
                        , "DBError: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        // TODO: extra logics for existing user and data -> refactor this for received stickers
        User user = new User(this.username, new ArrayList<>());
        database.getReference().child("users").child(user.username).setValue(user);

    }

    public void sendSticker(View view) {
        StickerMessagingActivity.this.onSend(database.getReference());
    }

    private void onSend(DatabaseReference reference) {
        String receiver = receiverUsername.getText().toString();
        if (receiver.matches("") || receiver.equals(this.username)) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            return;
        }
        // TODO: check if user exist in database
        // if (reference.child("user").child(receiver).get)

        reference.child("users").child(receiver).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {

                User user = mutableData.getValue(User.class);

                if (user == null) {
                    return Transaction.success(mutableData);
                }

                Sticker sticker = new Sticker(checkStickerType(), StickerMessagingActivity.this.username);
                user.receivedStickers.add(sticker);

                mutableData.setValue(user);

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean successful,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                if (successful) {
                    Toast.makeText(StickerMessagingActivity.this,
                            "Sticker Sent", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StickerMessagingActivity.this
                            , "DBError: " + databaseError, Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private String checkStickerType() {
        if (sticker.isChecked()) {
            return "Type 1";
        }
        return "Type 2";
    }


    private void showMostRecent(DataSnapshot snapshot) {
        User user = snapshot.getValue(User.class);
        return;
        // TODO: this logic is wrong, refactor as mentioned as above TODO
//        if (snapshot.getKey() != null && !user.receivedStickers.isEmpty()) {
//            recentReceivedStickerView.setText(String.valueOf(user.receivedStickers.get(-1)));
//        }
    }


}
