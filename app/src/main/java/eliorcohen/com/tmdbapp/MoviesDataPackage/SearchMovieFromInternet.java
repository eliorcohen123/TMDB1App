package eliorcohen.com.tmdbapp.MoviesDataPackage;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eliorcohen.com.tmdbapp.CustomAdapterPackage.MovieCustomAdapterInternet;
import eliorcohen.com.tmdbapp.DataAppPackage.MovieModel;
import eliorcohen.com.tmdbapp.MainAndOtherPackage.ItemDecoration;
import eliorcohen.com.tmdbapp.RetrofitPackage.GetDataService;
import eliorcohen.com.tmdbapp.DataAppPackage.JSONResponse;
import eliorcohen.com.tmdbapp.R;
import eliorcohen.com.tmdbapp.RetrofitPackage.RetrofitClientInstance;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchMovieFromInternet extends AppCompatActivity {

    private static ArrayList<MovieModel> mMovieListInternet;  // ArrayList of MovieModel
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
        recyclerView = findViewById(R.id.recyclerViewInternet);

        progressDialog = new ProgressDialog(SearchMovieFromInternet.this);
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

                    progressDialog.setMessage("Loading...");
                    progressDialog.show();

                    GetDataService apiService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

                    Observable<JSONResponse> observable = apiService.getAllPhotos("/3/search/movie?/&query="
                            + query +
                            "&api_key=4e0be2c22f7268edffde97481d49064a&language=en-US").subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread());
                    observable.subscribe(new Observer<JSONResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(JSONResponse products) {
                            mMovieListInternet = new ArrayList<MovieModel>(Arrays.asList(products.getResults()));
                            generateDataList(mMovieListInternet);
                            progressDialog.dismiss();
                        }
                    });
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
