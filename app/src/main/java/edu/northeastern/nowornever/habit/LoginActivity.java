package edu.northeastern.nowornever.habit;

import static edu.northeastern.nowornever.utils.Constants.BLANK_NOTI;
import static edu.northeastern.nowornever.utils.Constants.INVALID_CREDENTIAL;
import static edu.northeastern.nowornever.utils.Constants.RETRIEVAL_FAILURE;
import static edu.northeastern.nowornever.utils.Constants.ROOT_HABIT;
import static edu.northeastern.nowornever.utils.Constants.SHARED_PREF;
import static edu.northeastern.nowornever.utils.Constants.SUCCESS_LOGIN;
import static edu.northeastern.nowornever.utils.Constants.SUCCESS_SIGNUP;
import static edu.northeastern.nowornever.utils.Constants.USERNAME_KEY;
import static edu.northeastern.nowornever.utils.Constants.USER_EXISTS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.nowornever.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText usernameHabitInput, passwordHabitInput;
    private String username, password;
    private SharedPreferences sharedPreferences;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);

        username = sharedPreferences.getString(USERNAME_KEY, null);
        if (username != null) {
            Intent intent = new Intent(this, HabitsListActivity.class);
            startActivity(intent);
        }

        usernameHabitInput = findViewById(R.id.usernameHabitInput);
        passwordHabitInput = findViewById(R.id.passwordHabitInput);

        ref = FirebaseDatabase.getInstance().getReference().child(ROOT_HABIT);
    }

    public void startLogIn(View view) {
        this.username = usernameHabitInput.getText().toString();
        this.password = passwordHabitInput.getText().toString();
        if (checkBlank()) return;

        // Password validation
        ref.child(this.username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    DataSnapshot ds = task.getResult();
                    String storedPassword = String.valueOf(ds.child("password").getValue());
                    if (storedPassword.equals(LoginActivity.this.password)) {
                        Toast.makeText(getApplicationContext(), SUCCESS_LOGIN, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "User logged in: " + username);
                        saveDataToSharedPref();
                    } else {
                        Toast.makeText(getApplicationContext(), INVALID_CREDENTIAL, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), INVALID_CREDENTIAL, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), RETRIEVAL_FAILURE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void startSignUp(View view) {
        this.username = usernameHabitInput.getText().toString();
        this.password = passwordHabitInput.getText().toString();
        if (checkBlank()) return;

        // Check if user already exists and sign up new user
        ref.child(this.username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    HabitUser habitUser = new HabitUser(LoginActivity.this.username, LoginActivity.this.password);
                    ref.child(habitUser.getUsername()).setValue(habitUser).addOnCompleteListener(task -> {
                        Toast.makeText(getApplicationContext(), SUCCESS_SIGNUP, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "New user credential created: " + habitUser);
                        saveDataToSharedPref();
                    });
                } else {
                    Toast.makeText(getApplicationContext(), USER_EXISTS, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "signupUser onCancelled: " + error);
            }
        });

    }

    private void saveDataToSharedPref() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME_KEY, this.username);
        editor.apply();

        Intent intent = new Intent(this, HabitsListActivity.class);
        startActivity(intent);
    }

    private boolean checkBlank() {
        if (this.username.matches("") || this.password.matches("")) {
            Toast.makeText(getApplicationContext(), BLANK_NOTI, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

}