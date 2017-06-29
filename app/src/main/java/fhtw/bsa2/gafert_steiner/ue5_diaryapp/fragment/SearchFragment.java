package fhtw.bsa2.gafert_steiner.ue5_diaryapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import fhtw.bsa2.gafert_steiner.ue5_diaryapp.R;
import fhtw.bsa2.gafert_steiner.ue5_diaryapp.emotion.EmotionArrayAdapter;
import fhtw.bsa2.gafert_steiner.ue5_diaryapp.emotion.EmotionEntries;
import fhtw.bsa2.gafert_steiner.ue5_diaryapp.emotion.EmotionEntry;

public class SearchFragment extends Fragment {

    EmotionArrayAdapter arrayAdapter;     // Found search elements are added to this
    EmotionEntries entries;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        EditText searchEditText = (EditText) rootView.findViewById(R.id.searchEditText);

        ListView listView = (ListView) rootView.findViewById(R.id.searchListView);
        arrayAdapter = new EmotionArrayAdapter(getContext(), R.layout.emotion_list_item);
        listView.setAdapter(arrayAdapter);

        entries = EmotionEntries.getInstance();
        entries.setEntriesChangeListener(new EmotionEntries.EntriesChangedListener() {
            @Override
            public void onChanged() {
                arrayAdapter.clear();
                for (EmotionEntry entry : entries.getEntriesReversed()) {
                    arrayAdapter.add(entry);
                }
            }
        });

        for (EmotionEntry entry : entries.getEntriesReversed()) {
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

                for (EmotionEntry entry : entries.getEntriesReversed()) {
                    if (entry.getReason().toLowerCase().contains(text.toLowerCase())) {
                        arrayAdapter.add(entry);
                    }
                }


            }
        });

        return rootView;
    }
}
