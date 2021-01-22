package com.example.sendnotificationmysql.GridViewUtils.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sendnotificationmysql.GridViewUtils.Model.gridModelNews;
import com.example.sendnotificationmysql.R;

import java.util.List;

public class gridAdapterNews extends BaseAdapter {

    public List<gridModelNews> list;
    public Context context;

    public gridAdapterNews(List<gridModelNews> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_holder_news,parent,false);

        TextView name = view.findViewById(R.id.name);
        TextView description = view.findViewById(R.id.description);
        LinearLayout unread_layout = view.findViewById(R.id.unread_layout);

        name.setText(list.get(position).getName());
        description.setText(list.get(position).getDescription());

        if(list.get(position).getRead() != null){
            if(list.get(position).getRead().equals("false")) {
                unread_layout.setVisibility(View.VISIBLE);
            }
        }

        return view;
    }


}
