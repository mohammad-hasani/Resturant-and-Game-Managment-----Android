package com.nutrica.client.adapters;

/**
 * Created by root on 6/11/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nutrica.client.nutrica.R;
import com.nutrica.client.structures.StructMultipleData;

import org.xmlpull.v1.XmlPullParser;

import java.util.List;

public class AdapterMultipleData extends RecyclerView.Adapter<AdapterMultipleData.MyViewHolder> {

    private List<StructMultipleData> data;
    private int count;
    private int view;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView[] txt;

        public MyViewHolder(View view) {
            super(view);
            txt = new TextView[count];
            for(int i = 0 ; i < count; i++)
            {
                txt[i] = (TextView) view.findViewById(R.id.txt_0 + i);
            }
        }
    }


    public AdapterMultipleData(List<StructMultipleData> data, int count, int view) {
        this.data = data;
        this.count = count;
        this.view = view;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        StructMultipleData info = data.get(position);
        for (int i = 0 ; i< count ; i++)
        {
            holder.txt[i].setText(info.getData().get(i));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}