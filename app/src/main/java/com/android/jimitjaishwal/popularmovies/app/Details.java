package com.android.jimitjaishwal.popularmovies.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.jimitjaishwal.popularmovies.app.Models.TrailerModel;
import com.android.jimitjaishwal.popularmovies.app.NetworkServise.MovieService;
import com.android.jimitjaishwal.popularmovies.app.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Details extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String base_url = "http://api.themoviedb.org/3/";
    private ArrayList<TrailerModel> trailers;
    private int CURSOR_LOADER_ID = 0;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, Settings.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getTrailers(String trailerKey) {
        String videoBase_url = "https://www.youtube.com/watch?v=" + trailerKey;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoBase_url));
        startActivity(intent);
    }

    public void getMovieTrailers(Cursor c) {

        ImageView imageView = (ImageView) findViewById(R.id.backDrop_path);
        final String posterPath = "http://image.tmdb.org/t/p/w780" + c.getString(COLUMN_BACKDROP_PATH);
        Picasso.with(getApplication()).load(posterPath).into(imageView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieService service = retrofit.create(MovieService.class);
        Call<TrailerModel.TrailerResponse> call = service.getTrailerResponse(c.getInt(COLUMN_MOVIE_ID));
        call.enqueue(new Callback<TrailerModel.TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerModel.TrailerResponse> call, Response<TrailerModel.TrailerResponse> response) {
                trailers = response.body().getResults();

                for (final TrailerModel model : trailers) {
                    ImageButton imageButton = (ImageButton) findViewById(R.id.play_button);
                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getTrailers(model.getKey());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<TrailerModel.TrailerResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Intent intent = this.getIntent();

        return new CursorLoader(this,
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
            getMovieTrailers(data);

            data.close();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
