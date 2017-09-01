package com.sanzhs.dota2helper.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanzhs.dota2helper.R;
import com.sanzhs.dota2helper.util.StringUtils;
import com.sanzhs.dota2helper.web.Dota2API;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by sanzhs on 2017/8/30.
 */

public class ListViewAdapter extends BaseAdapter {

    private List<Map<String, Object>> data;
    private Context context;
    private LayoutInflater layoutInflater;

    //widget
    private ImageView ivHero;
    private TextView gameResult;
    private TextView endTime;
    private TextView kda;

    public ListViewAdapter(Context context, List<Map<String, Object>> data){
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listview_item, null);
        }

        findViewByIds(convertView);

        //eg. npc_dota_hero_riki -> riki
        String heroName = ((String)data.get(position).get("heroImageUrl")).substring(14);
        String imageUrl = Dota2API.imageUrl + "apps/dota2/images/heroes/" + heroName + "_sb.png";

        Picasso.with(context)
                .load(imageUrl)
                .resize(118,66)
                .into(ivHero);
        gameResult.setText((String)data.get(position).get("gameResult"));
        gameResult.setTextColor(Color.rgb(255,255,255));
        if(gameResult.getText().toString().equals("胜"))
            gameResult.setBackgroundColor(Color.rgb(0,100,0));
        else
            gameResult.setBackgroundColor(Color.rgb(96,96,96));

        String startTime = StringUtils.unixTimeStampToDate((long)data.get(position).get("startTime"),"yyyy-MM-dd");
        endTime.setText(startTime);
        kda.setText((String)data.get(position).get("kda"));

        return convertView;
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return this.data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void findViewByIds(View view){
        ivHero = (ImageView) view.findViewById(R.id.hero);
        gameResult = (TextView) view.findViewById(R.id.gameResult);
        endTime = (TextView) view.findViewById(R.id.startTime);
        kda = (TextView) view.findViewById(R.id.kda);
    }

}
