package com.nutrica.client.Fragments.AdminFragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nutrica.client.G;
import com.nutrica.client.Listeners.ClickListener;
import com.nutrica.client.Listeners.RecyclerTouchListener;
import com.nutrica.client.Tools.NetworkConnection;
import com.nutrica.client.adapters.AdapterGameStream;
import com.nutrica.client.adapters.AdapterMultipleData;
import com.nutrica.client.nutrica.R;
import com.nutrica.client.structures.StructGame;
import com.nutrica.client.structures.StructMultipleData;

import org.json.JSONArray;
import org.json.JSONException;

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
                            prepareGameData();
                        }
                    });
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadSocket.start();
//        prepareGameData();
        return view;
    }

    private void prepareGameData() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                games.clear();
//        for (int i = 0; i < 50; i++) {
//            StructGame s = new StructGame(x, (x * 2) + ":" + (x * 3));
//            games.add(s);
//            x++;
//        }

                String query = "no query" + G.spliter + "tournament" + G.spliter + "check for start pre"
                        + G.spliter + G.id;
                NetworkConnection networkConnection = new NetworkConnection();
                String result1 = networkConnection.sendQuery(query);
                networkConnection.close();

                query = "no query" + G.spliter + "tournament" + G.spliter + "get playing"
                        + G.spliter + G.id;
                networkConnection = new NetworkConnection();
                final String result2 = networkConnection.sendQuery(query);
                networkConnection.close();
                if (result2 != null)
                    if (result2.toLowerCase().equals("false") && result2.toLowerCase().equals("null")) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                JSONArray jsonParent = null;
                                try {
                                    jsonParent = new JSONArray(result2);
                                    final ArrayList<StructMultipleData> data = new ArrayList<StructMultipleData>();
                                    for (int i = 0; i < jsonParent.length(); i += 2) {
                                        JSONArray jsonChild1 = jsonParent.getJSONArray(i);
                                        int id1 = Integer.valueOf(jsonChild1.getString(0));
                                        String name1 = jsonChild1.getString(1);

                                        JSONArray jsonChild2 = jsonParent.getJSONArray(i + 1);
                                        int id2 = Integer.valueOf(jsonChild2.getString(0));
                                        String name2 = jsonChild2.getString(1);

                                        StructGame g = new StructGame();
                                        g.setId(id1);
                                        g.setName(name1 + " : " + name2);
                                        games.add(g);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });

            }
        }).start();
    }

    private void openAlertDialog(int index) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.view.getContext());
        String[] names = games.get(index).getName().split(":");
        String[] items = new String[names.length + 1];
        for (int i = 0; i < names.length; i++) {
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


