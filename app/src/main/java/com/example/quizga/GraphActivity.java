package com.example.quizga;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
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
        Button btnBack = findViewById(R.id.backbtn);

        if (lineChart == null) {
            Toast.makeText(this, "LineChart not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String raw = prefs.getString(SCORES_KEY, "");

        if (raw == null || raw.isEmpty()) {
            Toast.makeText(this, "No scores to display", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] entries = raw.split(";");
        ArrayList<Entry> dataPoints = new ArrayList<>();

        for (int i = 0; i < entries.length; i++) {
            try {
                String[] parts = entries[i].split("\\|");
                float score = Float.parseFloat(parts[0]);
                dataPoints.add(new Entry(i + 1, score)); // X: Quiz number, Y: Score
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // === Detect current theme ===
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean isDarkMode = nightModeFlags == Configuration.UI_MODE_NIGHT_YES;

        // === Create and style dataset ===
        LineDataSet dataSet = new LineDataSet(dataPoints, "Quiz Scores");
        dataSet.setColor(Color.CYAN);
        dataSet.setValueTextColor(isDarkMode ? Color.WHITE : Color.BLACK);
        dataSet.setCircleColor(Color.MAGENTA);
        dataSet.setLineWidth(2f);

        // === Set data and style chart ===
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.getXAxis().setGranularity(1f);
        lineChart.getXAxis().setLabelCount(dataPoints.size());
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        // === Axis colors based on theme ===
        int axisColor = isDarkMode ? Color.WHITE : Color.BLACK;
        lineChart.getXAxis().setTextColor(axisColor);
        lineChart.getAxisLeft().setTextColor(axisColor);
        lineChart.getLegend().setTextColor(axisColor);
        lineChart.getDescription().setTextColor(axisColor);
        lineChart.getAxisLeft().setAxisMinimum(0);
        lineChart.getAxisLeft().setGranularity(1f);
        lineChart.getAxisRight().setEnabled(false);

        // === Background optional ===
        lineChart.setBackgroundColor(isDarkMode ? Color.parseColor("#1C1C1E") : Color.WHITE);

        // === Enable interactions ===
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.moveViewToX(dataPoints.size());

        lineChart.getDescription().setText("Score vs Quiz Number");
        lineChart.invalidate(); // Refresh chart

        // === Back Button ===
        btnBack.setOnClickListener(v -> finish());
    }
}
