package eliorcohen.com.tmdbapp.AsyncTaskPackage;

import android.os.AsyncTask;
import android.widget.ListView;

import java.util.ArrayList;

import eliorcohen.com.tmdbapp.CustomAdapterPackage.MovieCustomAdapterMain;
import eliorcohen.com.tmdbapp.DataAppPackage.MovieDBHelper;
import eliorcohen.com.tmdbapp.DataAppPackage.MovieModel;

public class GetMoviesAsyncTaskManually extends AsyncTask<MovieDBHelper, Integer, ArrayList<MovieModel>> {

    private ListView mListView;  // Initialize of ListView
    private MovieCustomAdapterMain mMovieCustomAdapterMain;  // Initialize of MovieCustomAdapterMain
    private ArrayList<MovieModel> mMoviesList;  // Initialize of ArrayList of MovieModel

    // AsyncTask to the ListView
    public GetMoviesAsyncTaskManually(ListView list) {
        mListView = list;
    }

    // DoInBackground of the ArrayList of MovieModel that put the getAllMovies in the SQLiteHelper in the ArrayList of MovieModel
    @Override
    protected ArrayList<MovieModel> doInBackground(MovieDBHelper... movieDBHelpers) {
        MovieDBHelper myDb = movieDBHelpers[0];
        mMoviesList = myDb.getAllMovies();

        return mMoviesList;
    }

    // Delete movie
    public void deleteMovie(int moviePosition_) {
        mMovieCustomAdapterMain.remove(mMoviesList.get(moviePosition_));
    }

    // execute to add movies manually
    @Override
    protected void onPostExecute(ArrayList<MovieModel> movieModels) {
        super.onPostExecute(movieModels);
        mMovieCustomAdapterMain = new MovieCustomAdapterMain(mListView.getContext(), movieModels);
        mListView.setAdapter(mMovieCustomAdapterMain);
    }

}
