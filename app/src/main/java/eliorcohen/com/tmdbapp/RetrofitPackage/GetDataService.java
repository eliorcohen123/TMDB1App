package eliorcohen.com.tmdbapp.RetrofitPackage;

import eliorcohen.com.tmdbapp.ModelsPackage.JSONResponseModel;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface GetDataService {

    @GET()
    Observable<JSONResponseModel> getAllMovies(@Url String url);
}
