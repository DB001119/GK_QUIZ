package com.example.quizga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CategoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        int[] buttonIds = {
                R.id.btnScience, R.id.btnHistory, R.id.btnTech, R.id.btnSports,
                R.id.btnMusic, R.id.btnGeography, R.id.btnMovies, R.id.btnFitness
        };
        String[] categories = {
                "science", "history", "tech", "sports",
                "music", "geography", "movies", "fitness"
        };

        for (int i = 0; i < buttonIds.length; i++) {
            Button button = findViewById(buttonIds[i]);
            String category = categories[i];
            FontHelper.applyFontSize(this, button);

            button.setOnClickListener(v -> {
                Intent intent = new Intent(CategoryActivity.this, QuizActivity.class);
                intent.putExtra("category", category);
                startActivity(intent);
            });
        }
    }
}

