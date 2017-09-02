package com.sanzhs.dota2helper.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanzhs.dota2helper.R;
import com.sanzhs.dota2helper.util.StringUtils;
import com.sanzhs.dota2helper.web.Dota2API;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/**
 * Created by sanzhs on 2017/9/1.
 */

public class MatchAdapter extends RecyclerView.Adapter<ViewHolder>
{
    private List<Map<String, Object>> data;
    private Context context;

    public MatchAdapter(Context context, List<Map<String, Object>> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_match, parent,
                false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        //eg. npc_dota_hero_riki -> riki
        String heroName = ((String)data.get(position).get("heroImageUrl")).substring(14);
        String imageUrl = Dota2API.imageUrl + "apps/dota2/images/heroes/" + heroName + "_sb.png";

        Picasso.with(context)
                .load(imageUrl)
                .resize(177,99)
                .into(myViewHolder.ivHero);
        myViewHolder.gameResult.setText((String)data.get(position).get("gameResult"));
        myViewHolder.gameResult.setTextColor(Color.rgb(255,255,255));
        if(myViewHolder.gameResult.getText().toString().equals("胜"))
            myViewHolder.gameResult.setBackgroundColor(Color.rgb(0,100,0));
        else
            myViewHolder.gameResult.setBackgroundColor(Color.rgb(96,96,96));

        String startTime = StringUtils.unixTimeStampToDate((long)data.get(position).get("startTime"),"yyyy-MM-dd");
        myViewHolder.endTime.setText(startTime);
        myViewHolder.kda.setText((String)data.get(position).get("kda"));
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class MyViewHolder extends ViewHolder{

        ImageView ivHero;
        TextView gameResult;
        TextView endTime;
        TextView kda;

        public MyViewHolder(View view)
        {
            super(view);
            ivHero = (ImageView) view.findViewById(R.id.hero);
            gameResult = (TextView) view.findViewById(R.id.gameResult);
            endTime = (TextView) view.findViewById(R.id.startTime);
            kda = (TextView) view.findViewById(R.id.kda);
        }
    }

}
