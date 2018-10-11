package com.nutrica.client.Fragments.AdminFragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.nutrica.client.G;
import com.nutrica.client.Listeners.ClickListener;
import com.nutrica.client.Listeners.RecyclerTouchListener;
import com.nutrica.client.Tools.NetworkConnection;
import com.nutrica.client.adapters.AdapterOrderFood;
import com.nutrica.client.nutrica.R;
import com.nutrica.client.structures.StructOrderFood;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class OrderFood extends Fragment {

    private View view;

    private List<StructOrderFood> foods = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterOrderFood mAdapter;
    private FloatingActionButton btnOrderFood;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_order_food, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        btnOrderFood = (FloatingActionButton) view.findViewById(R.id.btn_order_food);

        btnOrderFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlertDialog();
            }
        });

        mAdapter = new AdapterOrderFood(foods);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                int count = foods.get(position).getCount();
                count += 1;
                foods.get(position).setCount(count);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLongClick(View view, int position) {
                int count = foods.get(position).getCount();
                if (count > 0) {
                    count -= 1;
                    foods.get(position).setCount(count);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }));

        prepareFoodData();
        return view;
    }

    private void openAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.view.getContext());
        View alertDialogView = getActivity().getLayoutInflater().inflate(R.layout.alert_dialog_order_food, null);
        final EditText edtCardID = (EditText) alertDialogView.findViewById(R.id.edt_card_id);
        final EditText edtTableID = (EditText) alertDialogView.findViewById(R.id.edt_table_number);
        alertDialogBuilder.setView(alertDialogView);
        alertDialogBuilder.setPositiveButton("Order",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String cardID = edtCardID.getText().toString();
                        String table = String.valueOf(1); //edtTableID.getText().toString();
                        orderFood(cardID, table);
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

    private void orderFood(final String cardID, String table) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder data = new StringBuilder();
                data.append("[");
                for (StructOrderFood s : foods) {
                    if (Integer.valueOf(s.getCount()) > 0) {
                        String tmp = "[" + s.getId() + "," + s.getCount() + "],";
                        data.append(tmp);
                    }
                }
                String tmpdata = data.toString().substring(0, data.length() - 1);
                tmpdata += "]";
                if (data.length() < 1)
                    return;
                String query = String.format("no query%sorderfood%s%s%s", G.spliter, G.spliter, cardID, G.spliter);
                query += tmpdata;
                NetworkConnection networkConnection = new NetworkConnection();
                final String result = networkConnection.sendQuery(query);
                networkConnection.close();
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (result.toLowerCase().equals("true")) {
                            Toast.makeText(getContext(), "Successful Operation", Toast.LENGTH_LONG).show();
                        }
                        else if (result.toLowerCase().equals("no credit")) {
                            Toast.makeText(getContext(), "No Credit", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        }).start();
    }

    private void prepareFoodData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String query = "select id, name, price from tbl_facility where type='food'";
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
                                int price = Integer.valueOf(jsonChild.getString(2));
                                StructOrderFood s = new StructOrderFood(id, name, price, 0);
                                foods.add(s);
                            }
                        } catch (JSONException e) {
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