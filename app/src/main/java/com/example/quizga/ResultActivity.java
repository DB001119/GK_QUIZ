package com.example.quizga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private TextView textViewScore;
    private Button buttonRetry, buttonViewScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_quiz);

        textViewScore = findViewById(R.id.textViewScore);
        buttonRetry = findViewById(R.id.buttonRetry);
        buttonViewScores = findViewById(R.id.buttonViewScores);

        int score = getIntent().getIntExtra("score", 0);
        textViewScore.setText("Your Score: " + score + "/20");
        saveScore(score); // <-- Important!

        buttonViewScores.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(ResultActivity.this, ScoreHistoryActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load score history", Toast.LENGTH_SHORT).show();
            }
        });


        buttonViewScores.setOnClickListener(view -> {
            Intent intent = new Intent(ResultActivity.this, ScoreHistoryActivity.class);
            startActivity(intent);
        });
    }
    private void saveScore(int score) {
        SharedPreferences prefs = getSharedPreferences("quiz_prefs", MODE_PRIVATE);
        String existing = prefs.getString("recent_scores", "");

        List<String> scoreList = new ArrayList<>(Arrays.asList(existing.split(",")));

        // Remove any empty values
        scoreList.removeIf(String::isEmpty);

        // Add latest score to top
        scoreList.add(0, String.valueOf(score));

        // Limit to last 10 scores only
        if (scoreList.size() > 10) {
            scoreList = scoreList.subList(0, 10);
        }

        String updatedScores = TextUtils.join(",", scoreList); // <- fix here

        prefs.edit().putString("recent_scores", updatedScores).apply();
    }

}
