package fhtw.bsa2.gafert_steiner.ue5_diaryapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import info.hoang8f.widget.FButton;

public class AddFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);

        final FButton dateButton = (FButton) rootView.findViewById(R.id.dateButton);
        final TextView dateText = (TextView) rootView.findViewById(R.id.dateTextView);

        // Opens the DatePicker and changes the dateText accordingly
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DatePicker);     // Custom DatePickerDialog with better Colors
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateText.setText(dayOfMonth + "." + month + "." + year);
                    }
                });
                datePickerDialog.show();
            }
        });

        return rootView;
    }

}
