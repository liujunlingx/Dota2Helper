package com.sanzhs.dota2helper.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.nuptboyzhb.lib.SuperSwipeRefreshLayout;
import com.google.gson.Gson;
import com.sanzhs.dota2helper.R;
import com.sanzhs.dota2helper.adapter.NewsAdapter;
import com.sanzhs.dota2helper.model.Dota2News;
import com.sanzhs.dota2helper.web.Dota2Api;
import com.sanzhs.dota2helper.web.Dota2ApiInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sanzhs on 2017/8/24.
 */

public class Fragment3 extends Fragment {

    //TODO 下拉加载更多

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
        Call<ResponseBody> call= Dota2ApiInstance.getInstance().getDota2Api().getNewsForApp(Dota2Api.key, Dota2Api.appId,10);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(response.body() == null) return;
                    Gson gson = new Gson();
                    Dota2News news = gson.fromJson(response.body().string(),Dota2News.class);
                    for(Dota2News.AppnewsBean.NewsitemsBean newsItem : news.getAppnews().getNewsitems()){
                        Map<String, Object> map = new HashMap<>();
                        map.put("title",newsItem.getTitle());
                        map.put("content",newsItem.getContents());
                        map.put("author",newsItem.getAuthor());
                        map.put("date",(long)newsItem.getDate());
                        adapter.add(map);
                    }

                    swipeRefreshLayout.setRefreshing(false);
                }  catch (Exception e) {
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
