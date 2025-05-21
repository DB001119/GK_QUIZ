package com.example.quizga;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {

    private static final String PREFS = "quiz_prefs";
    private static final String SCORES_KEY = "recent_scores";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        LineChart lineChart = findViewById(R.id.lineChart);
        if (lineChart == null) {
            Toast.makeText(this, "LineChart not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String raw = prefs.getString(SCORES_KEY, "");
        String[] entries = raw.split(";");

        ArrayList<Entry> dataPoints = new ArrayList<>();

        for (int i = 0; i < entries.length; i++) {
            String[] parts = entries[i].split("\\|");
            float score = 0f;
            try {
                score = Float.parseFloat(parts[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            dataPoints.add(new Entry(entries.length - i, score)); // Game 10 to 1
        }

        LineDataSet dataSet = new LineDataSet(dataPoints, "Quiz Scores");
        dataSet.setColor(Color.CYAN);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setCircleColor(Color.MAGENTA);
        dataSet.setLineWidth(2f);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getDescription().setText("Score vs Quiz Number");

        lineChart.invalidate();
    }
}
