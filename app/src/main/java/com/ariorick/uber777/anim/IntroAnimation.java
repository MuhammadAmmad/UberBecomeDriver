package com.ariorick.uber777.anim;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ariorick.uber777.R;
import com.ariorick.uber777.activities.IntroActivity;
import com.ariorick.uber777.utils.FontCache;

/**
 * Created by arior on 05.02.2017.
 */

public class IntroAnimation extends AnimationSet {
    private IntroActivity introActivity;
    private Button button;
    private TextView text;
    private final int time = 800;


    public IntroAnimation(Context context, AttributeSet attrs, final Button button, TextView text) {
        super(context, attrs);
        introActivity = (IntroActivity) context;


        setFillAfter(true);
        setStartOffset(2 * time);

        TranslateAnimation translate = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_PARENT, 0, TranslateAnimation.RELATIVE_TO_PARENT, -0.3f);
        translate.setDuration(time);
        //setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimation(translate);

        setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Появление кнопки
                TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
                translateAnimation.setDuration((int) (0.75f * time));
                translateAnimation.setStartOffset((int) (2 * time));
                translateAnimation.setFillAfter(true);
                button.startAnimation(translateAnimation);
                button.setAlpha(1);
                button.setClickable(true);

                // Появление текта
                AlphaAnimation alpha2 = new AlphaAnimation(0, 1);
                alpha2.setDuration(time);


                // Сам текствью
                RelativeLayout relativeLayout = (RelativeLayout) introActivity.findViewById(R.id.introLayout);
                TextView tv = new TextView(introActivity);
                tv.setText(introActivity.getString(R.string.introduction));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tv.setGravity(Gravity.CENTER);
                tv.setTextColor(Color.BLACK);

                tv.setTypeface(FontCache.get("fonts/Roboto-Light.ttf", introActivity));

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, introActivity.getResources().getDisplayMetrics()),
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.ABOVE, R.id.startButton);
                lp.setMargins(0, 0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, introActivity.getResources().getDisplayMetrics()));
                lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                tv.setLayoutParams(lp);

                relativeLayout.addView(tv);
                tv.startAnimation(alpha2);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}
