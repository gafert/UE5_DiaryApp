package fhtw.bsa2.gafert_steiner.ue5_diaryapp;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.listType;

public class EmotionEntries {

    private static EmotionEntries entries;
    private static ArrayList<EmotionEntry> entryList;
    private static ArrayList<EmotionEntry> entryListReversed;
    private EntriesChangedListener listener;

    private EmotionEntries() {
        this.listener = null;
        entryList = new ArrayList<EmotionEntry>();
        entryListReversed = new ArrayList<EmotionEntry>();
    }

    public static ArrayList<EmotionEntry> getEntries() {
        return entryList;
    }

    public static ArrayList<EmotionEntry> getEntriesReversed() {
        return entryListReversed;
    }

    public static EmotionEntries getInstance() {
        if (entries == null) {
            entries = new EmotionEntries();
        }
        return entries;
    }

    public void addEmotion(EmotionEntry newEntry){

        int i=0;
        boolean duplicate = false;
        for(EmotionEntry entry: entryList){
            if(newEntry.getEntryDate().equals(entry.entryDate)){
                duplicate=true;
                break;
            }
            i++;
        }
        if(duplicate==true){
            entryList.set(i, newEntry);
        }else{
            entryList.add(newEntry);
        }

        Collections.sort(entryList, new Comparator<EmotionEntry>() {
            public int compare(EmotionEntry o1, EmotionEntry o2) {
                return o2.getEntryDate().compareTo(o1.getEntryDate());
            }
        });


        refreshReverseList();

        // Fire the custom listener
        if (listener != null)
            listener.onChanged();   // Call listener

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

    // Assign the listener implementing events interface that will receive the events
    public void setEntriesChangeListener(EntriesChangedListener listener) {
        this.listener = listener;
    }

    public interface EntriesChangedListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        void onChanged();
    }

    public void setEntryList(ArrayList<EmotionEntry> entryList) {
        EmotionEntries.entryList = entryList;
        refreshReverseList();
    }

    public void refreshReverseList(){
        entryListReversed = (ArrayList<EmotionEntry>) entryList.clone();

        Collections.sort(entryList, new Comparator<EmotionEntry>() {
            public int compare(EmotionEntry o1, EmotionEntry o2) {
                return o1.getEntryDate().compareTo(o2.getEntryDate());
            }
        });
    }
}
