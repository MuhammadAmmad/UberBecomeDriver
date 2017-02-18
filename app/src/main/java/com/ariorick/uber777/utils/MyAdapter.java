package com.ariorick.uber777.utils;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ariorick.uber777.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by arior on 30.01.2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Uri> mDataset;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View view;

        ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public void addPlus() {
        mDataset.add(Uri.parse("android.resource://com.ariorick.gallery/drawable/plus"));
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Uri> myDataset, Context context) {
        this.context = context;
        mDataset = myDataset;
        //addPlus();
    }

    public void add(Uri... uri) {
        for (Uri u : uri) {
            mDataset.add(mDataset.size() - 1, u);
            notifyItemInserted(mDataset.size() - 2);
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
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
        ImageView delete = (ImageView) holder.view.findViewById(R.id.delete);
        Picasso.with(context)
                .load(mDataset.get(position))
                .fit()
                .into(photo);

        if (position == getItemCount() - 1) {
            delete.setClickable(false);
            delete.setVisibility(View.GONE);
            photo.setClickable(true);
        } else {
            photo.setClickable(false);
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
