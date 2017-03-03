package com.ariorick.uber777.activities;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ariorick.uber777.R;
import com.ariorick.uber777.utils.ItemOffsetDecoration;
import com.ariorick.uber777.utils.MyAdapter;

import java.util.ArrayList;

public class DocumentsActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recycler;
    private MyAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Uri> docsPhotos = new ArrayList<>();

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


        recycler = (RecyclerView) findViewById(R.id.docsRecycler);
        recycler.setHasFixedSize(true);


        recycler.setLayoutManager(new GridLayoutManager(this, 3));

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        recycler.addItemDecoration(itemDecoration);

        adapter = new MyAdapter(docsPhotos, getApplicationContext(), true, this);
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

    public void save() {
        SharedPreferences prefs = getSharedPreferences("info", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("doc_photos_size", docsPhotos.size() - 1);
        for (int i = 0; i < docsPhotos.size() - 1; i++) {
            editor.putString("doc_photo" + i, docsPhotos.get(i).toString());
        }

        editor.apply();
    }

    public void load() {
        SharedPreferences prefs = getSharedPreferences("info", MODE_PRIVATE);

        docsPhotos = new ArrayList<>();

        for (int i = 0; i < prefs.getInt("doc_photos_size", 0); i++) {
            String uri = prefs.getString("doc_photo" + i, "");
            if (!uri.equals(""))
                docsPhotos.add(Uri.parse(uri));
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
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
}
