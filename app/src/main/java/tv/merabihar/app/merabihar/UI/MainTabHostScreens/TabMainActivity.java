package tv.merabihar.app.merabihar.UI.MainTabHostScreens;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.SettingScreen;

public class TabMainActivity extends TabActivity implements TabHost.OnTabChangeListener{

    TabHost tabHost;
    View tabIndicatorHome,tabIndicatorStayView,tabIndicatorNotification,tabIndicatorVideo,tabIndicatorMenu;

    public static String HOME_TAB = "Home Tab";
    public static String SEARCH_TAB = "Search Tab";
    public static String STORY_TAB = "Story Tab";
    public static String VIDEO_TAB = "Video Tab";
    public static String ACCOUNT_TAB = "Account Tab";


    TextView labelHome, labelSearch, labelStory, labelVideo,labelAccount;
    ImageView imgHome, imgSearch, imgStory, imgVideo,imgAccount;

    int defaultValue = 0;
    public static final int MY_PERMISSIONS_REQUEST_RESULT = 1;

    String type="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {



            setContentView(R.layout.activity_tab_main);

            Bundle bundle = getIntent().getExtras();

            if(bundle!=null){

                type = bundle.getString("Activity");

            }




            tabHost = (TabHost) findViewById(android.R.id.tabhost);

            tabIndicatorHome = LayoutInflater.from(this).inflate(R.layout.tabhost_items_layout, null);
            tabIndicatorStayView= LayoutInflater.from(this).inflate(R.layout.tabhost_items_layout, null);
            tabIndicatorNotification = LayoutInflater.from(this).inflate(R.layout.tabhost_items_layout, null);
            tabIndicatorVideo = LayoutInflater.from(this).inflate(R.layout.tabhost_items_layout, null);
            tabIndicatorMenu = LayoutInflater.from(this).inflate(R.layout.tabhost_items_layout, null);

            labelHome = tabIndicatorHome.findViewById(R.id.tab_label);
            imgHome = tabIndicatorHome.findViewById(R.id.tab_image);

            labelSearch = tabIndicatorStayView.findViewById(R.id.tab_label);
            imgSearch = tabIndicatorStayView.findViewById(R.id.tab_image);

            labelStory = tabIndicatorNotification.findViewById(R.id.tab_label);
            imgStory = tabIndicatorNotification.findViewById(R.id.tab_image);

            labelVideo = tabIndicatorVideo.findViewById(R.id.tab_label);
            imgVideo = tabIndicatorVideo.findViewById(R.id.tab_image);

            labelAccount = tabIndicatorMenu.findViewById(R.id.tab_label);
            imgAccount = tabIndicatorMenu.findViewById(R.id.tab_image);

            TabHost.TabSpec tabHome = tabHost.newTabSpec(HOME_TAB);
            TabHost.TabSpec tabStay= tabHost.newTabSpec(SEARCH_TAB);
            TabHost.TabSpec tabNotification = tabHost.newTabSpec(STORY_TAB);
            TabHost.TabSpec tabVideo = tabHost.newTabSpec(VIDEO_TAB);
            TabHost.TabSpec tabMenu = tabHost.newTabSpec(ACCOUNT_TAB);
/*9C9C9C*/
            labelHome.setText(getResources().getString(R.string.home));
            imgHome.setImageResource(R.drawable.home_icon);
            tabHome.setIndicator(tabIndicatorHome);
            Intent dash = new Intent(this, TabHomeNewDesign.class);
            tabHome.setContent(dash);

            labelSearch.setText(getResources().getString(R.string.search));
            imgSearch.setImageResource(R.drawable.search_icons);
            tabStay.setIndicator(tabIndicatorStayView);
            tabStay.setContent(new Intent(this, TabSearchActivity2.class));
            //tabStay.setContent(new Intent(this, RoomViewStayActivity.class));

            labelStory.setText(getResources().getString(R.string.story));
            imgStory.setImageResource(R.drawable.add);
            tabNotification.setIndicator(tabIndicatorNotification);
            //tabNotification.setContent(new Intent(this, NotificationOptionsActivity.class));
            tabNotification.setContent(new Intent(this, TabPostMediaActivity.class));

            labelVideo.setText(getResources().getString(R.string.video));
            imgVideo.setImageResource(R.drawable.video_icon);
            tabVideo.setIndicator(tabIndicatorVideo);
            //tabNotification.setContent(new Intent(this, NotificationOptionsActivity.class));
            tabVideo.setContent(new Intent(this, TabVideoNewDesign.class));

            labelAccount.setText(getResources().getString(R.string.account));
            imgAccount.setImageResource(R.drawable.account_icon);
            tabMenu.setIndicator(tabIndicatorMenu);
            tabMenu.setContent(new Intent(this, TabAccountActivity.class));



