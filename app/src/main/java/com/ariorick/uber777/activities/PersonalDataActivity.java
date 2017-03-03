package com.ariorick.uber777.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ariorick.uber777.R;

public class PersonalDataActivity extends AppCompatActivity {

    private final String TAG = "PersonalDataActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
                if (everythingIsFine())
                    startActivity(new Intent(getApplicationContext(), CarPickerActivity.class));
            }
        });

        EditText phone = (EditText) findViewById(R.id.phone);
        //phone.setBackground(ContextCompat.getDrawable(this, R.drawable.redborder));
    }

    public boolean everythingIsFine() {
        if (!editTextIsEmpty(findViewById(R.id.surname)))
            return false;
        if (!editTextIsEmpty(findViewById(R.id.name)))
            return false;
        if (!editTextIsEmpty(findViewById(R.id.phone)))
            return false;
        if (!editTextIsEmpty(findViewById(R.id.email)))
            return false;

        return true;
    }

    public boolean editTextIsEmpty(View editText) {
        TextView edit = (TextView) editText;
        if (edit.getText().toString().length() == 0) {
            edit.setError("Обязательное поле");
            return false;
        }
        return true;
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

    @Override
    protected void onPause() {
        super.onPause();
        save();
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    public void save() {
        SharedPreferences prefs = getSharedPreferences("info", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("surname", ((EditText) findViewById(R.id.surname)).getText().toString());
        editor.putString("name", ((EditText) findViewById(R.id.name)).getText().toString());
        editor.putString("second_name", ((EditText) findViewById(R.id.second_name)).getText().toString());
        editor.putString("phone", ((EditText) findViewById(R.id.phone)).getText().toString());
        editor.putString("email", ((EditText) findViewById(R.id.email)).getText().toString());
        editor.apply();
    }

    public void load() {
        SharedPreferences prefs = getSharedPreferences("info", MODE_PRIVATE);
        ((EditText) findViewById(R.id.surname)).setText(prefs.getString("surname", ""));
        ((EditText) findViewById(R.id.name)).setText(prefs.getString("name", ""));
        ((EditText) findViewById(R.id.second_name)).setText(prefs.getString("second_name", ""));
        ((EditText) findViewById(R.id.phone)).setText(prefs.getString("phone", ""));
        ((EditText) findViewById(R.id.email)).setText(prefs.getString("email", ""));
    }
}
