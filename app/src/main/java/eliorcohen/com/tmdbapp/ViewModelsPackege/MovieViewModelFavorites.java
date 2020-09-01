package eliorcohen.com.tmdbapp.ViewModelsPackege;

import android.app.Application;
import android.database.Cursor;

import java.util.ArrayList;

import androidx.lifecycle.AndroidViewModel;

import eliorcohen.com.tmdbapp.DataAppPackage.MovieDBHelper;
import eliorcohen.com.tmdbapp.ModelsPackage.MovieModel;

public class MovieViewModelFavorites extends AndroidViewModel {

    private MovieDBHelper movieDBHelper;  // The SQLiteHelper of the app

    public MovieViewModelFavorites(Application application) {
        super(application);

        movieDBHelper = new MovieDBHelper(application);
    }

    public ArrayList<MovieModel> getAllMovies() {
        return movieDBHelper.getAllMovies();
    }

    public void insertMovie(String title, String overview, String url) {
        movieDBHelper.addMovie(title, overview, url);
    }

    public void deleteAll() {
        movieDBHelper.deleteData();
    }

    public void deleteMovie(MovieModel places) {
        movieDBHelper.deleteMovie(places);
    }

    public void updateMovie(String title, String overview, String url, int id) {
        movieDBHelper.updateMovie(title, overview, url, id);
    }

    public void updateMovieIsWatch(MovieModel movieModel_) {
        movieDBHelper.updateMovieIsWatch(movieModel_);
    }

    public Cursor getAllMoviesCursor() {
        return movieDBHelper.getAllMoviesCursor();
    }


}
