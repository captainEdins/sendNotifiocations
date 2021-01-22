package com.example.sendnotificationmysql.GridViewUtils.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sendnotificationmysql.GridViewUtils.Model.gridModelTotal;
import com.example.sendnotificationmysql.R;

import java.util.List;

public class gridAdapterTotal extends BaseAdapter {

    public List<gridModelTotal> list;
    public Context context;

    public gridAdapterTotal(List<gridModelTotal> list, Context context) {
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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_holder,parent,false);


        TextView count = view.findViewById(R.id.count);
        TextView title = view.findViewById(R.id.title);

        count.setText(list.get(position).getTotal());
        title.setText(list.get(position).getCategory());


        return view;
    }

}
