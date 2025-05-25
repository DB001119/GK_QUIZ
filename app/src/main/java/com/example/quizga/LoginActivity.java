package com.example.quizga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 🔐 Init Firebase Auth
        mAuth = FirebaseAuth.getInstance();

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
            String email = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            SharedPreferences prefs = getSharedPreferences("quiz_prefs", MODE_PRIVATE);
                            prefs.edit().putBoolean("isLoggedIn", true).apply();

                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                            navigateToMainActivity();
                            finish();
                        } else {
                            Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        Button btnToRegister = findViewById(R.id.btnToRegister);
        FontHelper.applyFontSize(this, btnToRegister);

        btnToRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            navigateToMainActivity();
            finish();
        }
    }


    private void navigateToMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }



}
