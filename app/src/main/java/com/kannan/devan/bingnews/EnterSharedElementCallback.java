package com.kannan.devan.bingnews;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.SharedElementCallback;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * Created by devan on 6/7/17.
 */

class EnterSharedElementCallback extends SharedElementCallback {

    private static final String TAG="EnterSharedElementCallback";
    private final float mStartSize;
    private final float mEndSize;
    public EnterSharedElementCallback(Context mContext) {
        Resources res =mContext.getResources();
        mStartSize=res.getDimensionPixelSize(R.dimen.small_text_size);
        mEndSize=res.getDimensionPixelSize(R.dimen.large_text_size);
    }

    @Override
    public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
        TextView textView= (TextView) sharedElements.get(0);

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mStartSize);
    }

    @Override
    public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
        TextView textView= (TextView) sharedElements.get(0);

        int oldWidth=textView.getMeasuredWidth();
        int oldHeight=textView.getMeasuredHeight();

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mEndSize);

        int widthSpec=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int heightSpec=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthSpec,heightSpec);

        int newWidth=textView.getMeasuredWidth();
        int newHeight=textView.getMeasuredHeight();

        int widthDiff=newWidth - oldWidth;
        int heightDiff = newHeight - oldHeight;
        textView.layout(textView.getLeft() - widthDiff/2,textView.getTop() - heightDiff/2,
                textView.getRight()+widthDiff/2,textView.getBottom()+heightDiff/2);

    }
}
