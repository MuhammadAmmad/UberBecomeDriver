package com.ariorick.uber777.activities;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.ariorick.uber777.R;
import com.ariorick.uber777.utils.ItemOffsetDecoration;
import com.ariorick.uber777.utils.MyAdapter;

import java.util.ArrayList;

public class CheckActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private MyAdapter adapter;
    private ArrayList<Uri> carPhotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        load();

        recycler = (RecyclerView) findViewById(R.id.carRecycler);
        recycler.setHasFixedSize(true);


        recycler.setLayoutManager(new GridLayoutManager(this, 3));

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        recycler.addItemDecoration(itemDecoration);

        adapter = new MyAdapter(carPhotos, getApplicationContext(), false, null);
        adapter.addPlus();
        recycler.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void load() {
        SharedPreferences prefs = getSharedPreferences("info", MODE_PRIVATE);

        // Personal data
        String name = prefs.getString("surname", "");
        name += " " + prefs.getString("name", "");
        name += " " + prefs.getString("second_name", "");
        Log.i("LOL", name);
        ((TextView) findViewById(R.id.name)).setText(name);
        ((TextView) findViewById(R.id.phone)).setText(prefs.getString("phone", ""));
        ((TextView) findViewById(R.id.email)).setText(prefs.getString("email", ""));

        // Car
        String carName = prefs.getString("brand", "");
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


    }
}
