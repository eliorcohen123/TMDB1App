package eliorcohen.com.tmdbapp.RetrofitPackage;

import eliorcohen.com.tmdbapp.DataAppPackage.JSONResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GetDataService {

    @GET()
    Call<JSONResponse> getAllPhotos(@Url String url);
}
