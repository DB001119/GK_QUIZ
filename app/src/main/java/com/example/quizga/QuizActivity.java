package com.example.quizga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

public class QuizActivity extends AppCompatActivity {

    private TextView tvQuestion, tvQuestionNumber;
    private Button btnOption1, btnOption2, btnOption3, btnOption4;

    private int currentIndex = 0;
    private int score = 0;

    private final ArrayList<Question> questionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_screen);

        tvQuestion = findViewById(R.id.tvQuestion);
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnOption4 = findViewById(R.id.btnOption4);

        FontHelper.applyFontSize(this, tvQuestion);
        FontHelper.applyFontSize(this, tvQuestionNumber);
        FontHelper.applyFontSize(this, btnOption1);
        FontHelper.applyFontSize(this, btnOption2);
        FontHelper.applyFontSize(this, btnOption3);
        FontHelper.applyFontSize(this, btnOption4);

        loadQuestionsFromJson();
        setQuestion();

        btnOption1.setOnClickListener(view -> handleAnswer(btnOption1.getText().toString()));
        btnOption2.setOnClickListener(view -> handleAnswer(btnOption2.getText().toString()));
        btnOption3.setOnClickListener(view -> handleAnswer(btnOption3.getText().toString()));
        btnOption4.setOnClickListener(view -> handleAnswer(btnOption4.getText().toString()));
    }

    private void loadQuestionsFromJson() {
        try {
            String category = getIntent().getStringExtra("category");
            InputStream is = getAssets().open(category +"_quiz.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String jsonStr = new String(buffer, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(jsonStr);
            ArrayList<Question> allQuestions = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String questionText = obj.getString("question");
                JSONArray opts = obj.getJSONArray("options");
                String[] options = new String[opts.length()];
                for (int j = 0; j < opts.length(); j++) {
                    options[j] = opts.getString(j);
                }
                String answer = obj.getString("answer");
                allQuestions.add(new Question(questionText, options, answer));
            }

            // Shuffle and pick 20
            Collections.shuffle(allQuestions);
            questionList.clear();
            questionList.addAll(allQuestions.subList(0, Math.min(20, allQuestions.size())));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load questions", Toast.LENGTH_SHORT).show();
        }
    }

    private void setQuestion() {
        if (currentIndex < questionList.size()) {
            Question q = questionList.get(currentIndex);
            tvQuestionNumber.setText("Question " + (currentIndex + 1) + " of 20");
            tvQuestion.setText(q.getQuestion());
            btnOption1.setText(q.getOptions()[0]);
            btnOption2.setText(q.getOptions()[1]);
            btnOption3.setText(q.getOptions()[2]);
            btnOption4.setText(q.getOptions()[3]);
        }
    }

    private void handleAnswer(String selectedAnswer) {
        Question currentQuestion = questionList.get(currentIndex);

        if (selectedAnswer.equals(currentQuestion.getAnswer())) {
            score++;
            currentIndex++;
            if (currentIndex < questionList.size()) {
                setQuestion();
            } else {
                goToResult();
            }
        } else {
            goToResult();
        }
    }

    private void goToResult() {
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra("score", score);
        startActivity(intent);
        finish();
    }

    // Model class for questions
    public static class Question {
        private final String question;
        private final String[] options;
        private final String answer;

        public Question(String question, String[] options, String answer) {
            this.question = question;
            this.options = options;
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public String[] getOptions() {
            return options;
        }

        public String getAnswer() {
            return answer;
        }
    }
}
