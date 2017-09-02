package com.sanzhs.dota2helper.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.nuptboyzhb.lib.SuperSwipeRefreshLayout;
import com.sanzhs.dota2helper.R;
import com.sanzhs.dota2helper.adapter.NewsAdapter;
import com.sanzhs.dota2helper.web.Dota2API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by sanzhs on 2017/8/24.
 */

public class Fragment3 extends Fragment {

    private SuperSwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<Map<String, Object>> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment3, container, false);
        findViewByIds(rootView);

        //下拉刷新
        swipeRefreshLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                getData();
            }

            @Override
            public void onPullDistance(int i) {

            }

            @Override
            public void onPullEnable(boolean b) {

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), OrientationHelper.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new NewsAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);

        getData();

        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void findViewByIds(View rootView){
        swipeRefreshLayout = (SuperSwipeRefreshLayout) rootView.findViewById(R.id.newsSwipeRefreshLayout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.newsRecyclerView);
    }

    private void getData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Dota2API.baseUrl)
                .build();

        Dota2API dota2API = retrofit.create(Dota2API.class);

        Call<ResponseBody> call= dota2API.getNewsForApp(Dota2API.key,Dota2API.appId,10);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(response.body() == null) return;
                    JSONObject appnews = new JSONObject(response.body().string()).getJSONObject("appnews");
                    JSONArray newsItems = appnews.getJSONArray("newsitems");
                    for(int i = 0;i<newsItems.length();i++){
                        Map<String, Object> map = new HashMap<>();

                        JSONObject newsItem = newsItems.getJSONObject(i);
                        map.put("title",newsItem.getString("title"));
                        map.put("content",newsItem.getString("contents"));
                        map.put("author",newsItem.getString("author"));
                        map.put("date",newsItem.getLong("date"));
                        adapter.add(map);
                    }
                    swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
