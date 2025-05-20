package com.example.quizga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ScoreHistoryActivity extends AppCompatActivity {

    private static final String PREFS = "quiz_prefs";
    private static final String SCORES_KEY = "recent_scores";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_history);

        TextView tvScoreList = findViewById(R.id.tvScoreList);
        Button btnRetryQuiz = findViewById(R.id.btnRetryQuiz);
        Button btnBackToMenu = findViewById(R.id.btnBackToMenu);

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String scoresRaw = prefs.getString(SCORES_KEY, "");

        Log.d("SCORE_HISTORY", "Loaded raw scores: " + scoresRaw);

        if (scoresRaw == null || scoresRaw.isEmpty()) {
            tvScoreList.setText("No scores recorded yet.");
        } else {
            String[] scores = scoresRaw.split(",");
            StringBuilder display = new StringBuilder();

            for (int i = 0; i < scores.length; i++) {
                display.append("🏆 Game ").append(scores.length - i)
                        .append(": ").append(scores[i]).append("/20\n");
            }

            tvScoreList.setText(display.toString().trim());
        }

        btnRetryQuiz.setOnClickListener(v -> {
            startActivity(new Intent(this, QuizActivity.class));
            finish();
        });

        btnBackToMenu.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}
