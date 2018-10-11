package com.nutrica.client.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nutrica.client.Enum.Enums;
import com.nutrica.client.Fragments.AdminFragments.AdminCompetition;
import com.nutrica.client.Fragments.AdminFragments.AdminMainPage;
import com.nutrica.client.Fragments.AdminFragments.AdminRanking;
import com.nutrica.client.Fragments.AdminFragments.GameStreamLeague;
import com.nutrica.client.Fragments.AdminFragments.GameStreamTournament;
import com.nutrica.client.Fragments.AdminFragments.AdminOrderFood;
import com.nutrica.client.Fragments.AdminFragments.AdminNewCompetition;
import com.nutrica.client.Fragments.AdminFragments.AdminOrderFoodReports;
import com.nutrica.client.Fragments.AdminFragments.SignupToGame;
import com.nutrica.client.Fragments.UserFragments.UserMainPage;
import com.nutrica.client.Fragments.UserFragments.UserCompetition;
import com.nutrica.client.Fragments.UserFragments.UserGamenets;
import com.nutrica.client.Fragments.UserFragments.UserRanking;
import com.nutrica.client.G;

public class MainActivityPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public MainActivityPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        if (G.loginType == Enums.login_type.Admin) {
            switch (position) {
                case 0:
                    AdminMainPage tab0 = new AdminMainPage();
                    return tab0;
                case 1:
                    AdminRanking tab1 = new AdminRanking();
                    return tab1;
                case 2:
                    AdminCompetition tab2 = new AdminCompetition();
                    return tab2;
                case 3:
                    AdminNewCompetition tab3 = new AdminNewCompetition();
                    return tab3;
                case 4:
                    GameStreamLeague tab4 = new GameStreamLeague();
                    return tab4;
                case 5:
                    GameStreamTournament tab5 = new GameStreamTournament();
                    return tab5;
                case 6:
                    AdminOrderFood tab6 = new AdminOrderFood();
                    return tab6;
                case 7:
                    AdminOrderFoodReports tab7 = new AdminOrderFoodReports();
                    return tab7;
                default:
                    return null;
            }
        } else if (G.loginType == Enums.login_type.Player) {
            switch (position) {
                case 0:
                    UserMainPage tab0 = new UserMainPage();
                    return tab0;
                case 1:
                    UserCompetition tab1 = new UserCompetition();
                    return tab1;
                case 2:
                    UserRanking tab2 = new UserRanking();
                    return tab2;
                case 3:
                    UserGamenets tab3 = new UserGamenets();
                    return tab3;
                default:
                    return null;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}