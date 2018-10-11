package com.nutrica.client.Fragments.UserFragments;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.nutrica.client.G;
import com.nutrica.client.Listeners.ClickListener;
import com.nutrica.client.Listeners.RecyclerTouchListener;
import com.nutrica.client.Tools.NetworkConnection;
import com.nutrica.client.Tools.Tools;
import com.nutrica.client.adapters.AdapterMultipleData;
import com.nutrica.client.nutrica.R;
import com.nutrica.client.structures.StructCity;
import com.nutrica.client.structures.StructMultipleData;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class UserGamenets extends Fragment {

    private View view;

    private RecyclerView recyclerView;
    private AdapterMultipleData adapterMultipleData;

    private ArrayList<StructCity> spDataCity;
    private ArrayList<StructMultipleData> data;
    private int spinnerId = 0;

    private Spinner spCity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_user_gamenets, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                int id = data.get(position).getId();
                sendData(id);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        spCity = (Spinner) view.findViewById(R.id.spinner_city);
        setSpinnersData(spCity);
        return view;
    }


    private void setData(final String query) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkConnection networkConnection = new NetworkConnection();
                final String result = networkConnection.sendQuery(query);
                networkConnection.close();
                if (result != null) {
                    if (!result.equals("null") || !result.toLowerCase().equals("false")) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                JSONArray jsonParent = null;
                                try {
                                    jsonParent = new JSONArray(result);
                                    data = new ArrayList<StructMultipleData>();
                                    for (int i = 0; i < jsonParent.length(); i++) {
                                        JSONArray jsonChild = jsonParent.getJSONArray(i);
                                        int id = Integer.valueOf(jsonChild.getString(0));
                                        String name = jsonChild.getString(1);
                                        String city = spDataCity.get(spinnerId - 1).getName();
                                        ArrayList<String> tmp = new ArrayList<String>();
                                        tmp.add(name);
                                        tmp.add(city);

                                        StructMultipleData s = new StructMultipleData();
                                        s.setId(id);
                                        s.setData(tmp);
                                        data.add(s);
                                    }
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapterMultipleData = new AdapterMultipleData(data, 2, R.layout.struct_user_gamenets);
                                            recyclerView.setAdapter(adapterMultipleData);
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
        }).start();
    }

    private void setSpinnersData(Spinner spinner) {
        String query = "";
        setSpinnerData(spinner, query);

        setSpinnerEvents(spinner);


    }

    private void sendData(final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String query = "no query" + G.spliter + "admin" + G.spliter +
                        "get admin description" + G.spliter + id;

                NetworkConnection connection = new NetworkConnection();
                final String result = connection.sendQuery(query);
                connection.close();
                if (result == null)
                    return;
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        openAlert(result);
                    }
                });
            }
        }).start();
    }

    private void openAlert(String description) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.view.getContext());
        alertDialogBuilder.setMessage(description);
        alertDialogBuilder.setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialogBuilder.setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void setSpinnerData(final Spinner spinner, final String query) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                spDataCity = new ArrayList<StructCity>();
                final ArrayList<String> categories = new ArrayList<String>();

                categories.add(getString(R.string.city) + " : ");
                ArrayList<StructCity> provinces = Tools.getProvinces(getContext());
                for (int i = 0; i < provinces.size(); i++) {
                    spDataCity.add(provinces.get(i));
                    categories.add(provinces.get(i).getName());
                }

                view.post(new Runnable() {
                              @Override
                              public void run() {
                                  ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, categories);
                                  dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                  spinner.setAdapter(dataAdapter);

                              }
                          }
                );
            }
        }

        ).start();

    }

    private void setSpinnerEvents(final Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerId = i;
                if (spinnerId != 0) {
                    int cityId = spDataCity.get(i - 2).getId();
                    String query = "no query" + G.spliter + "admin" + G.spliter +
                            "get admins" + G.spliter + cityId;
                    setData(query);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}