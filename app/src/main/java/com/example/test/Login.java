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

public class Login extends AppCompatActivity {
    public TextInputEditText ed_username, ed_password;
    public Button btn_login, btn_signin;
    public AuthViewModel authViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ed_username = findViewById(R.id.ed_username);
        ed_password = findViewById(R.id.ed_password);
        btn_login = findViewById(R.id.btn_login);
        btn_signin = findViewById(R.id.btn_signin);


        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.getStatusMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
        authViewModel.getAuthSuccess().observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                String user = ed_username.getText().toString().trim();
                int user_id = DatabaseHelper.getInstance(this).getUserId(user);
                SharedPreferences pref = getSharedPreferences("session", MODE_PRIVATE);
                pref.edit().putInt("userId", user_id).apply();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btn_login.setOnClickListener(v -> {
            String user = ed_username.getText().toString().trim();
            String pass = ed_password.getText().toString().trim();
            authViewModel.login(user, pass);
        });
        btn_signin.setOnClickListener(v -> {
            Intent i = new Intent(this, Signin.class);
            startActivity(i);
        });
    }
}