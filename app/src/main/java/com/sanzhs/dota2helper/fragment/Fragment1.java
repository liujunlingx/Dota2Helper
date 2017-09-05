package com.sanzhs.dota2helper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nuptboyzhb.lib.SuperSwipeRefreshLayout;
import com.sanzhs.dota2helper.MatchDetailActivity;
import com.sanzhs.dota2helper.R;
import com.sanzhs.dota2helper.adapter.MatchAdapter;
import com.sanzhs.dota2helper.web.Dota2API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
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

public class Fragment1 extends Fragment implements AdapterView.OnItemClickListener{

    //TODO 单场详细加个toolbar，左边返回，中间比赛id
    //TODO recyclerView右边搞个进度条
    //TODO 处理没有网络的情况
    //TODO 单击查看单场详细
    //TODO 搜索id
    //TODO 关注列表
    //TODO 怎么让所有东西一次性显示出来

    private int matches_requested = 20;

    public enum GameResult{
        WIN("赢一手"),
        LOSE("抱头鼠窜");
        public String info;
        GameResult(String info){
            this.info = info;
        }
    }

    private SuperSwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MatchAdapter adapter;
    private List<Map<String, Object>> list = new ArrayList<>();
    public static Map<Integer,String> heroMap = new HashMap<>();//key:hero_id, value:heroName eg. npc_dota_hero_riki

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()){
            case R.id.hero:
                Toast.makeText(getActivity(),"hero clicked,match id: " + list.get(position).get("matchId"),Toast.LENGTH_LONG).show();
                break;
            case R.id.gameResult:
                Toast.makeText(getActivity(),"gameResult clicked",Toast.LENGTH_SHORT).show();
                break;
            default:
                Intent intent = new Intent();
                intent.putExtra("matchId",(String) list.get(position).get("matchId"));
                intent.setClass(getContext(), MatchDetailActivity.class);
                startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment1, container, false);
        findViewByIds(rootView);

        //设置下拉刷新监听器
        swipeRefreshLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                int count = list.size();
                list.clear();
                getData(count,"");
            }

            @Override
            public void onPullDistance(int i) {

            }

            @Override
            public void onPullEnable(boolean b) {

            }
        });
        //设置上拉加载更多监听器
        swipeRefreshLayout.setOnPushLoadMoreListener(new SuperSwipeRefreshLayout.OnPushLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        int oldSize = list.size();
                        Long lastMatchId = Long.parseLong((String) list.get(oldSize-1).get("matchId"));
                        getData(matches_requested,String.valueOf(lastMatchId - 1));
                        swipeRefreshLayout.setLoadMore(false);
                    }
                }, 3000);
            }

            @Override
            public void onPushDistance(int i) {

            }

            @Override
            public void onPushEnable(boolean b) {

            }
        });
        swipeRefreshLayout.setFooterView(createFooterView());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), OrientationHelper.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new MatchAdapter(getActivity(),list,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                System.out.println();
            }
        });

        //初始化heroMap
        initHeroMap();

        //获取数据
        getData(matches_requested,null);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initHeroMap(){
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

                        if(response.body() == null) return;
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
                                gameResult = GameResult.WIN.info;
                            else
                                gameResult = GameResult.LOSE.info;
                        }else{
                            if(radiant_win)
                                gameResult = GameResult.LOSE.info;
                            else
                                gameResult = GameResult.WIN.info;
                        }

                        String kdaValue = new DecimalFormat("######0.00").format(((double)(kills + assists))/deaths);
                        String kda = kills + "\\" + deaths + "\\" + assists;

                        map.put("heroName",heroMap.get(hero_id));
                        map.put("gameResult",gameResult);
                        map.put("startTime",startTime);
                        map.put("kdaValue",kdaValue);
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

                        Fragment1.this.adapter.notifyDataSetChanged();
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

    private View createFooterView() {
        View footerView = LayoutInflater.from(swipeRefreshLayout.getContext())
                .inflate(R.layout.item_foot, null);
        TextView textView = (TextView) footerView.findViewById(R.id.footText);
        textView.setText("٩(๑❛ᴗ❛๑)۶  I'm loading  ٩(๑❛ᴗ❛๑)۶");
        return footerView;
    }

    private void findViewByIds(View rootView){
        swipeRefreshLayout = (SuperSwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvMatches);
    }

}
