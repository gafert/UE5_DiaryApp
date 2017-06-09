package fhtw.bsa2.gafert_steiner.ue5_diaryapp;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

import fhtw.bsa2.gafert_steiner.ue5_diaryapp.chart.ChartMarker;
import fhtw.bsa2.gafert_steiner.ue5_diaryapp.chart.DateFormatter;

import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_NORMAL;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_SAD;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_VERY_HAPPY;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_VERY_SAD;


public class ChartFragment extends Fragment {

    final String TAG = "ChartFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);

        //TODO: Change getData ArrayList
        ArrayList<Integer> data = getData();    // Gets the data from a save file or sth
        setupGraph(rootView, data);             // Makes/Styles the chart with given ArrayList
        setupCircles(rootView, data);           // Makes/Styles/Calculates the circles with given value

        return rootView;
    }

    private ArrayList<Integer> getData() {

        ArrayList<Integer> mData = new ArrayList<>();
        mData.add(FEELING_NORMAL);
        mData.add(FEELING_SAD);
        mData.add(FEELING_VERY_HAPPY);

        return mData;
    }

    private void setupGraph(View rootView, ArrayList<Integer> data) {

        LineChart chart = (LineChart) rootView.findViewById(R.id.chart);

        if (data != null) {
            if (!data.isEmpty()) {
                // Entry Array
                List<Entry> happinessEntries = new ArrayList<>();
                int count = 1;
                for (int entry : data) {
                    happinessEntries.add(new Entry(count, entry));
                    count++;
                }

                // Colors for styling
                int[] colors = new int[3];
                colors[0] = ContextCompat.getColor(getContext(), R.color.colorAccent);
                colors[1] = ContextCompat.getColor(getContext(), R.color.colorPrimary);

                LineDataSet happinessDateSet = new LineDataSet(happinessEntries, "Happiness");

                happinessDateSet.setColor(colors[0]);                       // Format Line
                happinessDateSet.setCircleColor(colors[0]);
                happinessDateSet.setCircleColorHole(colors[1]);
                happinessDateSet.setCircleRadius(7);
                happinessDateSet.setCircleHoleRadius(5);
                happinessDateSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);    // Makes it line smooth
                happinessDateSet.setHighlightEnabled(true);                 // Allow highlighting for DataSet
                happinessDateSet.setDrawHighlightIndicators(false);         // Draw point on which someone clicked
                happinessDateSet.setLineWidth(2.5f);

                List<ILineDataSet> dataSet = new ArrayList<ILineDataSet>();
                dataSet.add(happinessDateSet);                              // All lines are added to a dataSet

                LineData lineData = new LineData(dataSet);
                lineData.setDrawValues(false);

                chart.setData(lineData);
            }
        }

        // Add the lines to the chart
        chart.getLegend().setEnabled(false);                        // Disables Legend

        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setTextColor(Color.WHITE);
        chart.getXAxis().setValueFormatter(new DateFormatter());    // Format x values to see day
        chart.getXAxis().setGranularity(1);                         // Just whole numbers are represented
        chart.getXAxis().setLabelRotationAngle(45);
        chart.getXAxis().setLabelCount(15);                         // Max labels in the chart
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);   // X Values are at the bottom of the chart

        // Always draw as high as max values
        chart.getAxisLeft().setAxisMaximum(FEELING_VERY_HAPPY + 5); // 5 Offset
        chart.getAxisLeft().setAxisMinimum(FEELING_VERY_SAD - 5);

        chart.getAxisLeft().setDrawLabels(false);                   // Disable all y Axis
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisRight().setEnabled(false);

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
        chart.setVisibleXRangeMaximum(15);
        chart.moveViewTo(chart.getData().getEntryCount(), 0, YAxis.AxisDependency.RIGHT);

        // Add a highlight listener
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //TODO: Make Entry Dialog Display correct values
                makeEntryDialog(e);
            }

            @Override
            public void onNothingSelected() {
            }
        });

        chart.invalidate(); // Draw chart
    }

    private void makeEntryDialog(Entry e) {
        // Created a new Dialog
        Dialog dialog = new Dialog(getActivity(), R.style.BetterDialog);   // Custom Dialog with better style
        dialog.setCanceledOnTouchOutside(true);                             // Can close dialog with touch
        dialog.setContentView(R.layout.dialog_selected_element);            // Inflate the layout

        // Add an arrayList to the dialog
        // Will be ersetzt by custom ArrayAdapter
        //TODO: Adapt ArrayAdapter
        ListView listView = (ListView) dialog.findViewById(R.id.selected_item_listView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_2, android.R.id.text1);
        listView.setAdapter(arrayAdapter);

        arrayAdapter.add(e.copy().toString());  // Add the entry values to the array (temporary)
        dialog.show();  // Display the dialog
    }

    private void setupCircles(View rootView, ArrayList<Integer> data) {
        final ArcProgress donutProgress1 = (ArcProgress) rootView.findViewById(R.id.donutProgress1);
        final ArcProgress donutProgress2 = (ArcProgress) rootView.findViewById(R.id.donutProgress2);

        final View sadnessButton = rootView.findViewById(R.id.sadnessButton);
        final View happinessButton = rootView.findViewById(R.id.happinessButton);

        try {

            // Get Average
            int sum = 0;
            int count = 0;
            int average = 0;
            for (int entry : data) {
                sum += entry;
                count++;
            }
            average = sum / count;


            float span = FEELING_VERY_HAPPY - FEELING_VERY_SAD; // Span between max and min
            float percentModifier = 100 / span;
            int happiness = (int) ((20 + average) * percentModifier);       // 20 +/- the average; if + happy; if - sad
            int sadness = 100 - happiness;                      // Negative to happiness; 100 are percent

            donutProgress1.setProgress(happiness);
            donutProgress2.setProgress(sadness);

            happinessButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int progress = donutProgress1.getProgress();
                    String title = "You have " + progress + "% happiness";
                    String description = "";

                    if (progress <= 40) {
                        description = getString(R.string.feeling_happy_40);
                    } else if (progress > 40 && progress < 50) {
                        description = getString(R.string.feeling_happy_40_50);
                    } else if (progress == 50) {
                        description = getString(R.string.feeling_happy_50);
                    } else if (progress > 50) {
                        description = getString(R.string.feeling_happy_50_100);
                    }

                    makeCircleDialog(title, description);
                }
            });

            sadnessButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Currently the same as the other button
                    // Maybe change in the future
                    int progress = donutProgress1.getProgress();
                    String title = "You have " + donutProgress2.getProgress() + "% sadness";
                    String description = "";

                    if (progress <= 40) {
                        description = getString(R.string.feeling_happy_40);
                    } else if (progress > 40 && progress < 50) {
                        description = getString(R.string.feeling_happy_40_50);
                    } else if (progress == 50) {
                        description = getString(R.string.feeling_happy_50);
                    } else if (progress > 50) {
                        description = getString(R.string.feeling_happy_50_100);
                    }

                    makeCircleDialog(title, description);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "setupCircles: No items in array");
        }
    }

    private void makeCircleDialog(String title, String description) {
        // Created a new Dialog
        Dialog dialog = new Dialog(getActivity(), R.style.BetterDialog);    // Custom Dialog with better style
        dialog.setCanceledOnTouchOutside(true);                             // Can close dialog with touch
        dialog.setContentView(R.layout.dialog_circle);                      // Inflate the layout

        TextView titleTextView = (TextView) dialog.findViewById(R.id.titleText);
        TextView descriptionTextView = (TextView) dialog.findViewById(R.id.descriptionText);

        titleTextView.setText(title);
        descriptionTextView.setText(description);

        dialog.show();  // Display the dialog
    }
}



