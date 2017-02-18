package com.ariorick.uber777.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ariorick.uber777.R;
import com.ariorick.uber777.anim.IntroAnimation;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_intro);

        Button button = (Button) findViewById(R.id.startButton);
        button.setClickable(false);
        button.setAlpha(0);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PersonalDataActivity.class));
            }
        });

        View logo = findViewById(R.id.logo);
        logo.startAnimation(new IntroAnimation(this, null, button, new TextView(this)));

    }
}