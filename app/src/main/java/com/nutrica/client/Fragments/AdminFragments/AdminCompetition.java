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

public class AdminCompetition extends Fragment {

    private View view;

    private RecyclerView recyclerView;
    AdapterMultipleData adapterMultipleData;
    private ArrayList<StructMultipleData> spDataCompetitionType;
    private ArrayList<StructMultipleData> spDataGame;
    private ArrayList<StructCity> spDataCity;
    private int game_id = 0, city_id = 0, competition_type = 0;

    private ArrayList<StructMultipleData> games;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_user_competitions, container, false);

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
                                        String capacity = jsonChild.getString(1);
                                        String name = jsonChild.getString(2);
                                        String type = "";
                                        if (competition_type == 1) {
                                            type = getString(R.string.league);
                                        } else if (competition_type == 2) {
                                            type = getString(R.string.tournament);
                                        }

                                        String city = spDataCity.get(city_id - 1).getName();
                                        String game = spDataGame.get(game_id - 1).getData().get(0).toString();

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
        final ArrayList<String> categories = new ArrayList<>();
        categories.add(getString(R.string.competition_type) + " : ");
        categories.add(getString(R.string.league));
        categories.add(getString(R.string.tournament));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinners[0].setAdapter(dataAdapter);
//        setSpinnerData(spinners[0], query, "competition_type");

        query = "no query" + G.spliter + "get games list";
        type = "game";
        setSpinnerData(spinners[1], query, "game");

        query = "";
        type = "city";
        setSpinnerData(spinners[2], query, "city");

        setSpinnerEvents(spinners[0], "competition_type");
        setSpinnerEvents(spinners[1], "game");
        setSpinnerEvents(spinners[2], "city");

    }

    private void setSpinnerData(final Spinner spinner, final String query, final String type) {


        new Thread(new Runnable() {
            @Override
            public void run() {

                final ArrayList<String> categories = new ArrayList<>();
                if (type.equals("city")) {
                    spDataCity = new ArrayList<StructCity>();

                    categories.add(getString(R.string.city) + " : ");
                    ArrayList<StructCity> provinces = Tools.getProvinces(getContext());
                    for (int i = 0; i < provinces.size(); i++) {
                        spDataCity.add(provinces.get(i));
                        categories.add(provinces.get(i).getName());
                    }

                } else if (type.equals("competition_type")) {
                    categories.add(getString(R.string.competition_type) + " : ");
                    categories.add(getString(R.string.league));
                    categories.add(getString(R.string.tournament));
                } else {
                    NetworkConnection connection = new NetworkConnection();
                    String result = connection.sendQuery(query);
                    connection.close();
                    if (result == null)
                        return;
                    if (result.toLowerCase().equals("null") && result.toLowerCase().equals("false")) {
                        return;
                    }
                    JSONArray jsonParent = null;


                    switch (type) {
                        case "game": {
                            spDataGame = new ArrayList<StructMultipleData>();
                            categories.add(getString(R.string.game) + " : ");
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
                                case "game": {
                                    spDataGame.add(s);
                                    break;
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    private void setSpinnerEvents(final Spinner spinner, final String type) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (type) {
                    case "competition_type": {
                        competition_type = i;
                        break;
                    }
                    case "game": {
                        if (i > 0)
                            game_id = i;
                        break;
                    }
                    case "city": {
                        if (i > 0)
                            city_id = i;
                        break;
                    }
                }
                if (competition_type != 0 && game_id != 0 && city_id != 0) {
                    String query = "no query" + G.spliter + "competition" + G.spliter +
                            "get competition info" + G.spliter + (competition_type - 1) +
                            G.spliter + (spDataGame.get(game_id - 1).getId()) + G.spliter +
                            (spDataCity.get(city_id - 1).getId());
                    setData(query);
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}