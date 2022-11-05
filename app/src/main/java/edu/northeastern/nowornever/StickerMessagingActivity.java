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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.nowornever.model.sticker.Sticker;
import edu.northeastern.nowornever.model.sticker.User;

public class StickerMessagingActivity extends AppCompatActivity {
    private static final String TAG = StickerMessagingActivity.class.getSimpleName();

    private DatabaseReference ref;
    private TextView usernameView, recentReceivedStickerView;
    private String username;
    private EditText receiverUsername;
    private RadioButton sticker;
    private List<Sticker> tempList;

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

        ref = FirebaseDatabase.getInstance().getReference();

        ref.child("users").addChildEventListener(new ChildEventListener() {
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
        User user = new User(this.username);
        ref.child("users").child(user.username).setValue(user);
    }

    public void sendSticker(View view) {
        String receiver = receiverUsername.getText().toString();
        if (receiver.matches("") || receiver.equals(this.username)) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            return;
        }

//        // Read first
//        ref.child("users").child(receiver).child("receivedStickers").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.e(TAG, "Error getting data", task.getException());
//                    tempList = new ArrayList<>();
//                } else {
//                    Log.d(TAG, String.valueOf(task.getResult().getValue()));
//                    tempList = (List<Sticker>) task.getResult().getValue();
//                }
//            }
//        });
//
        // Add to list
        Sticker tempSticker = new Sticker(checkStickerType(), StickerMessagingActivity.this.username);
        tempList = new ArrayList<>();
        tempList.add(tempSticker);

        // Then write
        ref.child("users").child(receiver).child("receivedStickers").setValue(tempList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext()
                                , "Sticker sent successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext()
                                , "Failed to send sticker!", Toast.LENGTH_SHORT).show();
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
//         TODO: this logic is wrong, refactor as mentioned as above TODO
//        if (snapshot.getKey() != null && !user.receivedStickers.isEmpty()) {
//            recentReceivedStickerView.setText(String.valueOf(user.receivedStickers.get(-1)));
        //}
    }


}
