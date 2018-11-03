package tv.merabihar.app.merabihar.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tv.merabihar.app.merabihar.UI.Activity.FollowOptions.FollowFragments.CategoryFollowScreen;
import tv.merabihar.app.merabihar.UI.Activity.FollowOptions.FollowFragments.InterestFollowScreen;
import tv.merabihar.app.merabihar.UI.Activity.FollowOptions.FollowFragments.PeopleFollowingScreen;

/**
 * Created by ZingoHotels Tech on 03-11-2018.
 */

public class FollowingPageAdapter extends FragmentStatePagerAdapter {


    String[] tabTitles = {"People", "Interests", "Collection"};

    public FollowingPageAdapter(FragmentManager fm) {
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
                PeopleFollowingScreen people = new PeopleFollowingScreen();
                return people;

            case 1:
                InterestFollowScreen interest = new InterestFollowScreen();
                return interest;

            case 2:
                CategoryFollowScreen collection = new CategoryFollowScreen();
                return collection;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
