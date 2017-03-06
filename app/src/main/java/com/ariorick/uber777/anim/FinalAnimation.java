package com.ariorick.uber777.anim;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

/**
 * Created by arior on 06.03.2017.
 */

public class FinalAnimation extends AnimationSet {


    public FinalAnimation(Context context, AttributeSet attrs, Button button) {
        super(context, attrs);

        int time = 800;
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        translateAnimation.setDuration((int) (0.75f * time));
        translateAnimation.setFillAfter(true);
        addAnimation(translateAnimation);
        button.setAlpha(1);
        button.setClickable(true);
    }

}
