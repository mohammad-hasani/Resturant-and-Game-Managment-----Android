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
import com.nutrica.client.structures.StructGame;
import com.nutrica.client.structures.StructOrderFood;

import java.util.List;

public class AdapterGameStream extends RecyclerView.Adapter<AdapterGameStream.MyViewHolder> {

    private List<StructGame> games;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView player1, player2;

        public MyViewHolder(View view) {
            super(view);
            player1 = (TextView) view.findViewById(R.id.player1);
            player2 = (TextView) view.findViewById(R.id.player2);
        }
    }


    public AdapterGameStream(List<StructGame> games) {
        this.games = games;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.struct_game_stream, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        StructGame game = games.get(position);
        String[] names = game.getName().split(":");
        String player1 = names[0];
        String player2 = names[1];
        holder.player1.setText(player1);
        holder.player2.setText(player2);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }
}