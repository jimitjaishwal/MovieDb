package com.android.jimitjaishwal.popularmovies.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.jimitjaishwal.popularmovies.app.Models.TrailerModel;
import com.android.jimitjaishwal.popularmovies.app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Jimit Jaishwal on 7/28/2016.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerHolder> {

    Context mContext;
    public ArrayList<TrailerModel> trailers;

    public TrailerAdapter(Context mContext, ArrayList<TrailerModel> trailers){
        this.mContext = mContext;
        this.trailers = trailers;
    }

    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(mContext).inflate(R.layout.movie_trailer_item, parent, false);

        return new TrailerHolder(rootView);
    }


    @Override
    public void onBindViewHolder(final TrailerHolder holder, int position) {

        final TrailerModel trailer = trailers.get(position);

        String youtubeImage = "http://img.youtube.com/vi/" +
               trailer.getKey() + "/0.jpg";
        Picasso.with(mContext).load(youtubeImage).into(holder.imageView);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTrailers(trailer.getKey());
            }
        });
    }


    @Override
    public int getItemCount() {
        return trailers.size();
    }


    public void getTrailers(String trailerKey) {
        String videoBase_url = "https://www.youtube.com/watch?v=" + trailerKey;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoBase_url));
        mContext.startActivity(intent);}
    }