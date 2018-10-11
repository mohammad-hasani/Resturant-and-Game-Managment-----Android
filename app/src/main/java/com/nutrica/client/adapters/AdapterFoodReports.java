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
import com.nutrica.client.structures.StructFoodReport;
import com.nutrica.client.structures.StructOrderFood;

import java.util.List;

public class AdapterFoodReports extends RecyclerView.Adapter<AdapterFoodReports.MyViewHolder> {

    private List<StructFoodReport> foods;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, food, count;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            food = (TextView) view.findViewById(R.id.food);
            count = (TextView) view.findViewById(R.id.count);
        }
    }


    public AdapterFoodReports(List<StructFoodReport> foods) {
        this.foods = foods;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.struct_food_reports, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        StructFoodReport food = foods.get(position);
        holder.title.setText(food.getName());
        holder.food.setText(String.valueOf(food.getFood()));
        holder.count.setText(String.valueOf(food.getCount()));
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }
}