package fhtw.bsa2.gafert_steiner.ue5_diaryapp.chart;

import android.content.Context;

import com.github.mikephil.charting.components.MarkerView;

import fhtw.bsa2.gafert_steiner.ue5_diaryapp.R;

/**
 * Created by michi on 03.06.17.
 */

public class ChartMarker extends MarkerView {
    /**
     * Sets up the Hightlight Marker in the chart with a custom layout resource.
     */
    public ChartMarker(Context context) {
        super(context, R.layout.layout_marker);
    }
}
