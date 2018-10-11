package com.nutrica.client.Fragments.AdminFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nutrica.client.G;
import com.nutrica.client.Tools.NetworkConnection;
import com.nutrica.client.nutrica.R;
import com.nutrica.client.structures.StructLeague;
import com.nutrica.client.structures.StructTournament;

import java.util.ArrayList;

import com.nutrica.client.Enum.Enums.competition_type;

import org.json.JSONArray;
import org.json.JSONException;


public class SignupToGame extends Fragment {

    private View view;

    private competition_type type = null;

    private int index = -1;

    private ArrayList<StructLeague> league_list = new ArrayList<>();
    private ArrayList<StructTournament> tournament_list = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_signup_to_game, container, false);

        final EditText edtCardId = (EditText) this.view.findViewById(R.id.card_id);
        final Spinner spinnerCompetitionType = (Spinner) this.view.findViewById(R.id.spinner_competition_type);
        final Spinner spinnerCompetition = (Spinner) this.view.findViewById(R.id.spinner_competition);
        Button btnSignup = (Button) this.view.findViewById(R.id.btn_sing_up);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignup(edtCardId);
            }
        });

        spinnerCompetitionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                }
                type = competition_type.values()[i - 1];


                league_list.clear();
                tournament_list.clear();
                if (type == competition_type.League) {
                    loadCompetitions(spinnerCompetition, competition_type.League);
                } else if (type == competition_type.Tournament) {
                    loadCompetitions(spinnerCompetition, competition_type.Tournament);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCompetition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                index = i - 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        init(spinnerCompetitionType);

        return this.view;
    }

    private void init(final Spinner spinnerCompetitionType) {
        final ArrayList<String> categories = new ArrayList<>();
        categories.add("Type : ");
        String[] types = {"League", "Tournament"};
        for (int i = 0; i < types.length; i++) {
            categories.add(types[i]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCompetitionType.setAdapter(dataAdapter);

    }

    private void loadCompetitions(final Spinner spinnerGamesList, final competition_type type) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String title = "";
                String query = "";
                if (type == competition_type.League) {
                    title = "League Games : ";
                    query = "no query" + G.spliter + "competition" + G.spliter + "get available leagues";
                } else if (type == competition_type.Tournament) {
                    title = "Tournament Games : ";
                    query = "no query" + G.spliter + "competition" + G.spliter + "get available tournaments";
                }

                NetworkConnection connection = new NetworkConnection();
                String result = connection.sendQuery(query);
                connection.close();

                if (result.toLowerCase().equals("null") && result.toLowerCase().equals("false")) {
                    return;
                }

                final ArrayList<String> categories = new ArrayList<>();
                categories.add(title);

                JSONArray jsonParent = null;
                try {
                    jsonParent = new JSONArray(result);
                    for (int i = 0; i < jsonParent.length(); i++) {
                        JSONArray jsonChild = jsonParent.getJSONArray(i);
                        int id = Integer.valueOf(jsonChild.getString(0));
                        String name = jsonChild.getString(1);
                        if (type == competition_type.Tournament) {
                            StructTournament tournament = new StructTournament(id, name);
                            tournament_list.add(tournament);
                            categories.add(tournament.getName());
                        } else if (type == competition_type.League) {
                            StructLeague league = new StructLeague(id, name);
                            league_list.add(league);
                            categories.add(league.getName());
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
                        spinnerGamesList.setAdapter(dataAdapter);
                    }
                });
            }
        }).start();
    }

    private void attemptSignup(final EditText edtCardId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String card_id = edtCardId.getText().toString();
                String query = "";
                if (index == -1) {
                    return;
                }
                if (type == competition_type.League) {
                    int league_id = league_list.get(index).getId();
                    query = "no query" + G.spliter + "competition" + G.spliter + "signup to competition" + G.spliter + "league" +
                            G.spliter + card_id + G.spliter + league_id;
                } else if (type == competition_type.Tournament) {
                    int tournament_id = tournament_list.get(index).getId();
                    query = "no query" + G.spliter + "competition" + G.spliter + "signup to competition" + G.spliter + "tournament" +
                            G.spliter + card_id + G.spliter + tournament_id;
                }

                NetworkConnection networkConnection = new NetworkConnection();
                final String data = networkConnection.sendQuery(query);
                networkConnection.close();
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        if (data.toLowerCase().equals("true")) {
                            Toast.makeText(getContext(), "Successful Operation", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }).start();

    }
}
