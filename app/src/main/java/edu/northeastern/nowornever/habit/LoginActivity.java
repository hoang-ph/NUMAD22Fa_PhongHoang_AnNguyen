package edu.northeastern.nowornever.habit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.northeastern.nowornever.R;

public class LoginActivity extends AppCompatActivity {

    private static final String USERNAME_KEY = "usernameKey", SHARED_PREF = "userPref";

    private EditText usernameHabitInput, passwordHabitInput;
    private String username, password;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);

        username = sharedPreferences.getString(USERNAME_KEY, null);
        if (username != null) {
            Intent intent = new Intent(this, HabitActivity.class);
            startActivity(intent);
        }

        usernameHabitInput = findViewById(R.id.usernameHabitInput);
        passwordHabitInput = findViewById(R.id.passwordHabitInput);
    }

    public void startLogIn(View view) {
        username = usernameHabitInput.getText().toString();
        password = passwordHabitInput.getText().toString();
        if (checkBlank()) return;
        // TODO: validate password, need connect to database
        saveDataToSharedPref();
    }

    public void startSignUp(View view) {
        username = usernameHabitInput.getText().toString();
        password = passwordHabitInput.getText().toString();
        if (checkBlank()) return;
        // TODO: check if username exists, need connect to database
        saveDataToSharedPref();
    }

    private void saveDataToSharedPref() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME_KEY, username);
        editor.apply();

        Intent intent = new Intent(this, HabitActivity.class);
        startActivity(intent);
    }

    private boolean checkBlank() {
        if (username.matches("") || password.matches("")) {
            Toast.makeText(getApplicationContext(), "Username/password cannot be blank", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

}