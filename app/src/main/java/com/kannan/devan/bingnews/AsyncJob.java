package com.kannan.devan.bingnews;

import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.support.v7.graphics.Palette;

/**
 * Created by devan on 17/7/17.
 */

public class AsyncJob implements Palette.PaletteAsyncListener {

    NewsAdapter.Viewholder viewholder;
    public AsyncJob(NewsAdapter.Viewholder holder) {
        viewholder=holder;
    }

    @Override
    public void onGenerated(Palette palette) {
        final Palette.Swatch vibrant=palette.getVibrantSwatch();
                                    if (vibrant!=null){
                                        //viewholder.itemsView.setCardBackgroundColor(vibrant.getRgb());
                                        viewholder.description.setTextColor(vibrant.getTitleTextColor());
                                        viewholder.category.setTextColor(vibrant.getBodyTextColor());
                                        viewholder.elaboratedNews.setTextColor(vibrant.getBodyTextColor());
                                        viewholder.provider.setTextColor(vibrant.getBodyTextColor());
                                        ValueAnimator animator=ValueAnimator.ofArgb(viewholder.itemsView.getSolidColor(),vibrant.getRgb());
                                        animator.setDuration(300);
                                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                            @Override
                                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                                viewholder.itemsView.setCardBackgroundColor(ColorStateList.valueOf((int)valueAnimator.getAnimatedValue()));
                                            }
                                        });
                                        animator.start();
                                    }
    }
}
