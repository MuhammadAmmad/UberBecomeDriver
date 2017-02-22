package com.ariorick.uber777.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ariorick.uber777.R;

public class PersonalDataActivity extends AppCompatActivity {

    private final String TAG = "PersonalDataActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        if (!alreadyOpened())
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        findViewById(R.id.continueToCars).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CarPickerActivity.class));
            }
        });

        EditText phone = (EditText) findViewById(R.id.phone);
        //phone.setBackground(ContextCompat.getDrawable(this, R.drawable.redborder));
    }

    public boolean alreadyOpened() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        boolean result = prefs.getBoolean("PersonalDataActivityWasOpened", false);
        Log.i(TAG, "" + result);
        if (!result) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("PersonalDataActivityWasOpened", true);
            editor.apply();
        }
        return result;
    }
}
