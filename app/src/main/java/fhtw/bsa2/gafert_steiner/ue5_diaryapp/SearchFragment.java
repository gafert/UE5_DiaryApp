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

/**
 * Created by michi on 03.06.17.
 */

public class SearchFragment extends Fragment {

    ArrayAdapter<String> searchListViewAdapter;     // Found search elements are added to this

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        ListView searchListView = (ListView) rootView.findViewById(R.id.searchListView);
        EditText searchEditText = (EditText) rootView.findViewById(R.id.searchEditText);

        // ArrayAdapter will be custom
        searchListViewAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_2, android.R.id.text2);
        searchListView.setAdapter(searchListViewAdapter);

        // Called when the user is typing
        // Have instant search response without search button
        // #usability
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchListViewAdapter.clear();

                // Implement Search here
                searchListViewAdapter.add("You are searching for " + s);
            }
        });

        return rootView;
    }
}
