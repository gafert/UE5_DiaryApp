package fhtw.bsa2.gafert_steiner.ue5_diaryapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import info.hoang8f.widget.FButton;

import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.REQUEST_IMAGE_CAPTURE;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.REQUEST_TAKE_PHOTO;




public class AddFragment extends Fragment {

    private Date date;
    // Use this to get the date
    // Date needs to be formatted to before saving SAVE_DATE_FORMAT

    public ImageView iV;
    public ImageButton cameraButton;
    public String mCurrentPhotoPath="";
    public Integer emotionValue=0;
    public EditText additionalInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add, container, false);

        final ImageButton dateButton = (ImageButton) rootView.findViewById(R.id.dateButton);
        final TextView dateText = (TextView) rootView.findViewById(R.id.dateTextView);
        cameraButton = (ImageButton) rootView.findViewById(R.id.cameraButton);
        final FButton submitButton = (FButton) rootView.findViewById(R.id.submitButton);
        iV = (ImageView)rootView.findViewById(R.id.selfie);
        final RadioGroup emotion = (RadioGroup) rootView.findViewById(R.id.emotion);
        additionalInfo = (EditText) rootView.findViewById(R.id.additionalInfo);


        emotion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.veryHappyButton: emotionValue=20;
                        break;
                    case R.id.happyButton: emotionValue=10;
                        break;
                    case R.id.normalButton: emotionValue=0;
                        break;
                    case R.id.sadButton: emotionValue=-10;
                        break;
                    case R.id.verySadButton: emotionValue=-20;
                        break;
                }
            }
        });



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
                dispatchTakePictureIntent();
                //Toasty.warning(getContext(), "Not yet implemented", Toast.LENGTH_SHORT).show();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EmotionEntries entries = EmotionEntries.getInstance();

                String addInf = "";
                addInf= (String) additionalInfo.getText().toString();



                EmotionEntry emotion = new EmotionEntry(date, emotionValue, mCurrentPhotoPath, addInf);

                entries.addEmotion(emotion);

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

                //Toasty.warning(getContext(), "Saving not yet implemented", Toast.LENGTH_SHORT).show();
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
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = Bitmap.createBitmap((Bitmap) extras.get("data"));

            Uri imagePath = Uri.parse(mCurrentPhotoPath);

            //iV.setImageBitmap(imageBitmap);

            iV.setImageURI(imagePath);
            iV.setVisibility(View.VISIBLE);



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
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }




}
