package fhtw.bsa2.gafert_steiner.ue5_diaryapp.chart;

import android.icu.text.SimpleDateFormat;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.Date;

import fhtw.bsa2.gafert_steiner.ue5_diaryapp.emotion.EmotionEntry;


public class DateFormatter implements IAxisValueFormatter {

    private ArrayList<EmotionEntry> emotionEnries;

    public DateFormatter() {
        emotionEnries = new ArrayList<>();
    }

    public DateFormatter(ArrayList<EmotionEntry> emotionEnries) {
        this.emotionEnries = emotionEnries;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        String newDate = ""; // Set default

        if (!emotionEnries.isEmpty()) {
            try {
                EmotionEntry emotionEntry = emotionEnries.get((int) value);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E d. MMM");
                Date date = emotionEntry.getEntryDate();
                newDate = simpleDateFormat.format(date);                                           // Set the date to the text
            } catch (Exception e) {
                //Log.e("AddFragment", "onDateSet: Could not parse to date string");
            }
        }

        return newDate;
    }
}
