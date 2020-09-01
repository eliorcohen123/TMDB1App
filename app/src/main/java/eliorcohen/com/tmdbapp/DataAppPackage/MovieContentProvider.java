package eliorcohen.com.tmdbapp.DataAppPackage;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import eliorcohen.com.tmdbapp.OthersPackage.ConApp;
import eliorcohen.com.tmdbapp.ViewModelsPackege.MovieViewModelFavorites;

public class MovieContentProvider extends ContentProvider {

    private MovieViewModelFavorites movieViewModelFavorites;

    @Override
    public boolean onCreate() {
        movieViewModelFavorites = new MovieViewModelFavorites(ConApp.getApplication());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = movieViewModelFavorites.getAllMoviesCursor();
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}
