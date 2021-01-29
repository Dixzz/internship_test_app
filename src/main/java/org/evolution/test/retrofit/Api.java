package org.evolution.test.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    @GET("region/asia")
    Call<ResponseBody> getDetails();
}
