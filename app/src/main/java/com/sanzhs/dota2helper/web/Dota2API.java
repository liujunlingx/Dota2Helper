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
    /**
     * hero
     * http://cdn.dota2.com/apps/dota2/images/heroes/HERONAME_SUFFIX
     * suffix:
     * sb.png: 59x33px small horizontal portrait
     * lg.png: 205x105px large horizontal portrait
     * full.png: 256x144px full-quality horizontal portrait
     * vert.jpg: 235x272px full-quality vertical portrait (note that this is a .jpg)
     */

    /**
     * item
     * http://cdn.dota2.com/apps/dota2/images/items/ITEMNAME_lg.png
     */
    String imageUrl = "http://cdn.dota2.com/";

    int appId = 570;
    String key = "FA00A197B3CC052163DCF5C5B691009F";
    String account_id = "136647688";
//    String account_id = "76561198096913416";

    //GetMatchHistory
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

    //GetNewsForApp
    @GET("ISteamNews/GetNewsForApp/v0002")
    Call<ResponseBody> getNewsForApp(@Query("key")String key,
                                     @Query("appid")int appid,
                                     @Query("count")int count);

}
