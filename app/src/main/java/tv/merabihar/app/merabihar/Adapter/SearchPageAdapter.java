package tv.merabihar.app.merabihar.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tv.merabihar.app.merabihar.UI.MainTabHostScreens.SearchScreens.Fragments.InterestSearchFragment;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.SearchScreens.Fragments.PeopleSearchFragment;

/**
 * Created by ZingoHotels Tech on 01-11-2018.
 */

public class SearchPageAdapter extends FragmentStatePagerAdapter {


    String[] tabTitles = {"People", "Interests"};

    public SearchPageAdapter(FragmentManager fm) {
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
                PeopleSearchFragment people = new PeopleSearchFragment();
                return people;

            case 1:
                InterestSearchFragment interest = new InterestSearchFragment();
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
