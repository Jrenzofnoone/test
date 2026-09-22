package com.example.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.test.db.AuthViewModel;
import com.example.test.db.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

public class Signin extends AppCompatActivity {
    public TextInputEditText ed_username, ed_password;
    public Button btn_signin;

    public AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ed_username = findViewById(R.id.ed_username);
        ed_password = findViewById(R.id.ed_password);
        btn_signin = findViewById(R.id.btn_signin);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getStatusMessage().observe(this, msg -> {
            if (msg != null) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        authViewModel.getAuthSuccess().observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                String username = ed_username.getText().toString().trim();
                int userId = DatabaseHelper.getInstance(this).getUserId(username);
                SharedPreferences pref = getSharedPreferences("session", MODE_PRIVATE);
                pref.edit().putInt("userId", userId).apply();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_signin.setOnClickListener(v -> {
            String user = ed_username.getText().toString().trim();
            String pass = ed_password.getText().toString().trim();

            authViewModel.register(user, pass);
        });
    }
}