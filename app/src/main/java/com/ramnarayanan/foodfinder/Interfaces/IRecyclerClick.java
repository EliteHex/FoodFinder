package com.ramnarayanan.foodfinder.Interfaces;

import android.view.View;

/**
 * Created by Shadow on 4/7/2017.
 */

public interface IRecyclerClick {
    void onItemClick(View view, int position);

    void onItemLongClick(View view, int position);
}
