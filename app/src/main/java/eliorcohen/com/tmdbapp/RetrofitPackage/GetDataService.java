package eliorcohen.com.tmdbapp.RetrofitPackage;

import eliorcohen.com.tmdbapp.DataAppPackage.JSONResponse;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface GetDataService {

    @GET()
    Observable<JSONResponse> getAllPhotos(@Url String url);
}
