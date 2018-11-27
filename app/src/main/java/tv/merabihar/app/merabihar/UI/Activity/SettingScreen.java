package tv.merabihar.app.merabihar.UI.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import tv.merabihar.app.merabihar.Adapter.NavigationListAdapter;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Roboto_Regular;
import tv.merabihar.app.merabihar.CustomViews.SnackbarViewer;
import tv.merabihar.app.merabihar.Model.NavBarItems;
import tv.merabihar.app.merabihar.Model.SubscribedGoals;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.FriendList.FriendListScreen;
import tv.merabihar.app.merabihar.UI.Activity.Influencer.Income;
import tv.merabihar.app.merabihar.UI.Activity.Influencer.InfluencerProgramViewScreen;
import tv.merabihar.app.merabihar.UI.Activity.Influencer.InviteFriendsScreen;
import tv.merabihar.app.merabihar.UI.Activity.Influencer.InviteScreen;
import tv.merabihar.app.merabihar.UI.Activity.Influencer.Records;
import tv.merabihar.app.merabihar.UI.Activity.Influencer.WithdrawMoney;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ProfileAPI;
import tv.merabihar.app.merabihar.WebAPI.ProfileFollowAPI;
import tv.merabihar.app.merabihar.WebAPI.SubscribedGoalsAPI;

public class SettingScreen extends AppCompatActivity {

    MyTextView_Roboto_Regular mProfileName,mReferalCode;
    CircleImageView mProfilePhoto;
    ProgressBar progressBar;
    LinearLayout mWhatsapp,mFaceBook,mSms,mMore,mInviteScreen;
    TextView mCoins,mBalance,mInvite,mVideoCoins;
    LinearLayout withdraw_btn, records_btn, income_btn;
    LinearLayout coints_txt_btn, bal_txt_btn, invite_txt_btn;


    ListView navBarListView;
    String[] title = null;

    int profileId=0,wallet=0,coinsUsed=0;

    String referalCode,referCodeProfile;

    UserProfile profiles;

