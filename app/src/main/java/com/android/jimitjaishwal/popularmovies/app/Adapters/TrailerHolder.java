package com.android.jimitjaishwal.popularmovies.app.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.jimitjaishwal.popularmovies.app.R;

/**
 * Created by Jimit Jaishwal on 7/28/2016.
 */
public class TrailerHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public ImageButton button;


    public TrailerHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.trailer_Image);
        button = (ImageButton) itemView.findViewById(R.id.trailer_pause_button);

    }
}
