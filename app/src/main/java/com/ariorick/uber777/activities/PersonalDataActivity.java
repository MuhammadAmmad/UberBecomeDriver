package com.ariorick.uber777.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.ariorick.uber777.R;

public class PersonalDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        findViewById(R.id.continueToCars).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CarPickerActivity.class));
            }
        });

        EditText phone = (EditText) findViewById(R.id.phone);
        //phone.setBackground(ContextCompat.getDrawable(this, R.drawable.redborder));
    }
}
