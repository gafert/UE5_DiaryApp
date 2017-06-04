package fhtw.bsa2.gafert_steiner.ue5_diaryapp;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.FeelData.FEELING_HAPPY;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.FeelData.FEELING_NORMAL;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.FeelData.FEELING_SAD;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.FeelData.FEELING_VERY_HAPPY;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.FeelData.FEELING_VERY_SAD;


public class ChartFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);

        setupGraph(rootView);
        setupCircles(rootView);

        return rootView;
    }

    private void setupGraph(View rootView) {

        LineChart chart = (LineChart) rootView.findViewById(R.id.chart);

        // Colors for styling
        int[] colors = new int[3];
        colors[0] = ContextCompat.getColor(getContext(), R.color.colorAccent);
        colors[1] = ContextCompat.getColor(getContext(), R.color.colorAccent2);
        colors[2] = ContextCompat.getColor(getContext(), R.color.colorPrimary);

        // Entry Array
        // Later from saved data
        List<Entry> happinessEntries = new ArrayList<>();
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

        LineDataSet happinessDateSet = new LineDataSet(happinessEntries, "Happiness");

        happinessDateSet.setColor(colors[0]);                       // Format Line
        happinessDateSet.setCircleColor(colors[1]);
        happinessDateSet.setCircleColorHole(colors[2]);
        happinessDateSet.setCircleRadius(5);
        happinessDateSet.setCircleHoleRadius(4);
        happinessDateSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);    // Makes it line smooth
        happinessDateSet.setHighlightEnabled(true);                 // Allow highlighting for DataSet
        happinessDateSet.setDrawHighlightIndicators(false);         // Draw point on which someone clicked

        List<ILineDataSet> dataSet = new ArrayList<ILineDataSet>();
        dataSet.add(happinessDateSet);                              // All lines are added to a dataSet

        LineData lineData = new LineData(dataSet);
        lineData.setDrawValues(false);

        chart.setData(lineData);                                    // Add the lines to the chart
        chart.getLegend().setEnabled(false);                        // Disables Legend

        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setTextColor(Color.WHITE);
        chart.getXAxis().setValueFormatter(new DayAxisValueFormatter(chart));   // Format x values to see day
        chart.getXAxis().setGranularity(1);                         // Just whole numbers are represented
        chart.getXAxis().setLabelRotationAngle(45);
        chart.getXAxis().setLabelCount(10);                         // Max labels in the chart
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);   // X Values are at the bottom of the chart

        chart.getAxisLeft().setDrawLabels(false);                   // Disable all y Axis
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisRight().setDrawAxisLine(false);

        chart.setDescription(null);                                 // Remove Description

        chart.getAxisRight().setTextColor(Color.WHITE);             // Color axis white
        chart.getAxisLeft().setTextColor(Color.WHITE);

        chart.setDragEnabled(true);                                 // Chart is dragable
        chart.setScaleXEnabled(true);                               // Only scaleable on X
        chart.setScaleYEnabled(false);

        ChartMarker elevationMarker = new ChartMarker(getActivity());   // Make a custom marker
        elevationMarker.setOffset(
                -(elevationMarker.getWidth() / 2),
                -(elevationMarker.getHeight() / 2));                // Center the marker layout
        chart.setMarker(elevationMarker);                           // Set the new marker to the chart

        // Add a highlight listener
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                makeDialog(e);
            }

            @Override
            public void onNothingSelected() {
            }
        });

        chart.invalidate(); // Draw chart
    }

    private void makeDialog(Entry e) {
        // Created a new Dialog
        Dialog dialog = new Dialog(getActivity(), R.style.BetterDialog);   // Custom Dialog with better style
        dialog.setCanceledOnTouchOutside(true);                             // Can close dialog with touch
        dialog.setTitle("Dialog Title");                                    // Set the title
        dialog.setContentView(R.layout.dialog_selected_element);            // Inflate the layout

        // Add an arrayList to the dialog
        // Will be ersetzt by custom ArrayAdapter
        ListView listView = (ListView) dialog.findViewById(R.id.selected_item_listView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_2, android.R.id.text1);
        listView.setAdapter(arrayAdapter);

        arrayAdapter.add(e.copy().toString());  // Add the entry values to the array (temporary)

        dialog.show();  // Display the dialog
    }

    private void setupCircles(View rootView) {
        ArcProgress donutProgress1 = (ArcProgress) rootView.findViewById(R.id.donutProgress1);
        ArcProgress donutProgress2 = (ArcProgress) rootView.findViewById(R.id.donutProgress2);
        ArcProgress donutProgress3 = (ArcProgress) rootView.findViewById(R.id.donutProgress3);

        // Progress will be set to smth I dont know yet
        donutProgress1.setProgress(30);
        donutProgress2.setProgress(70);
        donutProgress3.setProgress(50);
    }
}



