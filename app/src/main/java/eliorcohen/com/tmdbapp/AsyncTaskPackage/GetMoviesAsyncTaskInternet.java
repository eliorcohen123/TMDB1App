package eliorcohen.com.tmdbapp.AsyncTaskPackage;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import eliorcohen.com.tmdbapp.DataAppPackage.MovieModel;
import eliorcohen.com.tmdbapp.MoviesDataPackage.SearchMovieFromInternet;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetMoviesAsyncTaskInternet extends AsyncTask<String, Integer, ArrayList<MovieModel>> {

    // startShowingProgressBar of SearchMovieFromInternet
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SearchMovieFromInternet.startShowingProgressBar();
    }

    // DoInBackground of the JSON
    @Override
    protected ArrayList<MovieModel> doInBackground(String... urls) {

        OkHttpClient client = new OkHttpClient();
        String urlQuery = urls[0];
        Request request = new Request.Builder()
                .url(urlQuery)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert response != null;
        if (!response.isSuccessful()) try {
            throw new IOException("Unexpected code " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert response.body() != null;
            return getMoviesListFromJson(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get movies from the JSON
    private ArrayList<MovieModel> getMoviesListFromJson(String jsonResponse) {

        List<MovieModel> stubMovieData = new ArrayList<MovieModel>();
        Gson gson = new GsonBuilder().create();
        MovieResponse response = gson.fromJson(jsonResponse, MovieResponse.class);
        stubMovieData = response.results;
        ArrayList<MovieModel> arrList = new ArrayList<>();
        arrList.addAll(stubMovieData);

        return arrList;
    }

    //The response of the JSON
    public class MovieResponse {
        List<MovieModel> results;

        public MovieResponse() {
            results = new ArrayList<MovieModel>();
        }
    }

    // execute the following:
    @Override
    protected void onPostExecute(ArrayList<MovieModel> movieModels) {
        super.onPostExecute(movieModels);
        SearchMovieFromInternet.stopShowingProgressBar();  // StopShowingProgressBar of SearchMovieFromInternet
        SearchMovieFromInternet.setMovies(movieModels);  // set movies in SearchMovieFromInternet
    }

}
