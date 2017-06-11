package fhtw.bsa2.gafert_steiner.ue5_diaryapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedDrawable;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import fhtw.bsa2.gafert_steiner.ue5_diaryapp.helper.BitmapScaler;

import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_HAPPY;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_NORMAL;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_SAD;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_VERY_HAPPY;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_VERY_SAD;

/**
 * Created by michi on 11.06.17.
 */

public class EmotionArrayAdapter extends ArrayAdapter<EmotionEntry> {

    private Context context;
    private int resource;
    private List<EmotionEntry> objects;

    public EmotionArrayAdapter(Context context, int resource) {
        super(context, resource);

        this.context = context;
        this.resource = resource;

        objects = new ArrayList<>();
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        LayoutInflater mLayoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = mLayoutInflater.inflate(resource, null);

        if (objects != null) {
            EmotionEntry emotionEntry = objects.get(position);

            TextView dateTextView = (TextView) view.findViewById(R.id.dateTextView);
            TextView reasonTextView = (TextView) view.findViewById(R.id.reasonTextView);
            ImageView emotionImageView = (ImageView) view.findViewById(R.id.emotionImageView);
            final ImageView additionalImageView = (ImageView) view.findViewById(R.id.additionalImageView);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE d. MMM YYYY");
            String date = simpleDateFormat.format(emotionEntry.getEntryDate());
            dateTextView.setText(date);

            if (!emotionEntry.getReason().equals("")) {
                reasonTextView.setText(emotionEntry.getReason());
            } else {
                reasonTextView.setText("No reason...");
            }

            if (emotionEntry.getPath() != null) {
                Uri uri = Uri.parse(emotionEntry.getPath());
                additionalImageView.setImageBitmap(BitmapScaler.lessResolution(emotionEntry.getPath(), 400, 400));
                additionalImageView.setVisibility(View.VISIBLE);
            }

            switch (emotionEntry.getMood()) {
                case FEELING_VERY_HAPPY:
                    emotionImageView.setImageResource(R.drawable.heart_eyes_emoji);
                    break;
                case FEELING_HAPPY:
                    emotionImageView.setImageResource(R.drawable.slightly_smiling_face_emoji);
                    break;
                case FEELING_SAD:
                    emotionImageView.setImageResource(R.drawable.sad_face_emoji);
                    break;
                case FEELING_NORMAL:
                    emotionImageView.setImageResource(R.drawable.confused_face_emoji);
                    break;
                case FEELING_VERY_SAD:
                    emotionImageView.setImageResource(R.drawable.loudly_crying_face_emoji);
                    break;
            }

            additionalImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Created a new Dialog
                    Dialog dialog = new Dialog(context, R.style.BetterDialog) {
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
                    Bitmap bitmap = Bitmap.createBitmap(((RoundedDrawable) additionalImageView.getDrawable()).getSourceBitmap());
                    selfie.setImageBitmap(bitmap);

                    dialog.show();
                }
            });
        }

        return view;
    }

    @Override
    public void add(@Nullable EmotionEntry object) {
        super.add(object);

        objects.add(object);
    }
}
