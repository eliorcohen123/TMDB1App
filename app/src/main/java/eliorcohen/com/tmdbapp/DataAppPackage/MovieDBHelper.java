package eliorcohen.com.tmdbapp.DataAppPackage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import eliorcohen.com.tmdbapp.ModelsPackage.Results;

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final String MOVIE_TABLE_NAME = "MOVIES";
    private static final String MOVIE_ID = "ID";
    private static final String MOVIE_TITLE = "TITLE";
    private static final String MOVIE_OVERVIEW = "OVERVIEW";
    private static final String MOVIE_URL = "URL";
    private static final String IS_WATCH = "IS_WATCH";
    private Context ctx;

    public MovieDBHelper(Context context) {
        super(context, MOVIE_TABLE_NAME, null, 1);

        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + MOVIE_TABLE_NAME + "(" +
                MOVIE_ID + " INTEGER PRIMARY KEY, " +
                MOVIE_TITLE + " TEXT, " +
                MOVIE_OVERVIEW + " TEXT, " +
                MOVIE_URL + " TEXT, " +
                IS_WATCH + " INTEGER DEFAULT 0 " + ")";
        try {
            db.execSQL(CREATE_TABLE);
        } catch (SQLiteException ex) {
            Log.e("SQLiteException", ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + MOVIE_TABLE_NAME);
        onCreate(db);
    }

    //Add movies
    public void addMovie(String title, String overview, String url) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor cursor1;
        String sql = "SELECT * FROM " + MOVIE_TABLE_NAME + " WHERE " + MOVIE_TITLE + "= '" + title + "'";
        cursor1 = db.rawQuery(sql, null);
        if (cursor1.getCount() > 0) {
            Toast.makeText(ctx, "Current movie already exist in your favorites", Toast.LENGTH_LONG).show();
        } else {
            contentValues.put(MOVIE_TITLE, title);
            contentValues.put(MOVIE_OVERVIEW, overview);
            contentValues.put(MOVIE_URL, url);

            long id = db.insertOrThrow(MOVIE_TABLE_NAME, null, contentValues);
            try {
                Log.d("MovieDBHelper", "insert new movie with id: " + id +
                        ", Name: " + title +
                        ", Overview: " + overview +
                        ", URL: " + url);
            } catch (SQLiteException ex) {
                Log.e("MovieDBHelper", ex.getMessage());
            } finally {
                db.close();
            }
        }
        cursor1.close();
    }

    //Edit Movies
    public void updateMovie(String title, String overview, String url, Integer id) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MOVIE_TITLE, title);
        values.put(MOVIE_OVERVIEW, overview);
        values.put(MOVIE_URL, url);

        int rowNumber1 = db.update(MOVIE_TABLE_NAME, values, MOVIE_ID + " = ?", new String[]{String.valueOf(id)});
        try {
            Log.d("MovieDBHelper", "update new movie with id: " + rowNumber1 +
                    ", Name: " + title);
        } catch (SQLiteException ex) {
            Log.e("MovieDBHelper", ex.getMessage());
            throw ex;
        } finally {
            db.close();
        }
    }

    //Edit Movies RadioButton
    public void updateMovieIsWatch(Results results_) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MOVIE_TITLE, results_.getTitle());
        values.put(MOVIE_OVERVIEW, results_.getOverview());
        values.put(MOVIE_URL, results_.getPoster_path());
        values.put(IS_WATCH, results_.getIs_watch());

        int rowNumber2 = db.update(MOVIE_TABLE_NAME, values, MOVIE_ID + " = ?", new String[]{String.valueOf(results_.getId())});
        try {
            Log.d("MovieDBHelper", "update new movie with id: " + rowNumber2 +
                    ", Name: " + results_);
        } catch (SQLiteException ex) {
            Log.e("movie", ex.getMessage());
            throw ex;
        } finally {
            db.close();
        }
    }

    // Delete movies
    public void deleteMovie(Results results) {

        SQLiteDatabase db = getWritableDatabase();

        String[] ids = new String[1];
        ids[0] = results.getId() + "";
        try {
            db.delete(MOVIE_TABLE_NAME, MOVIE_ID + " =? ", ids);
        } catch (SQLiteException e) {
            Log.e("MovieDBHelper", e.getMessage());
        } finally {
            db.close();
        }
    }

    // Delete all the data of the app
    public void deleteData() {

        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL("delete from " + MOVIE_TABLE_NAME);
        } catch (SQLiteException e) {
            Log.e("MovieDBHelper", e.getMessage());
        } finally {
            db.close();
        }
    }

    // Get all movies
    public ArrayList<Results> getAllMovies() {

        ArrayList<Results> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(MOVIE_TABLE_NAME, null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int colID = cursor.getColumnIndex(MOVIE_ID);
            int id = cursor.getInt(colID);
            String title = cursor.getString(1);
            String overview = cursor.getString(2);
            String poster_path = cursor.getString(3);
            int is_watch = cursor.getInt(4);
            Results results = new Results(title, overview, poster_path, is_watch);
            results.setId(id);
            arrayList.add(results);
        }
        cursor.close();
        return arrayList;
    }

    // Get all movies
    public Cursor getAllMoviesCursor() {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(MOVIE_TABLE_NAME, null, null, null, null, null, null, null);
        cursor.close();
        return cursor;
    }

}
