package fhtw.bsa2.gafert_steiner.ue5_diaryapp;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Fabian on 11.06.2017.
 */


public class FileIO {

    private static final String FILENAME = "documents.txt";
    public static Context context;
    private static FileIO fileIOInstance;
    private static File dataFile;
    private final String TAG="FileIO";

    private FileIO(){

    }


    public static FileIO getFileIOInstance() throws IOException {
        if(fileIOInstance==null){
            fileIOInstance = new FileIO();
        }

        return fileIOInstance;
    }

    private static void getEmotionEntriesStorageFile() throws IOException {
        // Get the directory for the app's private pictures directory.
        dataFile = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS), FILENAME);
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static void setContext(Context context) {
        FileIO.context = context;
    }

    public void writeEmotions(String jsonString){
        if(isExternalStorageWritable()){
            try {
                getEmotionEntriesStorageFile();

                if(dataFile.exists()){
                    dataFile.delete();
                }

                getEmotionEntriesStorageFile();
                dataFile.createNewFile();

                Log.d(TAG,"writeToFile(): " +jsonString);

                BufferedWriter bw = new BufferedWriter(new FileWriter(dataFile,true));
                PrintWriter writer = new PrintWriter(bw);

                writer.println(jsonString);
                writer.flush();
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String readEmotions(){

        String emotionEntries = null;
        if(isExternalStorageReadable()) {
            try {
                getEmotionEntriesStorageFile();
                if(dataFile.exists() && dataFile.length()!=0) {
                    BufferedReader reader = new BufferedReader(new FileReader(dataFile));
                    emotionEntries = reader.readLine();
                    reader.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return emotionEntries;
    }

    public void deleteFiles() {
        if (dataFile.exists()) {
            dataFile.delete();
        }

        File photoDirectory = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES).getPath());
        photoDirectory.delete();
    }
}
