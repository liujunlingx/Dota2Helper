package com.sanzhs.dota2helper.web;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by sanzhs on 2017/8/22.
 */

public interface GitAPI {

    @GET("users/{user}")
    Call<ResponseBody> getFeed(@Path("user")String user);

    //        try{
//            Log.i("tag","start");
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl("https://api.github.com/")
//                    .build();
//
//            GitAPI gitAPI = retrofit.create(GitAPI.class);
//
//            Call<ResponseBody> call= gitAPI.getFeed("castellanogg");
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    try {
//                        mTextMessage.setText(response.body().string());
//                        System.out.println("cool");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    Log.i("tag","onResponseDone");
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    mTextMessage.setText(t.toString());
//                    Log.i("tag","onFailureDone");
//                }
//            });
//        }catch (Exception e){
//            Log.i("tag",e.getMessage());
//        }
}
