package fhtw.bsa2.gafert_steiner.ue5_diaryapp.emotion;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import fhtw.bsa2.gafert_steiner.ue5_diaryapp.FileIO;

import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.listType;

public class EmotionEntries {

    private static EmotionEntries entries;
    private ArrayList<EmotionEntry> entryList;
    private ArrayList<EmotionEntry> entryListReversed;
    private ArrayList<EntriesChangedListener> listener;

    private EmotionEntries() {
        this.listener = null;
        entryList = new ArrayList<EmotionEntry>();
        entryListReversed = new ArrayList<EmotionEntry>();
        listener = new ArrayList<EntriesChangedListener>();
    }

    public ArrayList<EmotionEntry> getEntries() {
        return entryList;
    }

    public ArrayList<EmotionEntry> getEntriesReversed() {
        return entryListReversed;
    }

    public static EmotionEntries getInstance() {
        if (entries == null) {
            entries = new EmotionEntries();
        }
        return entries;
    }

    public void addEmotion(EmotionEntry newEntry) {

        int i = 0;
        boolean duplicate = false;
        for (EmotionEntry entry : entryList) {
            if (newEntry.getEntryDate().equals(entry.entryDate)) {
                duplicate = true;
                break;
            }
            i++;
        }
        if (duplicate == true) {
            entryList.set(i, newEntry);
        } else {
            entryList.add(newEntry);
        }

        Collections.sort(entryList, new Comparator<EmotionEntry>() {
            public int compare(EmotionEntry o1, EmotionEntry o2) {
                return o2.getEntryDate().compareTo(o1.getEntryDate());
            }
        });


        refreshReverseList();

        // Fire the custom listener
        if (listener != null){
            for(EntriesChangedListener oneListener: listener)
                oneListener.onChanged();   // Call listener
        }


        //Convert ArrayList of Emotion Entries to Json and save it to local file

        Gson gson = new Gson();

        String json = gson.toJson(entryList, listType);
        Log.d("JSON", json);

        try {
            FileIO IO = FileIO.getFileIOInstance();
            IO.writeEmotions(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setEntryList(ArrayList<EmotionEntry> entryList) {
        this.entryList = entryList;
        refreshReverseList();
    }

    public void refreshReverseList() {
        entryListReversed = (ArrayList<EmotionEntry>) entryList.clone();

        Collections.sort(entryList, new Comparator<EmotionEntry>() {
            public int compare(EmotionEntry o1, EmotionEntry o2) {
                return o1.getEntryDate().compareTo(o2.getEntryDate());
            }
        });
    }

    public void deleteAll() {
        entryList = null;
        entryListReversed = null;
        entries = null;

        try {
            FileIO IO = FileIO.getFileIOInstance();
            IO.deleteFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Fire the custom listener
        if (listener != null)
            for(EntriesChangedListener oneListener: listener)
                oneListener.onChanged();   // Call listener
    }

    // Assign the listener implementing events interface that will receive the events
    public void setEntriesChangeListener(EntriesChangedListener listener) {

        this.listener.add(listener);
    }

    public interface EntriesChangedListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        void onChanged();
    }
}
