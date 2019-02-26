package com.selectmakeathon.app.ui.main.myTeam;

import com.selectmakeathon.app.ui.main.myTeam.TeamFrag.CurrentTeamFrag;
import com.selectmakeathon.app.ui.main.myTeam.TeamFrag.PendingTeam;

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
            case 0:
                return new CurrentTeamFrag();
            case 1:
                return new PendingTeam();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {

        return numOfTabs;
    }

}
