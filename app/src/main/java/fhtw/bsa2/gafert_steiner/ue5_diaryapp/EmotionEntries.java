package fhtw.bsa2.gafert_steiner.ue5_diaryapp;

import android.icu.text.SimpleDateFormat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_NORMAL;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_SAD;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_VERY_HAPPY;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_VERY_SAD;

public class EmotionEntries {

    private static EmotionEntries entries;
    private static ArrayList<EmotionEntry> entryList;
    private EntriesChangedListener listener;

    private EmotionEntries() {
        this.listener = null;
        entryList = new ArrayList<EmotionEntry>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(GlobalVariables.SAVE_DATE_FORMAT);

        try {
            Date date1 = simpleDateFormat.parse("07062017");
            Date date2 = simpleDateFormat.parse("08062017");
            Date date3 = simpleDateFormat.parse("09062017");
            Date date4 = simpleDateFormat.parse("10062017");
            Date date5 = simpleDateFormat.parse("11062017");
            Date date6 = simpleDateFormat.parse("12062017");
            Date date7 = simpleDateFormat.parse("13062017");
            Date date8 = simpleDateFormat.parse("14062017");
            Date date9 = simpleDateFormat.parse("15062017");
            Date date10 = simpleDateFormat.parse("16062017");
            Date date11 = simpleDateFormat.parse("17062017");
            Date date12 = simpleDateFormat.parse("18062017");
            Date date13 = simpleDateFormat.parse("19062017");
            Date date14 = simpleDateFormat.parse("20062017");
            Date date15 = simpleDateFormat.parse("21062017");


            entryList.add(new EmotionEntry(date1, FEELING_NORMAL, "path", "AnyReason"));
            entryList.add(new EmotionEntry(date2, FEELING_SAD, "path", "AnyReason"));
            entryList.add(new EmotionEntry(date3, FEELING_VERY_HAPPY, "path", "AnyReason"));
            entryList.add(new EmotionEntry(date4, FEELING_NORMAL, "path", "AnyReason"));
            entryList.add(new EmotionEntry(date5, FEELING_SAD, "path", "AnyReason"));
            entryList.add(new EmotionEntry(date6, FEELING_NORMAL, "path", "AnyReason"));
            entryList.add(new EmotionEntry(date7, FEELING_SAD, "path", "AnyReason"));
            entryList.add(new EmotionEntry(date8, FEELING_VERY_HAPPY, "path", "AnyReason"));
            entryList.add(new EmotionEntry(date9, FEELING_VERY_SAD, "path", "AnyReason"));
            entryList.add(new EmotionEntry(date10, FEELING_SAD, "path", "AnyReason"));
            entryList.add(new EmotionEntry(date11, FEELING_NORMAL, "path", "AnyReason"));
            entryList.add(new EmotionEntry(date12, FEELING_SAD, "path", "AnyReason"));
            entryList.add(new EmotionEntry(date13, FEELING_VERY_HAPPY, "path", "AnyReason"));
            entryList.add(new EmotionEntry(date14, FEELING_NORMAL, "path", "AnyReason"));
        } catch (Exception e) {
            Log.e("EmotionEntries", "EmotionEntries: Se error here while creating default entries");
        }
    }

    public static ArrayList<EmotionEntry> getEntries() {
        return entryList;
    }

    public static EmotionEntries getInstance() {
        if (entries == null) {
            entries = new EmotionEntries();
        }
        return entries;
    }

    public void addEmotion(EmotionEntry newEntry) {
        entryList.add(newEntry);

        // Fire the custom listener
        if (listener != null)
            listener.onChanged();

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<EmotionEntry>>() {
        }.getType();
        String json = gson.toJson(entryList, listType);
    }

    // Assign the listener implementing events interface that will receive the events
    public void setEntriesChangeListener(EntriesChangedListener listener) {
        this.listener = listener;
    }

    public interface EntriesChangedListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        void onChanged();
    }
}
