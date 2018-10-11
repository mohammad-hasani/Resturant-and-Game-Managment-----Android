package com.nutrica.client.Fragments.AdminFragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nutrica.client.G;
import com.nutrica.client.Listeners.ClickListener;
import com.nutrica.client.Listeners.RecyclerTouchListener;
import com.nutrica.client.Tools.NetworkConnection;
import com.nutrica.client.adapters.AdapterFoodReports;
import com.nutrica.client.nutrica.R;
import com.nutrica.client.structures.StructFoodReport;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AdminOrderFoodReports extends Fragment {

    private View view;

    private List<StructFoodReport> foods = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterFoodReports mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_order_food_reports, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAdapter = new AdapterFoodReports(foods);

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

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        prepareFoodData();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(task, 0, 20000);
//        prepareFoodData();
        return view;
    }

    private void openAlertDialog(final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.view.getContext());
        alertDialogBuilder.setTitle("Info");
        alertDialogBuilder.setMessage(getString(R.string.continue1));
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int id = foods.get(position).getId();
                                String query = "update tbl_order set is_delivered=1 where id=" + id;
                                NetworkConnection networkConnection = new NetworkConnection();
                                final String result = networkConnection.sendQuery(query);
                                networkConnection.close();
                                recyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (result == null)
                                            return;
                                        if (result.toLowerCase().equals("true")) {
                                            Toast.makeText(getContext(), getString(R.string.successful_operation), Toast.LENGTH_LONG).show();
                                            prepareFoodData();
                                        } else {
                                            Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }).start();
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialogBuilder.setCancelable(false);


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    private void prepareFoodData() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String query = String.format("no query%sfoodreports%s%d", G.spliter, G.spliter, G.id);
                NetworkConnection networkConnection = new NetworkConnection();
                String result = networkConnection.sendQuery(query);
                networkConnection.close();
                if (result != null)
                    if (!result.toLowerCase().equals("false") && !result.toLowerCase().equals("null")) {
                        try {
                            foods.clear();
                            JSONArray jsonParent = new JSONArray(result);
                            for (int i = 0; i < jsonParent.length(); i++) {
                                JSONArray jsonChild = jsonParent.getJSONArray(i);
                                int id = Integer.valueOf(jsonChild.getString(0));
                                String name = jsonChild.getString(1);
                                String food = jsonChild.getString(2);
                                int count = Integer.valueOf(jsonChild.getString(3));
                                StructFoodReport s = new StructFoodReport(id, name, food, count);
                                foods.add(s);
                            }
                        } catch (JSONException e) {
                            Log.d("DDDDDD", e.getMessage());
                        }
                    }
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
}


