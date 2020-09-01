package eliorcohen.com.tmdbapp.RetrofitPackage;

import eliorcohen.com.tmdbapp.ModelsPackage.Results;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface GetDataService {

    @GET()
    Observable<Results> getAllMovies(@Url String url);
}
