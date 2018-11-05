package tv.merabihar.app.merabihar.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tv.merabihar.app.merabihar.UI.Activity.FollowOptions.FollowFragments.InterestFollowScreen;
import tv.merabihar.app.merabihar.UI.Activity.FriendList.DirectFriendFragment;
import tv.merabihar.app.merabihar.UI.Activity.FriendList.IndirectFriendFragment;
import tv.merabihar.app.merabihar.UI.Activity.Influencer.InfluencerTargetFragment;

/**
 * Created by ZingoHotels Tech on 05-11-2018.
 */

public class FriendListAdapter extends FragmentStatePagerAdapter {


    String[] tabTitles = {"Direct", "Indirect"};

    public FriendListAdapter(FragmentManager fm) {
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
                DirectFriendFragment people = new DirectFriendFragment();
                return people;

            case 1:
                IndirectFriendFragment interest = new IndirectFriendFragment();
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
