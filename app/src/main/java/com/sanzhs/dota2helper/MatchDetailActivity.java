package com.sanzhs.dota2helper;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sanzhs.dota2helper.adapter.PlayerDetailAdapter;
import com.sanzhs.dota2helper.model.MatchDetail;
import com.sanzhs.dota2helper.util.StringUtils;
import com.sanzhs.dota2helper.web.Dota2Api;
import com.sanzhs.dota2helper.web.Dota2ApiInstance;

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

    private Toolbar toolbar;
    private TextView toolbar_title;

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

        String matchId = getIntent().getStringExtra("matchId");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_48dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String titleStr = "比赛" + matchId;
        toolbar_title.setText(titleStr);
        toolbar_title.setTextColor(Color.rgb(255,255,255));
        toolbar.setBackgroundColor(Color.rgb(0,0,0));

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

        queryMatchDetail(matchId);

    }

    private void findViewByIds(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.title);
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

                    endTime.setText(StringUtils.unixTimeStampToDate((long)matchDetail.getResult().getStart_time(),"yyyy-MM-dd"));
                    String durationStr = matchDetail.getResult().getDuration()/60 + "分钟";
                    duration.setText(durationStr);
                    gameMode.setText(StringUtils.gameModeConversion(matchDetail.getResult().getGame_mode()));

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

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}
