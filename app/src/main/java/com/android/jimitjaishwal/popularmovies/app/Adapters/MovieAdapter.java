package com.android.jimitjaishwal.popularmovies.app.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.jimitjaishwal.popularmovies.app.MainScreen.MainActivityFragment;
import com.android.jimitjaishwal.popularmovies.app.R;
import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

/**
 * Created by Jimit Jaishwal on 7/25/2016.
 */
public class MovieAdapter extends CursorRecyclerViewAdapter<MovieHolder> {

    private Context mContext;

    public MovieAdapter(Context mContext, Cursor cursor) {
        super(mContext, cursor);
        this.mContext = mContext;
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, final Cursor cursor) {
        final String posterPath = "http://image.tmdb.org/t/p/w185" +
                cursor.getString(MainActivityFragment.COLUMN_POSTER_PATH);
        String original_title = cursor.getString(MainActivityFragment.COLUMN_ORIGINAL_TITLE);
        holder.titleView.setText(original_title);

        Picasso.with(mContext).load(posterPath).resize(185,278)
                .into(holder.posterView, PicassoPalette
                        .with(posterPath, holder.posterView)
                        .use(PicassoPalette.Profile.MUTED_DARK)
                        .intoBackground(holder.linearLayout));
    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(mContext).inflate(R.layout.card_movie_item, parent, false);
        return new MovieHolder(rootView);
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        return super.swapCursor(newCursor);
    }
}
