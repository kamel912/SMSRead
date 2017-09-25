package com.team_vii.smsread;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by MK on 9/24/2017.
 */

interface ApiInterface {
    @GET("Prod")
    Call<ResponseFromServer> getMessages(@Query("Clientid") String clientId,
                                         @Query("Type") String type,
                                         @Query("Number") String number,
                                         @Query("Message") String message);
}
