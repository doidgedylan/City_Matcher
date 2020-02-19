package com.example.city_matcher;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class RecyclerViewAdapter {
    private static final String TAG = "RecyclerViewAdapter";

    // holds views in memory ready to add the next ones
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
