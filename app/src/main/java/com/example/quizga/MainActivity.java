package com.example.quizga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    Button btnStartQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartQuiz = findViewById(R.id.btnStartQuiz);
        TextView tvHighScore = findViewById(R.id.tvHighScore); // ✅ now it's legal here

        btnStartQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            startActivity(intent);
        });

        Button toggleTheme = findViewById(R.id.btnToggleTheme);
        toggleTheme.setOnClickListener(v -> {
            int current = AppCompatDelegate.getDefaultNightMode();
            if (current == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            recreate(); // refresh activity
        });

        // Load high score
        SharedPreferences prefs = getSharedPreferences("quiz_prefs", MODE_PRIVATE);
        int highScore = prefs.getInt("high_score", 0);
        tvHighScore.setText("🏆 High Score: " + highScore + "/20");
    }
}
