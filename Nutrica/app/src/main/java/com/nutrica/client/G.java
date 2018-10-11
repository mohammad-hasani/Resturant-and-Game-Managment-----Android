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
    public static Enums.login_type loginType = Enums.login_type.Player;
    public static int id = -1;

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
    }
}
