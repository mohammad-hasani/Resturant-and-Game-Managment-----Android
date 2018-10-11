package com.nutrica.client.Fragments.UserFragments;

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
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.nutrica.client.Listeners.ClickListener;
import com.nutrica.client.Listeners.RecyclerTouchListener;
import com.nutrica.client.Tools.NetworkConnection;
import com.nutrica.client.adapters.AdapterMultipleData;
import com.nutrica.client.nutrica.R;
import com.nutrica.client.slider.CustomSlider;
import com.nutrica.client.structures.StructMultipleData;
import com.nutrica.client.structures.StructSlider;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class UserMainPage extends Fragment {

    private View view;

    private RecyclerView recyclerView;
    AdapterMultipleData adapterMultipleData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_user_main_page, container, false);

        TextView txtGame = (TextView) view.findViewById(R.id.txt_game);
        TextView txtDate = (TextView) view.findViewById(R.id.txt_date);
        TextView txtCompetitionName = (TextView) view.findViewById(R.id.txt_competitino_name);

        SliderLayout slider = (SliderLayout) view.findViewById(R.id.slider);

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
        fillNextGame(txtGame, txtDate, txtCompetitionName);
        setUpcomingData();
        setSlider(slider);
        return view;
    }

    private void setUpcomingData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String query = "no query%sorderfood%s%s%s";
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
                                        String game = jsonChild.getString(0);
                                        String date = jsonChild.getString(1);
                                        String competitionName = jsonChild.getString(2);
                                        ArrayList<String> tmp = new ArrayList<String>();
                                        tmp.add(game);
                                        tmp.add(date);
                                        tmp.add(competitionName);
                                        StructMultipleData s = new StructMultipleData();
                                        s.setId(0);
                                        s.setData(tmp);
                                        data.add(s);
                                    }
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapterMultipleData = new AdapterMultipleData(data, 3, R.layout.struct_three_data);
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

    private void fillNextGame(final TextView txtGame, final TextView txtDate,
                              final TextView txtCompetitionName) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                String query = "no query%sorderfood%s%s%s";
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

                                    for (int i = 0; i < jsonParent.length(); i++) {
                                        JSONArray jsonChild = jsonParent.getJSONArray(i);
                                        final String game = jsonChild.getString(0);
                                        final String date = jsonChild.getString(1);
                                        final String competitionName = jsonChild.getString(2);
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                txtGame.setText(game);
                                                txtDate.setText(date);
                                                txtCompetitionName.setText(competitionName);
                                            }
                                        });
                                    }
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

    private void setSlider(final SliderLayout slider) {
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
                            final ArrayList<StructSlider> data = new ArrayList<>();
                            JSONArray jsonParent = new JSONArray(result);
                            for (int i = 0; i < jsonParent.length(); i++) {
                                JSONArray jsonChild = jsonParent.getJSONArray(i);
                                int id = Integer.valueOf(jsonChild.getString(0));
                                String name = jsonChild.getString(1);
                                String url = jsonChild.getString(2);
                                StructSlider s = new StructSlider(id, name, url);
                                data.add(s);

                            }

                            final BaseSliderView.OnSliderClickListener listener = new CustomSlider();

                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    new CustomSlider().callSlider(slider, data, listener);
                                }
                            });

                        } catch (Exception e) {

                        }
                    }

            }
        }).start();
    }
}