package fhtw.bsa2.gafert_steiner.ue5_diaryapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import info.hoang8f.widget.FButton;


public class AddFragment extends Fragment {

    private Date date;
    // Use this to get the date
    // Date needs to be formatted to before saving SAVE_DATE_FORMAT

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add, container, false);

        final ImageButton dateButton = (ImageButton) rootView.findViewById(R.id.dateButton);
        final TextView dateText = (TextView) rootView.findViewById(R.id.dateTextView);
        final ImageButton cameraButton = (ImageButton) rootView.findViewById(R.id.cameraButton);
        final FButton submitButton = (FButton) rootView.findViewById(R.id.submitButton);

        // Set Current Date with format to TextView
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d. MMM yyyy");
        String currentDateAndTime = simpleDateFormat.format(new Date());
        dateText.setText(currentDateAndTime);

        // Sets new date picked in datePickerDialog
        View.OnClickListener onDatePick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DatePicker);     // Custom DatePickerDialog with better Colors
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        try {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddyyyy");               // Make a new Date with this format
                            date = simpleDateFormat.parse(String.valueOf(month + 1 + "" + dayOfMonth + "" + year));       // Make the date Object to save the date
                            simpleDateFormat = new SimpleDateFormat("d. MMM yyyy");                             // Reformat the date
                            dateText.setText(simpleDateFormat.format(date));                                    // Set the date to the text
                        } catch (ParseException e) {
                            Log.e("AddFragment", "onDateSet: Could not parse to date string");
                        }
                    }
                });
                datePickerDialog.show();
            }
        };

        // Opens the DatePicker and changes the dateText accordingly
        dateButton.setOnClickListener(onDatePick);
        dateText.setOnClickListener(onDatePick);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement Camera here
                Toasty.warning(getContext(), "Not yet implemented", Toast.LENGTH_SHORT).show();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Created a new Dialog
                final Dialog dialog = new Dialog(getActivity(), R.style.BetterDialog);    // Custom Dialog with better style
                dialog.setCanceledOnTouchOutside(true);                             // Can close dialog with touch
                dialog.setContentView(R.layout.dialog_submit);                      // Inflate the layout
                dialog.show();  // Display the dialog

                // Hide Dialog after certain time
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 1000);

                //TODO: Implement saving here
                Toasty.warning(getContext(), "Saving not yet implemented", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

}
