package fhtw.bsa2.gafert_steiner.ue5_diaryapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedDrawable;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import info.hoang8f.widget.FButton;

import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_HAPPY;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_NORMAL;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_SAD;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_VERY_HAPPY;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_VERY_SAD;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.REQUEST_IMAGE_CAPTURE;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.REQUEST_TAKE_PHOTO;

public class AddFragment extends Fragment {

    ImageView additonalImageView;                   // Shows a taken image
    TextView dateTextView;                          // Shows the date
    RadioGroup emotionPicker;                       // Sets the emotionValue
    String currentPhotoPath;                        // Get the photo path
    Integer emotionValue = FEELING_NORMAL;          // Get the emotion
    EditText reasonTextView;                        // Get the reason
    Date date;                                      // Get the date

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add, container, false);

        ImageButton dateImageButton = (ImageButton) rootView.findViewById(R.id.dateButton);
        ImageButton cameraImageButton = (ImageButton) rootView.findViewById(R.id.cameraButton);
        FButton submitButton = (FButton) rootView.findViewById(R.id.submitButton);

        emotionPicker = (RadioGroup) rootView.findViewById(R.id.emotionGroup);
        dateTextView = (TextView) rootView.findViewById(R.id.dateTextView);
        additonalImageView = (ImageView) rootView.findViewById(R.id.additionalImageView);
        reasonTextView = (EditText) rootView.findViewById(R.id.reasonTextView);

        // Set Current Date with format to TextView
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d. MMM yyyy");
        final String currentDateAndTime = simpleDateFormat.format(new Date());
        date = new Date();
        dateTextView.setText(currentDateAndTime);

        emotionPicker.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.veryHappyButton:
                        emotionValue = FEELING_VERY_HAPPY;
                        break;
                    case R.id.happyButton:
                        emotionValue = FEELING_HAPPY;
                        break;
                    case R.id.normalButton:
                        emotionValue = FEELING_NORMAL;
                        break;
                    case R.id.sadButton:
                        emotionValue = FEELING_SAD;
                        break;
                    case R.id.verySadButton:
                        emotionValue = FEELING_VERY_SAD;
                        break;
                }
            }
        });

        // Sets new date picked in datePickerDialog
        View.OnClickListener onDatePick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DatePicker);     // Custom DatePickerDialog with better Colors
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);                 // Only select present and future
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        try {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddyyyy");               // Make a new Date with this format
                            date = simpleDateFormat.parse(String.valueOf(month + 1 + "" + dayOfMonth + "" + year));       // Make the date Object to save the date
                            simpleDateFormat = new SimpleDateFormat("d. MMM yyyy");                             // Reformat the date
                            dateTextView.setText(simpleDateFormat.format(date));                                // Set the date to the text
                        } catch (ParseException e) {
                            Log.e("AddFragment", "onDateSet: Could not parse to date string");
                        }
                    }
                });
                datePickerDialog.show();
            }
        };

        // Opens the DatePicker and changes the dateTextView accordingly
        dateImageButton.setOnClickListener(onDatePick);
        dateTextView.setOnClickListener(onDatePick);

        cameraImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
                //Toasty.warning(getContext(), "Not yet implemented", Toast.LENGTH_SHORT).show();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmotionEntries entries = EmotionEntries.getInstance();
                String addInf = reasonTextView.getText().toString();
                EmotionEntry emotionEntry = new EmotionEntry(date, emotionValue, currentPhotoPath, addInf);
                entries.addEmotion(emotionEntry);

                // Created a new Dialog
                final Dialog submitDialog = new Dialog(getActivity(), R.style.BetterDialog);    // Custom Dialog with better style
                submitDialog.setCanceledOnTouchOutside(true);                                   // Can close submitDialog with touch
                submitDialog.setContentView(R.layout.dialog_submit);                            // Inflate the layout
                submitDialog.show();                                                            // Display the submitDialog

                // Hide Dialog after certain time
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        submitDialog.dismiss();
                    }
                }, 1000);

                // Reset Add site
                emotionPicker.check(R.id.normalButton);
                reasonTextView.setText("");
                additonalImageView.setImageURI(null);
                additonalImageView.setVisibility(View.GONE);

                //Toasty.warning(getContext(), "Saving not yet implemented", Toast.LENGTH_SHORT).show();
            }
        });

        additonalImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Created a new Dialog
                Dialog expandedImageDialog = new Dialog(getActivity(), R.style.BetterDialog) {
                    @Override
                    public boolean onTouchEvent(MotionEvent event) {
                        // Tap anywhere to close expandedImageDialog.
                        this.dismiss();
                        return true;
                    }
                };
                expandedImageDialog.setCanceledOnTouchOutside(true);                                   // Can close expandedImageDialog with touch
                expandedImageDialog.setContentView(R.layout.dialog_selfie);                            // Inflate the layout
                RoundedImageView expandedImageView = (RoundedImageView) expandedImageDialog.findViewById(R.id.selfieDialogView);
                Bitmap mBitmap = Bitmap.createBitmap(((RoundedDrawable) additonalImageView.getDrawable()).getSourceBitmap());
                expandedImageView.setImageBitmap(mBitmap);
                expandedImageDialog.show();
            }
        });

        return rootView;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "fhtw.bsa2.gafert_steiner.ue5_diaryapp.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = Bitmap.createBitmap((Bitmap) extras.get("data"));

            Uri imagePath = Uri.parse(currentPhotoPath);

            //additonalImageView.setImageBitmap(imageBitmap);

            additonalImageView.setImageURI(imagePath);
            additonalImageView.setVisibility(View.VISIBLE);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
