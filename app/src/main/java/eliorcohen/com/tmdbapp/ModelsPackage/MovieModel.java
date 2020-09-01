package eliorcohen.com.tmdbapp.ModelsPackage;

import android.text.TextUtils;

import java.io.Serializable;

public class MovieModel implements Serializable {

    private static String SMALL_POSTER_SIZE = "/w154";
    private static String BIG_POSTER_SIZE = "/original";
    private String poster_path;
    private String title;
    private double vote_average;
    private double popularity;
    private String release_date;
    private int vote_count;
    private String overview;
    private String original_title;
    private String original_language;
    private int id;
    private boolean video;
    private boolean adult;
    private String backdrop_path;
    private int is_watch;

    // Subject class to the SQLiteHelper
    public MovieModel(String title, String overview, String poster_path, int is_watch) {
        this.title = title;
        this.overview = overview;
        this.poster_path = poster_path;
        this.is_watch = is_watch;
    }

    public int getIs_watch() {
        return is_watch;
    }

    public void setIs_watch(int is_watch) {
        this.is_watch = is_watch;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getYearOfRelease() {
        if (!TextUtils.isEmpty(release_date)) {
            return release_date.substring(0, 4);
        } else {
            return "";
        }
    }

}
