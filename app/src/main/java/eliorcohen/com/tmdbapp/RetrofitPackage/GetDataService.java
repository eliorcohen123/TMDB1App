package eliorcohen.com.tmdbapp.RetrofitPackage;

import eliorcohen.com.tmdbapp.ModelsPackage.MovieModel;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface GetDataService {

    @GET()
    Observable<MovieModel> getAllMovies(@Url String url);
}
