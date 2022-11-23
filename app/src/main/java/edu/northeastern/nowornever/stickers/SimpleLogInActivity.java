package edu.northeastern.nowornever.stickers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.northeastern.nowornever.R;

public class SimpleLogInActivity extends AppCompatActivity {

    private static final String USERNAME_KEY = "usernameKey";

    private EditText usernameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_log_in);
        usernameInput = findViewById(R.id.usernameInput);
    }

    public void startStickerMsgAct(View view) {
        String username = usernameInput.getText().toString();
        if (username.matches("")) {
            Toast.makeText(getApplicationContext(), "Username cannot be blank", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, StickerMessagingActivity.class);
            intent.putExtra(USERNAME_KEY, username);
            startActivity(intent);
        }
    }
}