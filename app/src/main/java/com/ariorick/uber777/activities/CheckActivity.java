package com.ariorick.uber777.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ariorick.uber777.R;
import com.ariorick.uber777.utils.ItemOffsetDecoration;
import com.ariorick.uber777.utils.MyAdapter;

import java.util.ArrayList;

public class CheckActivity extends AppCompatActivity {

    private RecyclerView carRecycler;
    private MyAdapter carAdapter;
    private RecyclerView docsRecycler;
    private MyAdapter docsAdapter;
    private ArrayList<Uri> carPhotos = new ArrayList<>();
    private ArrayList<Uri> docsPhotos = new ArrayList<>();

    String name = "";
    String carName = "";
    String phone = "";
    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_check);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        load();

        carRecycler = (RecyclerView) findViewById(R.id.carRecycler);
        carRecycler.setHasFixedSize(true);
        carRecycler.setNestedScrollingEnabled(false);

        carRecycler.setLayoutManager(new GridLayoutManager(this, 3));

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        carRecycler.addItemDecoration(itemDecoration);

        carAdapter = new MyAdapter(carPhotos, getApplicationContext(), false, null);
        carAdapter.addPlus();
        carRecycler.setAdapter(carAdapter);

        // docs
        docsRecycler = (RecyclerView) findViewById(R.id.docsRecycler);
        docsRecycler.setHasFixedSize(true);
        docsRecycler.setNestedScrollingEnabled(false);

        docsRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        docsRecycler.addItemDecoration(itemDecoration);

        docsAdapter = new MyAdapter(docsPhotos, getApplicationContext(), false, null);
        docsAdapter.addPlus();
        docsRecycler.setAdapter(docsAdapter);

        findViewById(R.id.finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SendActivity.class));
            }
        });

        //((ScrollView)findViewById(R.id.scrollView)).fullScroll(ScrollView.FOCUS_UP);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void load() {
        SharedPreferences prefs = getSharedPreferences("info", MODE_PRIVATE);

        // Personal data
        name = prefs.getString("surname", "");
        name += " " + prefs.getString("name", "");
        name += " " + prefs.getString("second_name", "");
        Log.i("LOL", name);
        ((TextView) findViewById(R.id.name)).setText(name);
        phone = prefs.getString("phone", "");
        ((TextView) findViewById(R.id.phone)).setText(phone);
        email = prefs.getString("email", "");
        ((TextView) findViewById(R.id.email)).setText(email);

        // Car
        carName = prefs.getString("brand", "");
        carName += " " + prefs.getString("model", "");
        String year = " " + prefs.getString("year", "");
        if (!year.equals(" "))
            carName += year + "го года выпуска";
        ((TextView) findViewById(R.id.car)).setText(carName);

        carPhotos = new ArrayList<>();

        for (int i = 0; i < prefs.getInt("car_photos_size", 0); i++) {
            String uri = prefs.getString("car_photo" + i, "");
            if (!uri.equals(""))
                carPhotos.add(Uri.parse(uri));
        }

        // Docs
        docsPhotos = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            String uri = prefs.getString("docPhoto" + i, "");
            if (!uri.equals(""))
                docsPhotos.add(Uri.parse(uri));
        }


    }
}
