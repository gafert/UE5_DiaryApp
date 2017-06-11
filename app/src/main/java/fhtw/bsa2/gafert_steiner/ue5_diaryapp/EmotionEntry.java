package fhtw.bsa2.gafert_steiner.ue5_diaryapp;

import java.util.Date;

public class EmotionEntry {

    Date entryDate;
    Integer mood;
    String path;
    String reason;

    public EmotionEntry(Date entryDate, Integer mood, String path, String reason) {
        this.entryDate = entryDate;
        this.mood = mood;
        this.path = path;
        this.reason = reason;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Integer getMood() {
        return mood;
    }

    public void setMood(Integer mood) {
        this.mood = mood;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
