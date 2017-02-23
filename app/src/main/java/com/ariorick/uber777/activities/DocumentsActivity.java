package com.ariorick.uber777.activities;

import android.content.ClipData;
import android.content.Intent;
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
    private ArrayList<Uri> photos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        findViewById(R.id.continueToCheck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DocumentsActivity.this, CheckActivity.class));
            }
        });


        recycler = (RecyclerView) findViewById(R.id.docsRecycler);
        recycler.setHasFixedSize(true);


        recycler.setLayoutManager(new GridLayoutManager(this, 3));

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        recycler.addItemDecoration(itemDecoration);

        adapter = new MyAdapter(photos, getApplicationContext(), true, this);
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
}
