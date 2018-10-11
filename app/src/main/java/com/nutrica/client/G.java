package com.nutrica.client;

import android.app.Application;
import android.content.Context;

import com.nutrica.client.Enum.Enums;
import com.nutrica.client.Tools.Tools;
import com.nutrica.client.nutrica.R;

/**
 * Created by root on 22/08/2017.
 */

public class G extends Application {
    public static String spliter = ":!!:";
    public static Enums.login_type loginType = Enums.login_type.Admin;
    public static int id = 1;

    public static String APP_PATH = "data/data/com.nutrica.client.nutrica";
    public static String FILE_DATABASE = APP_PATH + "/game.db";
    public static String AVATAR_PATH = "avatars";

    public static String[] ADMIN_TABS;
    public static String[] USER_TABS;

    public static int USER_ID = -1;
    public static int ADMIN_ID = -1;

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        fillTabsNames();
        Tools.copyDB(context);
    }

    private void fillTabsNames() {
        USER_TABS = new String[4];
        USER_TABS[0] = context.getString(R.string.main_page);
        USER_TABS[1] = context.getString(R.string.tournaments);
        USER_TABS[2] = context.getString(R.string.ranking);
        USER_TABS[3] = context.getString(R.string.gamenets);

        ADMIN_TABS = new String[8];
        ADMIN_TABS[0] = getString(R.string.main_page);
        ADMIN_TABS[1] = getString(R.string.ranking);
        ADMIN_TABS[2] = getString(R.string.tournaments);
        ADMIN_TABS[3] = getString(R.string.new_competition);
        ADMIN_TABS[4] = getString(R.string.league);
        ADMIN_TABS[5] = getString(R.string.tournament);
        ADMIN_TABS[6] = getString(R.string.order_food);
        ADMIN_TABS[7] = getString(R.string.order_managment);
    }
}
