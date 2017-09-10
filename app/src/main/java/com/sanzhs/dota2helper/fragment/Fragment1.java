package com.sanzhs.dota2helper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nuptboyzhb.lib.SuperSwipeRefreshLayout;
import com.google.gson.Gson;
import com.sanzhs.dota2helper.MatchDetailActivity;
import com.sanzhs.dota2helper.R;
import com.sanzhs.dota2helper.adapter.MatchAdapter;
import com.sanzhs.dota2helper.model.MatchDetail;
import com.sanzhs.dota2helper.model.MatchHistory;
import com.sanzhs.dota2helper.util.StringUtils;
import com.sanzhs.dota2helper.web.Dota2Api;
import com.sanzhs.dota2helper.web.Dota2ApiInstance;

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

/**
 * Created by sanzhs on 2017/8/24.
 */

public class Fragment1 extends Fragment implements AdapterView.OnItemClickListener{

    //TODO 点击expandable content后，滑动的bug
    //TODO toolbar样式太丑，改为和原生actionBar一样
    //TODO recyclerView右边搞个进度条
    //TODO 处理没有网络的情况
    //TODO 处理卷轴物品
    //TODO 怎么让所有东西一次性显示出来

    private int matches_requested = 20;

    public enum GameResult{
        WIN("胜"),
        LOSE("负");
        public String info;
        GameResult(String info){
            this.info = info;
        }
    }

    private Toolbar toolbar;
    private SearchView searchView;
    private SuperSwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MatchAdapter adapter;
    private List<Map<String, Object>> list = new ArrayList<>();
    public static Map<Integer,String> heroMap = new HashMap<>();//key:hero_id, value:heroName eg. npc_dota_hero_riki
    public static Map<Integer,String> itemMap = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment1, container, false);
        findViewByIds(rootView);
        toolbar.setTitle("Dota2Helper");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        //searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                list.clear();
                getData(query,matches_requested,"");
                searchView.onActionViewCollapsed();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        //设置下拉刷新监听器
        swipeRefreshLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                getData(Dota2Api.account_id,matches_requested,"");
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
                        getData(Dota2Api.account_id,matches_requested,String.valueOf(lastMatchId - 1));
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

        //初始化heroMap
        initHeroMap();
        //初始化itemMap
        initItemMap();

        //获取数据
        getData(Dota2Api.account_id,matches_requested,null);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initHeroMap(){
        Call<ResponseBody> call= Dota2ApiInstance.getInstance().getDota2Api().getHeroes(Dota2Api.key);
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

    public void initItemMap(){
        Call<ResponseBody> call= Dota2ApiInstance.getInstance().getDota2Api().getItems(Dota2Api.key);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    JSONArray items = object.getJSONObject("result").getJSONArray("items");
                    for(int i = 0;i<items.length();i++){
                        JSONObject item = items.getJSONObject(i);
                        itemMap.put(item.getInt("id"),item.getString("name"));
                        //System.out.println("http://cdn.dota2.com/apps/dota2/images/items/" + item.getString("name").substring(5) + "_lg.png");
                    }
                    itemMap.put(0,"item_blank");
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

    private void getData(final String account_id, int matches_requested, String start_at_match_id){
        //getMatchHistory
        Call<ResponseBody> call= Dota2ApiInstance.getInstance().getDota2Api()
                .getMatchHistory(Dota2Api.key, account_id,
                        String.valueOf(matches_requested),
                        start_at_match_id == null?"":start_at_match_id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    MatchHistory matchHistory = gson.fromJson(response.body().string(),MatchHistory.class);
                    if(matchHistory.getResult().getStatus() != 1){
                        Snackbar.make(getView(),"数字id错误或未公开信息。",Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    List<String> matchIdList = new ArrayList<>();
                    for(MatchHistory.ResultBean.MatchesBean match : matchHistory.getResult().getMatches()){
                        String matchId = String.valueOf(match.getMatch_id());
                        matchIdList.add(matchId);
                    }
                    getMatchDetails(account_id,matchIdList);
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

    private void getMatchDetails(final String account_id, List<String> matchIdList) throws JSONException {
        for(final String matchId : matchIdList){
            Call<ResponseBody> call= Dota2ApiInstance.getInstance().getDota2Api().getMatchDetails(Dota2Api.key,matchId);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        int hero_id = -1,player_slot = -1,kills = -1,deaths = -1,assists = -1;

                        Map<String, Object> map = new HashMap<>();
                        map.put("matchId",matchId);

                        if(response.body() == null) return;
                        Gson gson = new Gson();
                        MatchDetail matchDetail = gson.fromJson(response.body().string(),MatchDetail.class);
                        for(MatchDetail.ResultBean.PlayersBean player : matchDetail.getResult().getPlayers()){
                            if(player.getAccount_id() == Long.valueOf(account_id)){
                                if(player.getLeaver_status() != 0)
                                    return;
                                hero_id = player.getHero_id();
                                player_slot = player.getPlayer_slot();
                                kills = player.getKills();
                                deaths = player.getDeaths();
                                assists = player.getAssists();
                            }
                        }

                        boolean radiant_win = matchDetail.getResult().isRadiant_win();
                        long startTime = matchDetail.getResult().getStart_time();
                        int gameMode = matchDetail.getResult().getGame_mode();

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
                        map.put("gameMode", gameMode);
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
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        searchView = (SearchView) rootView.findViewById(R.id.searchView);
        swipeRefreshLayout = (SuperSwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvMatches);
    }

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

}
