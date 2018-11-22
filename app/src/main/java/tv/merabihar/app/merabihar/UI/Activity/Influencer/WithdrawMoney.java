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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Model.TransactionHistroy;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.SettingScreen;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ProfileFollowAPI;
import tv.merabihar.app.merabihar.WebAPI.TransactionHistroyAPI;

public class WithdrawMoney extends AppCompatActivity {

    Button withdrawBtn;
    LinearLayout withdrawToPaytm;
    TextView currBal, withdraw_tc_txt;
    Toolbar mIncomeToolbar;
    String termsCondition =  "Cash withdrawal rules : \n 1.Only 200 rupees can be withdrawn at a time. \n 2. The maximum daily withdrawal limit is 5. \n 3. You will get the money within 72 hours in    your paytm account.";

    TransactionHistroy transactionHistroy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_money);
        
        currBal = findViewById(R.id.curr_bal_withdrawpage);
        withdraw_tc_txt = findViewById(R.id.t_c_withdraw_paytm);
        withdrawToPaytm = findViewById(R.id.ll_to_paytm);
        withdrawBtn = findViewById(R.id.withdraw_btn_withdrawpage);
        mIncomeToolbar = findViewById(R.id.withdraw_btn_toolbar);


        setSupportActionBar(mIncomeToolbar);
        getSupportActionBar().setTitle("Withdraw");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        
        try{

            final String rupessString = getIntent().getStringExtra("rupees_value");
            final float totalRupees = Float.parseFloat(rupessString);
            final String formatedRupee = String.format("%.02f", (totalRupees/100.0));
            currBal.setText("Balance ₹" + formatedRupee);
            withdraw_tc_txt.setText(termsCondition);

            withdrawToPaytm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // check whether user is valid paytm user. if valid give option to change the paytm account
                    validatePaytmUser(formatedRupee);
                }
            });

            withdrawBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(Util.isNetworkAvailable(WithdrawMoney.this)){
                        getDirectReferCount("MBR"+PreferenceHandler.getInstance(WithdrawMoney.this).getUserId(),formatedRupee);
                    }else{
                        Toast.makeText(WithdrawMoney.this, "You are offline", Toast.LENGTH_SHORT).show();
                    }
                  //  isWithDrawPossible(rupessString);
                }
            });

            mIncomeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // perform whatever you want on back arrow click
                    Intent settingIntent = new Intent(WithdrawMoney.this, SettingScreen.class);
                    settingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(settingIntent);
                }
            });





        }catch (Exception e){
            e.printStackTrace();
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
                .make(findViewById(R.id.withdraw_constraint_layout), msg , Snackbar.LENGTH_LONG );
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

    private void validatePaytmUser(String formatedRupee) {
        Toast.makeText(this, "In development Mode...", Toast.LENGTH_SHORT).show();
    }

    private void getDirectReferCount(final String code,final String formatedRupee){

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


                                if(responseProfile.size()>=400){
                                    //isWithDrawPossible(formatedRupee);
                                    showSnackbar("Withdrawal possible");
                                }else{

                                    TransactionHistroy tc = new TransactionHistroy();
                                    tc.setTitle("NewSignUp");
                                    tc.setProfileId(PreferenceHandler.getInstance(WithdrawMoney.this).getUserId());
                                    tc.setDescription("New SignUp for withdraw money");
                                    tc.setValue(responseProfile.size()+15);
                                    tc.setTransactionHistoryDate(new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").format(new Date()));
                                    // profileVideoWatched(tc);
                                    // showPopUpForReferal(responseProfile.size());

                                    getTransactionsByIdType(PreferenceHandler.getInstance(WithdrawMoney.this).getUserId(),"NewSignUp",tc,formatedRupee);

                                }

                            }
                            else
                            {
                                TransactionHistroy tc = new TransactionHistroy();
                                tc.setTitle("NewSignUp");
                                tc.setProfileId(PreferenceHandler.getInstance(WithdrawMoney.this).getUserId());
                                tc.setDescription("New SignUp for withdraw money");
                                tc.setValue(15);
                                tc.setTransactionHistoryDate(new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").format(new Date()));

                                // showPopUpForReferal(responseProfile.size());

                                getTransactionsByIdType(PreferenceHandler.getInstance(WithdrawMoney.this).getUserId(),"NewSignUp",tc,formatedRupee);
                            }
                        }
                        else
                        {

                            //mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((coinsValue*1.0)/100.0)));

                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed
                        //mBalance.setText("Rs "+new DecimalFormat("#,###.##").format(((coinsValue*1.0)/100.0)));


                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    private void showPopUpForReferal(int size) {
        final Dialog mPopupDialog;
        mPopupDialog = new Dialog(this);
        mPopupDialog.setContentView(R.layout.popup_box);

        TextView closePopup = mPopupDialog.findViewById(R.id.popupclose_txt);
        TextView popupTxt = mPopupDialog.findViewById(R.id.popup_msg_txt);
        String required_money = String.format("%.02f",15-size);


        String msg =  "The Amount is Redeemable after completing "+required_money+" New Signup using Your Referral Code within goal Expired period" ;
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

    public void profileVideoWatched(final TransactionHistroy sg) {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                TransactionHistroyAPI mapApi = Util.getClient().create(TransactionHistroyAPI.class);
                Call<TransactionHistroy> response = mapApi.postTransactionHistories(sg);
                response.enqueue(new Callback<TransactionHistroy>() {
                    @Override
                    public void onResponse(Call<TransactionHistroy> call, Response<TransactionHistroy> response) {

                        System.out.println(response.code());



                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {
                            showPopUpForReferal(0);


                        }
                        else
                        {

                        }
                    }

                    @Override
                    public void onFailure(Call<TransactionHistroy> call, Throwable t) {




                    }
                });
            }
        });
    }

    public void getTransactionsByIdType(final int id,final String type,final TransactionHistroy tc,final String formatedRupee)
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final TransactionHistroyAPI categoryAPI = Util.getClient().create(TransactionHistroyAPI.class);
                Call<ArrayList<TransactionHistroy>> getCat = categoryAPI.getTransactionHistoriesByProfileIdAndGoal(id,type);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<TransactionHistroy>>() {

                    @Override
                    public void onResponse(Call<ArrayList<TransactionHistroy>> call, Response<ArrayList<TransactionHistroy>> response) {



                        if(response.code() == 200 && response.body()!= null&&response.body().size()!=0)
                        {

                            transactionHistroy = response.body().get(0);

                            if(transactionHistroy!=null){

                                TransactionHistroy tcs = transactionHistroy;

                                if((tc.getValue()-tcs.getValue())>=15){

                                    isWithDrawPossible(formatedRupee);
                                }else{
                                    showPopUpForReferal(15-(tc.getValue()-tcs.getValue()));
                                }


                            }else{


                                profileVideoWatched(tc);
                            }



                        }else{


                            profileVideoWatched(tc);

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TransactionHistroy>> call, Throwable t) {



                        //System.out.println(TAG+" thread started");

                    }
                });




            }

        });

    }

}
