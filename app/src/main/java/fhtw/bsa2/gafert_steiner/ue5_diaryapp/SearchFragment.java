package fhtw.bsa2.gafert_steiner.ue5_diaryapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import info.hoang8f.widget.FButton;

/**
 * Created by michi on 03.06.17.
 */

public class SearchFragment extends Fragment {

    ListView searchListView;
    FButton searchButton;
    EditText searchEditText;
    ArrayAdapter<String> searchListViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        searchListView = (ListView) rootView.findViewById(R.id.searchListView);
        searchButton = (FButton) rootView.findViewById(R.id.searchButton);
        searchEditText = (EditText) rootView.findViewById(R.id.searchEditText);
        searchListViewAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_2, android.R.id.text2);
        searchListView.setAdapter(searchListViewAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchListViewAdapter.add("Im A search entry and you were sad :(");
                searchListViewAdapter.add("I am another search entry");
            }
        });

        return rootView;
    }
}
