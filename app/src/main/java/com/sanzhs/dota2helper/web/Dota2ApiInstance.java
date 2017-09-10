package com.sanzhs.dota2helper.web;

import retrofit2.Retrofit;

/**
 * Created by sanzhs on 2017/9/6.
 */

public class Dota2ApiInstance {

    private static Dota2ApiInstance instance;

    private Dota2Api dota2Api;

    public static Dota2ApiInstance getInstance(){
        if(instance == null)
            instance = new Dota2ApiInstance();
        return instance;
    }

    private Dota2ApiInstance(){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Dota2Api.baseUrl)
                    .build();

            this.dota2Api = retrofit.create(Dota2Api.class);
    }

    public Dota2Api getDota2Api(){
        return dota2Api;
    }
}
