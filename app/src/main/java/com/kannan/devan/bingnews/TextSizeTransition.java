package com.kannan.devan.bingnews;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.AttributeSet;
import android.util.Property;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by devan on 4/7/17.
 */

class TextSizeTransition extends Transition {
    private static final String PROPNAME_TEXT_SIZE="com.kannan.devan.bingnews:TextSizeTransition:textsize";
    String[] TRANSITION_PROPERTIES={PROPNAME_TEXT_SIZE};

    private static final Property<TextView,Float> TEXT_SIZE_PROPERTY=
            new Property<TextView, Float>(Float.class,"textSize") {
                @Override
                public Float get(TextView textView) {
                    return textView.getTextSize();
                }

                @Override
                public void set(TextView object, Float value) {
                    object.setTextSize(TypedValue.COMPLEX_UNIT_PX,value);
                }
            };
    public TextSizeTransition(){}

    public TextSizeTransition(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public String[] getTRANSITION_PROPERTIES() {
        return TRANSITION_PROPERTIES;
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    private void captureValues(TransitionValues transitionValues) {
        if (transitionValues.view instanceof TextView){
            TextView mTextView= (TextView) transitionValues.view;
            transitionValues.values.put(PROPNAME_TEXT_SIZE,mTextView.getTextSize());
        }
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if (startValues==null||endValues==null){
            return null;
        }

        Float startSize= (Float) startValues.values.get(PROPNAME_TEXT_SIZE);
        Float endSize= (Float) endValues.values.get(PROPNAME_TEXT_SIZE);
        if (startSize==null||endSize==null||startSize.floatValue()==endSize.floatValue()){
            return null;
        }

        TextView view= (TextView) endValues.view;
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX,startSize);
        return ObjectAnimator.ofFloat(view,TEXT_SIZE_PROPERTY,endSize);
    }
}
