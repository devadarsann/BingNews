package com.kannan.devan.bingnews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by devan on 4/8/17.
 */

public class SearchResultFragment extends Fragment {
    TextView mSearchTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_results,container,false);
        mSearchTextView = (TextView) rootView.findViewById(R.id.search_text);
        String searchText="Search results for "+getArguments().getString("query");
        mSearchTextView.setText(searchText);
        return rootView;
    }
}
