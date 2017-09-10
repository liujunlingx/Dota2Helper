package com.sanzhs.dota2helper.adapter;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sanzhs.dota2helper.R;
import com.sanzhs.dota2helper.fragment.Fragment1;
import com.sanzhs.dota2helper.model.MatchDetail;
import com.sanzhs.dota2helper.util.CircleTransform;
import com.sanzhs.dota2helper.util.StringUtils;
import com.sanzhs.dota2helper.web.Dota2Api;
import com.sanzhs.dota2helper.web.Dota2ApiInstance;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sanzhs on 2017/9/4.
 */

public class PlayerDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<MatchDetail.ResultBean.PlayersBean> data;
    private Context context;

    public PlayerDetailAdapter(Context context,List<MatchDetail.ResultBean.PlayersBean> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_player_detail, parent,
                false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        MatchDetail.ResultBean.PlayersBean player = data.get(position);

        try {
            String heroName = Fragment1.heroMap.get(player.getHero_id());
            Picasso.with(context)
                    .load("file:///android_asset/heros/" + heroName.substring(14) + "_full.png")
                    .resize(177,99)
                    .into(myViewHolder.hero);

            myViewHolder.level.setText(String.valueOf(player.getLevel()));

            String account_id = String.valueOf(player.getAccount_id());
            setProfileData(myViewHolder,account_id);

            String warRate = "参战率:" + player.getWarRate() + "%";
            myViewHolder.warRate.setText(warRate);
            String damageRate = "伤害:" + player.getDamageRate() + "%";
            myViewHolder.damageRate.setText(damageRate);

            int kills,deaths,assists;
            kills = player.getKills();
            deaths = player.getDeaths();
            assists = player.getAssists();
            myViewHolder.kda.setText(kills + "/" + deaths + "/" + assists);
            myViewHolder.kdaValue.setText(new DecimalFormat("######0.00").format(((double)(kills + assists))/deaths));

            Picasso.with(context)
                    .load("file:///android_asset/items/" + Fragment1.itemMap.get(player.getItem_0()).substring(5) + "_lg.png")
                    .resize(64,64)
                    .into(myViewHolder.item_0);

            Picasso.with(context)
                    .load("file:///android_asset/items/" + Fragment1.itemMap.get(player.getItem_1()).substring(5) + "_lg.png")
                    .resize(64,64)
                    .into(myViewHolder.item_1);

            Picasso.with(context)
                    .load("file:///android_asset/items/" + Fragment1.itemMap.get(player.getItem_2()).substring(5) + "_lg.png")
                    .resize(64,64)
                    .into(myViewHolder.item_2);

            Picasso.with(context)
                    .load("file:///android_asset/items/" + Fragment1.itemMap.get(player.getItem_3()).substring(5) + "_lg.png")
                    .resize(64,64)
                    .into(myViewHolder.item_3);

            Picasso.with(context)
                    .load("file:///android_asset/items/" + Fragment1.itemMap.get(player.getItem_4()).substring(5) + "_lg.png")
                    .resize(64,64)
                    .into(myViewHolder.item_4);

            Picasso.with(context)
                    .load("file:///android_asset/items/" + Fragment1.itemMap.get(player.getItem_5()).substring(5) + "_lg.png")
                    .resize(64,64)
                    .into(myViewHolder.item_5);

            //Expandable content
            String heroDamage = "英雄伤害:" + player.getHero_damage();
            myViewHolder.heroDamage.setText(heroDamage);
            Picasso.with(context)
                    .load("file:///android_asset/items/" + Fragment1.itemMap.get(player.getBackpack_0()).substring(5) + "_lg.png")
                    .resize(64,64)
                    .into(myViewHolder.backpack_0);

            Picasso.with(context)
                    .load("file:///android_asset/items/" + Fragment1.itemMap.get(player.getBackpack_1()).substring(5) + "_lg.png")
                    .resize(64,64)
                    .into(myViewHolder.backpack_1);

            Picasso.with(context)
                    .load("file:///android_asset/items/" + Fragment1.itemMap.get(player.getBackpack_2()).substring(5) + "_lg.png")
                    .resize(64,64)
                    .into(myViewHolder.backpack_2);

            String gold_per_min = "每分钟金钱:" + player.getGold_per_min();
            myViewHolder.gold_per_min.setText(gold_per_min);

            String tower_Damage = "建筑伤害:" + player.getTower_damage();
            myViewHolder.tower_damage.setText(tower_Damage);

            String last_hits = "正补:" + player.getLast_hits();
            myViewHolder.last_hits.setText(last_hits);

            String xp_per_min = "每分钟经验:" + player.getXp_per_min();
            myViewHolder.xp_per_min.setText(xp_per_min);

            String hero_healing = "英雄治疗:" + player.getHero_healing();
            myViewHolder.hero_healing.setText(hero_healing);

            String denies = "反补:" + player.getDenies();
            myViewHolder.denies.setText(denies);

            String gold_total = "财产总和:" + String.valueOf(player.getGold() + player.getGold_spent());
            myViewHolder.gold_total.setText(gold_total);

            String gold_spent = "花费金钱:" + player.getGold_spent();
            myViewHolder.gold_spent.setText(gold_spent);

            String gold = "当前金钱:" + player.getGold();
            myViewHolder.gold.setText(gold);

        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView hero;
        TextView level;
        TextView personName;
        TextView warRate;
        TextView damageRate;
        TextView kda;
        TextView kdaValue;
        ImageView item_0;
        ImageView item_1;
        ImageView item_2;
        ImageView item_3;
        ImageView item_4;
        ImageView item_5;
        ImageView avatar;

        LinearLayout expand;
        ExpandableLayout expandableLayout;
        TextView heroDamage;
        ImageView backpack_0;
        ImageView backpack_1;
        ImageView backpack_2;
        TextView gold_per_min;
        TextView tower_damage;
        TextView last_hits;
        TextView xp_per_min;
        TextView hero_healing;
        TextView denies;
        TextView gold_total;
        TextView gold_spent;
        TextView gold;


        public MyViewHolder(View itemView) {
            super(itemView);
            hero = (ImageView) itemView.findViewById(R.id.hero);
            level = (TextView) itemView.findViewById(R.id.level);
            personName = (TextView) itemView.findViewById(R.id.personName);
            warRate = (TextView) itemView.findViewById(R.id.warRate);
            damageRate = (TextView) itemView.findViewById(R.id.damageRate);
            kda = (TextView) itemView.findViewById(R.id.kda);
            kdaValue = (TextView) itemView.findViewById(R.id.kdaValue);
            item_0 = (ImageView) itemView.findViewById(R.id.item_0);
            item_1 = (ImageView) itemView.findViewById(R.id.item_1);
            item_2 = (ImageView) itemView.findViewById(R.id.item_2);
            item_3 = (ImageView) itemView.findViewById(R.id.item_3);
            item_4 = (ImageView) itemView.findViewById(R.id.item_4);
            item_5 = (ImageView) itemView.findViewById(R.id.item_5);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);

            //Expandable content
            expand = (LinearLayout)itemView.findViewById(R.id.expand);
            expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandlayout);
            heroDamage = (TextView) itemView.findViewById(R.id.heroDamage);
            backpack_0 = (ImageView) itemView.findViewById(R.id.backpack_0);
            backpack_1 = (ImageView) itemView.findViewById(R.id.backpack_1);
            backpack_2 = (ImageView) itemView.findViewById(R.id.backpack_2);
            //灰色
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);//饱和度 0灰色 100过度彩色，50正常
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            backpack_0.setColorFilter(filter);
            backpack_1.setColorFilter(filter);
            backpack_2.setColorFilter(filter);
            gold_per_min = (TextView) itemView.findViewById(R.id.gold_per_min);
            tower_damage = (TextView) itemView.findViewById(R.id.towerDamage);
            last_hits = (TextView) itemView.findViewById(R.id.last_hits);
            xp_per_min = (TextView) itemView.findViewById(R.id.xp_per_min);
            hero_healing = (TextView) itemView.findViewById(R.id.hero_healing);
            denies = (TextView) itemView.findViewById(R.id.denies);
            gold_total = (TextView) itemView.findViewById(R.id.gold_total);
            gold_spent = (TextView) itemView.findViewById(R.id.gold_spent);
            gold = (TextView) itemView.findViewById(R.id.gold);

            expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandableLayout.toggle();
                }
            });
        }
    }

    private void setProfileData(final MyViewHolder myViewHolder, String account_id){
        Call<ResponseBody> call= Dota2ApiInstance.getInstance().getDota2Api().getPlayerSummaries(Dota2Api.key,
                String.valueOf(Long.valueOf(account_id) + Long.valueOf(Dota2Api.offset)));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject result = new JSONObject(response.body().string());

                    if(result.getJSONObject("response").getJSONArray("players").length() != 0){
                        //personName
                        String personName = result.getJSONObject("response").getJSONArray("players").getJSONObject(0).getString("personaname");
                        //process personName too long
                        if(StringUtils.isContainChinese(personName)){
                            if(personName.length() > 10)
                                personName = personName.substring(0,9).trim() + "...";
                        }else{
                            if(personName.length() > 22)
                                personName = personName.substring(0,21).trim() + "...";
                        }
                        myViewHolder.personName.setText(personName);

                        //avatar picture
                        String avatarUrl = result.getJSONObject("response").getJSONArray("players").getJSONObject(0).getString("avatarfull");
                        Picasso.with(context)
                                .load(avatarUrl)
                                .transform(new CircleTransform())
                                .resize(128,128)
                                .into(myViewHolder.avatar);
                    }else{
                        //未公开资料
                        myViewHolder.personName.setText("匿名玩家");
                        Picasso.with(context)
                                .load("file:///android_asset/player/unknown.png")
                                .transform(new CircleTransform())
                                .resize(128,128)
                                .into(myViewHolder.avatar);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
