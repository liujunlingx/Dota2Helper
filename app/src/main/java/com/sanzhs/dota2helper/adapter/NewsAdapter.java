package com.sanzhs.dota2helper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sanzhs.dota2helper.R;
import com.sanzhs.dota2helper.util.StringUtils;

import java.util.List;
import java.util.Map;

import jaydenxiao.com.expandabletextview.ExpandableTextView;

/**
 * Created by sanzhs on 2017/9/2.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Map<String, Object>> data;
    private Context context;

    public NewsAdapter(Context context, List<Map<String, Object>> data){
        this.data = data;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent,
                false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.title.setText((String)data.get(position).get("title"));
        myViewHolder.content.setText(StringUtils.fromHtml((String)data.get(position).get("content")));
        myViewHolder.author.setText((String)data.get(position).get("author"));
        myViewHolder.date.setText(StringUtils.unixTimeStampToDate((long)data.get(position).get("date"),"yyyy-MM-dd"));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ExpandableTextView content;
        TextView author;
        TextView date;

        public MyViewHolder(View view)
        {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            content = (ExpandableTextView) view.findViewById(R.id.content);
            author = (TextView) view.findViewById(R.id.author);
            date = (TextView) view.findViewById(R.id.date);
        }
    }

    public void add(Map<String, Object> item){
        data.add(item);
        notifyDataSetChanged();
    }

}
