package eliorcohen.com.tmdbapp.MoviesDataPackage;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import eliorcohen.com.tmdbapp.AsyncTaskPackage.GetMoviesAsyncTaskInternet;
import eliorcohen.com.tmdbapp.CustomAdapterPackage.MovieCustomAdapterInternet;
import eliorcohen.com.tmdbapp.DataAppPackage.MovieDBHelper;
import eliorcohen.com.tmdbapp.DataAppPackage.MovieModel;
import eliorcohen.com.tmdbapp.R;

public class SearchMovieFromInternet extends AppCompatActivity {

    private static ArrayList<MovieModel> mMovieListInternet;  // ArrayList of MovieModel
    private static MovieCustomAdapterInternet mAdapterInternet;  // CustomAdapter of SearchMovieFromInternet
    private static ListView mListViewInternet;  // ListView of SearchMovieFromInternet
    private GetMoviesAsyncTaskInternet mGetMoviesAsyncTaskInternet;  // AsyncTask to search and add movies from SearchMovieFromInternet to MainActivity
    private MovieDBHelper mMovieDBHelperInternet;  // The SQLiteHelper of the app
    private static ProgressDialog mProgressDialogInternet;  // ProgressDialog
    private static SearchMovieFromInternet mSearchMovieFromInternet;  // Initialize SearchMovieFromInternet

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_movie_internet);

        mListViewInternet = findViewById(R.id.list2);  // ID of the ListView of MainActivity

        mSearchMovieFromInternet = this;  // Put SearchMovieFromInternet in SearchMovieFromInternet

        mMovieDBHelperInternet = new MovieDBHelper(this);  // Put the SQLiteHelper in SearchMovieFromInternet
        mMovieListInternet = mMovieDBHelperInternet.getAllMovies();  // Put the getAllMovies of SQLiteHelper in the ArrayList of SearchMovieFromInternet
        mAdapterInternet = new MovieCustomAdapterInternet(this, mMovieListInternet);  // Comparing the ArrayList of SearchMovieFromInternet to the CustomAdapter
        registerForContextMenu(mListViewInternet);  // Sets off the menu in SearchMovieFromInternet

        // Put extra from SearchMovieFromInternet to EditMovie and pass from SearchMovieFromInternet to EditMovie with the put extra when you click on item in ListView
        mListViewInternet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaPlayer sMove = MediaPlayer.create(SearchMovieFromInternet.this, R.raw.cancel_and_move_sound);
                sMove.start();  // Play sound

                Intent intentSearchToAddInternet = new Intent(SearchMovieFromInternet.this, AddMovieFromInternet.class);
                intentSearchToAddInternet.putExtra(getString(R.string.movie_add_from_internet), mMovieListInternet.get(position));
                startActivity(intentSearchToAddInternet);
            }
        });
    }


    // Set movies in SearchMovieFromInternet
    public static void setMovies(ArrayList<MovieModel> list) {
        mMovieListInternet = list;
        mAdapterInternet.clear();
        mAdapterInternet.addAll(list);
        mListViewInternet.setAdapter(mAdapterInternet);
    }

    // Sets off the menu of activity_menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu_internet, menu);

        // SearchView of SearchMovieFromInternet
        final MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        // Change colors of the searchView upper panel
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Set styles for expanded state here
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Set styles for collapsed state here
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                return true;
            }
        });

        // Continued of SearchView of SearchMovieFromInternet
        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menuItem.getActionView();
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(Html.fromHtml("<font color = #FFEA54>" + getResources().getString(R.string.hint) + "</font>"));
            searchView.setSubmitButtonEnabled(true);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    MediaPlayer sSearch = MediaPlayer.create(SearchMovieFromInternet.this, R.raw.search_and_refresh_sound);
                    sSearch.start();  // Play sound

                    // Search movies from that URL and put them in the SQLiteHelper
                    String urlQuery = "https://api.themoviedb.org/3/search/movie?/&query=" + query + "&api_key=4e0be2c22f7268edffde97481d49064a&language=en-US&page=";
                    mGetMoviesAsyncTaskInternet = new GetMoviesAsyncTaskInternet();
                    mGetMoviesAsyncTaskInternet.execute(urlQuery);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    // Options in the activity_menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:  // Start search
                break;
            case R.id.backSearch:  // Back to the previous activity
                MediaPlayer sCancel = MediaPlayer.create(SearchMovieFromInternet.this, R.raw.cancel_and_move_sound);
                sCancel.start();  // Play sound

                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // stopShowingProgressBar
    public static void stopShowingProgressBar() {
        if (mProgressDialogInternet != null) {
            mProgressDialogInternet.dismiss();
            mProgressDialogInternet = null;
        }
    }

    // startShowingProgressBar
    public static void startShowingProgressBar() {
        mProgressDialogInternet = ProgressDialog.show(mSearchMovieFromInternet, "Loading...",
                "Please wait...", true);
        mProgressDialogInternet.show();
    }

}
