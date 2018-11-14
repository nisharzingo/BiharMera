package tv.merabihar.app.merabihar.UI.Activity.Influencer;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.SettingScreen;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;

public class Income extends AppCompatActivity {

    Toolbar mIncomeToolbar;
    Button  withDrawBtn, earnMoreBtn, shareBtn;
    TextView coinTxt, rupeesTxt ;

    String shareContent = "Save time. Download Mera Bihar,The Only App for Bihar,To Read,Share your Stories and Earn Rs 1000\n\n Use my referal code for Sign-Up MBR"+ PreferenceHandler.getInstance(Income.this).getUserId()+"\n http://bit.ly/2JXcOnw";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        mIncomeToolbar = findViewById(R.id.income_toolbar);

        setSupportActionBar(mIncomeToolbar);
        getSupportActionBar().setTitle(R.string.income);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        withDrawBtn = findViewById(R.id.income_withdraw_btn);
        earnMoreBtn = findViewById(R.id.income_earn_more_btn);
        shareBtn = findViewById(R.id.income_share_btn);
        coinTxt = findViewById(R.id.income_totalcoins_txt);
        rupeesTxt = findViewById(R.id.income_totalrupees_txt);

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


}
