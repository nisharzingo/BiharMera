package tv.merabihar.app.merabihar.UI.Activity.FriendList;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tv.merabihar.app.merabihar.Adapter.FriendListAdapter;
import tv.merabihar.app.merabihar.Adapter.InfluencerOptionAdapter;
import tv.merabihar.app.merabihar.R;

public class FriendListScreen extends AppCompatActivity implements TabLayout.OnTabSelectedListener  {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_friend_list_screen);

            tabLayout = (TabLayout) findViewById(R.id.influnecer_friends_tabs);
            tabLayout.setTabGravity(TabLayout.MODE_FIXED);
            viewPager = (ViewPager) findViewById(R.id.friends_list_vp);

            FriendListAdapter adapter = new FriendListAdapter(getSupportFragmentManager());

            //Adding adapter to pager
            viewPager.setAdapter(adapter);

            //Adding onTabSelectedListener to swipe views
            tabLayout.addOnTabSelectedListener(this);
            tabLayout.setupWithViewPager(viewPager);


        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
