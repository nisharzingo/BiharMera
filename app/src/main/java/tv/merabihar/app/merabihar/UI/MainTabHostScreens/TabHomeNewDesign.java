package tv.merabihar.app.merabihar.UI.MainTabHostScreens;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import tv.merabihar.app.merabihar.Adapter.MainContentScreenAdapter;
import tv.merabihar.app.merabihar.R;

public class TabHomeNewDesign extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView mFollowPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_tab_home_new_design);

            tabLayout = (TabLayout) findViewById(R.id.main_title_tabs);
            tabLayout.setTabGravity(TabLayout.MODE_FIXED);
            viewPager = (ViewPager) findViewById(R.id.main_content_vp);

            MainContentScreenAdapter adapter = new MainContentScreenAdapter(getSupportFragmentManager());

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
