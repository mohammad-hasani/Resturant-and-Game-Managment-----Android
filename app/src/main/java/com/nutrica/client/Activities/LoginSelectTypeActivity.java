package com.nutrica.client.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nutrica.client.nutrica.R;

/**
 * Created by root on 15/10/2017.
 */

public class LoginSelectTypeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_select_type);

        Button btnAdmin = (Button) findViewById(R.id.btn_sign_in_admin);
        Button btnUser = (Button) findViewById(R.id.btn_sign_in_user);

        events(btnAdmin, btnUser);
    }

    private void events(Button btnAdmin, Button btnUser)
    {
        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginSelectTypeActivity.this, LoginActivityAdmin.class);
                startActivity(intent);
            }
        });
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginSelectTypeActivity.this, LoginActivityUser.class);
                startActivity(intent);
            }
        });
    }
}
