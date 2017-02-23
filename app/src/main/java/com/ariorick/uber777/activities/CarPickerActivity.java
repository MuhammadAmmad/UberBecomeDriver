package com.ariorick.uber777.activities;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.ariorick.uber777.R;
import com.ariorick.uber777.utils.ItemOffsetDecoration;
import com.ariorick.uber777.utils.MyAdapter;
import com.opencsv.CSVReader;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CarPickerActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "CarPickerActivity";
    String brandID = "0";

    private RecyclerView recycler;
    private MyAdapter adapter;
    private ArrayList<Uri> carPhotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_car_picker);


        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);  */


        load();
        ((AutoCompleteTextView) findViewById(R.id.brand)).dismissDropDown();


        if (!alreadyOpened())
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        workWithAutoComplete1();

        // button to next activity
        findViewById(R.id.continueToDocs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CarPickerActivity.this, DocumentsActivity.class));
            }
        });


        // managing with recycler
        recycler = (RecyclerView) findViewById(R.id.recycle);
        recycler.setHasFixedSize(true);


        recycler.setLayoutManager(new GridLayoutManager(this, 3));

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        recycler.addItemDecoration(itemDecoration);

        adapter = new MyAdapter(carPhotos, getApplicationContext(), true, this);
        adapter.addPlus();
        recycler.setAdapter(adapter);

    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= 18)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.setType("image/*");
        Intent chooser = Intent.createChooser(intent, getString(R.string.choose_photo));
        startActivityForResult(chooser, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            adapter.add(parseUri(data.getClipData(), data.getData()));
        }
    }

    private Uri[] parseUri(ClipData clipData, Uri uri) {
        if (clipData != null) {
            Uri[] uris = new Uri[clipData.getItemCount()];
            for (int i = 0; i < uris.length; i++) {
                uris[i] = clipData.getItemAt(i).getUri();
            }

            return uris;
        }

        return new Uri[]{uri};
    }


    // Autocomplete TextViews and everything around them

    public void workWithAutoComplete1() {
        Log.i(TAG, "working with first autocomplete textview");
        String[] brands = null;
        final List<String[]> csv = readCSV("csv/1_brands.csv");

        if (csv != null) {
            brands = new String[csv.size()];
            brands[0] = "";
            for (int i = 1; i < csv.size(); i++) {
                brands[i] = csv.get(i)[1];
            }
        }

        AutoCompleteTextView auto = (AutoCompleteTextView) findViewById(R.id.brand);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, brands);
        auto.setThreshold(1);
        auto.setAdapter(adapter);
        auto.setHint(getString(R.string.brand_example));
        auto.dismissDropDown();
        //auto.requestFocus();


        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = ((AppCompatTextView) view).getText().toString();
                Log.i(TAG, "text " + text);
                for (int j = 1; j < csv.size(); j++) {
                    if (text.equals(csv.get(j)[1]))
                        brandID = csv.get(j)[0];
                }
                Log.i(TAG, "" + brandID);
                workWithAutoComplete2();
            }
        });
    }

    public void workWithAutoComplete2() {
        Log.i(TAG, "working with second autocomplete textview");
        List<String[]> csv = readCSV("csv/2_models.csv");

        ArrayList<String> modelList = new ArrayList<>();

        Log.i(TAG, "brand " + brandID);

        for (int i = 1; i < csv.size(); i++) {
            String modelBrandID = csv.get(i)[1];
            if (modelBrandID.equals(brandID)) {
                modelList.add(csv.get(i)[2]);
                //Log.i(TAG, csv.get(i)[2]);
            }
        }


        String[] models = modelList.toArray(new String[0]);

        AutoCompleteTextView auto = (AutoCompleteTextView) findViewById(R.id.model);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, models);
        auto.setAdapter(adapter);
        auto.setHint(getString(R.string.model_example));
        //auto.showDropDown();
        auto.requestFocus();
    }

    public List<String[]> readCSV(String path) {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new InputStreamReader(getAssets().open(path)));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        List<String[]> csv = null;

        if (reader != null) {
            try {
                csv = reader.readAll();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return csv;
    }

    // save and load

    @Override
    protected void onPause() {
        super.onPause();
        save();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void save() {
        SharedPreferences prefs = getSharedPreferences("info", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("brandID", brandID);
        editor.putString("brand", ((AutoCompleteTextView) findViewById(R.id.brand)).getText().toString());
        editor.putString("model", ((AutoCompleteTextView) findViewById(R.id.model)).getText().toString());
        editor.putString("year", ((EditText) findViewById(R.id.year)).getText().toString());

        editor.putInt("car_photos_size", carPhotos.size() - 1);
        for (int i = 0; i < carPhotos.size() - 1; i++) {
            editor.putString("car_photo" + i, carPhotos.get(i).toString());
        }

        editor.apply();
    }

    public void load() {
        SharedPreferences prefs = getSharedPreferences("info", MODE_PRIVATE);
        brandID = prefs.getString("brandID", "0");
        ((AutoCompleteTextView) findViewById(R.id.brand)).setText(prefs.getString("brand", ""));
        ((AutoCompleteTextView) findViewById(R.id.model)).setText(prefs.getString("model", ""));
        ((EditText) findViewById(R.id.year)).setText(prefs.getString("year", ""));

        carPhotos = new ArrayList<>();

        for (int i = 0; i < prefs.getInt("car_photos_size", 0); i++) {
            String uri = prefs.getString("car_photo" + i, "");
            if (!uri.equals(""))
                carPhotos.add(Uri.parse(uri));
        }

    }

    public boolean alreadyOpened() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        boolean result = prefs.getBoolean("CarPickerActivityWasOpened", false);
        Log.i(TAG, "" + result);
        if (!result) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("CarPickerActivityWasOpened", true);
            editor.apply();
        }
        return result;
    }
}
