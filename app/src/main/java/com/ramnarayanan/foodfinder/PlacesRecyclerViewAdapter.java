package com.ramnarayanan.foodfinder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Shadow on 4/5/2017.
 */

public class PlacesRecyclerViewAdapter extends RecyclerView.Adapter<PlacesRecyclerViewAdapter.PlacesViewHolder> {
    private static final String TAG = "PlacesRecyclerViewAdapt";
    //private Context mContext;
    private String[] mDataSet;

    public PlacesRecyclerViewAdapter(String[] dataset) {
        this.mDataSet = dataset;
    }

    @Override
    public PlacesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_item, parent, false);

        return new PlacesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlacesViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Element" + position + " set.");
        holder.thumbnail.setImageResource(R.drawable.placeholder);
        holder.title.setText(mDataSet[position]);

        /*
        if((mPhotosList==null)||mPhotosList.size()==0){
            holder.thumbnail.setImageResource(R.drawable.placeholder);
            holder.title.setText(R.string.empty_photo);
        }else{
            Photo photoItem = mPhotosList.get(position);
            Log.d(TAG, "onBindViewHolder: " + photoItem.getTitle() + "->" + position);
            Picasso.with(mContext).load(photoItem.getImage())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.thumbnail);

            holder.title.setText(photoItem.getTitle());
        }
        */
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    static class PlacesViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "PlacesViewHolder";
        ImageView thumbnail = null;
        TextView title = null;

        public PlacesViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "PlacesViewHolder: starts");
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
