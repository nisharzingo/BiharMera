package tv.merabihar.app.merabihar.UI.MainTabHostScreens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import tv.merabihar.app.merabihar.Adapter.MainContentScreenAdapter;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.VideoPlayerScreen;
import tv.merabihar.app.merabihar.UI.YoutubePlayList.YouTubeListScreen;

public class TabHomeNewDesign extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView mVideo;
    boolean isFirstTimePressed = false;

    public static final String PROGRESS_UPDATE = "progress_update";
    private static final int PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{


            setContentView(R.layout.activity_tab_home_new_design);

            tabLayout = (TabLayout) findViewById(R.id.main_title_tabs);
            tabLayout.setTabGravity(TabLayout.MODE_FIXED);
            viewPager = (ViewPager) findViewById(R.id.main_content_vp);
            mVideo = (ImageView) findViewById(R.id.video);

            MainContentScreenAdapter adapter = new MainContentScreenAdapter(getSupportFragmentManager());

            //Adding adapter to pager
            viewPager.setAdapter(adapter);


            //Adding onTabSelectedListener to swipe views
            tabLayout.addOnTabSelectedListener(this);
            tabLayout.setupWithViewPager(viewPager);

            mVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent video  = new Intent(TabHomeNewDesign.this, VideoCategoryScreens.class);
                    //Intent video  = new Intent(TabHomeNewDesign.this, YouTubeListScreen.class);
                    startActivity(video);
                }
            });


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
    public void onBackPressed() {




       /* new CountDownTimer(2000, 2000) {
            @Override
            public void onTick(long l) {
                if(!isFirstTimePressed)
                {
                    //System.out.println("isFirstTimePressed = "+isFirstTimePressed);
                    Toast.makeText(TabHomeNewDesign.this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                    isFirstTimePressed = true;
                }
                else
                {

                    isFirstTimePressed = false;
                    finish();

                }
            }

            @Override
            public void onFinish() {
                isFirstTimePressed = false;
            }
        }.start();*/

        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        TabHomeNewDesign.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }

}
