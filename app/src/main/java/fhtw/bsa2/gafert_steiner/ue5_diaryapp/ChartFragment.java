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

import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_VERY_HAPPY;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_VERY_SAD;

public class ChartFragment extends Fragment {

    final String TAG = "ChartFragment";

    private View rootView;
    private View noDataView;
    private View dataView;
    private EmotionEntries emotionEntries;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chart, container, false);
        noDataView = rootView.findViewById(R.id.noDataView);
        dataView = rootView.findViewById(R.id.dataView);

        emotionEntries = EmotionEntries.getInstance();

        drawElements();

        // When the list changes redraw the chart and circles
        emotionEntries.setEntriesChangeListener(new EmotionEntries.EntriesChangedListener() {
            @Override
            public void onChanged() {
                drawElements();
            }
        });

        return rootView;
    }

    private void drawElements() {
        ArrayList<EmotionEntry> data = EmotionEntries.getEntries();         // Gets the data from a save file or sth

        // Only draws chart and circles if there is data
        if (data != null && !data.isEmpty()) {
            noDataView.setVisibility(View.INVISIBLE);
            dataView.setVisibility(View.VISIBLE);
            setupGraph(rootView, data);                                     // Makes/Styles the chart with given ArrayList
            setupCircles(rootView, data);                                   // Makes/Styles/Calculates the circles with given value
        } else {
            noDataView.setVisibility(View.VISIBLE);
            dataView.setVisibility(View.INVISIBLE);
        }
    }

    private void setupGraph(View rootView, final ArrayList<EmotionEntry> data) {

        LineChart chart = (LineChart) rootView.findViewById(R.id.chart);

        if (data != null) {
            if (!data.isEmpty()) {
                // Entry Array
                List<Entry> happinessEntries = new ArrayList<>();
                int count = 0;
                for (EmotionEntry entry : data) {
                    happinessEntries.add(new Entry(count, entry.getMood()));
                    count++;
                }

                // Das die werte nicht auf der seite kleben
                chart.getXAxis().setAxisMinimum(-1.6f);
                chart.getXAxis().setAxisMaximum(count + 0.2f);

                // Colors for styling
                int[] colors = new int[3];
                colors[0] = ContextCompat.getColor(getContext(), R.color.colorAccent);
                colors[1] = ContextCompat.getColor(getContext(), R.color.colorPrimary);

                // Make a new data set with entries
                LineDataSet happinessDateSet = new LineDataSet(happinessEntries, "Happiness");

                // Style the dataSet
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
                lineData.setDrawValues(false);                              // Disable point labeling

                chart.setData(lineData);
                chart.moveViewTo(chart.getData().getEntryCount(), 0, YAxis.AxisDependency.RIGHT);   // Set viewport to last entries
            }
        }

        // Add the lines to the chart
        chart.getLegend().setEnabled(false);                                // Disables Legend

        // Style X Axis
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setTextColor(Color.WHITE);
        chart.getXAxis().setValueFormatter(new DateFormatter(data));        // Format x values to see day
        chart.getXAxis().setGranularity(1);                                 // Just whole numbers are represented
        chart.getXAxis().setLabelRotationAngle(30);
        chart.getXAxis().setLabelCount(10);                                 // Max labels in the chart
        chart.getXAxis().setTextSize(8);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);           // X Values are at the bottom of the chart

        // Range of
        chart.setVisibleXRangeMaximum(10);
        chart.setVisibleXRangeMinimum(5);

        // Style Y Axis
        chart.getAxisRight().setTextColor(Color.WHITE);
        chart.getAxisLeft().setTextColor(Color.WHITE);

        // Always draw Y as high as max values + offset
        chart.getAxisLeft().setAxisMaximum(FEELING_VERY_HAPPY + 5);
        chart.getAxisLeft().setAxisMinimum(FEELING_VERY_SAD - 3);

        // Disable all Y Axis except 1
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getAxisRight().setEnabled(false);

        chart.setDescription(null);                                         // Remove Description

        // Chart interactive
        chart.setDragEnabled(true);                                         // Chart is dragable
        chart.setScaleXEnabled(true);                                       // Only scaleable on X
        chart.setScaleYEnabled(false);

        // Custom marker to hightlight entries
        ChartMarker elevationMarker = new ChartMarker(getActivity());       // Make a custom marker
        elevationMarker.setOffset(
                -(elevationMarker.getWidth() / 2),
                -(elevationMarker.getHeight() / 2));                        // Center the marker layout
        chart.setMarker(elevationMarker);                                   // Set the new marker to the chart
        chart.setViewPortOffsets(0f, 0f, 0f, 120f);

        // Add a marker hightlight listener
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //TODO: Make Entry Dialog Display correct values
                makeEntryDialog(e, data);
            }

            @Override
            public void onNothingSelected() {
            }
        });

        chart.invalidate(); // Draw chart
    }

    private void makeEntryDialog(Entry e, ArrayList<EmotionEntry> data) {
        // Created a new Dialog
        Dialog dialog = new Dialog(getActivity(), R.style.BetterDialog);   // Custom Dialog with better style
        dialog.setCanceledOnTouchOutside(true);                             // Can close dialog with touch
        dialog.setContentView(R.layout.dialog_selected_element);            // Inflate the layout

        // Add an arrayList to the dialog
        ListView listView = (ListView) dialog.findViewById(R.id.selected_item_listView);
        EmotionArrayAdapter arrayAdapter = new EmotionArrayAdapter(getContext(), R.layout.emotion_list_item);
        listView.setAdapter(arrayAdapter);

        //Get the emotionEntry
        EmotionEntry emotionEntry = data.get((int) e.getX());
        arrayAdapter.add(emotionEntry);  // Add the entry values to the array (temporary)
        dialog.show();  // Display the dialog
    }

    private void setupCircles(View rootView, ArrayList<EmotionEntry> data) {
        final ArcProgress donutProgress1 = (ArcProgress) rootView.findViewById(R.id.happyArc);
        final ArcProgress donutProgress2 = (ArcProgress) rootView.findViewById(R.id.sadArc);

        final View sadnessButton = rootView.findViewById(R.id.sadCircle);
        final View happinessButton = rootView.findViewById(R.id.happyCircle);

        try {

            // Get Average
            int sum = 0;
            int count = 0;
            int average = 0;
            for (EmotionEntry entry : data) {
                sum += entry.getMood();
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



