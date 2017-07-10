package com.kannan.devan.bingnews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by devan on 2/7/17.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    public static final int[] ATTRS=new int[]{android.R.attr.listDivider};
    public int HORIZONTAL_LIST= LinearLayoutManager.HORIZONTAL;
    public int VERTICAL_LIST=LinearLayoutManager.VERTICAL;
    Drawable mDivider;
    public int mOrientation;

    public DividerItemDecoration(Context mContext,int orientation) {
        final TypedArray mTA=mContext.obtainStyledAttributes(ATTRS);
        mDivider=mTA.getDrawable(0);
        mTA.recycle();
        SetOrientation(orientation);

    }

    private void SetOrientation(int orientation) {
        mOrientation=orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
