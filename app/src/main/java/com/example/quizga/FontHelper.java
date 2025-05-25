package com.example.quizga;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.widget.TextView;

public class FontHelper {
    public static void applyFontSize(Context context, TextView view) {
        SharedPreferences prefs = context.getSharedPreferences("quiz_prefs", Context.MODE_PRIVATE);
        float size = prefs.getFloat("text_size", 18f); // default size
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }
}

