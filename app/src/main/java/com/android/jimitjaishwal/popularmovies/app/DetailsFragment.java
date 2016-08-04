package com.android.jimitjaishwal.popularmovies.app;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jimitjaishwal.popularmovies.app.Adapters.TrailerAdapter;
import com.android.jimitjaishwal.popularmovies.app.Models.TrailerModel;
import com.android.jimitjaishwal.popularmovies.app.NetworkServise.MovieService;
import com.android.jimitjaishwal.popularmovies.app.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private TrailerAdapter adapter;
    public View rootView;
    ArrayList<TrailerModel> movieTrailers;
    private static final int LOADER_MANAGER_ID = 0;

    public static final String[] COLUMNS = {
            MovieContract.FavouriteMovie.TABLE_NAME + "." + MovieContract.FavouriteMovie._ID,
            MovieContract.FavouriteMovie.COLUMN_MOVIE_ID,
            MovieContract.FavouriteMovie.COLUMN_BACKDROP_PATH,
            MovieContract.FavouriteMovie.COLUMN_POSTER_PATH,
            MovieContract.FavouriteMovie.COLUMN_OVERVIEW,
            MovieContract.FavouriteMovie.COLUMN_RELEASE_DATE,
            MovieContract.FavouriteMovie.COLUMN_ORIGINAL_TITLE,
            MovieContract.FavouriteMovie.COLUMN_VOTE_AVERAGE
    };

    public static final int _ID = 0;
    public static final int COLUMN_MOVIE_ID = 1;
    public static final int COLUMN_BACKDROP_PATH = 2;
    public static final int COLUMN_POSTER_PATH = 3;
    public static final int COLUMN_OVERVIEW = 4;
    public static final int COLUMN_RELEASE_DATE = 5;
    public static final int COLUMN_ORIGINAL_TITLE = 6;
    public static final int COLUMN_VOTE_AVERAGE = 7;

    public DetailsFragment() {
    }

    public class DetailViewHolder {
        public ImageView posterView;
        public TextView titleView;
        public TextView releaseDate;
        public TextView voteAverage;
        public TextView overView;
        public RecyclerView recyclerView;

        public DetailViewHolder(View rootView) {
            posterView = (ImageView) rootView.findViewById(R.id.detail_poster);
            recyclerView = (RecyclerView) rootView.findViewById(R.id.trailer_recyclerView);
            RecyclerView.LayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(manager);
            titleView = (TextView) rootView.findViewById(R.id.detail_original_title);
            releaseDate = (TextView) rootView.findViewById(R.id.detail_release_date);
            voteAverage = (TextView) rootView.findViewById(R.id.vote_average);
            overView = (TextView) rootView.findViewById(R.id.overView);
        }
    }

    public ArrayList<TrailerModel> getMovieTrailerIntoDatabase(Cursor c) {

        movieTrailers = new ArrayList<>();

        if (c.moveToFirst()) {
            TrailerModel trailer = new TrailerModel();
           do {
               String key = c.getString(c.getColumnIndex(MovieContract.MovieTrailers.COLUMN_TRAILER_KEY));
               trailer.setKey(key);

               int movieId = c.getInt(c.getColumnIndex(MovieContract.MovieTrailers.COLUMN_MOVIE_ID));
               trailer.setId(Integer.toString(movieId));

               String trailerName = c.getString(c.getColumnIndex(MovieContract.MovieTrailers.COLUMN_TRAILER_NAME));
               trailer.setName(trailerName);
               movieTrailers.add(trailer);
           }while (c.moveToNext());
        }
        c.close();
        return movieTrailers;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_details, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_MANAGER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }

        return new CursorLoader(
                getActivity(),
                intent.getData(),
                COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            DetailViewHolder viewHolder = new DetailViewHolder(rootView);
            int movieId = data.getInt(COLUMN_MOVIE_ID);

            FetchMovieTrailers movieTrailers = new FetchMovieTrailers(getActivity());
            movieTrailers.execute(movieId);

            Cursor c = getActivity().getContentResolver().query(
                    MovieContract.MovieTrailers.BASE_URI,
                    null,
                    null,
                    null,
                    null);

            ArrayList<TrailerModel> trailer = getMovieTrailerIntoDatabase(c);
            adapter = new TrailerAdapter(getActivity(), trailer);
            viewHolder.recyclerView.setAdapter(adapter);

            final String posterPath = "http://image.tmdb.org/t/p/w185" + data.getString(COLUMN_POSTER_PATH);
            Picasso.with(getActivity()).load(posterPath).into(viewHolder.posterView);

            String originalTitle = data.getString(COLUMN_ORIGINAL_TITLE);
            viewHolder.titleView.setText(originalTitle);

            String overView = data.getString(COLUMN_OVERVIEW);
            viewHolder.overView.setText(overView);

            String release_date = data.getString(COLUMN_RELEASE_DATE);
            viewHolder.releaseDate.setText(release_date);

            double vote_average = data.getDouble(COLUMN_VOTE_AVERAGE);
            viewHolder.voteAverage.setText(Double.toString(vote_average));
            data.close();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public class FetchMovieTrailers extends AsyncTask<Integer, Void, ArrayList<TrailerModel>> {

        private String LOG_TAG = FetchMovieTrailers.class.getSimpleName();
        private static final String base_url = "http://api.themoviedb.org/3/";
        Context mContext;
        ArrayList<TrailerModel> trailers;

        public FetchMovieTrailers(Context mContext) {
            this.mContext = mContext;
        }

        public long addMovieTrailerIntoDatabase(int movieId, String trailerName, String trailerKey) {
            long id;
            Cursor trailerCursor = mContext.getContentResolver().query(
                    MovieContract.MovieTrailers.BASE_URI,
                    new String[]{MovieContract.MovieTrailers._ID},
                    MovieContract.FavouriteMovie.COLUMN_MOVIE_ID + "=?",
                    new String[]{Integer.toString(movieId)},
                    null
            );

            if (trailerCursor.moveToFirst()) {
                int columnIndex = trailerCursor.getColumnIndex(MovieContract.MovieTrailers._ID);
                id = trailerCursor.getLong(columnIndex);
            } else {

                ContentValues values = new ContentValues();
                values.put(MovieContract.MovieTrailers.COLUMN_MOVIE_ID, movieId);
                values.put(MovieContract.MovieTrailers.COLUMN_TRAILER_NAME, trailerName);
                values.put(MovieContract.MovieTrailers.COLUMN_TRAILER_KEY, trailerKey);

                Uri buildTrailerUri = mContext.getContentResolver()
                        .insert(MovieContract.MovieTrailers.BASE_URI,
                                values);

                id = ContentUris.parseId(buildTrailerUri);
                Log.d(LOG_TAG, "Trailers Table Rows " + id + " Rows inserted");

            }
            trailerCursor.close();
            return id;
        }

        @Override
        protected ArrayList<TrailerModel> doInBackground(Integer... results) {

            try {
                int movieId = results[0];
                Log.d(LOG_TAG, "doInBackground: " + movieId);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(base_url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                MovieService service = retrofit.create(MovieService.class);
                Call<TrailerModel.TrailerResponse> call = service.getTrailerResponse(movieId);
                Response<TrailerModel.TrailerResponse> response = call.execute();
                TrailerModel.TrailerResponse TrailerResponse = response.body();
                trailers = TrailerResponse.getResults();
                for (TrailerModel trailer : trailers) {

                    String trailerName = trailer.getName();
                    Log.d(LOG_TAG, "doInBackground: " + trailerName);
                    String trailerKey = trailer.getKey();

                    Log.d(LOG_TAG, "doInBackground: "+ trailerKey);
                    addMovieTrailerIntoDatabase(movieId, trailerName, trailerKey);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return trailers;
        }

        @Override
        protected void onPostExecute(ArrayList<TrailerModel> trailerModels) {
            if (trailerModels != null) {
                for (TrailerModel model : trailerModels) {
                    movieTrailers.add(model);
                }
            }
        }
    }

}
