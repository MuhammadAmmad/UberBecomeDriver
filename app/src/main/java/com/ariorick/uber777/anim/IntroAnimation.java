package com.ariorick.uber777.anim;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ariorick.uber777.R;
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


            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Появление кнопки
                AlphaAnimation alpha = new AlphaAnimation(0, 1);
                alpha.setDuration(1500);
                alpha.setStartOffset(3000);
                alpha.setFillAfter(true);
                button.startAnimation(alpha);
                button.setClickable(true);

                // Появление текта
                AlphaAnimation alpha2 = new AlphaAnimation(0, 1);
                alpha2.setDuration(1000);

                // Сам текствью
                RelativeLayout relativeLayout = (RelativeLayout) introActivity.findViewById(R.id.introLayout);
                TextView tv = new TextView(introActivity);
                tv.setText(introActivity.getString(R.string.introduction));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tv.setGravity(Gravity.CENTER);

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, introActivity.getResources().getDisplayMetrics()),
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                tv.setLayoutParams(lp);

                relativeLayout.addView(tv);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
