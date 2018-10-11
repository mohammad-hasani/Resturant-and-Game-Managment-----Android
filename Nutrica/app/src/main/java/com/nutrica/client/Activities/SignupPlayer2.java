package com.nutrica.client.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.nutrica.client.G;
import com.nutrica.client.Tools.Database;
import com.nutrica.client.Tools.NetworkConnection;
import com.nutrica.client.Tools.Tools;
import com.nutrica.client.nutrica.R;
import com.nutrica.client.structures.StructCity;
import com.nutrica.client.structures.StructGame;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class SignupPlayer2 extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;

    private ArrayList<StructGame> games = null;
    private int game_id = -1;
    private int avatar_id = -1;
    private EditText edtPhonenumber;
    private EditText edtEmail;

    private int city_id = -1;
    private ArrayList<StructCity> states = null;
    private ArrayList<StructCity> cities = null;

    private ImageButton btnAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_player2);

        final Intent preIntent = getIntent();

        // Set up the login form.
        final EditText edtCreditCardNumber = (EditText) findViewById(R.id.edt_credit_card_number);
        final Spinner spinnerGamesList = (Spinner) findViewById(R.id.spinner_favorite_game);
        edtPhonenumber = (EditText) findViewById(R.id.edt_credit_card_number);
        edtEmail = (EditText) findViewById(R.id.edt_credit_card_number);///////////////////////////////////////////////////
        btnAvatar = (ImageButton) findViewById(R.id.btn_avatar);
        btnAvatar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setAvatar();
            }
        });

        Button btnLater = (Button)findViewById(R.id.btn_later);
        btnLater.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        Button btnSignup = (Button) findViewById(R.id.btn_sing_up);
        btnSignup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin(preIntent, city_id, edtEmail, edtPhonenumber, spinnerGamesList, edtCreditCardNumber);
            }
        });


        spinnerGamesList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    int id = games.get(i - 1).getId();
                    game_id = id;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner spinnerStates = (Spinner) findViewById(R.id.spinner_state);
        final Spinner spinnerCities = (Spinner) findViewById(R.id.spinner_city);
        spinnerStates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    int id = states.get(i - 1).getId();
                    fillCities(spinnerCities, id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinnerCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    int id = cities.get(i - 1).getId();
                    city_id = id;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fillGamesList(spinnerGamesList);
        fillStates(spinnerStates);
    }

    private void setAvatar() {
        Intent intent = new Intent(SignupPlayer2.this, ImageSelector.class);
        startActivityForResult(intent, 0);
    }

    private void fillGamesList(final Spinner spinner) {
        games = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String query = "no query" + G.spliter + "get games list";
                NetworkConnection connection = new NetworkConnection();
                String result = connection.sendQuery(query);
                connection.close();

                if (result == null || result.toLowerCase().equals("null") || result.toLowerCase().equals("false")) {
                    return;
                }

                final ArrayList<String> categories = new ArrayList<>();
                categories.add(getString(R.string.favorite_game) + " : ");

                JSONArray jsonParent = null;
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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SignupPlayer2.this, android.R.layout.simple_spinner_item, categories);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(dataAdapter);
                    }
                });
            }
        }).start();
    }

    private void fillStates(Spinner spinner) {
        states = new ArrayList<>();
        ArrayList<String> categories = new ArrayList<>();
        categories.add(getString(R.string.province) + " : ");

        String query = "select id, name from province";

        Database db = new Database();
        Cursor cursor = db.getData(query);
        db.close();

        for (int i = 0; i < cursor.getCount(); i++) {
            StructCity city = new StructCity();
            city.setId(cursor.getInt(0));
            city.setName(cursor.getString(1));
            categories.add(city.getName());
            states.add(city);
            cursor.moveToNext();
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    private void fillCities(Spinner spinner, int id) {
        cities = new ArrayList<>();
        ArrayList<String> categories = new ArrayList<>();
        categories.add(getString(R.string.city) + " : ");

        String query = String.format("select id, name from city where province_id=%d", id);

        Database db = new Database();
        Cursor cursor = db.getData(query);
        db.close();

        for (int i = 0; i < cursor.getCount(); i++) {
            StructCity city = new StructCity();
            city.setId(cursor.getInt(0));
            city.setName(cursor.getString(1));
            categories.add(city.getName());
            cities.add(city);
            cursor.moveToNext();
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    private void attemptLogin(Intent intent, int city_id, final EditText edtEmail, final EditText edtPhonenumber,
                              final Spinner spinnerGamesList, final EditText edtCreditCardNumber) {

        edtPhonenumber.setError(null);
        edtEmail.setError(null);

        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        String name = intent.getStringExtra("name");
        String phonenumber = edtPhonenumber.getText().toString();
        String email = edtEmail.getText().toString();


        // Reset errors.
        edtEmail.setError(null);
        edtPhonenumber.setError(null);
        edtCreditCardNumber.setError(null);

        final String creditCardNumber = edtCreditCardNumber.getText().toString();


        boolean cancel = false;
        View focusView = null;


//        if (TextUtils.isEmpty(creditCardNumber)) {
//            edtCreditCardNumber.setError(getString(R.string.error_field_required));
//            focusView = edtCreditCardNumber;
//            cancel = true;
//        }
//        if (TextUtils.isEmpty(phonenumber)) {
//            edtPhonenumber.setError(getString(R.string.this_field_is_required));
//            focusView = edtPhonenumber;
//            cancel = true;
//        }
//        if (TextUtils.isEmpty(email)) {
//            edtEmail.setError(getString(R.string.this_field_is_required));
//            focusView = edtEmail;
//            cancel = true;
//        }


        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    NetworkConnection networkConnection = new NetworkConnection();
//                    String query = "no query" + G.spliter + "login" + G.spliter + type + G.spliter + username + G.spliter + password;
//                    final String data = networkConnection.sendQuery(query);
//                    networkConnection.close();
//                    if (data != null && !data.equals("false") && !data.equals("None") && data.length() > 0) {
//                        startActivity(new Intent(SignupPlayer2.this, LoginActivity.class));
//                    }
//                    edtCreditCardNumber.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            showProgress(false);
//                        }
//                    });
                }
            }).start();
        }
    }


    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                int id = data.getIntExtra("id", -1);
                avatar_id = id;
                setAvatarPicture(id);
            }
        }
    }

    private void setAvatarPicture(int id)
    {
        Drawable image = Tools.readImagesFromAssetsFromId(this, G.AVATAR_PATH, id);
        btnAvatar.setImageDrawable(image);
    }
}