            tabHost.setOnTabChangedListener(this);

            checkPermission();



           if(type!=null&&!type.isEmpty()){

                AlertDialog.Builder builder = new AlertDialog.Builder(TabMainActivity.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.earn_money_popup,null);

                final ImageView ad = (ImageView) view.findViewById(R.id.signin_popup_image);
                final Button now = (Button) view.findViewById(R.id.signin_popup_showContent_btn);
                final Button later = (Button) view.findViewById(R.id.signin_popup_earn_btn);
                final ImageView close = (ImageView) view.findViewById(R.id.close);


                builder.setView(view);

                final AlertDialog dialog = builder.create();

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());


            lp.width = 500;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setAttributes(lp);




                now.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        //tabHost.setBackgroundColor(null);


                    }
                });

               close.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       dialog.dismiss();


                   }
               });


                later.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        Intent approve = new Intent(TabMainActivity.this,SettingScreen.class);

                        startActivity(approve);

                    }
                });

                ad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    Intent approve = new Intent(TabMainActivity.this,SettingScreen.class);

                    startActivity(approve);

                }
            });

           }


            /** Add the tabs to the TabHost to display. */
            tabHost.addTab(tabHome);
            tabHost.addTab(tabStay);
            tabHost.addTab(tabNotification);
            tabHost.addTab(tabVideo);
            tabHost.addTab(tabMenu);


            int page = getIntent().getIntExtra("ARG_PAGE", defaultValue);



            int pageno = getIntent().getIntExtra("TABNAME",0);
            if(pageno != 0)
            {
                tabHost.setCurrentTab(pageno-1);
            }
            else
            {
                tabHost.setCurrentTab(page);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }


    @Override
    public void onTabChanged(String tabId) {

        labelHome.setTextColor(Color.parseColor("#000000"));
        labelHome.setTypeface(Typeface.DEFAULT);
        imgHome.setImageResource(R.drawable.home_icon);

        labelSearch.setTextColor(Color.parseColor("#000000"));
        labelSearch.setTypeface(Typeface.DEFAULT);
        imgSearch.setImageResource(R.drawable.search_icons);

        labelStory.setTextColor(Color.parseColor("#000000"));
        labelStory.setTypeface(Typeface.DEFAULT);
        imgStory.setImageResource(R.drawable.add);

        labelVideo.setTextColor(Color.parseColor("#000000"));
        labelVideo.setTypeface(Typeface.DEFAULT);
        imgVideo.setImageResource(R.drawable.video_icon);

        labelAccount.setTextColor(Color.parseColor("#000000"));
        labelAccount.setTypeface(Typeface.DEFAULT);
        imgAccount.setImageResource(R.drawable.account_icon);


        changeTabSelection(tabId);

    }

    public void changeTabSelection(String tabId) {
        if (HOME_TAB.equals(tabId)) {
            labelHome.setTextColor(Color.parseColor("#540CFA"));
            labelHome.setTypeface(null, Typeface.BOLD);
            imgHome.setImageResource(R.drawable.selected_home_icon);
        } else if (SEARCH_TAB.equals(tabId)) {
            labelSearch.setTextColor(Color.parseColor("#540CFA"));
            labelSearch.setTypeface(null, Typeface.BOLD);
            imgSearch.setImageResource(R.drawable.selected_search_icons);
        } else if (STORY_TAB.equals(tabId)) {
            labelStory.setTextColor(Color.parseColor("#540CFA"));
            labelStory.setTypeface(null, Typeface.BOLD);
            imgStory.setImageResource(R.drawable.add);
        } else if (VIDEO_TAB.equals(tabId)) {
            labelVideo.setTextColor(Color.parseColor("#540CFA"));
            labelVideo.setTypeface(null, Typeface.BOLD);
            imgVideo.setImageResource(R.drawable.selected_video_icon);
        } else if (ACCOUNT_TAB.equals(tabId)) {
            labelAccount.setTextColor(Color.parseColor("#540CFA"));
            labelAccount.setTypeface(null, Typeface.BOLD);
            imgAccount.setImageResource(R.drawable.selected_account_icon);
        }
    }

    public boolean checkPermission() throws Exception{
        if ((ContextCompat.checkSelfPermission(TabMainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(TabMainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)&&
                (ContextCompat.checkSelfPermission(TabMainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(TabMainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(TabMainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(TabMainActivity.this, android.Manifest.permission.CALL_PHONE))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(TabMainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(TabMainActivity.this, android.Manifest.permission.CAMERA))) {

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(TabMainActivity.this,
                        new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CALL_PHONE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_RESULT);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(TabMainActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CALL_PHONE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_RESULT);


            }
            return false;
        } else {
            return true;
        }
    }




}
