package com.nutrica.client.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nutrica.client.Enum.Enums;
import com.nutrica.client.Fragments.AdminFragments.GameStreamLeague;
import com.nutrica.client.Fragments.AdminFragments.GameStreamTournament;
import com.nutrica.client.Fragments.AdminFragments.OrderFood;
import com.nutrica.client.Fragments.AdminFragments.NewGame;
import com.nutrica.client.Fragments.AdminFragments.OrderFoodReports;
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
                    OrderFood tab0 = new OrderFood();
                    return tab0;
                case 1:
                    OrderFoodReports tab1 = new OrderFoodReports();
                    return tab1;
                case 2:
                    NewGame tab2 = new NewGame();
                    return tab2;
                case 3:
                    SignupToGame tab3 = new SignupToGame();
                    return tab3;
                case 4:
                    GameStreamLeague tab4 = new GameStreamLeague();
                    return tab4;
                case 5:
                    GameStreamTournament tab5 = new GameStreamTournament();
                    return tab5;
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