package fhtw.bsa2.gafert_steiner.ue5_diaryapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by michi on 03.06.17.
 */

public class SearchFragment extends Fragment {

    EmotionArrayAdapter arrayAdapter;     // Found search elements are added to this
    EmotionEntries entries;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        ListView searchListView = (ListView) rootView.findViewById(R.id.searchListView);
        EditText searchEditText = (EditText) rootView.findViewById(R.id.searchEditText);

        // ArrayAdapter will be custom
        ListView listView = (ListView) rootView.findViewById(R.id.searchListView);
        arrayAdapter = new EmotionArrayAdapter(getContext(), R.layout.emotion_list_item);
        listView.setAdapter(arrayAdapter);

        entries = EmotionEntries.getInstance();

        for(EmotionEntry entry: entries.getEntriesReversed()){
            arrayAdapter.add(entry);
        }

        // Called when the user is typing
        // Have instant search response without search button
        // #usability
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayAdapter.clear();
            }

            @Override
            public void afterTextChanged(Editable s) {
                arrayAdapter.clear();
                arrayAdapter.notifyDataSetChanged();
                String text = s.toString();
                //ArrayList<EmotionEntry> resultEntries = new ArrayList<EmotionEntry>();

                for(EmotionEntry entry:entries.getEntriesReversed()){
                    if(entry.getReason().contains(text)){
                        arrayAdapter.add(entry);
                    }
                }


            }
        });

        return rootView;
    }
}
