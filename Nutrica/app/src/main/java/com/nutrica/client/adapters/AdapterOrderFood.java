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
import com.nutrica.client.structures.StructOrderFood;

import java.util.List;

public class AdapterOrderFood extends RecyclerView.Adapter<AdapterOrderFood.MyViewHolder> {

    private List<StructOrderFood> foods;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, price, count;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.price);
            count = (TextView) view.findViewById(R.id.count);
        }
    }


    public AdapterOrderFood(List<StructOrderFood> foods) {
        this.foods = foods;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.struct_order_food, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        StructOrderFood food = foods.get(position);
        holder.title.setText(food.getName());
        holder.price.setText(String.valueOf(food.getPrice()));
        holder.count.setText(String.valueOf(food.getCount()));
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }
}