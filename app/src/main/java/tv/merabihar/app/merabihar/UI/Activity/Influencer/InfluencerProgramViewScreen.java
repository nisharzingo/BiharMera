package tv.merabihar.app.merabihar.UI.Activity.Influencer;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import tv.merabihar.app.merabihar.Adapter.InfluencerOptionAdapter;
import tv.merabihar.app.merabihar.R;

public class InfluencerProgramViewScreen extends AppCompatActivity implements TabLayout.OnTabSelectedListener  {


    TabLayout tabLayout;
    ViewPager viewPager;

    //int positionFragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_influencer_program_view_screen);
            Toolbar toolbar = (Toolbar)findViewById(R.id.collapsing_toolbar);
            AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.collapsing_toolbar_appbarlayout);


            tabLayout = (TabLayout) findViewById(R.id.influnecer_tabs);
            tabLayout.setTabGravity(TabLayout.MODE_FIXED);
            viewPager = (ViewPager) findViewById(R.id.main_content_vp);

            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();

            if(actionBar!=null)
            {
                // Display home menu item.
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            // Set collapsing tool bar title.
            final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar_layout);
            // Set collapsing tool bar image.
            ImageView collapsingToolbarImageView = (ImageView)findViewById(R.id.collapsing_toolbar_image_view);
            //collapsingToolbarImageView.setImageResource(R.drawable.img1);



            collapsingToolbarLayout.setTitle("Influencer Program");
            Picasso.with(InfluencerProgramViewScreen.this).load("https://www.martyrogers.co.uk/wp-content/uploads/2018/02/cashback-sites-e1518692271396.jpg)")
                    .placeholder(R.drawable.no_image).
                    error(R.drawable.theme_combo).into(collapsingToolbarImageView);

            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                boolean isShow = true;
                int scrollRange = -1;

                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.getTotalScrollRange();
                    }
                    if (scrollRange + verticalOffset == 0) {
                        collapsingToolbarLayout.setTitle("Influencer Program");
                        isShow = true;
                    } else if(isShow) {
                        collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                        isShow = false;
                    }
                }
            });

           /* Bundle bundle = getIntent().getExtras();
            if(bundle!=null){
                positionFragment = bundle.getInt("Position");

            }*/

            InfluencerOptionAdapter adapter = new InfluencerOptionAdapter(getSupportFragmentManager());

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        // When user click home menu item then quit this activity.
        if(itemId==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
