package tv.merabihar.app.merabihar.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tv.merabihar.app.merabihar.Rss.SourceSearchFragment;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.SearchScreens.Fragments.InterestSearchFragment;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.SearchScreens.Fragments.PeopleSearchFragment;

/**
 * Created by ZingoHotels Tech on 30-11-2018.
 */

public class NewsSearchAdapter  extends FragmentStatePagerAdapter {


    String[] tabTitles = {"Sources", "Keywords"};

    public NewsSearchAdapter(FragmentManager fm) {
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
                SourceSearchFragment sources = new SourceSearchFragment();
                return sources;

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
