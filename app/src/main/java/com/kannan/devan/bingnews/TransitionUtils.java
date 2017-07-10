package com.kannan.devan.bingnews;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionSet;

/**
 * Created by devan on 4/7/17.
 */

public final class TransitionUtils {
    public static Transition makeSharedElementTransition(Context mContext) {
        TransitionSet mTransitionSet=new TransitionSet();
        Transition textSize=new TextSizeTransition();
        textSize.addTarget(R.id.article_heading);
        mTransitionSet.addTransition(mTransitionSet);
        return mTransitionSet;
    }

    public static Transition makeEnterTransition() {
        Transition fade=new Fade();
        fade.excludeTarget(android.R.id.navigationBarBackground,true);
        fade.excludeTarget(android.R.id.statusBarBackground,true);
        return fade;
    }
}
