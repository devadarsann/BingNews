package com.kannan.devan.bingnews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

/**
 * Created by devan on 15/4/17.
 */

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
    private NewsItemClickListener mNewsItemClickListener;
    public RecyclerTouchListener(Context context, RecyclerView newsView, NewsItemClickListener newsItemClickListener) {
        mNewsItemClickListener=newsItemClickListener;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
