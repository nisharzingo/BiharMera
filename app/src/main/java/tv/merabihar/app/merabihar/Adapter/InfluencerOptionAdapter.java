package tv.merabihar.app.merabihar.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tv.merabihar.app.merabihar.Model.Interest;
import tv.merabihar.app.merabihar.UI.Activity.FollowOptions.FollowFragments.InterestFollowScreen;
import tv.merabihar.app.merabihar.UI.Activity.FollowOptions.FollowFragments.PeopleFollowingScreen;
import tv.merabihar.app.merabihar.UI.Activity.Influencer.InfluencerTargetFragment;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.HomeFragments.ForFollowersFragment;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.HomeFragments.ForYouFragment;

/**
 * Created by ZingoHotels Tech on 05-11-2018.
 */

public class InfluencerOptionAdapter  extends FragmentStatePagerAdapter {


    String[] tabTitles = {"Target", "Active Target"};

    public InfluencerOptionAdapter(FragmentManager fm) {
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
                InfluencerTargetFragment people = new InfluencerTargetFragment();
                return people;

            case 1:
                InterestFollowScreen interest = new InterestFollowScreen();
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
