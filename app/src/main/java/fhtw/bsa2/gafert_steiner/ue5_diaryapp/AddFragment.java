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

    public ImageView selfieView;
    public String currentPhotoPath = "";
    public Integer emotionValue = FEELING_NORMAL;
    public EditText additionalInfo;
    public TextView dateText;

    // Use this to get the date
    // Date needs to be formatted to before saving SAVE_DATE_FORMAT
    private Date date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add, container, false);

        ImageButton dateButton = (ImageButton) rootView.findViewById(R.id.dateButton);
        ImageButton cameraButton = (ImageButton) rootView.findViewById(R.id.cameraButton);
        FButton submitButton = (FButton) rootView.findViewById(R.id.submitButton);
        RadioGroup emotion = (RadioGroup) rootView.findViewById(R.id.emotion);

        dateText = (TextView) rootView.findViewById(R.id.dateTextView);
        selfieView = (ImageView) rootView.findViewById(R.id.selfie);
        additionalInfo = (EditText) rootView.findViewById(R.id.additionalInfo);

        emotion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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


        // Set Current Date with format to TextView
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d. MMM yyyy");
        final String currentDateAndTime = simpleDateFormat.format(new Date());
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

        View.OnClickListener cameraClickListerner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
                //Toasty.warning(getContext(), "Not yet implemented", Toast.LENGTH_SHORT).show();
            }
        };

        cameraButton.setOnClickListener(cameraClickListerner);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EmotionEntries entries = EmotionEntries.getInstance();

                String addInf = additionalInfo.getText().toString();

                EmotionEntry emotion = new EmotionEntry(date, emotionValue, currentPhotoPath, addInf);

                entries.addEmotion(emotion);

                // Created a new Dialog
                final Dialog dialog = new Dialog(getActivity(), R.style.BetterDialog);    // Custom Dialog with better style
                dialog.setCanceledOnTouchOutside(true);                                   // Can close dialog with touch
                dialog.setContentView(R.layout.dialog_submit);                            // Inflate the layout
                dialog.show();                                                            // Display the dialog

                // Hide Dialog after certain time
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 1000);

                //Toasty.warning(getContext(), "Saving not yet implemented", Toast.LENGTH_SHORT).show();
            }
        });

        selfieView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Created a new Dialog
                Dialog dialog = new Dialog(getActivity(), R.style.BetterDialog) {
                    @Override
                    public boolean onTouchEvent(MotionEvent event) {
                        // Tap anywhere to close dialog.
                        this.dismiss();
                        return true;
                    }
                };
                dialog.setCanceledOnTouchOutside(true);                                   // Can close dialog with touch
                dialog.setContentView(R.layout.dialog_selfie);                            // Inflate the layout
                RoundedImageView selfie = (RoundedImageView) dialog.findViewById(R.id.selfieDialogView);
                Bitmap bitmap = Bitmap.createBitmap(((RoundedDrawable) selfieView.getDrawable()).getSourceBitmap());
                selfie.setImageBitmap(bitmap);

                dialog.show();
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

            //selfieView.setImageBitmap(imageBitmap);

            selfieView.setImageURI(imagePath);
            selfieView.setVisibility(View.VISIBLE);
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
