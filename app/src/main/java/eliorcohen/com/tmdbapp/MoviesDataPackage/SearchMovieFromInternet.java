package eliorcohen.com.tmdbapp.MoviesDataPackage;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eliorcohen.com.tmdbapp.CustomAdapterPackage.MovieCustomAdapterInternet;
import eliorcohen.com.tmdbapp.DataAppPackage.MovieDBHelper;
import eliorcohen.com.tmdbapp.DataAppPackage.MovieModel;
import eliorcohen.com.tmdbapp.MainAndOtherPackage.ItemDecoration;
import eliorcohen.com.tmdbapp.RetrofitPackage.GetDataService;
import eliorcohen.com.tmdbapp.DataAppPackage.JSONResponse;
import eliorcohen.com.tmdbapp.R;
import eliorcohen.com.tmdbapp.RetrofitPackage.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMovieFromInternet extends AppCompatActivity {

    private static ArrayList<MovieModel> mMovieListInternet;  // ArrayList of MovieModel
    //    private static MovieCustomAdapterInternet mAdapterInternet;  // MovieCustomAdapterInternet of SearchMovieFromInternet
//    private static ListView mListViewInternet;  // ListView of SearchMovieFromInternet
    //    private GetMoviesAsyncTaskInternet mGetMoviesAsyncTaskInternet;  // AsyncTask to search and add movies from SearchMovieFromInternet to MainActivity
    private MovieDBHelper mMovieDBHelperInternet;  // The SQLiteHelper of the app
    private static ProgressDialog mProgressDialogInternet;  // ProgressDialog
    private static SearchMovieFromInternet mSearchMovieFromInternet;
    private MovieCustomAdapterInternet adapter;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_movie_internet);

        initUI();
    }

    private void initUI() {
        recyclerView = findViewById(R.id.myRecyclerView);

        mSearchMovieFromInternet = this;  // Put SearchMovieFromInternet in SearchMovieFromInternet
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
        final androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(Html.fromHtml("<font color = #FFEA54>" + getResources().getString(R.string.hint) + "</font>"));
            searchView.setSubmitButtonEnabled(true);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    MediaPlayer sSearch = MediaPlayer.create(SearchMovieFromInternet.this, R.raw.search_and_refresh_sound);
                    sSearch.start();  // Play sound

                    progressDialog = new ProgressDialog(SearchMovieFromInternet.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();

                    /*Create handle for the RetrofitInstance interface*/
                    GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                    Call<JSONResponse> call = service.getAllPhotos("/3/search/movie?/&query="
                            + query +
                            "&api_key=4e0be2c22f7268edffde97481d49064a&language=en-US");
                    call.enqueue(new Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                            progressDialog.dismiss();
                            JSONResponse jsonResponse = response.body();
                            mMovieListInternet = new ArrayList<MovieModel>(Arrays.asList(jsonResponse.getResults()));
                            generateDataList(mMovieListInternet);
                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(SearchMovieFromInternet.this, "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Search movies from that URL and put them in the SQLiteHelper
                    SharedPreferences.Editor editor = getSharedPreferences("total_query", MODE_PRIVATE).edit();
                    editor.putString("query", query);
                    editor.apply();
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

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList(List<MovieModel> photoList) {
        adapter = new MovieCustomAdapterInternet(this, photoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemDecoration itemDecoration = new ItemDecoration(20);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(adapter);
    }

}
