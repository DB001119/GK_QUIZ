package com.example.quizga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button btnStartQuiz, btnSettings;
    private TextView tvHighScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainCheck", "MainActivity launched");
        Toast.makeText(this, "MainActivity Opened", Toast.LENGTH_SHORT).show();

        ThemeHelper.applyTheme(this); // Apply current theme
        super.onCreate(savedInstanceState);

        // 🔐 Check login
        SharedPreferences prefs = getSharedPreferences("quiz_prefs", MODE_PRIVATE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // 🎯 Bind Views
        btnStartQuiz = findViewById(R.id.btnStartQuiz);
        btnSettings = findViewById(R.id.btnSettings);
        tvHighScore = findViewById(R.id.tvHighScore);

        // 📈 Show High Score
        int highScore = prefs.getInt("high_score", 0);
        tvHighScore.setText("🏆 High Score: " + highScore + "/20");

        // 🎮 Start Quiz
        btnStartQuiz.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CategoryActivity.class));
        });

        // ⚙️ Open Settings
        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 🔠 Reapply font sizes when returning from SettingsActivity
        FontHelper.applyFontSize(this, tvHighScore);
        FontHelper.applyFontSize(this, btnStartQuiz);
        FontHelper.applyFontSize(this, btnSettings);
    }
}
