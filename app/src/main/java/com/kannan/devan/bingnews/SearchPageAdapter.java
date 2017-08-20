package com.kannan.devan.bingnews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by devan on 4/8/17.
 */

public class SearchPageAdapter extends FragmentStatePagerAdapter {

    String mSearchQuery;
    String mSearchAPI;

    public SearchPageAdapter(FragmentManager fm, String query, String searchAPI) {
        super(fm);
        mSearchQuery=query;
        mSearchAPI = searchAPI;
    }

    @Override
    public Fragment getItem(int position) {
        int pos=position;
        Fragment mSearResultFragMent= new SearchResultFragment();
        Bundle args=new Bundle();
        args.putString("query",mSearchQuery);
        args.putString("API",mSearchAPI);
        mSearResultFragMent.setArguments(args);
        return mSearResultFragMent;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
