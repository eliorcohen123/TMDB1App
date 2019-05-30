package eliorcohen.com.tmdbapp.MainAndOtherPackage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import eliorcohen.com.tmdbapp.AsyncTaskPackage.GetMoviesAsyncTaskManually;
import eliorcohen.com.tmdbapp.CustomAdapterPackage.MovieCustomAdapterMain;
import eliorcohen.com.tmdbapp.DataAppPackage.MovieDBHelper;
import eliorcohen.com.tmdbapp.DataAppPackage.MovieModel;
import eliorcohen.com.tmdbapp.LoginPackage.UsersListActivity;
import eliorcohen.com.tmdbapp.MoviesDataPackage.AddMovie;
import eliorcohen.com.tmdbapp.MoviesDataPackage.DataOfMovie;
import eliorcohen.com.tmdbapp.MoviesDataPackage.DeleteAllData;
import eliorcohen.com.tmdbapp.MoviesDataPackage.DeleteMovie;
import eliorcohen.com.tmdbapp.MoviesDataPackage.EditMovie;
import eliorcohen.com.tmdbapp.MoviesDataPackage.SearchMovieFromInternet;
import eliorcohen.com.tmdbapp.R;

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

    private ArrayList<MovieModel> mMovieList;  // ArrayList of MovieModel
    private MovieCustomAdapterMain mAdapter;  // CustomAdapter of MainActivity
    private GetMoviesAsyncTaskManually mGetMoviesAsyncTaskManually;  // AsyncTask for AddMovie to add movie to MainActivity
    private ListView mListView;  // ListView of MainActivity
    private MovieDBHelper mMovieDBHelper;  // The SQLiteHelper of the app
    private SwipeRefreshLayout swipeRefreshLayout;  // SwipeRe freshLayout of MainActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        getData();
        listLongClick();
    }

    private void initUI() {
        mListView = findViewById(R.id.listViewMain);  // ID of the ListView of MainActivity
        swipeRefreshLayout = findViewById(R.id.swipe_container);  // ID of the SwipeRefreshLayout of MainActivity

        registerForContextMenu(mListView);  // Sets off the menu in MainActivity

        AppRater.app_launched(this);
    }

    private void getData() {
        if (!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();

        mMovieDBHelper = new MovieDBHelper(this);  // Put the SQLiteHelper in MainActivity
        mMovieList = mMovieDBHelper.getAllMovies();  // Put the getAllMovies of SQLiteHelper in the ArrayList of MainActivity
        mAdapter = new MovieCustomAdapterMain(this, mMovieList);  // Comparing the ArrayList of MainActivity to the CustomAdapter

        // Put AsyncTask in the ListView of MainActivity to execute the SQLiteHelper
        mGetMoviesAsyncTaskManually = new GetMoviesAsyncTaskManually(mListView);
        mGetMoviesAsyncTaskManually.execute(mMovieDBHelper);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorOrange));  // Colors of the SwipeRefreshLayout of MainActivity
        // Refresh the MovieDBHelper of app in ListView of MainActivity
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mGetMoviesAsyncTaskManually = new GetMoviesAsyncTaskManually(mListView);
                mGetMoviesAsyncTaskManually.execute(mMovieDBHelper);

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

                MediaPlayer sSearch = MediaPlayer.create(MainActivity.this, R.raw.search_and_refresh_sound);
                sSearch.start();  // Play sound

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void listLongClick() {
        // Put extra from MainActivity to EditMovie and pass from MainActivity to EditMovie with the put extra when you click on item in ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaPlayer sMove = MediaPlayer.create(MainActivity.this, R.raw.cancel_and_move_sound);
                sMove.start();  // Play sound

                Intent intent = new Intent(MainActivity.this, DataOfMovie.class);
                intent.putExtra(getString(R.string.movie_id), mMovieList.get(position).getId());
                intent.putExtra(getString(R.string.movie_edit), mMovieList.get(position));
                startActivity(intent);
            }
        });
    }

    // Sets off the menu of activity_menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Sets off the menu of list_menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.list_menu, menu);
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

                MediaPlayer sUnMute = MediaPlayer.create(MainActivity.this, R.raw.cancel_and_move_sound);
                sUnMute.start();  // Play sound

                Toast toastUnMute = Toast.makeText(MainActivity.this, "The sound are on!", Toast.LENGTH_SHORT);
                View viewUnMute = toastUnMute.getView();
                viewUnMute.getBackground().setColorFilter(getResources().getColor(R.color.colorLightBlue), PorterDuff.Mode.SRC_IN);
                TextView textUnMute = viewUnMute.findViewById(android.R.id.message);
                textUnMute.setTextColor(getResources().getColor(R.color.colorBrown));
                toastUnMute.show();  // Toast
                break;
            case R.id.addManually:  // Pass from MainActivity to AddMovie for add movies
                MediaPlayer sMove = MediaPlayer.create(MainActivity.this, R.raw.cancel_and_move_sound);
                sMove.start();  // Play sound

                Intent intentAddManually = new Intent(MainActivity.this, AddMovie.class);
                startActivity(intentAddManually);
                break;
            case R.id.addFromInternet:  // Pass from MainActivity to SearchMovieFromInternet for search movies
                MediaPlayer sMoveInternet = MediaPlayer.create(MainActivity.this, R.raw.cancel_and_move_sound);
                sMoveInternet.start();  // Play sound

                Intent intentAddFromInternet = new Intent(MainActivity.this, SearchMovieFromInternet.class);
                startActivity(intentAddFromInternet);
                break;
            case R.id.credits:  // Credits of the creator of the app
                MediaPlayer sMoveCredits = MediaPlayer.create(MainActivity.this, R.raw.cancel_and_move_sound);
                sMoveCredits.start();  // Play sound

                Intent intentCredits = new Intent(MainActivity.this, Credits.class);
                startActivity(intentCredits);
                break;
            case R.id.accounts:
                MediaPlayer sAccounts = MediaPlayer.create(MainActivity.this, R.raw.cancel_and_move_sound);
                sAccounts.start();  // Play sound

                Intent intentAccounts = new Intent(MainActivity.this, UsersListActivity.class);
                startActivity(intentAccounts);
                break;
            case R.id.deleteAllData:  // Delete all data of the app for delete all the data of the app
                MediaPlayer sMoveDeleteData = MediaPlayer.create(MainActivity.this, R.raw.cancel_and_move_sound);
                sMoveDeleteData.start();  // Play sound

                Intent intentDeleteAllData = new Intent(MainActivity.this, DeleteAllData.class);
                startActivity(intentDeleteAllData);
                break;
            case R.id.shareIntentApp:
                MediaPlayer sMoveShare = MediaPlayer.create(MainActivity.this, R.raw.cancel_and_move_sound);
                sMoveShare.start();  // Play sound

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=eliorcohen.com.tmdbapp");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.exit:  // Exit from the app
                MediaPlayer sExit = MediaPlayer.create(MainActivity.this, R.raw.exit_sound);
                sExit.start();  // Play sound

                ActivityCompat.finishAffinity(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Options in the list_menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int listPosition = info.position;
        switch (item.getItemId()) {
            case R.id.edit:  // Edit the movies on MainActivity
                MediaPlayer sMove = MediaPlayer.create(MainActivity.this, R.raw.cancel_and_move_sound);
                sMove.start();  // Play sound

                Intent intent = new Intent(MainActivity.this, EditMovie.class);
                intent.putExtra(getString(R.string.movie_id), mMovieList.get(listPosition).getId());
                intent.putExtra(getString(R.string.movie_edit), mMovieList.get(listPosition));
                startActivity(intent);
                break;
            case R.id.shareIntent:  // Share the content of movie
                MediaPlayer sSave = MediaPlayer.create(MainActivity.this, R.raw.cancel_and_move_sound);
                sSave.start();  // Play sound

                String title = mMovieList.get(listPosition).getTitle();
                String overview = mMovieList.get(listPosition).getOverview();
                String URL = "https://image.tmdb.org/t/p/original" + mMovieList.get(listPosition).getPoster_path();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Title: " + title + "\nOverview: " + overview + "\nURL: " + URL);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.delete:  // Delete item(movie) on MainActivity
                MediaPlayer sDelete = MediaPlayer.create(MainActivity.this, R.raw.delete_sound);
                sDelete.start();  // Play sound

                mGetMoviesAsyncTaskManually.deleteMovie(listPosition);
                mMovieDBHelper.deleteMovie(mMovieList.get(listPosition));

                Intent intentDeleteData = new Intent(MainActivity.this, DeleteMovie.class);
                startActivity(intentDeleteData);
                break;
        }
        return super.onContextItemSelected(item);
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
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder;
    }

}
