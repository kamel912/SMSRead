package com.team_vii.smsread;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by MK on 9/24/2017.
 */

class ApiClient {
    private static final String BASE_URL = "https://1ryh8tvl74.execute-api.us-east-1.amazonaws.com/";
    private static Retrofit retrofit ;

    static Retrofit getApiClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().
                    baseUrl(BASE_URL).
                    addConverterFactory(GsonConverterFactory.create()).
                    build();
        }
        return retrofit;
    }
}
