package com.android.jimitjaishwal.popularmovies.app.MainScreen;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.jimitjaishwal.popularmovies.app.Adapters.MovieAdapter;
import com.android.jimitjaishwal.popularmovies.app.Adapters.RecyclerItemClickListener;
import com.android.jimitjaishwal.popularmovies.app.Details;
import com.android.jimitjaishwal.popularmovies.app.MovieDbService.FetchMovieTask;
import com.android.jimitjaishwal.popularmovies.app.R;
import com.android.jimitjaishwal.popularmovies.app.Utility;
import com.android.jimitjaishwal.popularmovies.app.data.MovieContract;

import static com.android.jimitjaishwal.popularmovies.app.data.MovieContract.FavouriteMovie;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private MovieAdapter adapter;

    public static final int CURSOR_LOADER_ID = 0;
    RecyclerView recyclerView;

    public static final String[] COLUMNS = {
            FavouriteMovie.TABLE_NAME + "." + FavouriteMovie._ID,
            FavouriteMovie.COLUMN_MOVIE_ID,
            FavouriteMovie.COLUMN_BACKDROP_PATH,
            FavouriteMovie.COLUMN_POSTER_PATH,
            FavouriteMovie.COLUMN_OVERVIEW,
            FavouriteMovie.COLUMN_RELEASE_DATE,
            FavouriteMovie.COLUMN_ORIGINAL_TITLE,
            FavouriteMovie.COLUMN_VOTE_AVERAGE
    };

    public static final int _ID = 0;
    public static final int COLUMN_MOVIE_ID = 1;
    public static final int COLUMN_BACKDROP_PATH = 2;
    public static final int COLUMN_POSTER_PATH = 3;
    public static final int COLUMN_OVERVIEW = 4;
    public static final int COLUMN_RELEASE_DATE = 5;
    public static final int COLUMN_ORIGINAL_TITLE = 6;
    public static final int COLUMN_VOTE_AVERAGE = 7;


    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMovies();
    }


    public void getMovies() {
        final String sort_by = Utility.getMovies_sort_by(getActivity());
        final FetchMovieTask movieTask = new FetchMovieTask(getActivity());
        movieTask.execute(sort_by);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.movie_item_recyclerView);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        return rootView;
    }

    void onMovieSortByChanged() {
        getMovies();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(
                getActivity(),
                FavouriteMovie.BASE_URI,
                COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {

        if (data != null) {
            adapter = new MovieAdapter(getActivity(), null);
            recyclerView.setAdapter(adapter);
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {
                            Intent intent = new Intent(getActivity(), Details.class);
                            intent.setData(MovieContract.FavouriteMovie.buildMovieWithID(data.getInt(COLUMN_MOVIE_ID)));
                            startActivity(intent);
                        }
                    }) {
            });
            adapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
