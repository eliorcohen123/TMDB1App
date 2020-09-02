package eliorcohen.com.tmdbapp.PagesPackage;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eliorcohen.com.tmdbapp.CustomAdaptersPackage.CustomAdapterInternet;
import eliorcohen.com.tmdbapp.InterfacesPackage.SearchMovieInterface;
import eliorcohen.com.tmdbapp.ModelsPackage.Results;
import eliorcohen.com.tmdbapp.OthersPackage.ItemDecoration;
import eliorcohen.com.tmdbapp.RetrofitPackage.GetDataService;
import eliorcohen.com.tmdbapp.ModelsPackage.MovieModel;
import eliorcohen.com.tmdbapp.R;
import eliorcohen.com.tmdbapp.RetrofitPackage.RetrofitClientInstance;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchMovieFromInternetActivity extends AppCompatActivity implements SearchMovieInterface, View.OnClickListener {

    private static ArrayList<Results> mMovieListInternet;  // ArrayList of MovieModel
    private CustomAdapterInternet mAdapterInternet;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private ItemDecoration itemDecoration;
    private ImageView imagePre, imageNext, imagePreFirst;
    private int myPage = 1, sumPage;
    private String myStringQuery;
    private TextView textPage;
    private MediaPlayer sSearch, sMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_movie_internet);

        initUI();
        initListeners();
    }

    private void initUI() {
        recyclerView = findViewById(R.id.recyclerViewInternet);
        imagePre = findViewById(R.id.imagePre);
        imageNext = findViewById(R.id.imageNext);
        imagePreFirst = findViewById(R.id.imagePreFirst);
        textPage = findViewById(R.id.textPage);

        progressDialog = new ProgressDialog(this);

        imagePre.setVisibility(View.GONE);
        imageNext.setVisibility(View.GONE);
        imagePreFirst.setVisibility(View.GONE);
        textPage.setVisibility(View.GONE);

        sSearch = MediaPlayer.create(SearchMovieFromInternetActivity.this, R.raw.search_and_refresh_sound);
        sMove = MediaPlayer.create(SearchMovieFromInternetActivity.this, R.raw.cancel_and_move_sound);
    }

    private void initListeners() {
        imageNext.setOnClickListener(this);
        imagePre.setOnClickListener(this);
        imagePreFirst.setOnClickListener(this);
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
                    sSearch.start();  // Play sound

                    startProgressDialog();

                    myPage = 1;

                    textPage.setVisibility(View.VISIBLE);

                    myStringQuery = query;

                    getPage(myStringQuery);
                    getPageText(myPage);
                    getSumPage(myStringQuery, myPage);
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

    private void getMyData(String query, int page) {
        GetDataService apiService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Observable<MovieModel> observable = apiService.getAllMovies("/3/search/movie?/&query="
                + query +
                "&api_key=" + getString(R.string.key_search) + "&language=en-US&page=" + page).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Observer<MovieModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(MovieModel products) {
                generateDataList(Arrays.asList(products.getResults()));

                stopProgressDialog();
            }
        });
    }

    private void getSumPage(String query, int page) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.themoviedb.org/3/search/movie?/&query="
                + query +
                "&api_key=" + getString(R.string.key_search) + "&language=en-US&page=" + page, response -> {
            try {
                JSONObject mainObj = new JSONObject(response);
                sumPage = mainObj.getInt("total_pages");
                getCheckMaxPage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {

        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getPage(String query) {
        if (myPage <= 0) {
            myPage = 1;
        } else {
            getMyData(query, myPage);
        }

        if (myPage == 1) {
            imagePre.setVisibility(View.GONE);
        } else {
            imagePre.setVisibility(View.VISIBLE);
        }

        if (myPage > 2) {
            imagePreFirst.setVisibility(View.VISIBLE);
        } else {
            imagePreFirst.setVisibility(View.GONE);
        }
    }

    private void getPageText(int page) {
        textPage.setText(String.valueOf(page));
    }

    private void getCheckMaxPage() {
        if (myPage == sumPage) {
            imageNext.setVisibility(View.GONE);
        } else if (myPage < sumPage) {
            imageNext.setVisibility(View.VISIBLE);
        }
    }

    // Options in the activity_menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:  // Start search
                break;
            case R.id.backSearch:  // Back to the previous activity
                sMove.start();  // Play sound

                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*Method to generate List of data using RecyclerView with custom mAdapterInternet*/
    private void generateDataList(List<Results> photoList) {
        mMovieListInternet = new ArrayList<Results>(photoList);
        mAdapterInternet = new CustomAdapterInternet(this, mMovieListInternet);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (itemDecoration == null) {
            itemDecoration = new ItemDecoration(20);
            recyclerView.addItemDecoration(itemDecoration);
        }
        recyclerView.setAdapter(mAdapterInternet);
    }

    @Override
    public void startProgressDialog() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    @Override
    public void stopProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageNext:
                myPage++;
                performBtn(myPage);
                break;
            case R.id.imagePre:
                myPage--;
                performBtn(myPage);
                break;
            case R.id.imagePreFirst:
                myPage = 1;
                performBtn(myPage);
                break;
        }
    }

    private void performBtn(int page) {
        sMove.start();  // Play sound

        getPage(myStringQuery);
        getPageText(page);
        getSumPage(myStringQuery, page);
        getCheckMaxPage();
    }

}
