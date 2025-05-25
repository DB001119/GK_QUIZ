package com.example.quizga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;

    // Hardcoded credentials for demonstration
    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "admin123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applyTheme(this); // Apply light/dark mode
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 🔐 Auto-login if already logged in
        SharedPreferences prefs = getSharedPreferences("quiz_prefs", MODE_PRIVATE);
        if (prefs.getBoolean("isLoggedIn", false)) {
            navigateToMainActivity();
            finish();
            return;
        }

        // 🎯 Bind Views
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // 🔠 Apply font size
        FontHelper.applyFontSize(this, etUsername);
        FontHelper.applyFontSize(this, etPassword);
        FontHelper.applyFontSize(this, btnLogin);

        // 🔓 Login Logic
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
                prefs.edit().putBoolean("isLoggedIn", true).apply();
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                navigateToMainActivity();
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }
}
