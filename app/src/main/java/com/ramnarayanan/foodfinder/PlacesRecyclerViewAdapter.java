package com.ramnarayanan.foodfinder;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Shadow on 4/5/2017.
 */

public class PlacesRecyclerViewAdapter extends RecyclerView.Adapter<PlacesRecyclerViewAdapter.PlacesViewHolder> {
    private static final String TAG = "PlacesRecyclerViewAdapt";
    private static final String placePictureAPI = "https://maps.googleapis.com/maps/api/place/photo?";
    private static final String APIKEY = "AIzaSyDSqwO8QnOMqty5laLxP6tEnzZ9P70tBDk";

    //private Context mContext;
    private List<MapPlace> mDataSet;
//    private List<MapPlace> mPlaceList;

    public PlacesRecyclerViewAdapter(List<MapPlace> dataset) {
        this.mDataSet = dataset;
    }

    void loadNewData(List<MapPlace> placesList) {
        if (placesList.isEmpty()) return;

        mDataSet = placesList;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
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

        if (mDataSet == null || mDataSet.size() == 0) {
            holder.thumbnail.setImageResource(R.drawable.placeholder);
            holder.title.setText(String.valueOf(position));
        } else {
            MapPlace placeItem = mDataSet.get(position);

            holder.thumbnail.setImageResource(R.drawable.placeholder);
            if (placeItem.photoreference != "") {
                StringBuilder searchstring = new StringBuilder(placePictureAPI);
                searchstring.append("maxwidth=400&maxheight=400")
                        .append("&photoreference=" + placeItem.photoreference)
                        .append("&key=" + APIKEY);
                Uri googleUri = Uri.parse(searchstring.toString());
                Picasso.with(holder.itemView.getContext())
                        .load(googleUri)
                        .into(holder.thumbnail);
            }

            holder.title.setText(placeItem.placeName);
        }

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
        return mDataSet.size();
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
