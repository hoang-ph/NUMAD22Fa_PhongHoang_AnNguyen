package edu.northeastern.nowornever;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import edu.northeastern.nowornever.habit.LoginActivity;
import edu.northeastern.nowornever.stickers.SimpleLogInActivity;
import edu.northeastern.nowornever.webservice.WebServiceActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showWebServiceActivity(View view) {
        startActivity(new Intent(this, WebServiceActivity.class));
    }

    public void showStickerMessagingActivity(View view) {
        startActivity(new Intent(this, SimpleLogInActivity.class));
    }

    public void showHabitCentralPage(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}