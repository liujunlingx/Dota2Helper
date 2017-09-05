package com.sanzhs.dota2helper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanzhs.dota2helper.R;
import com.sanzhs.dota2helper.fragment.Fragment1;
import com.sanzhs.dota2helper.util.CircleTransform;
import com.sanzhs.dota2helper.web.Dota2API;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by sanzhs on 2017/9/4.
 */

public class PlayerDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<JSONObject> data;
    private Context context;

    public PlayerDetailAdapter(Context context,List<JSONObject> data){
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
        JSONObject player = data.get(position);

        try {
            String heroName = Fragment1.heroMap.get(player.getInt("hero_id"));
            Picasso.with(context)
                    .load("file:///android_asset/heros/" + heroName.substring(14) + "_full.png")
                    .resize(177,99)
                    .into(myViewHolder.hero);

            String account_id = player.getString("account_id");
            setProfileData(myViewHolder,account_id);

            String warRate = "参战率:" + player.getString("warRate") + "%";
            myViewHolder.warRate.setText(warRate);
            String damageRate = "伤害:" + player.getString("damageRate") + "%";
            myViewHolder.damageRate.setText(damageRate);

            int kills,deaths,assists;
            kills = player.getInt("kills");
            deaths = player.getInt("deaths");
            assists = player.getInt("assists");
            myViewHolder.kda.setText(kills + "/" + deaths + "/" + assists);
            myViewHolder.kdaValue.setText(new DecimalFormat("######0.00").format(((double)(kills + assists))/deaths));

            Picasso.with(context)
                    .load("file:///android_asset/items/" + Fragment1.itemMap.get(player.getInt("item_0")).substring(5) + "_lg.png")
                    .resize(64,64)
                    .into(myViewHolder.item_0);

            Picasso.with(context)
                    .load("file:///android_asset/items/" + Fragment1.itemMap.get(player.getInt("item_1")).substring(5) + "_lg.png")
                    .resize(64,64)
                    .into(myViewHolder.item_1);

            Picasso.with(context)
                    .load("file:///android_asset/items/" + Fragment1.itemMap.get(player.getInt("item_2")).substring(5) + "_lg.png")
                    .resize(64,64)
                    .into(myViewHolder.item_2);

            Picasso.with(context)
                    .load("file:///android_asset/items/" + Fragment1.itemMap.get(player.getInt("item_3")).substring(5) + "_lg.png")
                    .resize(64,64)
                    .into(myViewHolder.item_3);

            Picasso.with(context)
                    .load("file:///android_asset/items/" + Fragment1.itemMap.get(player.getInt("item_4")).substring(5) + "_lg.png")
                    .resize(64,64)
                    .into(myViewHolder.item_4);

            Picasso.with(context)
                    .load("file:///android_asset/items/" + Fragment1.itemMap.get(player.getInt("item_5")).substring(5) + "_lg.png")
                    .resize(64,64)
                    .into(myViewHolder.item_5);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView hero;
        //TODO load from api,process if too long
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
        //TODO load from api
        ImageView avatar;

        public MyViewHolder(View itemView) {
            super(itemView);
            hero = (ImageView) itemView.findViewById(R.id.hero);
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
        }
    }

    private void setProfileData(final MyViewHolder myViewHolder, String account_id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Dota2API.baseUrl)
                .build();

        Dota2API dota2API = retrofit.create(Dota2API.class);

        Call<ResponseBody> call= dota2API.getPlayerSummaries(Dota2API.key,
                String.valueOf(Long.valueOf(account_id) + Long.valueOf(Dota2API.offset)));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject result = new JSONObject(response.body().string());

                    if(result.getJSONObject("response").getJSONArray("players").length() != 0){
                        //personName
                        String personName = result.getJSONObject("response").getJSONArray("players").getJSONObject(0).getString("personaname");
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
