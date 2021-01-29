package org.evolution.test.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URl = "https://restcountries.eu/rest/v2/"; // TODO: 2/2/2021 CHANGE_IF_RQD

    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }


    public Api getApi() {
        return retrofit.create(Api.class);
    }
}