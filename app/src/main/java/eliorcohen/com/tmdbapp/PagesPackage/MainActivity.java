package eliorcohen.com.tmdbapp.PagesPackage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import eliorcohen.com.tmdbapp.CustomAdaptersPackage.CustomAdapterMain;
import eliorcohen.com.tmdbapp.ModelsPackage.Results;
import eliorcohen.com.tmdbapp.ViewModelsPackege.MovieViewModelFavorites;
import eliorcohen.com.tmdbapp.OthersPackage.ConApp;
import eliorcohen.com.tmdbapp.OthersPackage.ItemDecoration;
import eliorcohen.com.tmdbapp.R;
import guy4444.smartrate.SmartRate;

/*
 **
 ***
 ****
 ***** Created by Elior Cohen on 30/05/2019.
 ****
 ***
 **
 */

public class MainActivity extends AppCompatActivity {

    private ArrayList<Results> mMovieListMain;  // ArrayList of MovieModel
    private CustomAdapterMain mAdapterMain;  // MovieCustomAdapterInternet of MainActivity
    private MovieViewModelFavorites movieViewModelFavorites;
    private SwipeRefreshLayout swipeRefreshLayout;  // SwipeRe freshLayout of MainActivity
    private RecyclerView recyclerView;
    private ItemDecoration itemDecoration;
    private MediaPlayer sSearch, sMove, sExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initAppRater();
        refreshData();
        myRecyclerView();
        getData();
    }

    private void initUI() {
        recyclerView = findViewById(R.id.recyclerViewMain);  // ID of the RecyclerView of MainActivity
        swipeRefreshLayout = findViewById(R.id.swipe_container);  // ID of the SwipeRefreshLayout of MainActivity

        movieViewModelFavorites = new MovieViewModelFavorites(ConApp.getApplication());
        mMovieListMain = new ArrayList<>();

        sSearch = MediaPlayer.create(MainActivity.this, R.raw.search_and_refresh_sound);
        sMove = MediaPlayer.create(MainActivity.this, R.raw.cancel_and_move_sound);
        sExit = MediaPlayer.create(MainActivity.this, R.raw.exit_sound);
    }

    private void initAppRater() {
        SmartRate.Rate(MainActivity.this
                , "Rate Us"
                , "Tell others what you think about this app"
                , "Continue"
                , "Please take a moment and rate us on Google Play"
                , "click here"
                , "Ask me later"
                , "Never ask again"
                , "Cancel"
                , "Thanks for the feedback"
                , Color.parseColor("#2196F3")
                , 5
                , 1
                , 1
        );
    }

    private void refreshData() {
        if (!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorOrange));  // Colors of the SwipeRefreshLayout of MainActivity
        // Refresh the MovieDBHelper of app in RecyclerView of MainActivity
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Vibration for 0.1 second
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(100);
                }

                finish();
                startActivity(getIntent());  // Refresh activity

                Toast toast = Toast.makeText(MainActivity.this, "The list are refreshed!", Toast.LENGTH_SHORT);
                View view = toast.getView();
                view.getBackground().setColorFilter(getResources().getColor(R.color.colorLightBlue), PorterDuff.Mode.SRC_IN);
                TextView text = view.findViewById(android.R.id.message);
                text.setTextColor(getResources().getColor(R.color.colorBrown));
                toast.show();  // Toast

                sSearch.start();  // Play sound

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void myRecyclerView() {
        mAdapterMain = new CustomAdapterMain(this, mMovieListMain);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (itemDecoration == null) {
            itemDecoration = new ItemDecoration(20);
            recyclerView.addItemDecoration(itemDecoration);
        }
        recyclerView.setAdapter(mAdapterMain);
    }

    // This method is to fetch all user records from SQLite
    private void getData() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                mMovieListMain.clear();
                mMovieListMain.addAll(movieViewModelFavorites.getAllMovies());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mAdapterMain.notifyDataSetChanged();
            }
        }.execute();
    }

    // Sets off the menu of activity_menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Options in the activity_menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mute:  // Mute all the sound in app
                AudioManager managerYes = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                managerYes.setStreamMute(AudioManager.STREAM_MUSIC, true);

                Toast toastMute = Toast.makeText(MainActivity.this, "The sound are mute!", Toast.LENGTH_SHORT);
                View viewMute = toastMute.getView();
                viewMute.getBackground().setColorFilter(getResources().getColor(R.color.colorLightBlue), PorterDuff.Mode.SRC_IN);
                TextView textMute = viewMute.findViewById(android.R.id.message);
                textMute.setTextColor(getResources().getColor(R.color.colorBrown));
                toastMute.show();  // Toast
                break;
            case R.id.unMute:  // Allow all the sound in app
                AudioManager managerNo = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                managerNo.setStreamMute(AudioManager.STREAM_MUSIC, false);

                sMove.start();  // Play sound

                Toast toastUnMute = Toast.makeText(MainActivity.this, "The sound are on!", Toast.LENGTH_SHORT);
                View viewUnMute = toastUnMute.getView();
                viewUnMute.getBackground().setColorFilter(getResources().getColor(R.color.colorLightBlue), PorterDuff.Mode.SRC_IN);
                TextView textUnMute = viewUnMute.findViewById(android.R.id.message);
                textUnMute.setTextColor(getResources().getColor(R.color.colorBrown));
                toastUnMute.show();  // Toast
                break;
            case R.id.addManually:  // Pass from MainActivity to AddMovie for add movies
                sMove.start();  // Play sound

                Intent intentAddManually = new Intent(MainActivity.this, AddMovieActivity.class);
                startActivity(intentAddManually);
                break;
            case R.id.addFromInternet:  // Pass from MainActivity to SearchMovieFromInternet for search movies
                sMove.start();  // Play sound

                Intent intentAddFromInternet = new Intent(MainActivity.this, SearchMovieFromInternetActivity.class);
                startActivity(intentAddFromInternet);
                break;
            case R.id.credits:  // Credits of the creator of the app
                sMove.start();  // Play sound

                Intent intentCredits = new Intent(MainActivity.this, CreditsActivity.class);
                startActivity(intentCredits);
                break;
            case R.id.accounts:
                sMove.start();  // Play sound

                Intent intentAccounts = new Intent(MainActivity.this, UsersListActivity.class);
                startActivity(intentAccounts);
                break;
            case R.id.deleteAllData:  // Delete all data of the app for delete all the data of the app
                sMove.start();  // Play sound

                Intent intentDeleteAllData = new Intent(MainActivity.this, DeleteAllDataActivity.class);
                startActivity(intentDeleteAllData);
                break;
            case R.id.shareIntentApp:
                sMove.start();  // Play sound

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=eliorcohen.com.tmdbapp");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.exit:  // Exit from the app
                sExit.start();  // Play sound

                ActivityCompat.finishAffinity(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else
            return false;
    }

    private AlertDialog.Builder buildDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi To use the Internet services. Press ok to Resume");
        builder.setPositiveButton("Ok", (dialog, which) -> {
        });
        return builder;
    }

}
