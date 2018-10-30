package tv.merabihar.app.merabihar.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tv.merabihar.app.merabihar.UI.SlideFragments.SlideOne;
import tv.merabihar.app.merabihar.UI.SlideFragments.SlideTwo;

/**
 * Created by ZingoHotels Tech on 30-10-2018.
 */

public class SlideAdapter extends FragmentStatePagerAdapter {

    public SlideAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SlideOne tab1 = new SlideOne();
                return tab1;
            case 1:
                SlideTwo tab2 = new SlideTwo();
                return tab2;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
