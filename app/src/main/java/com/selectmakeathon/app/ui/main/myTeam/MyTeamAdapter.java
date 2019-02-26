package com.selectmakeathon.app.ui.main.myTeam;

import com.selectmakeathon.app.ui.main.myTeam.TeamFrag.CurrentTeamFragment;
import com.selectmakeathon.app.ui.main.myTeam.TeamFrag.PendingTeamFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyTeamAdapter extends FragmentPagerAdapter {
    private int numOfTabs;

    public MyTeamAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs=numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            //case 0:
             //   return new CurrentTeamFragment();
            case 1:
                return new PendingTeamFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {

        return numOfTabs;
    }

}
