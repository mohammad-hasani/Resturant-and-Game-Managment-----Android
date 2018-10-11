package com.nutrica.client.Fragments.AdminFragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nutrica.client.Listeners.ClickListener;
import com.nutrica.client.Listeners.RecyclerTouchListener;
import com.nutrica.client.adapters.AdapterGameStream;
import com.nutrica.client.nutrica.R;
import com.nutrica.client.structures.StructGame;

import java.util.ArrayList;
import java.util.List;

public class GameStreamTournament extends Fragment {

    private View view;

    private List<StructGame> games = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterGameStream mAdapter;

    private Thread threadSocket = null;

    private int x = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_game_stream, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mAdapter = new AdapterGameStream(games);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                openAlertDialog(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        threadSocket = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "A", Toast.LENGTH_SHORT).show();
                            prepareGameData();
                        }
                    });
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadSocket.start();
        prepareGameData();
        return view;
    }

    private void prepareGameData() {
        games.clear();
        for (int i = 0; i < 50; i++) {
            StructGame s = new StructGame(x, (x * 2) + ":" + (x * 3));
            games.add(s);
            x++;
        }
        String query = "select tbl_tournament_playing.id, tbl_user.name from tbl_tournament_playing\n" +
                "join tbl_user\n" +
                "join tbl_tournament_playing_user\n" +
                "on \n" +
                "tbl_tournament_playing_user.id_tournament_playing=tbl_tournament_playing.id \n" +
                "and \n" +
                "tbl_tournament_playing_user.id_user=tbl_user.id";
//        NetworkConnection networkConnection = new NetworkConnection();
//        String result = networkConnection.sendQuery(query);
//        networkConnection.close();
//        if (result.toLowerCase().equals("false") && result.toLowerCase().equals("null")) {
//            String[] data = result.split(";");
//            for (String tmp : data) {
//                String[] data2 = tmp.split("!");
//                int id = Integer.valueOf(data2[0]);
//                String name = data2[1];
//                int price = Integer.valueOf(data2[2]);
//                StructOrderFood s = new StructOrderFood(id, name, price, 0);
//                foods.add(s);
//            }
//        }
        mAdapter.notifyDataSetChanged();
    }

    private void openAlertDialog(int index) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.view.getContext());
        String[] names = games.get(index).getName().split(":");
        String[] items = new String[names.length + 1];
        for (int i = 0 ; i < names.length; i++)
        {
            items[i] = "Win " + names[i];
        }
        items[names.length] = "Ternimate Game";
        alertDialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialogBuilder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialogBuilder.setCancelable(false);


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

}


