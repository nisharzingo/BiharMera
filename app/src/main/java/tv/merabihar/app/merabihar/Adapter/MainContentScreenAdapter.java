package tv.merabihar.app.merabihar.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tv.merabihar.app.merabihar.UI.MainTabHostScreens.HomeFragments.ForFollowersFragment;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.HomeFragments.ForYouFragment;

/**
 * Created by ZingoHotels Tech on 31-10-2018.
 */

public class MainContentScreenAdapter  extends FragmentStatePagerAdapter {


    String[] tabTitles = {"For You", "Follow"};

    public MainContentScreenAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ForYouFragment people = new ForYouFragment();
                return people;

            case 1:
                ForFollowersFragment interest = new ForFollowersFragment();
                return interest;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
