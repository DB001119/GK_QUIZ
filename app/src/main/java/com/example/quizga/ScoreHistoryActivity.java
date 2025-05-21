package com.example.quizga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ScoreHistoryActivity extends AppCompatActivity {

    private static final String PREFS = "quiz_prefs";
    private static final String SCORES_KEY = "recent_scores";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_history);

        TextView tvScoreList = findViewById(R.id.tvScoreList);
        Button btnRetryQuiz = findViewById(R.id.btnRetryQuiz);
        Button btnBackToMenu = findViewById(R.id.btnBackToMenu);
        Button btnGraphView = findViewById(R.id.btnGraphView);
        Button btnClearHistory = findViewById(R.id.btnClearHistory);

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String scoresRaw = prefs.getString(SCORES_KEY, "");

        if (scoresRaw == null || scoresRaw.isEmpty()) {
            tvScoreList.setText("No scores recorded yet.");
        } else {
            String[] entries = scoresRaw.split(";");
            SpannableStringBuilder styledDisplay = new SpannableStringBuilder();

            for (int i = 0; i < entries.length; i++) {
                String[] parts = entries[i].split("\\|");
                String score = parts[0];
                String time = parts.length > 1 ? parts[1] : "Unknown time";

                String line1 = "🏆 Quiz " + (entries.length - i) + ": " + score + "/20\n";
                String line2 = "📅 Played on: " + time + "\n\n";

                styledDisplay.append(line1);  // Normal size score line

                SpannableString timestampSpan = new SpannableString(line2);
                timestampSpan.setSpan(new RelativeSizeSpan(0.60f), 0, line2.length(), 0); // Small timestamp
                styledDisplay.append(timestampSpan);
            }

            tvScoreList.setText(styledDisplay);
        }

        btnRetryQuiz.setOnClickListener(v -> {
            startActivity(new Intent(this, QuizActivity.class));
            finish();
        });

        btnBackToMenu.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        btnGraphView.setOnClickListener(v -> {
            startActivity(new Intent(this, GraphActivity.class));
        });

        btnClearHistory.setOnClickListener(v -> {
            SharedPreferences prefs1 = getSharedPreferences(PREFS, MODE_PRIVATE);
            prefs1.edit().remove(SCORES_KEY).apply();
            tvScoreList.setText("Scores Cleared!!");
            Toast.makeText(this, "Score history cleared!", Toast.LENGTH_SHORT).show();
        });
    }
}
