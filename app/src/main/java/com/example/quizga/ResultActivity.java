package com.example.quizga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    private boolean scoreSaved = false;
    private TextView textViewScore;
    private Button buttonRetry, buttonViewScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applyTheme(this); // Apply theme
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_quiz);

        textViewScore = findViewById(R.id.textViewScore);
        buttonRetry = findViewById(R.id.buttonRetry);
        buttonViewScores = findViewById(R.id.buttonViewScores);

        // Optional: Apply font resizing if using FontHelper
        FontHelper.applyFontSize(this, textViewScore);
        FontHelper.applyFontSize(this, buttonRetry);
        FontHelper.applyFontSize(this, buttonViewScores);


        int score = getIntent().getIntExtra("score", 0);
        textViewScore.setText("Your Score: " + score + "/20");

        if (!scoreSaved) {
            saveScore(score);
            updateHighScore(score);
            scoreSaved = true;
        }

        buttonRetry.setOnClickListener(view -> {
            startActivity(new Intent(ResultActivity.this, QuizActivity.class));
            finish();
        });

        buttonViewScores.setOnClickListener(view -> {
            try {
                startActivity(new Intent(ResultActivity.this, ScoreHistoryActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load score history", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveScore(int score) {
        SharedPreferences prefs = getSharedPreferences("quiz_prefs", MODE_PRIVATE);
        String existing = prefs.getString("recent_scores", "");
        List<String> scoreList = new ArrayList<>(Arrays.asList(existing.split(";")));

        // Clean up
        scoreList.removeIf(String::isEmpty);

        // Format timestamp
        String timestamp = new SimpleDateFormat("dd MMM yyyy – h:mm a", Locale.getDefault()).format(new Date());
        String entry = score + "|" + timestamp;

        // Add and trim
        scoreList.add(0, entry);
        if (scoreList.size() > 10) {
            scoreList = scoreList.subList(0, 10);
        }

        // Save
        String updatedScores = TextUtils.join(";", scoreList);
        prefs.edit().putString("recent_scores", updatedScores).apply();
    }

    private void updateHighScore(int currentScore) {
        SharedPreferences prefs = getSharedPreferences("quiz_prefs", MODE_PRIVATE);
        int highScore = prefs.getInt("high_score", 0);

        if (currentScore > highScore) {
            prefs.edit().putInt("high_score", currentScore).apply();
        }
    }
}
