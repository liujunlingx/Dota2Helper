package com.sanzhs.dota2helper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sanzhs.dota2helper.adapter.PlayerDetailAdapter;
import com.sanzhs.dota2helper.model.MatchDetail;
import com.sanzhs.dota2helper.util.StringUtils;
import com.sanzhs.dota2helper.web.Dota2Api;
import com.sanzhs.dota2helper.web.Dota2ApiInstance;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by sanzhs on 2017/9/4.
 */

public class MatchDetailActivity extends Activity {

    private TextView endTime;
    private TextView duration;
    private TextView gameMode;

    private RecyclerView radiantPlayersRecyclerView;
    private RecyclerView direPlayersRecyclerView;
    private List<MatchDetail.ResultBean.PlayersBean> radiantList = new ArrayList<>();
    private List<MatchDetail.ResultBean.PlayersBean> direList = new ArrayList<>();
    private PlayerDetailAdapter radiantAdapter;
    private PlayerDetailAdapter direAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_match_detail);

        findViewByIds();

        radiantPlayersRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        radiantPlayersRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), OrientationHelper.VERTICAL));
        radiantPlayersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        radiantAdapter = new PlayerDetailAdapter(getApplicationContext(),radiantList);
        radiantPlayersRecyclerView.setAdapter(radiantAdapter);

        direPlayersRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        direPlayersRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), OrientationHelper.VERTICAL));
        direPlayersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        direAdapter = new PlayerDetailAdapter(getApplicationContext(),direList);
        direPlayersRecyclerView.setAdapter(direAdapter);

        String matchId = getIntent().getStringExtra("matchId");
        queryMatchDetail(matchId);

    }

    private void findViewByIds(){
        endTime = (TextView) findViewById(R.id.endTime);
        duration = (TextView) findViewById(R.id.duration);
        gameMode = (TextView) findViewById(R.id.gameMode);
        radiantPlayersRecyclerView = (RecyclerView) findViewById(R.id.radiantPlayers);
        direPlayersRecyclerView = (RecyclerView) findViewById(R.id.direPlayers);
    }

    private void queryMatchDetail(String matchId){
        Call<ResponseBody> call= Dota2ApiInstance.getInstance().getDota2Api().getMatchDetails(Dota2Api.key,matchId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    MatchDetail matchDetail = gson.fromJson(response.body().string(),MatchDetail.class);

                    //JSONObject result = new JSONObject(response.body().string()).getJSONObject("result");

                    endTime.setText(StringUtils.unixTimeStampToDate((long)matchDetail.getResult().getStart_time(),"yyyy-MM-dd"));
                    String durationStr = matchDetail.getResult().getDuration() + "秒";
                    duration.setText(durationStr);
                    gameMode.setText(gameModeConversion(matchDetail.getResult().getGame_mode()));

                    //JSONArray players = result.getJSONArray("players");
                    int radiant_score = matchDetail.getResult().getRadiant_score();
                    int dire_score = matchDetail.getResult().getDire_score();

                    int radiantTotalDamage = 0, direTotalDamage = 0;
                    for(MatchDetail.ResultBean.PlayersBean player : matchDetail.getResult().getPlayers()){
                        int player_slot = player.getPlayer_slot();
                        int hero_damage = player.getHero_damage();
                        if(player_slot <= 4){// 0 1 2 3 4 radiant,128 129 130 131 132 dire
                            radiantTotalDamage += hero_damage;
                        }else{
                            direTotalDamage += hero_damage;
                        }
                    }
//                    for(int i = 0;i<players.length();i++){
//                        JSONObject player = players.getJSONObject(i);
//                        int player_slot = player.getInt("player_slot");
//                        int hero_damage = player.getInt("hero_damage");
//                        if(player_slot <= 4){// 0 1 2 3 4 radiant,128 129 130 131 132 dire
//                            radiantTotalDamage += hero_damage;
//                        }else{
//                            direTotalDamage += hero_damage;
//                        }
//                    }

                    for(MatchDetail.ResultBean.PlayersBean player : matchDetail.getResult().getPlayers()){
                        int player_slot = player.getPlayer_slot();
                        int kills = player.getKills();
                        int assists = player.getAssists();
                        int hero_damage = player.getHero_damage();

                        if(player_slot <= 4){// 0 1 2 3 4 radiant,128 129 130 131 132 dire
                            if(radiant_score != 0)
                                player.setWarRate(new DecimalFormat("######0.0").format(((double)(kills + assists))/radiant_score*100));
                            else
                                player.setWarRate(new DecimalFormat("######0.0").format(((double)(kills + assists))/1*100));

                            player.setDamageRate(new DecimalFormat("######0.0").format((double)hero_damage/radiantTotalDamage*100));
                            radiantList.add(player);
                            radiantAdapter.notifyDataSetChanged();
                        }
                        else{
                            if(dire_score != 0)
                                player.setWarRate(new DecimalFormat("######0.0").format(((double)(kills + assists))/dire_score*100));
                            else
                                player.setWarRate(new DecimalFormat("######0.0").format(((double)(kills + assists))/1*100));

                            player.setDamageRate(new DecimalFormat("######0.0").format((double)hero_damage/direTotalDamage*100));
                            direList.add(player);
                            direAdapter.notifyDataSetChanged();
                        }
                    }

//                    for(int i = 0;i<players.length();i++){
//                        JSONObject player = players.getJSONObject(i);
//                        int player_slot = player.getInt("player_slot");
//                        int kills = player.getInt("kills");
//                        int assists = player.getInt("assists");
//                        int hero_damage = player.getInt("hero_damage");
//
//                        if(player_slot <= 4){// 0 1 2 3 4 radiant,128 129 130 131 132 dire
//                            if(radiant_score != 0)
//                                player.put("warRate",new DecimalFormat("######0.0").format(((double)(kills + assists))/radiant_score*100));
//                            else
//                                player.put("warRate",new DecimalFormat("######0.0").format(((double)(kills + assists))/1*100));
//
//                            player.put("damageRate",new DecimalFormat("######0.0").format((double)hero_damage/radiantTotalDamage*100));
//                            radiantList.add(player);
//                            radiantAdapter.notifyDataSetChanged();
//                        }
//                        else{
//                            if(dire_score != 0)
//                                player.put("warRate",new DecimalFormat("######0.0").format(((double)(kills + assists))/dire_score*100));
//                            else
//                                player.put("warRate",new DecimalFormat("######0.0").format(((double)(kills + assists))/1*100));
//
//                            player.put("damageRate",new DecimalFormat("######0.0").format((double)hero_damage/direTotalDamage*100));
//                            direList.add(player);
//                            direAdapter.notifyDataSetChanged();
//                        }
//                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private String gameModeConversion(int gameModeValue){
        switch (gameModeValue){
            case 0:
                return "None";
            case 1:
                return "全阵营选择";
            case 2:
                return "队长模式";
            case 3:
                return "随机征召";
            case 4:
                return "单一征召";
            case 5:
                return "全阵营随机";
            case 6:
                return "None";
            case 7:
                return "None";
            case 8:
                return "None";
            case 9:
                return "None";
            case 10:
                return "None";
            case 11:
                return "None";
            case 12:
                return "None";
            case 13:
                return "None";
            case 14:
                return "None";
            case 15:
                return "None";
            case 16:
                return "None";
            case 18:
                return "None";
            case 20:
                return "None";
            case 21:
                return "None";
            case 22:
                return "None";
            default:
                return "未知";
        }
    }

}
