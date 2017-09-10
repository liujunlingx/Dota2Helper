package com.sanzhs.dota2helper.web;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by sanzhs on 2017/9/8.
 */

public interface PubgApi {

    String baseUrl = "https://pubgtracker.com/";
    String key =  "7f3227bb-b61a-4dc3-9389-0a9aeddb9ae0";

    @GET("api/profile/pc/{pubg-nickname}")
    Call<ResponseBody> getPlayerStats(@Header("TRN-Api-Key")String key,@Path("pubg-nickname")String name);
}
