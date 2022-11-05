package edu.northeastern.nowornever;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.Map;
import java.util.Objects;

import edu.northeastern.nowornever.model.sticker.Sticker;
import edu.northeastern.nowornever.model.sticker.User;

public class StickerMessagingActivity extends AppCompatActivity {
    private static final String TAG = StickerMessagingActivity.class.getSimpleName(), ROOT = "users";

    // Reference here: https://developer.android.com/reference/android/R.drawable.html
    private static final Map<String, Integer> STICKER_SOURCES = Map.of(
            "Star", 17301516,
            "Microphone", 17301681,
            "Music", 17301635,
            "Phone",17301638,
            "Bluetooth", 17301632
    );

    private DatabaseReference ref;
    private TextView recentReceivedStickerView;
    private String username;
    private EditText receiverUsername;
    private String selectedStickerType;
    private ImageView stickerImageView;
    private List<Sticker> receiverList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_messaging);

        stickerImageView = findViewById(R.id.stickerImgView);
        TextView usernameView = findViewById(R.id.usernameView);
        username = getIntent().getStringExtra("usernameKey");
        usernameView.setText(username);

        // Sticker type Spinner
        Spinner typeSpinner = findViewById(R.id.stickerTypeSpinner);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,R.array.sticker_type_array, android.R.layout.simple_spinner_dropdown_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedStickerType = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "type onItemSelected: " + selectedStickerType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        recentReceivedStickerView = findViewById(R.id.recentReceivedSticker);
        receiverUsername = findViewById(R.id.receiverUsername);

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

    public void getAllUsers(View view) {
        final Dialog dialog = new Dialog(StickerMessagingActivity.this);
        dialog.setContentView(R.layout.about_popup);
        TextView allUserViews = dialog.findViewById(R.id.allUsersView);
        ref.child(ROOT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> allUserList = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name = (String) ds.child("username").getValue();
                    allUserList.add(name);
                }
                allUserViews.setText(allUserList.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        Button backToMainScreen = dialog.findViewById(R.id.doneBtn);
        backToMainScreen.setOnClickListener(view1 -> dialog.dismiss());
        dialog.show();
    }

    public void sendSticker(View view) {
        String receiver = receiverUsername.getText().toString();

        if (receiver.matches("") || receiver.equals(this.username)) {
            Toast.makeText(getApplicationContext(),
                            "Receiver can't be null nor be the same as current username",
                            Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedStickerType == null) {
            Toast.makeText(getApplicationContext(), "A sticker must be selected",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Check if receiver exists? Might not be necessary

        // Retrieve existing data first
        ref.child(ROOT).child(receiver).child("receivedStickers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<List<Sticker>> t = new GenericTypeIndicator<>() {};
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
        Sticker tempSticker = new Sticker(selectedStickerType, this.username);
        receiverList.add(tempSticker);

        // Then write
        ref.child(ROOT).child(receiver).child("receivedStickers")
                .setValue(receiverList)
                .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(),
                        "Sticker sent successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext()
                        , "Failed to send sticker!", Toast.LENGTH_SHORT).show());
    }


    private void showMostRecent(DataSnapshot snapshot) {
        if (snapshot != null) {
            GenericTypeIndicator<List<Sticker>> t = new GenericTypeIndicator<>() {};
            List<Sticker> list = snapshot.getValue(t);
            Log.d(TAG, "receivedStickers onDataChange: snapshot = " + (snapshot.getValue(t)));
            if (list != null) {
                if (list.size() != 0) {
                    Sticker tempSticker = list.get(list.size() -1);
                    recentReceivedStickerView.setText(tempSticker.getStickerInfo());
                    stickerImageView.setImageResource(STICKER_SOURCES.get(tempSticker.type));
                }
            }
        }
    }


}
