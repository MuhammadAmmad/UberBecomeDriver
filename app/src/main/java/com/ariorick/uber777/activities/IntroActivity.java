package com.ariorick.uber777.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ariorick.uber777.R;
import com.ariorick.uber777.anim.IntroAnimation;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Button button = (Button) findViewById(R.id.startButton);
        button.setClickable(false);
        //button.setAlpha(0);

        ImageView imageView = (ImageView) findViewById(R.id.logo);
        imageView.startAnimation(new IntroAnimation(this, null, button, new TextView(this)));

    }
}