package com.android.jimitjaishwal.popularmovies.app.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jimitjaishwal.popularmovies.app.R;

/**
 * Created by Jimit Jaishwal on 7/25/2016.
 */
public class MovieHolder extends RecyclerView.ViewHolder {

    public ImageView posterView;
    public TextView titleView;
    public TextView movieTypeView;
    public ImageButton favourite_button;
    public LinearLayout linearLayout;
    public View view;

    public MovieHolder(View view) {
        super(view);
        posterView = (ImageView) view.findViewById(R.id.poster_path);
        titleView = (TextView) view.findViewById(R.id.original_title);
        movieTypeView = (TextView) view.findViewById(R.id.movie_type);
        favourite_button = (ImageButton) view.findViewById(R.id.favourite_button);
        linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout);
        this.view = view;
    }
}
