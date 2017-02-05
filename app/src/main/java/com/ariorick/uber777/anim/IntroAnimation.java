package com.ariorick.uber777.anim;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.ariorick.uber777.activities.IntroActivity;

/**
 * Created by arior on 05.02.2017.
 *
 */

public class IntroAnimation extends AnimationSet {
    private IntroActivity introActivity;
    private Button button;
    private TextView text;


    public IntroAnimation(Context context, AttributeSet attrs, final Button button, TextView text) {
        super(context, attrs);
        introActivity = (IntroActivity) context;


        setFillAfter(true);
        setStartOffset(2000);

        TranslateAnimation translate = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_PARENT, 0, TranslateAnimation.RELATIVE_TO_PARENT, -0.3f);
        translate.setDuration(1000);
        setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimation(translate);

        setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                AlphaAnimation alpha = new AlphaAnimation(0,1);
                alpha.setDuration(2000);
                alpha.setStartOffset(2000);
                alpha.setFillAfter(true);
                button.startAnimation(alpha);
                button.setClickable(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
