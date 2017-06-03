package fhtw.bsa2.gafert_steiner.ue5_diaryapp;


import android.graphics.Color;
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

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

import fhtw.bsa2.gafert_steiner.ue5_diaryapp.chart.ChartMarker;
import fhtw.bsa2.gafert_steiner.ue5_diaryapp.chart.DayAxisValueFormatter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {

    public final static int FEELING_VERY_HAPPY = 20;
    public final static int FEELING_HAPPY = 10;
    public final static int FEELING_NORMAL = 0;
    public final static int FEELING_SAD = -10;
    public final static int FEELING_VERY_SAD = -20;


    LinearLayout firstPage;
    ScrollView scrollView;
    ListView latestEntriesListView;
    ArrayAdapter<String> latestEntriesAdapter;  // Listenelemente hinzufügen


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

        int[] colors = new int[2];

        colors[0] = ContextCompat.getColor(getContext(), R.color.colorAccent);
        colors[1] = ContextCompat.getColor(getContext(), R.color.colorAccent2);

        LineChart chart = (LineChart) rootView.findViewById(R.id.chart);

        List<Entry> happinessEntries = new ArrayList<>();
        List<String> timeEntries = new ArrayList<>();

        happinessEntries.add(new Entry(1, FEELING_NORMAL));
        happinessEntries.add(new Entry(2, FEELING_SAD));
        happinessEntries.add(new Entry(3, FEELING_NORMAL));
        happinessEntries.add(new Entry(4, FEELING_VERY_SAD));
        happinessEntries.add(new Entry(5, FEELING_SAD));
        happinessEntries.add(new Entry(6, FEELING_HAPPY));
        happinessEntries.add(new Entry(7, FEELING_VERY_HAPPY));
        happinessEntries.add(new Entry(8, FEELING_NORMAL));
        happinessEntries.add(new Entry(9, FEELING_SAD));
        happinessEntries.add(new Entry(10, FEELING_HAPPY));

        timeEntries.add("Monday");
        timeEntries.add("Tuesday");
        timeEntries.add("Wednesday");
        timeEntries.add("Thursday");
        timeEntries.add("Friday");
        timeEntries.add("Saturday");
        timeEntries.add("Sunday");
        timeEntries.add("Monday");
        timeEntries.add("Tuesday");
        timeEntries.add("Wednesday");

        LineDataSet happinessDateSet = new LineDataSet(happinessEntries, "Happiness");
        happinessDateSet.setColor(colors[0]);
        happinessDateSet.setCircleColor(colors[1]);
        happinessDateSet.setCircleColorHole(Color.TRANSPARENT);
        happinessDateSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        happinessDateSet.setHighlightEnabled(true); // allow highlighting for DataSet
        happinessDateSet.setDrawHighlightIndicators(false);

        List<ILineDataSet> dataSet = new ArrayList<ILineDataSet>();
        dataSet.add(happinessDateSet);

        LineData lineData = new LineData(dataSet);
        lineData.setDrawValues(false);

        // Add the lines to the chart
        chart.setData(lineData);

        // Disables Legend
        chart.getLegend().setEnabled(false);

        chart.getXAxis().setEnabled(true);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setTextColor(Color.WHITE);
        chart.getXAxis().setValueFormatter(new DayAxisValueFormatter(chart));
        chart.getXAxis().setGranularity(1);
        chart.getXAxis().setLabelRotationAngle(45);
        chart.getXAxis().setLabelCount(10);

        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisRight().setDrawAxisLine(false);

        // Remove Description
        chart.setDescription(null);

        // Color axis
        chart.getAxisRight().setTextColor(Color.WHITE);
        chart.getAxisLeft().setTextColor(Color.WHITE);

        // Make the chart interactive
        chart.setDragEnabled(true);
        chart.setScaleXEnabled(true);
        chart.setScaleYEnabled(false);

        // Make a marker
        ChartMarker elevationMarker = new ChartMarker(getActivity());
        elevationMarker.setOffset(-(elevationMarker.getWidth() / 2), -(elevationMarker.getHeight() / 2));
        chart.setMarker(elevationMarker);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                latestEntriesAdapter.clear();
                latestEntriesAdapter.add("You selected: " + e.copy());
                scrollView.smoothScrollTo(0, scrollView.getTop());
            }

            @Override
            public void onNothingSelected() {

            }
        });

        chart.invalidate();

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



