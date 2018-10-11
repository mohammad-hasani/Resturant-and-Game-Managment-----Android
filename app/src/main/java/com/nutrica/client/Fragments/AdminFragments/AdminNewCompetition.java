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
import android.widget.TextView;
import android.widget.Toast;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.nutrica.client.G;
import com.nutrica.client.Tools.JalaliCalendar;
import com.nutrica.client.Tools.NetworkConnection;
import com.nutrica.client.nutrica.R;
import com.nutrica.client.structures.StructGame;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;


public class AdminNewCompetition extends Fragment {

    private View view;

    private EditText edtNickname;

    private int id_game = -1;
    private int id_competition_type = -1;
    private int team_count = 2;

    private ArrayList<StructGame> games = new ArrayList<>();
    private String signupDate, startDate;

    private Spinner spinnerGamesList, spinnerCompetitionsType, spinnerTeamCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_new_game, container, false);

        edtNickname = (EditText) this.view.findViewById(R.id.edt_nickname);

        spinnerGamesList = (Spinner) this.view.findViewById(R.id.spinner_game_list);
        spinnerCompetitionsType = (Spinner) this.view.findViewById(R.id.spinner_competition_type);
        spinnerTeamCount = (Spinner) this.view.findViewById(R.id.spinner_team_count);

        spinnerGamesList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 1) {
                    id_game = games.get(i - 1).getId();
                } else {
                    id_game = -1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerCompetitionsType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadTeamCount(spinnerTeamCount, i);
                id_competition_type = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerTeamCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                team_count = Integer.valueOf(((TextView) view).getText().toString());
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button btnSignupDate = (Button) this.view.findViewById(R.id.btn_signup_date);
        btnSignupDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(0);
            }
        });

        Button btnStartDate = (Button) this.view.findViewById(R.id.btn_start_date);
        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(1);
            }
        });

        Button btnSend = (Button) this.view.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });

        loadGames(spinnerGamesList);
        loadCompetitionsType(spinnerCompetitionsType);


        return this.view;
    }

    private void loadGames(final Spinner spinnerGamesList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String query = "no query" + G.spliter + "competition" + G.spliter + "get games list";
                NetworkConnection connection = new NetworkConnection();
                String result = connection.sendQuery(query);
                connection.close();
                if (result == null) return;
                if (result.toLowerCase().equals("null") && result.toLowerCase().equals("false")) return;

                JSONArray jsonParent = null;
                final ArrayList<String> categories = new ArrayList<>();
                categories.add("Game : ");
                try {
                    jsonParent = new JSONArray(result);
                    for (int i = 0; i < jsonParent.length(); i++) {
                        JSONArray jsonChild = jsonParent.getJSONArray(i);
                        int id = Integer.valueOf(jsonChild.getString(0));
                        String name = jsonChild.getString(1);
                        StructGame game = new StructGame(id, name);
                        games.add(game);
                        categories.add(game.getName());
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

    private void loadCompetitionsType(Spinner spinnerCompetitionTypes) {
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Competition Type : ");
//        categories.add("Free Play");
        categories.add("Tuornament");
        categories.add("League");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.view.getContext(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCompetitionTypes.setAdapter(dataAdapter);

    }

    private void openDatePicker(final int x) {
        PersianCalendar persianCalendar = new PersianCalendar();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        openTimePicker(year, monthOfYear, dayOfMonth, x);
                    }
                },
                persianCalendar.getPersianYear(),
                persianCalendar.getPersianMonth(),
                persianCalendar.getPersianDay()
        );
        datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    private void openTimePicker(final int year, final int month, final int day, final int x) {
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

                        JalaliCalendar.YearMonthDate yearMonthDate = new JalaliCalendar.YearMonthDate(year, month, day);
                        JalaliCalendar.YearMonthDate gregorian = JalaliCalendar.jalaliToGregorian(yearMonthDate);

                        String date = String.format("%d-%d-%d %d:%d:%d",
                                gregorian.getYear(), gregorian.getMonth(), gregorian.getDate(), hourOfDay, minute, 0);

                        if (x == 0) {
                            signupDate = date;
                        } else if (x == 1) {
                            startDate = date;
                        }
                    }
                },
                Calendar.getInstance().get(Calendar.HOUR),
                Calendar.getInstance().get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    private void loadTeamCount(Spinner spinnerTeamCount, int id) {
        ArrayList<String> categories = new ArrayList<>();
//        if (id == 1) {
//            categories.add("2");
//            spinnerTeamCount.setEnabled(false);
//        }
        if (id == 1) {
            for (int i = 1; i < 6; i++) {
                int value = (int) Math.pow(2, i);
                categories.add(String.valueOf(value));
            }
            spinnerTeamCount.setEnabled(true);
        }
        if (id == 2) {
            for (int i = 1; i < 6; i++) {
                int value = (int) Math.pow(2, i);
                categories.add(String.valueOf(value));
            }
            spinnerTeamCount.setEnabled(true);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.view.getContext(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTeamCount.setAdapter(dataAdapter);
    }


    private void sendData() {
        final String nickname = edtNickname.getText().toString();
        final String query = String.format("no query" + G.spliter + "competition" + G.spliter + "new competition" + G.spliter
                        + "%s" + G.spliter + "%d" + G.spliter + "%d" + G.spliter + "%d" + G.spliter + "%s" + G.spliter + "%s" +
                        G.spliter + "%s",
                nickname, id_game, id_competition_type + 1, team_count, signupDate, startDate, G.id);

        new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkConnection networkConnection = new NetworkConnection();
                final String result = networkConnection.sendQuery(query);
                networkConnection.close();
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        if (result.toLowerCase().equals("true")) {
                            Toast.makeText(getContext(), getString(R.string.successful_operation), Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                        }
                        resetValues();
                    }
                });
            }
        }).start();
    }

    private void resetValues() {
        Spinner[] spinners = {spinnerCompetitionsType, spinnerGamesList, spinnerTeamCount};
        id_game = -1;
        id_competition_type = -1;
        team_count = 2;
        edtNickname.setText("");
        for (int i = 0; i < spinners.length; i++) {
            spinners[i].setSelection(0);
        }
    }
}
