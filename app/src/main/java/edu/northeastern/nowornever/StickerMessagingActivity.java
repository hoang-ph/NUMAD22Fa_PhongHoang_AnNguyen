package edu.northeastern.nowornever;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StickerMessagingActivity extends AppCompatActivity {

    private TextView usernameView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_messaging);

        usernameView = findViewById(R.id.usernameView);
        String username = getIntent().getStringExtra("usernameKey");
        usernameView.setText(username);
    }
}
