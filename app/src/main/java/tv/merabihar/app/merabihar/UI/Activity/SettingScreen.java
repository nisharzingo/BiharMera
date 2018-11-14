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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.NavigationListAdapter;
import tv.merabihar.app.merabihar.Adapter.ReferalPeopleListAdapter;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Roboto_Regular;
import tv.merabihar.app.merabihar.Model.NavBarItems;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.FriendList.FriendListScreen;
import tv.merabihar.app.merabihar.UI.Activity.Influencer.Income;
import tv.merabihar.app.merabihar.UI.Activity.Influencer.InfluencerProgramViewScreen;
import tv.merabihar.app.merabihar.UI.Activity.Influencer.InviteScreen;
import tv.merabihar.app.merabihar.UI.Activity.Influencer.WithdrawMoney;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.TabAccountActivity;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ProfileAPI;
import tv.merabihar.app.merabihar.WebAPI.ProfileFollowAPI;

public class SettingScreen extends AppCompatActivity {

    MyTextView_Roboto_Regular mProfileName,mReferalCode;
    CircleImageView mProfilePhoto;
    ProgressBar progressBar;
    LinearLayout mWhatsapp,mFaceBook,mSms,mMore,mInviteScreen;
    TextView mCoins,mBalance,mInvite;
    LinearLayout withdraw_btn, balance_btn, income_btn;

    ListView navBarListView;
    String[] title = null;

    int profileId=0,wallet=0,coinsUsed=0;

    String referalCode,referCodeProfile;

    UserProfile profiles;

    static String shareContent = "Save time. Download Mera Bihar,The Only App for Bihar,To Read,Share your Stories and Earn Rs 1000\n\n\n http://bit.ly/2JXcOnw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_setting_screen);

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

            balance_btn  = findViewById(R.id.balance_ll_btn);





            profileId = PreferenceHandler.getInstance(SettingScreen.this).getUserId();
            if(profileId!=0){

                getProfile(profileId);

            }else{

                Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
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
                            String referCodeText = Base64.encodeToString(ref.getBytes(), Base64.DEFAULT);
                            PreferenceHandler.getInstance(SettingScreen.this).setReferalcode(ref);
                            if(profile.getReferralCodeToUseForOtherProfile()!=null){

                                if(referCodeText.equals(profile.getReferralCodeToUseForOtherProfile())){
                                    mReferalCode.setText(""+profile.getReferralCodeToUseForOtherProfile());
                                }else{
                                    mReferalCode.setText(""+referCodeText);
                                }


                            }else{

                                ref = "MBR"+profile.getProfileId();
                                referCodeText = Base64.encodeToString(ref.getBytes(), Base64.DEFAULT);
                                mReferalCode.setText(""+referCodeText);
                            }

                            coinsUsed = profile.getUsedAmount();
                            wallet = profile.getWalletBalance();

                            // open income activity
                            openIncomeActivity( String.valueOf(coinsUsed), String.valueOf( (coinsUsed*1.0)/100  ) );
                            openWithDrawActivity(String.valueOf( (coinsUsed*1.0)/100  ) );

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

                            if(referalCode!=null&&!referalCode.isEmpty()){
                                getDirectRefer(referalCode,profile.getReferralAmount(),profile.getReferralAmountForOtherProfile());
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
                                mCoins.setText(""+(int)coinsValue);
                                mBalance.setText("Rs "+((coinsValue*1.0)/100.0));

                            }
                        }
                        else
                        {

                            mCoins.setText(""+(int)coinsValue);
                            mBalance.setText("Rs "+((coinsValue*1.0)/100.0));

                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed
                        mCoins.setText(""+(int)coinsValue);
                        mBalance.setText("Rs "+((coinsValue*1.0)/100.0));


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


                                mInvite.setText(""+responseProfile.size());

                                int indirectCount = responseProfile.size();
                                int balance = (indirectCount - directCount);


                                double amount = (balance * 10)+(directCount*50)+coinsValue;



                                mCoins.setText(""+(int)amount);
                                mBalance.setText("Rs "+((amount*1.0)/100.0));


                            }
                            else
                            {
                                mCoins.setText(""+(int)coinsValue);
                                mBalance.setText("Rs "+((coinsValue*1.0)/100.0));

                            }
                        }
                        else
                        {
                            mCoins.setText(""+(int)coinsValue);
                            mBalance.setText("Rs "+((coinsValue*1.0)/100.0));

                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed
                        mCoins.setText(""+(int)coinsValue);
                        mBalance.setText("Rs "+((coinsValue*1.0)/100.0));


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
                System.out.println(" Base 64 "+converted);
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

            if(wallet!=(int)balance){

                profiles.setWalletBalance((int)balance);
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

        income_btn = findViewById(R.id.income_ll_btn);

        income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent incomeIntent = new Intent(SettingScreen.this, Income.class);
                //get data from api
                incomeIntent.putExtra("coins_value", coins);
                incomeIntent.putExtra("rupees_value", rupees);
                startActivity(incomeIntent);
            }
        });
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


}
