package com.sanzhs.dota2helper.web;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sanzhs on 2017/8/29.
 */

public interface Dota2API {

    String baseUrl = "http://api.steampowered.com/";
    String imageUrl = "http://cdn.dota2.com/";
    String key = "FA00A197B3CC052163DCF5C5B691009F";
    String account_id = "136647688";
//    String account_id = "76561198096913416";

    //GetMatchHistory
//    @GET("IDOTA2Match_570/GetMatchHistory/v1")
//    Call<ResponseBody> getMatchHistory(@Query("key")String key,
//                                       @Query("account_id")String account_id,
//                                       @Query("matches_requested")String matches_requested);

    @GET("IDOTA2Match_570/GetMatchHistory/v1")
    Call<ResponseBody> getMatchHistory(@Query("key")String key,
                                       @Query("account_id")String account_id,
                                       @Query("matches_requested")String matches_requested,
                                       @Query("start_at_match_id")String start_at_match_id);

    //GetMatchDetails
    @GET("IDOTA2Match_570/GetMatchDetails/v1")
    Call<ResponseBody> getMatchDetails(@Query("key")String key,
                                       @Query("match_id")String match_id);

    //GetHeroes
    @GET("IEconDOTA2_570/GetHeroes/v1")
    Call<ResponseBody> getHeroes(@Query("key")String key);

//    //GetHeroImage
//    @GET("apps/dota2/images/heroes/{HERONAME_SUFFIX}")
//    Call<ResponseBody> getHeroImage(@Path("HERONAME_SUFFIX")String heroname_suffix);
}
