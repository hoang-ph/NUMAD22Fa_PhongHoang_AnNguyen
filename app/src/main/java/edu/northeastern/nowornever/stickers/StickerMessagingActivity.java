package edu.northeastern.nowornever.stickers;

import static edu.northeastern.nowornever.utils.Constants.ROOT;
import static edu.northeastern.nowornever.utils.Constants.STICKER_RESOURCES;
import static edu.northeastern.nowornever.utils.Constants.STICKER_STORAGE;
import static edu.northeastern.nowornever.utils.Constants.USERNAME_KEY;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.nowornever.R;
import pl.droidsonroids.gif.GifImageView;

public class StickerMessagingActivity extends AppCompatActivity {

    private static final String TAG = StickerMessagingActivity.class.getSimpleName();
    private final int NOTIFICATION_STICKER_CODE = 7;

    private DatabaseReference ref;
    private TextView recentReceivedStickerView;
    private String username;
    private EditText receiverUsername;
    private String selectedStickerType;
    private GifImageView stickerImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Notification channel
        createNotificationChannel();
        setContentView(R.layout.activity_sticker_messaging);

        stickerImageView = findViewById(R.id.stickerImgView);
        TextView usernameView = findViewById(R.id.usernameView);
        username = getIntent().getStringExtra(USERNAME_KEY);
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
        ref.child(ROOT).child(username).child(STICKER_STORAGE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showMostRecent(snapshot);
                // Send notification
                sendNotification(username);
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

        if (receiver.matches("")) {
            Toast.makeText(getApplicationContext(), "Receiver can't be blank", Toast.LENGTH_SHORT).show();
            return;
        } else if (receiver.equals(this.username)) {
            Toast.makeText(getApplicationContext(), "Receiver can't be the same as current username", Toast.LENGTH_SHORT).show();
            return;
        } else if (selectedStickerType == null) {
            Toast.makeText(getApplicationContext(), "A sticker must be selected", Toast.LENGTH_SHORT).show();
            return;
        }

        Sticker newSticker = new Sticker(selectedStickerType, this.username);
        ref.child(ROOT).child(receiver).child(STICKER_STORAGE).push().setValue(newSticker)
                .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(),
                        "Sticker sent successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext()
                        , "Failed to send sticker!", Toast.LENGTH_SHORT).show());
    }

    public void showHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        intent.putExtra(USERNAME_KEY, username);
        startActivity(intent);
    }

    private void showMostRecent(DataSnapshot snapshot) {
        if (snapshot != null) {
            List<Sticker> list = new ArrayList<>();
            for (DataSnapshot ds : snapshot.getChildren()) {
                Sticker sticker = ds.getValue(Sticker.class);
                list.add(sticker);
            }
            if (list.size() != 0) {
                Sticker latestSticker = list.get(list.size() -1);
                recentReceivedStickerView.setText(latestSticker.getStickerInfo());
                stickerImageView.setImageResource(STICKER_RESOURCES.get(latestSticker.type));
            }
        }
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Sticker Messaging Notification";
            String description = getString(R.string.description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("StickyMessagingChannel", name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendNotification(String username) {
        PendingIntent sendMessage = PendingIntent.getActivity(this, (int)System.currentTimeMillis(), new Intent(this, StickerMessagingActivity.class), PendingIntent.FLAG_IMMUTABLE);
        String channelID = getString(R.string.channel_id);
        Notification notification = new NotificationCompat.Builder(this, channelID)
                .setContentTitle(username)
                .setSmallIcon(R.drawable.text_icon)
                .setContentText("hidden")
                .setTicker("Ticker text")
                .addAction(R.drawable.text_icon, "Message", sendMessage)
                .setContentIntent(sendMessage).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(NOTIFICATION_STICKER_CODE, notification);
    }
}
