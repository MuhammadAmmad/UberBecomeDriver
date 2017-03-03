package com.ariorick.uber777.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import com.ariorick.uber777.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DocumentsActivity extends AppCompatActivity {


    private Uri outputFileUri;

    private Uri[] docsPhotos = new Uri[5];
    private int clickedViewnumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_documents);

        findViewById(R.id.continueToCheck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DocumentsActivity.this, CheckActivity.class));
            }
        });

        load();
    }

    public void addPhoto(int docNumber) {
        clickedViewnumber = docNumber - 1;

        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        root.mkdirs();
        final String fname = "img_" + System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // gallery
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.setType("image/*");

        Intent chooser = Intent.createChooser(intent, getString(R.string.choose_photo));
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooser, 1);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add1:
                addPhoto(1);
                break;
            case R.id.add2:
                addPhoto(2);
                break;
            case R.id.add3:
                addPhoto(3);
                break;
            case R.id.add4:
                addPhoto(4);
                break;
            case R.id.add5:
                addPhoto(5);
                break;

        }

    }

    public void setChecked(int number) {
        switch (number) {
            case 1:
                setMarkVisible(R.id.card1);
                break;
            case 2:
                setMarkVisible(R.id.card2);
                break;
            case 3:
                setMarkVisible(R.id.card3);
                break;
            case 4:
                setMarkVisible(R.id.card4);
                break;
            case 5:
                setMarkVisible(R.id.card5);
                break;
        }
    }

    private void setMarkVisible(int id) {
        CardView cardView = (CardView) findViewById(id);
        cardView.findViewById(R.id.check).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (data != null) {
                docsPhotos[clickedViewnumber] = data.getData();
                setChecked(clickedViewnumber + 1);
            } else if (outputFileUri != null) {
                docsPhotos[clickedViewnumber] = outputFileUri;
                outputFileUri = null;
                setChecked(clickedViewnumber + 1);
            }
        }
    }

    public void save() {
        SharedPreferences prefs = getSharedPreferences("info", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        for (int i = 0; i < 5; i++) {
            if (docsPhotos[i] != null)
                editor.putString("doc_photo" + i, docsPhotos[i].toString());
        }

        editor.apply();
    }

    public void load() {
        SharedPreferences prefs = getSharedPreferences("info", MODE_PRIVATE);

        docsPhotos = new Uri[5];

        for (int i = 0; i < 5; i++) {
            String uri = prefs.getString("doc_photo" + i, "");
            if (!uri.equals("")) {
                docsPhotos[i] = Uri.parse(uri);
                setChecked(i + 1);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
    }

}
