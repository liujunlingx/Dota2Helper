package com.sanzhs.dota2helper.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanzhs.dota2helper.R;
import com.sanzhs.dota2helper.fragment.Fragment1;
import com.sanzhs.dota2helper.util.StringUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;
import java.util.Map;

/**
 * Created by sanzhs on 2017/9/1.
 */

public class MatchAdapter extends RecyclerView.Adapter<ViewHolder>
{
    private List<Map<String, Object>> data;
    private Context context;
    private AdapterView.OnItemClickListener onItemClickListener;

    public MatchAdapter(Context context, List<Map<String, Object>> data, AdapterView.OnItemClickListener onItemClickListener){
        this.context = context;
        this.data = data;
        this.onItemClickListener = onItemClickListener;
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
        String heroName = ((String)data.get(position).get("heroName")).substring(14);

        //load from network
//        String imageUrl = Dota2Api.imageUrl + "apps/dota2/images/heroes/" + heroName + "_sb.png";
//        Picasso.with(context)
//                .load(imageUrl)
//                .resize(177,99)
//                .into(myViewHolder.ivHero);

        //load from file
        Picasso.with(context)
                .load("file:///android_asset/heros/" + heroName + "_full.png")
                //.transform(new CircleTransform())
                .resize(177,99)
                .into(myViewHolder.ivHero);
        myViewHolder.gameResult.setText((String)data.get(position).get("gameResult"));
        myViewHolder.gameResult.setTextColor(Color.rgb(255,255,255));
        if(myViewHolder.gameResult.getText().toString().equals(Fragment1.GameResult.WIN.info))
            myViewHolder.gameResult.setBackgroundColor(Color.rgb(0,100,0));
        else
            myViewHolder.gameResult.setBackgroundColor(Color.rgb(96,96,96));

        String startTime = StringUtils.unixTimeStampToDate((long)data.get(position).get("startTime"),"yyyy-MM-dd");
        myViewHolder.endTime.setText(startTime);
        String gameModeStr = StringUtils.gameModeConversion((Integer) data.get(position).get("gameMode"));
        myViewHolder.gameMode.setText(gameModeStr);

        myViewHolder.kdaValue.setTypeface(null, Typeface.BOLD);
        myViewHolder.kdaValue.setText((String)data.get(position).get("kdaValue"));
        myViewHolder.kda.setText((String)data.get(position).get("kda"));

    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class MyViewHolder extends ViewHolder implements View.OnClickListener{

        ImageView ivHero;
        TextView gameResult;
        TextView endTime;
        TextView gameMode;
        TextView kdaValue;
        TextView kda;

        public MyViewHolder(View view)
        {
            super(view);
            ivHero = (ImageView) view.findViewById(R.id.hero);
            gameResult = (TextView) view.findViewById(R.id.gameResult);
            endTime = (TextView) view.findViewById(R.id.startTime);
            gameMode = (TextView) view.findViewById(R.id.gameMode);
            kdaValue = (TextView) view.findViewById(R.id.kdaValue);
            kda = (TextView) view.findViewById(R.id.kda);

            ivHero.setOnClickListener(this);
            gameResult.setOnClickListener(this);
            endTime.setOnClickListener(this);
            gameMode.setOnClickListener(this);
            kdaValue.setOnClickListener(this);
            kda.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(null, v, getAdapterPosition(), v.getId());
        }
    }

}
