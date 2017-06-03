package fhtw.bsa2.gafert_steiner.ue5_diaryapp;


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.db.chart.model.BarSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.view.BarChartView;
import com.github.lzyzsd.circleprogress.ArcProgress;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {

    public final static int FEELING_VERY_HAPPY = 20;
    public final static int FEELING_HAPPY = 10;
    public final static int FEELING_NORMAL = 1;
    public final static int FEELING_SAD = -10;
    public final static int FEELING_VERY_SAD = -20;


    LinearLayout firstPage;
    ScrollView scrollView;
    ListView latestEntriesListView;
    ArrayAdapter<String> latestEntriesAdapter;  // Listenelemente hinzufügen
    BarSet barGraphSet; // Add here elements to chart
    BarChartView barGraphView; // Update the chart

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);

        firstPage = (LinearLayout) rootView.findViewById(R.id.firstPage);
        scrollView = (ScrollView) rootView.findViewById(R.id.scrollView);
        latestEntriesListView = (ListView)rootView.findViewById(R.id.LatestEntries);

        setupGraph(rootView);
        setupCircles(rootView);

        // Add things to a list
        latestEntriesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_2, android.R.id.text1);
        latestEntriesListView.setAdapter(latestEntriesAdapter);

        latestEntriesAdapter.add("You were sad");
        latestEntriesAdapter.add("You were happy");
        latestEntriesAdapter.add("You were very sad");
        latestEntriesAdapter.add("You were the sadness");

        // Button to move to the list
        ImageButton latestEntriesButton = (ImageButton)rootView.findViewById(R.id.latestEntriesButton);
        latestEntriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.smoothScrollTo(0, scrollView.getBottom());
            }
        });

        FloatingActionButton backButton = (FloatingActionButton)rootView.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.smoothScrollTo(0, scrollView.getTop());
            }
        });

        return rootView;
    }

    private void setupGraph(View rootView){
        barGraphView = (BarChartView) rootView.findViewById(R.id.hapinessChart);

        barGraphSet = new BarSet();
        // Happiness -30 - 30 = 6 Stimmungen
        barGraphSet.addBar("Monday", FEELING_HAPPY);
        barGraphSet.addBar("Tuesday", FEELING_NORMAL);
        barGraphSet.addBar("Wednesday", FEELING_VERY_HAPPY);
        barGraphSet.addBar("Thursday", FEELING_SAD);
        barGraphSet.addBar("Friday", FEELING_NORMAL);
        barGraphSet.addBar("Saturday", FEELING_VERY_SAD);
        barGraphSet.addBar("Sunday", FEELING_VERY_HAPPY);

        int[] colors = new int[2];

        colors[0] = ContextCompat.getColor(getContext(), R.color.graphcolor1);
        colors[1] = ContextCompat.getColor(getContext(), R.color.graphcolor2);

        barGraphSet.setGradientColor(colors, new float[]{FEELING_VERY_SAD, FEELING_VERY_HAPPY});

        barGraphView.setYAxis(false);
        barGraphView.setXAxis(false);

        // Make a grid
        Paint mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.darkgrey));
        mPaint.setStrokeWidth(5);
        barGraphView.setGrid(6, 0, mPaint);

        barGraphView.setAxisBorderValues(FEELING_VERY_SAD - 10, FEELING_VERY_HAPPY + 10);
        barGraphView.setYLabels(AxisRenderer.LabelPosition.NONE);
        barGraphView.setXLabels(AxisRenderer.LabelPosition.INSIDE);
        barGraphView.setLabelsColor(Color.WHITE);
        barGraphView.setFontSize(20);

        //barGraphView.setAxisLabelsSpacing(50);
        //barGraphView.setAxisColor(ContextCompat.getColor(getContext(), R.color.grey));
        //barGraphView.setAxisThickness(1);

        barGraphView.addData(barGraphSet);
        barGraphView.show();

    }

    private void setupCircles(View rootView){
        ArcProgress donutProgress1 = (ArcProgress) rootView.findViewById(R.id.donutProgress1);
        ArcProgress donutProgress2 = (ArcProgress) rootView.findViewById(R.id.donutProgress2);
        ArcProgress donutProgress3 = (ArcProgress) rootView.findViewById(R.id.donutProgress3);

        donutProgress1.setProgress(30);
        donutProgress2.setProgress(70);
        donutProgress3.setProgress(50);
    }
}



