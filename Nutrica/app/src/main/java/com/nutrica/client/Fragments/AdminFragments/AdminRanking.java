package com.nutrica.client.Fragments.AdminFragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.nutrica.client.Listeners.ClickListener;
import com.nutrica.client.Listeners.RecyclerTouchListener;
import com.nutrica.client.Tools.NetworkConnection;
import com.nutrica.client.adapters.AdapterMultipleData;
import com.nutrica.client.nutrica.R;
import com.nutrica.client.structures.StructMultipleData;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class AdminRanking extends Fragment {

    private View view;

    private RecyclerView recyclerView;
    AdapterMultipleData adapterMultipleData;
    private ArrayList<StructMultipleData> spDataCompetitionType;
    private ArrayList<StructMultipleData> spDataGame;
    private ArrayList<StructMultipleData> spDataCity;

    private ArrayList<StructMultipleData> games;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_user_ranking, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        Spinner spCompetitionType = (Spinner) view.findViewById(R.id.spinner_competition_type);
        Spinner spGame = (Spinner) view.findViewById(R.id.spinner_game);
        Spinner spCity = (Spinner) view.findViewById(R.id.spinner_city);

        setSpinnersData(spCompetitionType, spGame, spCity);

//        setData();
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
                                    final ArrayList<StructMultipleData> data = new ArrayList<StructMultipleData>();
                                    for (int i = 0; i < jsonParent.length(); i++) {
                                        JSONArray jsonChild = jsonParent.getJSONArray(i);
                                        int id = Integer.valueOf(jsonChild.getString(0));
                                        String name = jsonChild.getString(1);
                                        String type = jsonChild.getString(2);
                                        String city = jsonChild.getString(3);
                                        String game = jsonChild.getString(4);
                                        String capacity = jsonChild.getString(5);
                                        ArrayList<String> tmp = new ArrayList<String>();
                                        tmp.add(name);
                                        tmp.add(type);
                                        tmp.add(city);
                                        tmp.add(game);
                                        tmp.add(capacity);
                                        StructMultipleData s = new StructMultipleData();
                                        s.setId(id);
                                        s.setData(tmp);
                                        data.add(s);
                                    }
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapterMultipleData = new AdapterMultipleData(data, 5, R.layout.struct_competition);
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

    private void setSpinnersData(Spinner... spinners) {
        String query = "";
        String type = "competition_type";
        setSpinnerData(spinners[0], query, type);

        query = "";
        type = "game";
        setSpinnerData(spinners[1], query, type);

        query = "";
        type = "city";
        setSpinnerData(spinners[2], query, type);

        setSpinnerEvents(spinners[0], type);
        setSpinnerEvents(spinners[1], type);
        setSpinnerEvents(spinners[2], type);

    }

    private void setSpinnerData(final Spinner spinner, final String query, final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkConnection connection = new NetworkConnection();
                String result = connection.sendQuery(query);
                connection.close();
                if(result == null)
                    return;
                if (result.toLowerCase().equals("null") && result.toLowerCase().equals("false")) {
                    return;
                }
                JSONArray jsonParent = null;
                final ArrayList<String> categories = new ArrayList<>();

                switch (type) {
                    case "competition_type": {
                        categories.add(getString(R.string.competition_type) + " : ");
                        break;
                    }
                    case "game": {
                        categories.add(getString(R.string.game) + " : ");
                        break;
                    }
                    case "city": {
                        categories.add(getString(R.string.city) + " : ");
                        break;
                    }
                }
                try {
                    jsonParent = new JSONArray(result);
                    for (int i = 0; i < jsonParent.length(); i++) {
                        JSONArray jsonChild = jsonParent.getJSONArray(i);
                        int id = Integer.valueOf(jsonChild.getString(0));
                        String name = jsonChild.getString(1);
                        ArrayList<String> game = new ArrayList<String>();
                        game.add(name);
                        categories.add(name);
                        StructMultipleData s = new StructMultipleData();
                        s.setId(id);
                        s.setData(game);
                        switch (type) {
                            case "competition_type": {
                                spDataCompetitionType.add(s);
                                break;
                            }
                            case "game": {
                                spDataGame.add(s);
                                break;
                            }
                            case "city": {
                                spDataCity.add(s);
                                break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, categories);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(dataAdapter);

                    }
                });
            }
        }).start();
    }

    private void setSpinnerEvents(Spinner spinner, final String type) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String query = "";
                switch (type) {
                    case "competition_type": {
                        query = "";
                        break;
                    }
                    case "game": {
                        query = "";
                        break;
                    }
                    case "city": {
                        query = "";
                        break;
                    }
                }
                setData(query);
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}