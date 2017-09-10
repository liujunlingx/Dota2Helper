package com.sanzhs.dota2helper.web;

import retrofit2.Retrofit;

/**
 * Created by sanzhs on 2017/9/8.
 */

public class PubgApiInstance {

    private static PubgApiInstance instance;

    private PubgApi pubgApi;

    public static PubgApiInstance getInstance(){
        if(instance == null)
            instance = new PubgApiInstance();
        return instance;
    }

    private PubgApiInstance(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PubgApi.baseUrl)
                .build();

        this.pubgApi = retrofit.create(PubgApi.class);
    }

    public PubgApi getPubgApi(){
        return pubgApi;
    }
}
