package com.example.quizga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup themeGroup;
    private Button btnLogout, btnBack;
    private SeekBar seekBarTextSize;
    private TextView previewText, textSizeLabel;
    private SharedPreferences prefs;

    private final float[] sizeValues = {14f, 18f, 22f, 26f};
    private final String[] sizeLabels = {"S", "M", "L", "XL"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("quiz_prefs", MODE_PRIVATE);

        // Theme controls
        themeGroup = findViewById(R.id.themeRadioGroup);
        btnLogout = findViewById(R.id.btnLogout);
        btnBack = findViewById(R.id.btnBack);

        // Font size controls
        previewText = findViewById(R.id.textSizePreview);
        textSizeLabel = findViewById(R.id.textSizeLabel);
        seekBarTextSize = findViewById(R.id.seekBarTextSize);

        // Load text size
        float savedSize = prefs.getFloat("text_size", 18f);
        int initialProgress = getSizeIndex(savedSize);
        seekBarTextSize.setProgress(initialProgress);
        previewText.setTextSize(TypedValue.COMPLEX_UNIT_SP, savedSize);
        textSizeLabel.setText("Current: " + sizeLabels[initialProgress]);

        // Handle SeekBar change
        seekBarTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float selectedSize = sizeValues[progress];
                previewText.setTextSize(TypedValue.COMPLEX_UNIT_SP, selectedSize);
                textSizeLabel.setText("Current: " + sizeLabels[progress]);
                prefs.edit().putFloat("text_size", selectedSize).apply();
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(SettingsActivity.this, "Font size updated", Toast.LENGTH_SHORT).show();
            }
        });

        // Theme logic
        int current = AppCompatDelegate.getDefaultNightMode();
        if (current == AppCompatDelegate.MODE_NIGHT_YES) {
            ((RadioButton) findViewById(R.id.radioDark)).setChecked(true);
        } else if (current == AppCompatDelegate.MODE_NIGHT_NO) {
            ((RadioButton) findViewById(R.id.radioLight)).setChecked(true);
        } else {
            ((RadioButton) findViewById(R.id.radioSystem)).setChecked(true);
        }

        themeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioLight) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (checkedId == R.id.radioDark) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
            recreate();
        });

        btnLogout.setOnClickListener(v -> {
            // Clear local preferences
            SharedPreferences prefs = getSharedPreferences("quiz_prefs", MODE_PRIVATE);
            prefs.edit().clear().apply();

            // Sign out from Firebase
            FirebaseAuth.getInstance().signOut();

            // Navigate to login screen
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // 👈 Prevent going back
            startActivity(intent);
            finish();
        });


        btnBack.setOnClickListener(v -> finish());
    }

    private int getSizeIndex(float size) {
        for (int i = 0; i < sizeValues.length; i++) {
            if (Math.abs(size - sizeValues[i]) < 1f) return i;
        }
        return 1; // default to M
    }
}
