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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.SettingScreen;

public class WithdrawMoney extends AppCompatActivity {

    Button withdrawBtn;
    LinearLayout withdrawToPaytm;
    TextView currBal, withdraw_tc_txt;
    Toolbar mIncomeToolbar;
    String termsCondition =  "Cash withdrawal rules : \n 1.Only 200 rupees can be withdrawn at a time. \n 2. The maximum daily withdrawal limit is 5. \n 3. You will get the money within 72 hours in    your paytm account.";


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
            final String formatedRupee = String.format("%.02f", totalRupees);
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
                    isWithDrawPossible(rupessString);
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
}
