package com.nutrica.client.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.nutrica.client.Enum.Enums;
import com.nutrica.client.G;
import com.nutrica.client.adapters.MainActivityPagerAdapter;
import com.nutrica.client.nutrica.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (G.loginType == Enums.login_type.Admin) {
            for (int i = 0; i < G.ADMIN_TABS.length; i++) {
                tabLayout.addTab(tabLayout.newTab().setText(G.ADMIN_TABS[i]));
            }
        } else if (G.loginType == Enums.login_type.Player) {
            for (int i = 0; i < G.USER_TABS.length; i++) {
                tabLayout.addTab(tabLayout.newTab().setText(G.USER_TABS[i]));
            }
        }
//        tabLayout.addTab(tabLayout.newTab().setText("Order Food"));
//        tabLayout.addTab(tabLayout.newTab().setText("Order Food Reports"));
//        tabLayout.addTab(tabLayout.newTab().setText("New Game"));
//        tabLayout.addTab(tabLayout.newTab().setText("Signup"));
//        tabLayout.addTab(tabLayout.newTab().setText("Game Stream League"));
//        tabLayout.addTab(tabLayout.newTab().setText("Game Stream Tournament"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final MainActivityPagerAdapter adapter = new MainActivityPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            Intent i = new Intent(MainActivity.this, SignupPlayer2.class);
            i.putExtra("id", G.id);
            startActivity(i);
            return true;
        }
//        else if (id == R.id.action_notification) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

}