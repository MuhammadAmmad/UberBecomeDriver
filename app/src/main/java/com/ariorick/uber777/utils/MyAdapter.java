package com.ariorick.uber777.utils;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ariorick.uber777.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;



public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Uri> mDataset;
    private Context context;
    private boolean editable = false;
    private View.OnClickListener clickCallback;

    private int pictureSize = 100;


    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View view;

        ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public void addPlus() {
        if (editable)
            mDataset.add(Uri.parse("android.resource://com.ariorick.uber777/drawable/add"));
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Uri> myDataset, Context context, boolean editable, View.OnClickListener clickCallback) {
        mDataset = myDataset;
        this.context = context;
        this.editable = editable;
        this.clickCallback = clickCallback;
        //addPlus();


        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        pictureSize = (int) (width / 3.9);

    }

    public void add(Uri... uri) {
        for (Uri u : uri) {
            mDataset.add(mDataset.size() - 1, u);
            notifyItemInserted(mDataset.size() - 2);
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        // set the view's size, margins, paddings and layout parameters


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ImageView photo = (ImageView) holder.view.findViewById(R.id.photo);
        photo.setOnClickListener(clickCallback);
        ImageView delete = (ImageView) holder.view.findViewById(R.id.delete);
        if (!editable) {
            delete.setVisibility(View.GONE);
            delete.setClickable(false);
        }


        ViewGroup.LayoutParams photoParams = photo.getLayoutParams();
        photoParams.height = pictureSize;
        photoParams.width = pictureSize;
        photo.setLayoutParams(photoParams);

        ViewGroup.LayoutParams deleteParams = delete.getLayoutParams();
        deleteParams.height = (int) (pictureSize * 0.3f);
        deleteParams.width = (int) (pictureSize * 0.3f);


        Glide.with(context)
                .load(mDataset.get(position))
                .centerCrop()
                .into(photo);


        if (position == getItemCount() - 1) {
            delete.setClickable(false);
            delete.setVisibility(View.GONE);
            photo.setClickable(true);
        } else {
            photo.setClickable(false);
            if (editable) {
                delete.setVisibility(View.VISIBLE);
                delete.setClickable(true);
            }
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataset.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