    String shareContent,type,nonreed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_setting_screen);

            shareContent = "Hi friends I get 50 coins from Mera Bihar, Install Mera Bihar and use my referral code and get 50 coins immediately.\n\n Use my referal code for Sign-Up MBR"+PreferenceHandler.getInstance(SettingScreen.this).getUserId()+"\n http://bit.ly/2JXcOnw";

            mProfileName = (MyTextView_Roboto_Regular)findViewById(R.id.profile_name_settings);
            mReferalCode = (MyTextView_Roboto_Regular)findViewById(R.id.profile_referal_code_settings);
            mProfilePhoto = (CircleImageView)findViewById(R.id.profile_photo_settings);

            progressBar = (ProgressBar)findViewById(R.id.progressBar_settings);

            mWhatsapp = (LinearLayout)findViewById(R.id.whatsapp_invite);
            mFaceBook = (LinearLayout)findViewById(R.id.facebook_invite);
            mSms = (LinearLayout)findViewById(R.id.sms_invite);
            mMore = (LinearLayout)findViewById(R.id.more_invite);
            mInviteScreen = (LinearLayout)findViewById(R.id.invite_screen);

            mCoins = (TextView) findViewById(R.id.coins_value);
            mBalance = (TextView)findViewById(R.id.balance_value);
            mInvite = (TextView)findViewById(R.id.invite_value);
            mVideoCoins = (TextView)findViewById(R.id.videoCoins);

            records_btn  = findViewById(R.id.balance_ll_btn);
            coints_txt_btn = findViewById(R.id.coins_txt_btn);
            bal_txt_btn = findViewById(R.id.bal_txt_btn);
            invite_txt_btn = findViewById(R.id.invite_txt_btn);
            income_btn = findViewById(R.id.income_ll_btn);

            withdraw_btn = findViewById(R.id.withdraw_ll_btn);


            records_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent recordsIntent = new Intent(SettingScreen.this, Records.class);
                    startActivity(recordsIntent);
                }
            });

            invite_txt_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inviteIntent = new Intent(SettingScreen.this, InviteFriendsScreen.class);
                    startActivity(inviteIntent);
                }
            });



            income_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    openIncome(mCoins.getText().toString());
                }
            });

            coints_txt_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    openIncome(mCoins.getText().toString());

                }
            });


            bal_txt_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    openIncome(mCoins.getText().toString());

                }
            });



            withdraw_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent withDrawIntent = new Intent(SettingScreen.this, WithdrawMoney.class);
                    //get data from api
                    withDrawIntent.putExtra("rupees_value", mCoins.getText().toString());
                    startActivity(withDrawIntent);
                }
            });

            profileId = PreferenceHandler.getInstance(SettingScreen.this).getUserId();
            if(profileId!=0){

                if (Util.isNetworkAvailable(SettingScreen.this)) {
                    getProfile(profileId);
                }else{
                    SnackbarViewer.showSnackbar(findViewById(R.id.settings_screen_rl),"No Internet connection");
                    progressBar.setVisibility(View.GONE);
                }

            }else{
                SnackbarViewer.showSnackbar(findViewById(R.id.settings_screen_rl),"Something went wrong ! ");
                progressBar.setVisibility(View.GONE);

//                Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }

            navBarListView = findViewById(R.id.settings_list);

            setUpNavbarBasedOnRole();

            navBarListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    try
                    {
                        displayViewBasedOnRoles(title[position]);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            });


            mProfilePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SettingScreen.this.finish();
                }
            });



            mWhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                    whatsappIntent.setType("text/plain");
                    whatsappIntent.setPackage("com.whatsapp");
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
                    try {
                        Objects.requireNonNull(SettingScreen.this).startActivity(whatsappIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp")));
                    }

                }
            });

            mFaceBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String textBody = shareContent;
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT,!TextUtils.isEmpty(textBody) ? textBody : "");

                    boolean facebookAppFound = false;
                    List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo info : matches) {
                        if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana") ||
                                info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.lite")) {
                            intent.setPackage(info.activityInfo.packageName);
                            facebookAppFound = true;
                            break;
                        }
                    }
                    if (!facebookAppFound) {
                        String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + "https://play.google.com/store/apps/details?id=app.zingo.bihartourismguide";
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                    }
                    startActivity(intent);
                }
            });

            mSms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("sms_body", shareContent);

                    if (smsIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(smsIntent);
                    } else {
                             Log.d("SMS", "Can't resolve app for ACTION_SENDTO Intent");
                             try {
                                Intent smsIntent2 = new Intent(android.content.Intent.ACTION_VIEW);
                                smsIntent2.putExtra("sms_body", shareContent);
                                smsIntent2.setData(Uri.parse("sms:"));
                                startActivity(smsIntent2);
                             } catch (android.content.ActivityNotFoundException anfe) {
                                        Log.d("Error SMS" , "Error");
                                }
                        }
                }
            });

            mMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, "Send to"));
                }
            });

            mInviteScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent friend = new Intent(SettingScreen.this, InviteScreen.class);
                    startActivity(friend);
                }
            });



        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getProfile(final int id){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ProfileAPI subCategoryAPI = Util.getClient().create(ProfileAPI.class);
                Call<UserProfile> getProf = subCategoryAPI.getProfileById(id);
                //Call<ArrayList<Blogs>> getBlog = blogApi.getBlogs();

                getProf.enqueue(new Callback<UserProfile>() {

                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.code() == 200)
                        {
                            System.out.println("Inside api");

                            UserProfile profile = response.body();

                            profiles = response.body();



                            mProfileName.setText(""+profile.getFullName());
                            String ref = "MBR"+profile.getProfileId();
                            String referCodeText = ref;
                            //String referCodeText = Base64.encodeToString(ref.getBytes(), Base64.DEFAULT);
                            PreferenceHandler.getInstance(SettingScreen.this).setReferalcode(ref);
                            if(profile.getReferralCodeToUseForOtherProfile()!=null){

                                if(referCodeText.equals(profile.getReferralCodeToUseForOtherProfile())){
                                    mReferalCode.setText(""+profile.getReferralCodeToUseForOtherProfile());
                                }else{
                                    mReferalCode.setText(""+referCodeText);
                                }


                            }else{

                                ref = "MBR"+profile.getProfileId();
                                referCodeText = ref;
                                //referCodeText = Base64.encodeToString(ref.getBytes(), Base64.DEFAULT);
                                mReferalCode.setText(""+referCodeText);
                            }

                            coinsUsed = profile.getUsedAmount();
                            wallet = profile.getWalletBalance();

                            // open income activity
                           // openIncomeActivity( String.valueOf(coinsUsed), String.valueOf( (coinsUsed*1.0)/100  ) );
                            //openWithDrawActivity(String.valueOf( (coinsUsed*1.0)/100  ) );

                            referCodeProfile = "MBR"+profile.getProfileId();
                            if(profile.getProfilePhoto()!=null){

                                String base=profile.getProfilePhoto();

                                if(base != null && !base.isEmpty()){
                                    Picasso.with(SettingScreen.this).load(base).placeholder(R.drawable.profile_image).
                                            error(R.drawable.profile_image).into(mProfilePhoto);

                                }else{
                                    mProfilePhoto.setImageResource(R.drawable.profile_image);
                                }
                            }

                            referalCode = PreferenceHandler.getInstance(SettingScreen.this).getReferalcode();


                            if (Util.isNetworkAvailable(SettingScreen.this)) {
                                getGoalsByProfileId(profile.getProfileId(),profile);
                            }else{
                                SnackbarViewer.showSnackbar(findViewById(R.id.settings_screen_rl),"No Internet connection");
                                progressBar.setVisibility(View.GONE);
                            }





                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {

                    }
                });

            }

        });
    }

    public void UpdateProfile(final UserProfile userProfile){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ProfileAPI subCategoryAPI = Util.getClient().create(ProfileAPI.class);
                Call<UserProfile> getProf = subCategoryAPI.updateProfile(userProfile.getProfileId(),userProfile);
                //Call<ArrayList<Blogs>> getBlog = blogApi.getBlogs();

                getProf.enqueue(new Callback<UserProfile>() {

                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.code() == 204)
                        {/*
                            System.out.println("Inside api");

                            UserProfile profile = response.body();

                            PreferenceHandler.getInstance(SettingScreen.this).setReferalcode(profile.getReferralCodeToUseForOtherProfile());

                            mProfileName.setText(""+profile.getFullName());
                            if(profile.getReferralCodeToUseForOtherProfile()!=null){

                                mReferalCode.setText(""+profile.getReferralCodeToUseForOtherProfile());
                            }else{

                                mReferalCode.setText("MR"+profile.getProfileId());
                            }


                            if(profile.getProfilePhoto()!=null){

                                String base=profile.getProfilePhoto();



                                if(base != null && !base.isEmpty()){
                                    Picasso.with(SettingScreen.this).load(base).placeholder(R.drawable.profile_image).
                                            error(R.drawable.profile_image).into(mProfilePhoto);

                                }else{
                                    mProfilePhoto.setImageResource(R.drawable.profile_image);
                                }
                            }


*/
                            SettingScreen.this.finish();

                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {

                    }
                });

            }

        });
    }

    private void setUpNavbarBasedOnRole() throws Exception{




        title = getResources().getStringArray(R.array.nav_user);


        if(title != null)
        {
            ArrayList<NavBarItems> navBarItemsList = new ArrayList<>();

            for (String t:title) {

                NavBarItems navbarItem = new NavBarItems(t);
                navBarItemsList.add(navbarItem);

            }

            NavigationListAdapter adapter = new NavigationListAdapter(getApplicationContext(),navBarItemsList);
            navBarListView.setAdapter(adapter);
            setListViewHeightBasedOnChildren(navBarListView);

        }

    }

    public void displayViewBasedOnRoles(String i) throws Exception
    {
        switch (i)
        {

            case "Check-In":
                break;

            case "Influencer Program":

                Intent influencer = new Intent(SettingScreen.this, InfluencerProgramViewScreen.class);
                startActivity(influencer);
                break;

            case "Time Watched":

                Intent tw = new Intent(SettingScreen.this, WatchedHistroyScreen.class);
                startActivity(tw);
                break;


            case "Friends List":
                Intent friend = new Intent(SettingScreen.this, FriendListScreen.class);
                startActivity(friend);
                break;



            case "Logout":

                PreferenceHandler.getInstance(SettingScreen.this).clear();
                Toast.makeText(SettingScreen.this,"Logout done successfully",Toast.LENGTH_SHORT).show();
                PreferenceHandler.getInstance(SettingScreen.this).clear();
                Intent log = new Intent(SettingScreen.this, SlideOptionScreen.class);
                log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(log);
                finish();
                break;
        }
    }

    public  void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))+20;
        listView.setLayoutParams(params);
        listView.requestLayout();
        //mImagesList.getChildAt(0).requestFocus();
    }

    private void getDirectRefer(final String code,final double coinsValue,final double rupees){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getDirectReferedProfile(code);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();


                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {
                                //Collections.shuffle(responseProfile);


                                getInDirectRefer(code,responseProfile.size(),coinsValue,rupees);


                            }
                            else
                            {
                                double coinsValues = coinsValue -wallet;

                                if(coinsValues>0){
                                    mCoins.setText(""+(int)coinsValues);
                                    mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((coinsValues*1.0)/100.0)));
                                }else{
                                    mCoins.setText(""+(int)coinsValue);
                                    mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((coinsValue*1.0)/100.0)));
                                }


                            }
                        }
                        else
                        {

                            double coinsValues = coinsValue -wallet;

                            if(coinsValues>0){
                                mCoins.setText(""+(int)coinsValues);
                                mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((coinsValues*1.0)/100.0)));
                            }else{
                                mCoins.setText(""+(int)coinsValue);
                                mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((coinsValue*1.0)/100.0)));
                            }

                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed
                        double coinsValues = coinsValue -wallet;

                        if(coinsValues>0){
                            mCoins.setText(""+(int)coinsValues);
                            mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((coinsValues*1.0)/100.0)));
                        }else{
                            mCoins.setText(""+(int)coinsValue);
                            mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((coinsValue*1.0)/100.0)));
                        }

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    private void getInDirectRefer(final String code,final int directCount,final double coinsValue,final double rupees){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getInDirectReferedProfile(code);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();


                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {
                                //Collections.shuffle(responseProfile);


                                mInvite.setText(""+(responseProfile.size()));

                                int indirectCount = responseProfile.size();
                                int balance = (indirectCount - directCount);


                                double amount = (balance * 10)+(directCount*50)+coinsValue-wallet;




                                if(amount>0){
                                    mCoins.setText(""+(int)amount);
                                    mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((amount*1.0)/100.0)));
                                }else{
                                    mCoins.setText("0");
                                    mBalance.setText("Rs 0");
                                }


                                //mBalance.setText("Rs "+((amount*1.0)/100.0));


                            }
                            else
                            {

                                getInDirectParentRefer(code,directCount,coinsValue,rupees);
                               /* mInvite.setText(""+directCount);
                                mCoins.setText(""+(int)coinsValue);
                                mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((coinsValue*1.0)/100.0)));*/

                            }
                        }
                        else
                        {
                            getInDirectParentRefer(code,directCount,coinsValue,rupees);
                               /* mInvite.setText(""+directCount);
                                mCoins.setText(""+(int)coinsValue);
                                mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((coinsValue*1.0)/100.0)));*/

                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed
                        getInDirectParentRefer(code,directCount,coinsValue,rupees);
                               /* mInvite.setText(""+directCount);
                                mCoins.setText(""+(int)coinsValue);
                                mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((coinsValue*1.0)/100.0)));*/


                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    private void getInDirectParentRefer(final String code,final int directCount,final double coinsValue,final double rupees){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getInDirectReferedParentProfile(code);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();


                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {
                                //Collections.shuffle(responseProfile);


                                mInvite.setText(""+(responseProfile.size()+directCount));

                                int indirectCount = responseProfile.size();
                                int balance = (indirectCount);


                                double amount = (balance * 10)+(directCount*50)+coinsValue-wallet;



                                if(amount>0){
                                    mCoins.setText(""+(int)amount);
                                    mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((amount*1.0)/100.0)));
                                }else{
                                    mCoins.setText("0");
                                    mBalance.setText("Rs 0");
                                }
                                //mBalance.setText("Rs "+((amount*1.0)/100.0));


                            }
                            else
                            {

                                double amount =(directCount*50)+coinsValue-wallet;
                                mInvite.setText(""+directCount);
                                if(amount>0){
                                    mCoins.setText(""+(int)amount);
                                    mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((amount*1.0)/100.0)));
                                }else{
                                    mCoins.setText("0");
                                    mBalance.setText("Rs 0");
                                }

                            }
                        }
                        else
                        {
                            double amount =(directCount*50)+coinsValue-wallet;
                            mInvite.setText(""+directCount);
                            if(amount>0){
                                mCoins.setText(""+(int)amount);
                                mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((amount*1.0)/100.0)));
                            }else{
                                mCoins.setText("0");
                                mBalance.setText("Rs 0");
                            }

                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed
                        double amount =(directCount*50)+coinsValue-wallet;
                        mInvite.setText(""+directCount);
                        if(amount>0){
                            mCoins.setText(""+(int)amount);
                            mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((amount*1.0)/100.0)));
                        }else{
                            mCoins.setText("0");
                            mBalance.setText("Rs 0");
                        }
                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        boolean update = false;

        if(profiles!=null){

            if(referCodeProfile==null||referCodeProfile.isEmpty()||!referCodeProfile.equals(mReferalCode.getText().toString())){

                String converted = Base64.encodeToString(mReferalCode.getText().toString().getBytes(), Base64.DEFAULT);

                profiles.setReferralCodeToUseForOtherProfile(referCodeProfile);
                update = true;

            }

            int coins = Integer.parseInt(mCoins.getText().toString());

            String balanceText = mBalance.getText().toString();
            double balance = 0;

            if(balanceText!=null&&!balanceText.isEmpty()&&balanceText.contains("Rs")){

                balance = Double.parseDouble(balanceText.split(" ")[1]);


            }

            if(coinsUsed!=coins){

                profiles.setUsedAmount(coins);
                update = true;

            }




            if(update){

                UpdateProfile(profiles);
            }


        }else{
            SettingScreen.this.finish();
        }
    }

    // opening the incomeActivity
    private void openIncomeActivity( final String coins, final String rupees) {





    }

    private void openIncome(String coins) {
        Intent incomeIntent = new Intent(SettingScreen.this, Income.class);
        //get data from api
        incomeIntent.putExtra("coins_value", coins);
        incomeIntent.putExtra("non_reed", nonreed);
        incomeIntent.putExtra("type", type);
        startActivity(incomeIntent);

    }


    private void openWithDrawActivity(final String rupees) {

        withdraw_btn = findViewById(R.id.withdraw_ll_btn);

        withdraw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent withDrawIntent = new Intent(SettingScreen.this, WithdrawMoney.class);
                //get data from api
                withDrawIntent.putExtra("rupees_value", rupees);
                startActivity(withDrawIntent);
            }
        });
    }
    public void getGoalsByProfileId(final int id,final UserProfile profile)
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final SubscribedGoalsAPI categoryAPI = Util.getClient().create(SubscribedGoalsAPI.class);
                Call<ArrayList<SubscribedGoals>> getCat = categoryAPI.getSubscribedGoalsByProfileIdAndGoal(id,3);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<SubscribedGoals>>() {

                    @Override
                    public void onResponse(Call<ArrayList<SubscribedGoals>> call, Response<ArrayList<SubscribedGoals>> response) {



                        if(response.code() == 200 && response.body()!= null)
                        {

                            if(response.body().size()!=0){

                                SubscribedGoals sg = response.body().get(0);

                                if(sg!=null){

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                                    String expdate = sg.getEndDate();
                                    type = sg.getStatus();
                                    double valuer = (int)(Integer.parseInt(sg.getRewardsEarned()))*.20;
                                    nonreed = ""+(int)valuer;
                                    Date past = null;


                                    String duration = null;

                                    if(expdate.contains("T")){

                                        String[] tDates = expdate.split("T");
                                        try {
                                            past = dateFormat.parse(tDates[0]);
                                            duration = duration(tDates[0]);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    if ( new Date().getTime() < past.getTime()) {

                                        double amount = profiles.getReferralAmount();
                                        double valuea = (int)(Integer.parseInt(sg.getRewardsEarned()))*.20;

                                        if(sg.getStatus().equalsIgnoreCase("Activated")){

                                            mVideoCoins.setText("Non Redeemable Coins : "+(int)valuea);

                                            if(referalCode!=null&&!referalCode.isEmpty()){
                                                getDirectRefer(referalCode,profile.getReferralAmount(),profile.getReferralAmountForOtherProfile());
                                            }else{
                                                referalCode = "MBR"+PreferenceHandler.getInstance(SettingScreen.this).getUserId();
                                                getDirectRefer(referalCode,profile.getReferralAmount(),profile.getReferralAmountForOtherProfile());
                                            }

                                        }else if(sg.getStatus().equalsIgnoreCase("Completed")){

                                            mVideoCoins.setText("Non Redeemable Coins : 0");
                                            if(referalCode!=null&&!referalCode.isEmpty()){
                                                getDirectRefer(referalCode,profile.getReferralAmount()+(int)valuea,profile.getReferralAmountForOtherProfile());
                                            }else{
                                                referalCode = "MBR"+PreferenceHandler.getInstance(SettingScreen.this).getUserId();
                                                getDirectRefer(referalCode,profile.getReferralAmount()+(int)valuea,profile.getReferralAmountForOtherProfile());
                                            }
                                        }else if(sg.getStatus().equalsIgnoreCase("Penalty")){

                                            mVideoCoins.setText("Non Redeemable Coins : "+(int)valuea);

                                            if(referalCode!=null&&!referalCode.isEmpty()){
                                                getDirectRefer(referalCode,profile.getReferralAmount(),profile.getReferralAmountForOtherProfile());
                                            }else{
                                                referalCode = "MBR"+PreferenceHandler.getInstance(SettingScreen.this).getUserId();
                                                getDirectRefer(referalCode,profile.getReferralAmount(),profile.getReferralAmountForOtherProfile());
                                            }
                                        }

                                    }else{

                                        double amount = profiles.getReferralAmount();
                                        double valuea = (int)(Integer.parseInt(sg.getRewardsEarned()))*.20;

                                        if(sg.getStatus().equalsIgnoreCase("Activated")){

                                            mVideoCoins.setText("Non Redeemable Coins : "+(int)valuea);

                                            if(referalCode!=null&&!referalCode.isEmpty()){
                                                getDirectRefer(referalCode,profile.getReferralAmount(),profile.getReferralAmountForOtherProfile());
                                            }else{
                                                referalCode = "MBR"+PreferenceHandler.getInstance(SettingScreen.this).getUserId();
                                                getDirectRefer(referalCode,profile.getReferralAmount(),profile.getReferralAmountForOtherProfile());
                                            }

                                        }else if(sg.getStatus().equalsIgnoreCase("Completed")){

                                            mVideoCoins.setText("Non Redeemable Coins : 0");
                                            if(referalCode!=null&&!referalCode.isEmpty()){
                                                getDirectRefer(referalCode,profile.getReferralAmount()+(int)valuea,profile.getReferralAmountForOtherProfile());
                                            }else{
                                                referalCode = "MBR"+PreferenceHandler.getInstance(SettingScreen.this).getUserId();
                                                getDirectRefer(referalCode,profile.getReferralAmount()+(int)valuea,profile.getReferralAmountForOtherProfile());
                                            }
                                        }else if(sg.getStatus().equalsIgnoreCase("Penalty")){

                                            mVideoCoins.setText("Non Redeemable Coins : "+(int)valuea);

                                            if(referalCode!=null&&!referalCode.isEmpty()){
                                                getDirectRefer(referalCode,profile.getReferralAmount(),profile.getReferralAmountForOtherProfile());
                                            }else{
                                                referalCode = "MBR"+PreferenceHandler.getInstance(SettingScreen.this).getUserId();
                                                getDirectRefer(referalCode,profile.getReferralAmount(),profile.getReferralAmountForOtherProfile());
                                            }
                                        }

                                    }



                                }else{
                                    if(referalCode!=null&&!referalCode.isEmpty()){
                                        getDirectRefer(referalCode,profile.getReferralAmount(),profile.getReferralAmountForOtherProfile());
                                    }else{
                                        referalCode = "MBR"+PreferenceHandler.getInstance(SettingScreen.this).getUserId();
                                        getDirectRefer(referalCode,profile.getReferralAmount(),profile.getReferralAmountForOtherProfile());
                                    }
                                }


                            }else{
                                if(referalCode!=null&&!referalCode.isEmpty()){
                                    getDirectRefer(referalCode,profile.getReferralAmount(),profile.getReferralAmountForOtherProfile());
                                }else{
                                    referalCode = "MBR"+PreferenceHandler.getInstance(SettingScreen.this).getUserId();
                                    getDirectRefer(referalCode,profile.getReferralAmount(),profile.getReferralAmountForOtherProfile());
                                }
                            }


                        }else{

                            if(referalCode!=null&&!referalCode.isEmpty()){
                                getDirectRefer(referalCode,profile.getReferralAmount(),profile.getReferralAmountForOtherProfile());
                            }else{
                                referalCode = "MBR"+PreferenceHandler.getInstance(SettingScreen.this).getUserId();
                                getDirectRefer(referalCode,profile.getReferralAmount(),profile.getReferralAmountForOtherProfile());
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<SubscribedGoals>> call, Throwable t) {


                        if(referalCode!=null&&!referalCode.isEmpty()){
                            getDirectRefer(referalCode,profile.getReferralAmount(),profile.getReferralAmountForOtherProfile());
                        }else{
                            referalCode = "MBR"+PreferenceHandler.getInstance(SettingScreen.this).getUserId();
                            getDirectRefer(referalCode,profile.getReferralAmount(),profile.getReferralAmountForOtherProfile());
                        }

                        Toast.makeText(SettingScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }
    public String duration(String fromm) throws Exception{

        String from = fromm;




        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

        Date d1 = null;
        Date d2 = null;
        try {

            d1 = format1.parse(from);


            long diff = d1.getTime()-new Date().getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            return  String.valueOf(diffDays);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }


}
