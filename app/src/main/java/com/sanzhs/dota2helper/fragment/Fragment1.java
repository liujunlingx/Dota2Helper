package com.sanzhs.dota2helper.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sanzhs.dota2helper.R;
import com.sanzhs.dota2helper.adapter.ListViewAdapter;
import com.sanzhs.dota2helper.web.Dota2API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

public class Fragment1 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    //TODO 移植使用RecyclerView
    //TODO 下拉刷新
    //TODO 上拉加载更多
    //TODO 单击查看单场详细
    //TODO 关注列表
    //TODO 怎么让所有东西一次性显示出来
    //TODO 上传到github，写readme

    private int matches_requested = 10;
    private SwipeRefreshLayout refresh;
    private ListView matches;
    private List<Map<String, Object>> list = new ArrayList<>();
    private Map<Integer,String> heroMap = new HashMap<>();//key:hero_id value:heroImageUrl

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment1, container, false);

        refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(this);
        matches = (ListView) rootView.findViewById(R.id.matches);

        initHeroMap();
        getData(matches_requested,null);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initHeroMap(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Dota2API.baseUrl)
                .build();

        Dota2API dota2API = retrofit.create(Dota2API.class);

        Call<ResponseBody> call= dota2API.getHeroes(Dota2API.key);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    JSONArray heros = object.getJSONObject("result").getJSONArray("heroes");
                    for(int i = 0;i<heros.length();i++){
                        JSONObject hero = heros.getJSONObject(i);
                        heroMap.put(hero.getInt("id"),hero.getString("name"));
                    }
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

    private void getData(int matches_requested, String start_at_match_id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Dota2API.baseUrl)
                .build();

        Dota2API dota2API = retrofit.create(Dota2API.class);

        Call<ResponseBody> call= dota2API.getMatchHistory(Dota2API.key,
                Dota2API.account_id,
                String.valueOf(matches_requested),
                start_at_match_id == null?"":start_at_match_id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    JSONArray matches = object.getJSONObject("result").getJSONArray("matches");
                    List<String> matchIdList = new ArrayList<>();
                    for(int i = 0;i<matches.length();i++){
                        String matchId = matches.getJSONObject(i).getString("match_id");
                        matchIdList.add(matchId);
                    }
                    getMatchDetails(matchIdList);
                    refresh.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.err.println(t.getMessage());
            }
        });

    }

    private void getMatchDetails(List<String> matchIdList) throws JSONException {
        for(final String matchId : matchIdList){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Dota2API.baseUrl)
                    .build();

            Dota2API dota2API = retrofit.create(Dota2API.class);

            Call<ResponseBody> call= dota2API.getMatchDetails(Dota2API.key,matchId);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        int hero_id = -1,player_slot = -1,kills = -1,deaths = -1,assists = -1;

                        Map<String, Object> map = new HashMap<>();
                        map.put("matchId",matchId);
                        JSONObject result = new JSONObject(response.body().string()).getJSONObject("result");
                        JSONArray players = result.getJSONArray("players");
                        for(int i = 0;i<players.length();i++){
                            JSONObject player = players.getJSONObject(i);
                            if(player.getString("account_id").equals(Dota2API.account_id)){
                                if(player.getInt("leaver_status") != 0)
                                    return;
                                hero_id = player.getInt("hero_id");
                                player_slot = player.getInt("player_slot");
                                kills = player.getInt("kills");
                                deaths = player.getInt("deaths");
                                assists = player.getInt("assists");
                            }
                        }

                        boolean radiant_win = result.getBoolean("radiant_win");
                        long startTime = result.getLong("start_time");

                        String gameResult;
                        //player_slot is a 8bit structure
                        //the most significant bit represents team,0 for radiant,1 for dire
                        if(player_slot>=0 && player_slot<=4){
                            if(radiant_win)
                                gameResult = "胜";
                            else
                                gameResult = "负";
                        }else{
                            if(radiant_win)
                                gameResult = "负";
                            else
                                gameResult = "胜";
                        }

                        String kda = kills + "\\" + deaths + "\\" + assists;

                        map.put("heroImageUrl",heroMap.get(hero_id));
                        map.put("gameResult",gameResult);
                        map.put("startTime",startTime);
                        map.put("kda",kda);
                        list.add(map);

                        Collections.sort(list, new Comparator<Map<String, Object>>() {
                            @Override
                            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                                long startTime1 = (long) o1.get("startTime");
                                long startTime2 = (long) o2.get("startTime");
                                if(startTime1 > startTime2)
                                    return -1;
                                else
                                    return 1;
                            }
                        });

                        ListViewAdapter adapter = new ListViewAdapter(getActivity(),list);
                        Fragment1.this.matches.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.err.println(t.getMessage());
                }
            });
        }

    }

    @Override
    public void onRefresh() {
        Long lastMatchId = Long.parseLong((String) list.get(list.size()-1).get("matchId"));
        getData(matches_requested,String.valueOf(lastMatchId - 1));
    }
}
