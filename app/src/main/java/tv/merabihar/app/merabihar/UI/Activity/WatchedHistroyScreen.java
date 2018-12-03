package tv.merabihar.app.merabihar.UI.Activity;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.FriendList.FriendListScreen;
import tv.merabihar.app.merabihar.UI.Activity.Influencer.InviteFriendsScreen;
import tv.merabihar.app.merabihar.UI.Activity.Influencer.InviteScreen;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ProfileAPI;

public class WatchedHistroyScreen extends TabActivity implements TabHost.OnTabChangeListener{

    TabHost tabHost;
    ImageView mBack;
    View tabIndicatorInvite,tabIndicatorFriend;

    public static String HOME_TAB = "Home Tab";
    public static String SEARCH_TAB = "Search Tab";



    TextView labelHome, labelSearch,mReferCode,mReferProfile;
    LinearLayout linearInvite,linearFriend;


    int defaultValue = 0;
    public static final int MY_PERMISSIONS_REQUEST_RESULT = 1;

    int profileId=0;
    String type="",rCode="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_watched_histroy_screen);

            tabHost = (TabHost) findViewById(android.R.id.tabhost);
            mBack = (ImageView) findViewById(R.id.back);
            mReferCode = (TextView) findViewById(R.id.referalCodeUsed);
            mReferProfile = (TextView) findViewById(R.id.referal_profile);

            Bundle bundle = getIntent().getExtras();

            if(bundle!=null){

                profileId = bundle.getInt("ProfileId");
                type = bundle.getString("FriendType");
                rCode = bundle.getString("ReferalCode");
            }

            tabIndicatorInvite = LayoutInflater.from(this).inflate(R.layout.tab_host_invite, null);
            tabIndicatorFriend= LayoutInflater.from(this).inflate(R.layout.tab_host_invite, null);


            labelHome = tabIndicatorInvite.findViewById(R.id.tab_labe2);


            labelSearch = tabIndicatorFriend.findViewById(R.id.tab_labe2);

            linearInvite = tabIndicatorInvite.findViewById(R.id.tab_linear);


            linearFriend = tabIndicatorFriend.findViewById(R.id.tab_linear);


            TabHost.TabSpec tabHome = tabHost.newTabSpec(HOME_TAB);
            TabHost.TabSpec tabStay= tabHost.newTabSpec(SEARCH_TAB);

/*9C9C9C*/
            labelHome.setText(getResources().getString(R.string.daily));

            if(rCode!=null&&!rCode.isEmpty()){

                mReferCode.setVisibility(View.VISIBLE);
                mReferCode.setText("Referal Code Used : "+rCode);

                if(rCode.contains("MBR")){

                    checkUserByReferalCode(rCode.replaceAll("MBR",""));
                }else{
                    checkUserByReferalCode(rCode);
                }
            }

            tabHome.setIndicator(tabIndicatorInvite);
            Intent dash = new Intent(this, TimeWatchedScreen.class);
            dash.putExtra("ProfileId",profileId);
            dash.putExtra("Friend",type);
            dash.putExtra("ReferalCode",rCode);
            tabHome.setContent(dash);

            labelSearch.setText(getResources().getString(R.string.target));
            tabStay.setIndicator(tabIndicatorFriend);
            Intent watch = new Intent(this, TargetWatchTime.class);
            watch.putExtra("ProfileId",profileId);
            watch.putExtra("Friend",type);
            watch.putExtra("ReferalCode",rCode);
            tabStay.setContent(watch);
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

            mBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    WatchedHistroyScreen.this.finish();
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

        labelHome.setTextColor(Color.parseColor("#540CFA"));
        labelHome.setTypeface(Typeface.DEFAULT);
        linearInvite.setBackgroundResource(R.drawable.login_ract);


        labelSearch.setTextColor(Color.parseColor("#540CFA"));
        labelSearch.setTypeface(Typeface.DEFAULT);
        linearFriend.setBackgroundResource(R.drawable.login_ract);



        changeTabSelection(tabId);

    }

    public void changeTabSelection(String tabId) {
        if (HOME_TAB.equals(tabId)) {
            labelHome.setTextColor(Color.parseColor("#FFFFFF"));
            linearInvite.setBackgroundColor(R.drawable.theme_combo);
            linearFriend.setBackgroundColor(Color.parseColor("#FFFFFF"));
            labelHome.setTypeface(null, Typeface.BOLD);
        } else if (SEARCH_TAB.equals(tabId)) {
            labelSearch.setTextColor(Color.parseColor("#FFFFFF"));
            linearFriend.setBackgroundColor(R.drawable.theme_combo);
            linearInvite.setBackgroundColor(Color.parseColor("#FFFFFF"));
            labelSearch.setTypeface(null, Typeface.BOLD);
        }
    }


    private void checkUserByReferalCode(final String i){


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileAPI apiService =
                        Util.getClient().create(ProfileAPI.class);

                Call<UserProfile> call = apiService.getProfileById(Integer.parseInt(i));

                call.enqueue(new Callback<UserProfile>() {
                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        if(statusCode == 200 || statusCode == 204)
                        {
                            UserProfile responseProfile = response.body();
                            if(responseProfile != null )
                            {



                                mReferProfile.setVisibility(View.VISIBLE);
                                mReferProfile.setText("Refered By "+responseProfile.getFullName());;

                            }
                            else
                            {


                            }
                        }
                        else
                        {

                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }



}
