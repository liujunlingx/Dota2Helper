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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

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

            myViewHolder.personName.setText("臭臭的嗨");
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
                    .load("https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/ec/ec6644436762757f5a8b4df1b5c1e6e783e81325.jpg")
                    .resize(64,64)
                    .into(myViewHolder.item_0);

            Picasso.with(context)
                    .load("https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/ec/ec6644436762757f5a8b4df1b5c1e6e783e81325.jpg")
                    .resize(64,64)
                    .into(myViewHolder.item_1);

            Picasso.with(context)
                    .load("https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/ec/ec6644436762757f5a8b4df1b5c1e6e783e81325.jpg")
                    .resize(64,64)
                    .into(myViewHolder.item_2);

            Picasso.with(context)
                    .load("https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/ec/ec6644436762757f5a8b4df1b5c1e6e783e81325.jpg")
                    .resize(64,64)
                    .into(myViewHolder.item_3);

            Picasso.with(context)
                    .load("https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/ec/ec6644436762757f5a8b4df1b5c1e6e783e81325.jpg")
                    .resize(64,64)
                    .into(myViewHolder.item_4);

            Picasso.with(context)
                    .load("https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/ec/ec6644436762757f5a8b4df1b5c1e6e783e81325.jpg")
                    .resize(64,64)
                    .into(myViewHolder.item_5);

            Picasso.with(context)
                    .load("https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/ec/ec6644436762757f5a8b4df1b5c1e6e783e81325.jpg")
                    .resize(128,128)
                    .into(myViewHolder.avatar);
        } catch (JSONException e) {
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
}
