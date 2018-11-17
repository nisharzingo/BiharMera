package tv.merabihar.app.merabihar.UI.Activity.Influencer;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.CustomViews.SnackbarViewer;
import tv.merabihar.app.merabihar.Model.SubscribedGoals;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.SettingScreen;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ProfileAPI;
import tv.merabihar.app.merabihar.WebAPI.ProfileFollowAPI;
import tv.merabihar.app.merabihar.WebAPI.SubscribedGoalsAPI;

public class Income extends AppCompatActivity {

    Toolbar mIncomeToolbar;
    Button  withDrawBtn, earnMoreBtn, shareBtn;
    TextView coinTxt, rupeesTxt,mReedem,mNon ;

    String shareContent = "Save time. Download Mera Bihar,The Only App for Bihar,To Read,Share your Stories and Earn Rs 1000\n\n Use my referal code for Sign-Up MBR"+ PreferenceHandler.getInstance(Income.this).getUserId()+"\n http://bit.ly/2JXcOnw";

    int profileId=0,wallet=0,coinsUsed=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        mIncomeToolbar = findViewById(R.id.income_toolbar);

        setSupportActionBar(mIncomeToolbar);
        getSupportActionBar().setTitle(R.string.income);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileId = PreferenceHandler.getInstance(Income.this).getUserId();

        withDrawBtn = findViewById(R.id.income_withdraw_btn);
        earnMoreBtn = findViewById(R.id.income_earn_more_btn);
        shareBtn = findViewById(R.id.income_share_btn);
        coinTxt = findViewById(R.id.income_totalcoins_txt);
        rupeesTxt = findViewById(R.id.income_totalrupees_txt);
        mReedem = findViewById(R.id.redeemable);
        mNon = findViewById(R.id.non_redeemable);

        mIncomeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                Intent settingIntent = new Intent(Income.this, SettingScreen.class);
                settingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(settingIntent);
            }
        });



        // fetch rupees from SettingScreen Activity
       //  String rupessString = "199.65222555";

        String coins = getIntent().getStringExtra("coins_value");
        String rupessString = getIntent().getStringExtra("rupees_value");


        final float totalRupees = Float.parseFloat(rupessString);
        final String formatedRupee = String.format("%.02f", totalRupees);
        rupeesTxt.setText("₹" + formatedRupee);
        coinTxt.setText(coins);

        withDrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWithDrawPossible(formatedRupee);
            }
        });


        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Send to"));
            }
        });

        earnMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Income.this, InfluencerProgramViewScreen.class);
                startActivity(sendIntent);
                Income.this.finish();

            }
        });

        if(profileId!=0){

            if (Util.isNetworkAvailable(Income.this)) {
                getProfile(profileId);
            }else{
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            }

        }




    }

    private void isWithDrawPossible(String totalRupees) {

        float rupees = Float.parseFloat(totalRupees);


        if(rupees < 200 ){
            showPopUp(rupees);
        }
        else {
            showSnackbar("Withdrawal possible");
        }

    }

    private void showPopUp(float totalRupees) {
        final Dialog mPopupDialog;
        mPopupDialog = new Dialog(this);
        mPopupDialog.setContentView(R.layout.popup_box);

        TextView closePopup = mPopupDialog.findViewById(R.id.popupclose_txt);
        TextView popupTxt = mPopupDialog.findViewById(R.id.popup_msg_txt);
        String required_money = String.format("%.02f",200-totalRupees);


        String msg =  "Sorry! Insufficient balance. You need    ₹ "  +  required_money +  "  more to withdraw money." ;
        popupTxt.setText(msg);

        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close the popup
                mPopupDialog.dismiss();

            }
        });
        mPopupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupDialog.show();

    }


        private void showSnackbar(String msg) {
            final Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.income_constraint_layout), msg , Snackbar.LENGTH_LONG );
            // Changing message text color
            snackbar.setActionTextColor(Color.WHITE);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            textView.setTextColor(Color.WHITE);
            snackbar.show();
            snackbar.setAction("Close", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });

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



                        if (response.code() == 200)
                        {
                            System.out.println("Inside api");

                            UserProfile profile = response.body();








                            coinsUsed = profile.getUsedAmount();
                            wallet = profile.getWalletBalance();








                            if (Util.isNetworkAvailable(Income.this)) {
                                getGoalsByProfileId(profile.getProfileId(),profile);
                            }else{
                                Toast.makeText(Income.this, "No Internet connection", Toast.LENGTH_SHORT).show();
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


    private void getDirectRefer(final String code,final double coinsValue){

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


                                getInDirectRefer(code,responseProfile.size(),coinsValue);


                            }
                            else
                            {
                                mNon.setText(""+(int)coinsValue);
                               // mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((coinsValue*1.0)/100.0)));

                            }
                        }
                        else
                        {

                            mNon.setText(""+(int)coinsValue);
                            //mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((coinsValue*1.0)/100.0)));

                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed
                        mNon.setText(""+(int)coinsValue);
                        //mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((coinsValue*1.0)/100.0)));


                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    private void getInDirectRefer(final String code,final int directCount,final double coinsValue){

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




                                int indirectCount = responseProfile.size();
                                int balance = (indirectCount - directCount);


                                double amount = (balance * 10)+(directCount*50);



                                mReedem.setText(""+(int)amount);
                                mNon.setText(""+(int)coinsValue);
                               // mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((amount*1.0)/100.0)));
                                //mBalance.setText("Rs "+((amount*1.0)/100.0));


                            }
                            else
                            {

                                getInDirectParentRefer(code,directCount,coinsValue);
                               /* mInvite.setText(""+directCount);
                                mCoins.setText(""+(int)coinsValue);
                                mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((coinsValue*1.0)/100.0)));*/

                            }
                        }
                        else
                        {
                            getInDirectParentRefer(code,directCount,coinsValue);
                               /* mInvite.setText(""+directCount);
                                mCoins.setText(""+(int)coinsValue);
                                mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((coinsValue*1.0)/100.0)));*/

                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed
                        getInDirectParentRefer(code,directCount,coinsValue);
                               /* mInvite.setText(""+directCount);
                                mCoins.setText(""+(int)coinsValue);
                                mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((coinsValue*1.0)/100.0)));*/


                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    private void getInDirectParentRefer(final String code,final int directCount,final double coinsValue){

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




                                int indirectCount = responseProfile.size();
                                int balance = (indirectCount);


                                double amount = (balance * 10)+(directCount*50);



                                mReedem.setText(""+(int)amount);
                                mNon.setText(""+(int)coinsValue);
                                //mBalance.setText("Rs "+((amount*1.0)/100.0));


                            }
                            else
                            {
                                mReedem.setText("0");
                                mNon.setText(""+(int)coinsValue);

                            }
                        }
                        else
                        {
                            mReedem.setText("0");
                            mNon.setText(""+(int)coinsValue);

                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed
                        mReedem.setText("0");
                        mNon.setText(""+(int)coinsValue);

                        Log.e("TAG", t.toString());
                    }
                });
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

                                    if ( new Date().getTime() > past.getTime()) {

                                        double amount = profile.getReferralAmount();
                                        double valuea = (Double.parseDouble(sg.getRewardsEarned()))*.20;

                                        mNon.setText(""+(int)valuea);

                                        getDirectRefer("MBR"+PreferenceHandler.getInstance(Income.this).getUserId(),valuea);

                                    }



                                }else{
                                    getDirectRefer("MBR"+PreferenceHandler.getInstance(Income.this).getUserId(),0);
                                }


                            }else{
                                getDirectRefer("MBR"+PreferenceHandler.getInstance(Income.this).getUserId(),0);
                            }


                        }else{

                            getDirectRefer("MBR"+PreferenceHandler.getInstance(Income.this).getUserId(),0);

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<SubscribedGoals>> call, Throwable t) {


                        getDirectRefer("MBR"+PreferenceHandler.getInstance(Income.this).getUserId(),0);

                        Toast.makeText(Income.this,t.getMessage(),Toast.LENGTH_SHORT).show();
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
