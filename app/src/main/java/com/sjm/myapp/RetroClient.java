package com.sjm.myapp;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by Jitesh Dalsaniya on 24-Nov-16.
 */

public class RetroClient {
    private static final String ROOT_URL = "http://surfinternet.in/webservice/";
    private static Retrofit getRetrofitInstance(){
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .client(clientBuilder.build())
                .addConverterFactory(new ToStringConverterFactory())
                .build();
    }

    public static ApiService getApiService(){
        return getRetrofitInstance().create(ApiService.class);
    }
}
