package tv.merabihar.app.merabihar.UI.MainTabHostScreens;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;

import tv.merabihar.app.merabihar.Adapter.MainContentScreenAdapter;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.ContentDetailScreen;
import tv.merabihar.app.merabihar.UI.Activity.VideoPlayerScreen;
import tv.merabihar.app.merabihar.UI.YoutubePlayList.YouTubeListScreen;
import tv.merabihar.app.merabihar.Util.Util;

public class TabHomeNewDesign extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView mVideo;
    boolean isFirstTimePressed = false;

    public static final String PROGRESS_UPDATE = "progress_update";
    private static final int PERMISSION_REQUEST_CODE = 1;

    NativeExpressAdView mAdView;
    VideoController mVideoController;


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

                    if(Util.isNetworkAvailable(TabHomeNewDesign.this)){

                        Intent video  = new Intent(TabHomeNewDesign.this, VideoCategoryScreens.class);
                        //Intent video  = new Intent(TabHomeNewDesign.this, YouTubeListScreen.class);
                        startActivity(video);

                    }else {

                        Toast.makeText(TabHomeNewDesign.this, "Please check your intternet connection", Toast.LENGTH_SHORT).show();

                    }



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


        final Dialog main_dialog;

        LayoutInflater dialogLayout = LayoutInflater.from(TabHomeNewDesign.this);
        View DialogView = dialogLayout.inflate(R.layout.ad_exit_pop_up, null);

        main_dialog = new Dialog(TabHomeNewDesign.this, R.style.CustomAlertDialog);
        main_dialog.setContentView(DialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(main_dialog.getWindow().getAttributes());
        lp.width = (int)(getResources().getDisplayMetrics().widthPixels*.80);
        lp.height = (int)(getResources().getDisplayMetrics().heightPixels*.45);
        main_dialog.getWindow().setAttributes(lp);

        final TextView yes = (TextView) DialogView.findViewById(R.id.yes);
        final TextView no = (TextView) DialogView.findViewById(R.id.no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stopping the Asynctask

                TabHomeNewDesign.this.finish();
                main_dialog.dismiss();

            }
        });


        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stopping the Asynctask


                main_dialog.dismiss();

            }
        });

        mAdView = (NativeExpressAdView) DialogView.findViewById(R.id.adView);
        // Set its video options.
        mAdView.setVideoOptions(new VideoOptions.Builder()
                .setStartMuted(true)
                .build());

        // The VideoController can be used to get lifecycle events and info about an ad's video
        // asset. One will always be returned by getVideoController, even if the ad has no video
        // asset.
        mVideoController = mAdView.getVideoController();
        mVideoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            @Override
            public void onVideoEnd() {
                Log.d("Ad", "Video playback is finished.");
                super.onVideoEnd();
            }
        });

        // Set an AdListener for the AdView, so the Activity can take action when an ad has finished
        // loading.
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (mVideoController.hasVideoContent()) {
                    Log.d("Ad", "Received an ad that contains a video asset.");
                } else {
                    Log.d("Ad", "Received an ad that does not contain a video asset.");
                }
            }
        });


        mAdView.loadAd(new AdRequest.Builder().build());

        main_dialog.setCancelable(false);
        main_dialog.setCanceledOnTouchOutside(false);


        main_dialog.show();
        /*AlertDialog.Builder builder = new AlertDialog.Builder(TabHomeNewDesign.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.ad_exit_pop_up,null);

        final TextView yes = (TextView)view.findViewById(R.id.yes);
        final TextView no = (TextView)view.findViewById(R.id.no);



        builder.setView(view);
        *//*builder.setTitle("Blog Approve Need!");*//*
        final AlertDialog dialog = builder.create();
        dialog.show();





        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TabHomeNewDesign.this.finish();


            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });*/

    }

}
