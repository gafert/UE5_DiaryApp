package fhtw.bsa2.gafert_steiner.ue5_diaryapp.chart;

import android.icu.text.SimpleDateFormat;
import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.SAVE_DATE_FORMAT;

/**
 * Created by michi on 08.06.17.
 */

public class DateFormatter implements IAxisValueFormatter {

    private ArrayList arrayList;

    public DateFormatter() {
        arrayList = new ArrayList();
    }

    public DateFormatter(ArrayList<?> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        String newDate = String.valueOf(value); // Set default

        if (!arrayList.isEmpty()) {
            try {
                String savedDate = String.valueOf(arrayList.get((int) value));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SAVE_DATE_FORMAT);               // Make a new Date with this format
                Date date = simpleDateFormat.parse(savedDate);                         // Make the date Object to save the date
                simpleDateFormat = new SimpleDateFormat("d. MMM yyyy");                            // Reformat the date
                newDate = simpleDateFormat.format(date);                                           // Set the date to the text
            } catch (ParseException e) {
                Log.e("AddFragment", "onDateSet: Could not parse to date string");
            }
        }

        return newDate;
    }
}
