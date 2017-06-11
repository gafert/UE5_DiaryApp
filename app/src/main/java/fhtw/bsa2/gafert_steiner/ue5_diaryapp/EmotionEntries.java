package fhtw.bsa2.gafert_steiner.ue5_diaryapp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class EmotionEntries {

    private static EmotionEntries entries;
    private static ArrayList<EmotionEntry> entryList;
    private EntriesChangedListener listener;

    private EmotionEntries() {
        this.listener = null;
        entryList = new ArrayList<EmotionEntry>();
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
        //TODO: Check if today was already entered a day. If so overwrite today´s entry with a new one
        entryList.add(newEntry);

        // Fire the custom listener
        if (listener != null)
            listener.onChanged();   // Call listener

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
