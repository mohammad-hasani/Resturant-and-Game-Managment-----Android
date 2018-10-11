package com.nutrica.client.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.nutrica.client.G;
import com.nutrica.client.Tools.NetworkConnection;
import com.nutrica.client.nutrica.R;


public class SignupPlayer extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;

    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_player);
        // Set up the login form.
        edtUsername = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        edtName = (EditText) findViewById(R.id.name);

        Button mEmailSignInButton = (Button) findViewById(R.id.btn_sing_up);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        Button btnSignup = (Button) findViewById(R.id.btn_sing_up);
        btnSignup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {

        // Reset errors.
        edtUsername.setError(null);
        edtPassword.setError(null);
        edtName.setError(null);

        final String username = edtUsername.getText().toString();
        final String password = edtPassword.getText().toString();
        final String name = edtName.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            edtUsername.setError(getString(R.string.this_field_is_required));
            focusView = edtUsername;
            cancel = true;
        }

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            edtPassword.setError(getString(R.string.this_password_is_too_short));
            focusView = edtPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(name)) {
            edtName.setError(getString(R.string.this_field_is_required));
            focusView = edtName;
            cancel = true;
        }

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
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                showProgress(false);
                                Intent intent = new Intent(SignupPlayer.this, SignupPlayer2.class);
//                                intent.putExtra("username", username);
//                                intent.putExtra("password", password);
//                                intent.putExtra("name", name);
                                startActivity(intent);
//                                finish();
//                            }
//                        });
//                    }

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

}

