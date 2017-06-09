package fhtw.bsa2.gafert_steiner.ue5_diaryapp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_NORMAL;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_SAD;
import static fhtw.bsa2.gafert_steiner.ue5_diaryapp.GlobalVariables.FEELING_VERY_HAPPY;

/**
 * Created by Fabian on 08.06.2017.
 */

public class EmotionEntries {

    private static EmotionEntries entries;
    ArrayList<EmotionEntry> entryList;


    private EmotionEntries(){
        entryList = new ArrayList<EmotionEntry>();

        EmotionEntry entry1 = new EmotionEntry(new Date(), FEELING_NORMAL, "path", "AnyReason");
        EmotionEntry entry2 = new EmotionEntry(new Date(), FEELING_SAD, "path", "AnyReason");
        EmotionEntry entry3 = new EmotionEntry(new Date(), FEELING_VERY_HAPPY, "path", "AnyReason");
        EmotionEntry entry4 = new EmotionEntry(new Date(), FEELING_NORMAL, "path", "AnyReason");
        EmotionEntry entry5 = new EmotionEntry(new Date(), FEELING_VERY_HAPPY, "path", "AnyReason");
    }

    public static EmotionEntries getInstance () {
        if (entries == null) {
            entries = new EmotionEntries ();
        }
        return entries;
    }

    public void addEmotion(EmotionEntry newEntry){
        entryList.add(newEntry);

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<EmotionEntry>>() {}.getType();
        String json = gson.toJson(entryList, listType);
    }
}
