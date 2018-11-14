package tv.merabihar.app.merabihar.UI.MainTabHostScreens;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.ContentListScreen;
import tv.merabihar.app.merabihar.UI.Activity.FriendList.FriendListScreen;
import tv.merabihar.app.merabihar.UI.Activity.Influencer.InviteFriendsScreen;
import tv.merabihar.app.merabihar.UI.Activity.Influencer.InviteScreen;

public class TabHomeHostScreen extends TabActivity implements TabHost.OnTabChangeListener{

    TabHost tabHost;
    ImageView mVideo;
    View tabIndicatorForYou,tabIndicatorFollow;

    public static String HOME_TAB = "Home Tab";
    public static String SEARCH_TAB = "Search Tab";



    TextView labelHome, labelSearch;
    LinearLayout linearForYou,linearFollow;


    int defaultValue = 0;
    public static final int MY_PERMISSIONS_REQUEST_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_tab_home_host_screen);

            tabHost = (TabHost) findViewById(android.R.id.tabhost);
            mVideo = (ImageView) findViewById(R.id.video);

            tabIndicatorForYou = LayoutInflater.from(this).inflate(R.layout.tab_host_home, null);
            tabIndicatorFollow= LayoutInflater.from(this).inflate(R.layout.tab_host_home, null);


            labelHome = tabIndicatorForYou.findViewById(R.id.tab_labe2);


            labelSearch = tabIndicatorFollow.findViewById(R.id.tab_labe2);

            linearForYou = tabIndicatorForYou.findViewById(R.id.tab_linear);


            linearFollow = tabIndicatorFollow.findViewById(R.id.tab_linear);


            TabHost.TabSpec tabHome = tabHost.newTabSpec(HOME_TAB);
            TabHost.TabSpec tabStay= tabHost.newTabSpec(SEARCH_TAB);

/*9C9C9C*/
            labelHome.setText(getResources().getString(R.string.for_you));

            tabHome.setIndicator(tabIndicatorForYou);
            Intent dash = new Intent(this, ContentListScreen.class);
            tabHome.setContent(dash);

            labelSearch.setText(getResources().getString(R.string.follow));
            tabStay.setIndicator(tabIndicatorFollow);
            tabStay.setContent(new Intent(this, FriendListScreen.class));
            //tabStay.setContent(new Intent(this, RoomViewStayActivity.class));




            tabHost.setOnTabChangedListener(this);







            /** Add the tabs to the TabHost to display. */
            tabHost.addTab(tabHome);
            tabHost.addTab(tabStay);



            int page = getIntent().getIntExtra("ARG_PAGE", defaultValue);



            int pageno = getIntent().getIntExtra("TABNAME",0);
            if(pageno != 0)
            {
                tabHost.setCurrentTab(pageno);
            }
            else
            {
                tabHost.setCurrentTab(page);
            }

            mVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent video  = new Intent(TabHomeHostScreen.this, VideoCategoryScreens.class);
                    //Intent video  = new Intent(TabHomeNewDesign.this, YouTubeListScreen.class);
                    startActivity(video);
                }
            });


        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }


    @Override
    public void onTabChanged(String tabId) {

        labelHome.setTextColor(Color.parseColor("#FFFFFF"));
        labelHome.setTypeface(Typeface.DEFAULT);



        labelSearch.setTextColor(Color.parseColor("#FFFFFF"));
        labelSearch.setTypeface(Typeface.DEFAULT);




        changeTabSelection(tabId);

    }

    public void changeTabSelection(String tabId) {
        if (HOME_TAB.equals(tabId)) {
            labelHome.setTextColor(Color.parseColor("#FF5AC5"));
            labelHome.setTypeface(null, Typeface.BOLD);
        } else if (SEARCH_TAB.equals(tabId)) {
            labelSearch.setTextColor(Color.parseColor("#FF5AC5"));
            labelSearch.setTypeface(null, Typeface.BOLD);
        }
    }






}
