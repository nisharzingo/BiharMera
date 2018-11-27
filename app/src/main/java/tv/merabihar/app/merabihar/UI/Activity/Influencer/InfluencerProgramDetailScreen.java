package tv.merabihar.app.merabihar.UI.Activity.Influencer;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.CustomViews.SnackbarViewer;
import tv.merabihar.app.merabihar.Model.Goals;
import tv.merabihar.app.merabihar.Model.SubscribedGoals;
import tv.merabihar.app.merabihar.Model.TransactionHistroy;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.GoalAPI;
import tv.merabihar.app.merabihar.WebAPI.ProfileFollowAPI;

public class InfluencerProgramDetailScreen extends AppCompatActivity {

    TextView viewMOre,mGoalTc,mGoalName,mExpDate;
    TextView mTv1,mTv2,mTv3,mTv4,mTv5,mPenalty;
    SubscribedGoals targetDesc;
    LinearLayout mOfferExpireLay,mInPgmStage2;
    ImageView mCircle1,mCircle2,mCircle3,mCircle4,mCircle5;
    View mCircleView1,mCircleView2,mCircleView3,mCircleView4;

    ImageView mCircle21,mCircle22,mCircle23,mCircle24,mCircle25,mCircle26,mCircle27;
    View mCircleView21,mCircleView22,mCircleView23,mCircleView24,mCircleView25,mCircleView26;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_influencer_program_detail_screen);
            viewMOre = findViewById(R.id.termsCondition_view_more);
            mExpDate = findViewById(R.id.expire_days_left_txt);
            mGoalName = findViewById(R.id.goal_name);
            mGoalTc = findViewById(R.id.goals_tc);

            mCircle1 = findViewById(R.id.circle1);
            mCircle2 = findViewById(R.id.circle2);
            mCircle3 = findViewById(R.id.circle3);
            mCircle4 = findViewById(R.id.circle4);
            mCircle5 = findViewById(R.id.circle5);

            mCircle21 = findViewById(R.id.circle21);
            mCircle22 = findViewById(R.id.circle22);
            mCircle23 = findViewById(R.id.circle23);
            mCircle24 = findViewById(R.id.circle24);
            mCircle25 = findViewById(R.id.circle25);
            mCircle26 = findViewById(R.id.circle26);
            mCircle27 = findViewById(R.id.circle27);

            mTv1 = findViewById(R.id.tv1);
            mTv2 = findViewById(R.id.tv2);
            mTv3 = findViewById(R.id.tv3);
            mTv4 = findViewById(R.id.tv4);
            mTv5 = findViewById(R.id.tv5);
            mPenalty = findViewById(R.id.penalty_amount);

            mCircleView1 = findViewById(R.id.view1);
            mCircleView2 = findViewById(R.id.view2);
            mCircleView3 = findViewById(R.id.view3);
            mCircleView4 = findViewById(R.id.view4);

            mCircleView21 = findViewById(R.id.view21);
            mCircleView22 = findViewById(R.id.view22);
            mCircleView23 = findViewById(R.id.view23);
            mCircleView24 = findViewById(R.id.view24);
            mCircleView25 = findViewById(R.id.view25);
            mCircleView26 = findViewById(R.id.view26);

            mOfferExpireLay = findViewById(R.id.offer_expire_lay);
            mInPgmStage2 = findViewById(R.id.influncer_stage2);

            Bundle bundle = getIntent().getExtras();

            if(bundle!=null){

                targetDesc = (SubscribedGoals)bundle.getSerializable("Goals");

            }
            //open bottom popup box
            viewMOre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(Util.isNetworkAvailable(InfluencerProgramDetailScreen.this)){
                        showPopUp();

                    }else{

                        SnackbarViewer.showSnackbar(findViewById(R.id.influencer_detail_main),"No internet connection ");

                    }
                }
            });

            if(targetDesc!=null){

                if(Util.isNetworkAvailable(InfluencerProgramDetailScreen.this)){

                getGoals(targetDesc.getGoalId(),mGoalTc);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                String expdate = targetDesc.getEndDate();
                Date past = null;


                String duration = null;

                if(expdate.contains("T")){

                    String[] tDates = expdate.split("T");
                    try {
                        past = dateFormat.parse(tDates[0]);
                        duration = duration(tDates[0]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if ( new Date().getTime() > past.getTime()) {

                    mOfferExpireLay.setVisibility(View.GONE);

                    if(targetDesc.getGoalId()==3){

                        getDirectReferCount("MBR"+PreferenceHandler.getInstance(InfluencerProgramDetailScreen.this).getUserId(),targetDesc);

                        mInPgmStage2.setVisibility(View.VISIBLE);
                        int penalty = Integer.parseInt(targetDesc.getExtraDescription());
                        if(penalty==0){

                            mPenalty.setVisibility(View.GONE);
                        }else{
                            mPenalty.setVisibility(View.VISIBLE);

                            int[] vale = splitToComponentTimes(penalty);

                            if(vale.length!=0&&vale.length==2){
                                mPenalty.setText("Last Week Penalty "+vale[0]+" hr "+vale[1]+" min" );
                            }

                        }

                        mTv1.setText("3 hours watched");
                        mTv2.setText("6 hours watched");
                        mTv3.setText("9 hours watched");
                        mTv4.setText("12 hours watched");
                        mTv5.setText("15 hours watched");

                        String rewards = targetDesc.getRewardsEarned();

                        int value = Integer.parseInt(rewards);


                        if (value >= (10800+penalty) && value <= (21600+penalty)) {
                            mCircle1.setImageResource(R.drawable.ovel_tick);
                            mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));



                            mCircle2.setImageResource(R.drawable.oval_cross);
                            mCircleView2.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle3.setImageResource(R.drawable.oval_cross);
                            mCircleView3.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle4.setImageResource(R.drawable.oval_cross);
                            mCircleView4.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle5.setImageResource(R.drawable.oval_cross);
                        } else if (value >= (21600+penalty) && value <= (32400+penalty)) {
                            mCircle2.setImageResource(R.drawable.ovel_tick);
                            mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                            mCircle3.setImageResource(R.drawable.oval_cross);
                            mCircleView3.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle4.setImageResource(R.drawable.oval_cross);
                            mCircleView4.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle5.setImageResource(R.drawable.oval_cross);
                        } else if (value >= (32400+penalty) && value <= (43200+penalty)) {
                            mCircle3.setImageResource(R.drawable.ovel_tick);
                            mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                            mCircle4.setImageResource(R.drawable.oval_cross);
                            mCircleView4.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle5.setImageResource(R.drawable.oval_cross);
                        } else if (value >= (43200+penalty) && value <= (54000+penalty)) {
                            mCircle4.setImageResource(R.drawable.ovel_tick);
                            mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));

                            mCircle5.setImageResource(R.drawable.oval_cross);
                        } else if (value >= (54000+penalty)) {
                            mCircle5.setImageResource(R.drawable.ovel_tick);
                        }else{
                            mCircle1.setImageResource(R.drawable.oval_cross);
                            mCircleView1.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle2.setImageResource(R.drawable.oval_cross);
                            mCircleView2.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle3.setImageResource(R.drawable.oval_cross);
                            mCircleView3.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle4.setImageResource(R.drawable.oval_cross);
                            mCircleView4.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle5.setImageResource(R.drawable.oval_cross);

                        }



                    }else if(targetDesc.getGoalId()==1){
                        mTv1.setText("1st Month");
                        mTv2.setText("2nd Month");
                        mTv3.setText("3rd Month");
                        mTv4.setText("4th Month");
                        mTv5.setText("5th Month");

                        mInPgmStage2.setVisibility(View.GONE);

                        mCircle1.setImageResource(R.drawable.oval_cross);
                        mCircleView1.setBackgroundColor(Color.parseColor("#FF0000"));

                        mCircle2.setImageResource(R.drawable.oval_cross);
                        mCircleView2.setBackgroundColor(Color.parseColor("#FF0000"));

                        mCircle3.setImageResource(R.drawable.oval_cross);
                        mCircleView3.setBackgroundColor(Color.parseColor("#FF0000"));

                        mCircle4.setImageResource(R.drawable.oval_cross);
                        mCircleView4.setBackgroundColor(Color.parseColor("#FF0000"));

                        mCircle5.setImageResource(R.drawable.oval_cross);

                        String rewards = targetDesc.getRewardsEarned();
                        if(rewards.contains(",")){

                            String rew[] = rewards.split(",");

                            if(rew[0].equals("1")&&Integer.parseInt(rew[1])>=100){

                                mCircle1.setImageResource(R.drawable.ovel_tick);
                                mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                            }else if(rew[0].equals("1")&&Integer.parseInt(rew[1])<100){

                                mCircle1.setImageResource(R.drawable.oval_cross);
                                mCircleView1.setBackgroundColor(Color.parseColor("#FF0000"));

                            }

                            if(rew[0].equals("2")&&Integer.parseInt(rew[1])>=300){

                                mCircle2.setImageResource(R.drawable.ovel_tick);
                                mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                            }else if(rew[0].equals("2")&&Integer.parseInt(rew[1])<300){

                                mCircle2.setImageResource(R.drawable.oval_cross);
                                mCircleView2.setBackgroundColor(Color.parseColor("#FF0000"));

                            }

                            if(rew[0].equals(3)&&Integer.parseInt(rew[1])>=900){

                                mCircle3.setImageResource(R.drawable.ovel_tick);
                                mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                            }else if(rew[0].equals("3")&&Integer.parseInt(rew[1])<900){

                                mCircle3.setImageResource(R.drawable.oval_cross);
                                mCircleView3.setBackgroundColor(Color.parseColor("#FF0000"));

                            }

                            if(rew[0].equals("4")&&Integer.parseInt(rew[1])>=2700){

                                mCircle4.setImageResource(R.drawable.ovel_tick);
                                mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));

                            }else if(rew[0].equals("4")&&Integer.parseInt(rew[1])<2700){

                                mCircle4.setImageResource(R.drawable.oval_cross);
                                mCircleView4.setBackgroundColor(Color.parseColor("#FF0000"));

                            }

                            if(rew[0].equals("5")&&Integer.parseInt(rew[1])>=5000){

                                mCircle5.setImageResource(R.drawable.ovel_tick);


                            }else if(rew[0].equals("5")&&Integer.parseInt(rew[1])<5000){

                                mCircle5.setImageResource(R.drawable.oval_cross);

                            }
                        }

                    }else if(targetDesc.getGoalId()==2) {
                        mTv1.setText("1st Day");
                        mTv2.setText("2nd Day");
                        mTv3.setText("3rd Day");
                        mTv4.setText("4th Day");
                        mTv5.setText("5th Day");

                        mInPgmStage2.setVisibility(View.GONE);

                        mCircle1.setImageResource(R.drawable.oval_cross);
                        mCircleView1.setBackgroundColor(Color.parseColor("#FF0000"));

                        mCircle2.setImageResource(R.drawable.oval_cross);
                        mCircleView2.setBackgroundColor(Color.parseColor("#FF0000"));

                        mCircle3.setImageResource(R.drawable.oval_cross);
                        mCircleView3.setBackgroundColor(Color.parseColor("#FF0000"));

                        mCircle4.setImageResource(R.drawable.oval_cross);
                        mCircleView4.setBackgroundColor(Color.parseColor("#FF0000"));

                        mCircle5.setImageResource(R.drawable.oval_cross);
                        String rewards = targetDesc.getRewardsEarned();

                        int value = Integer.parseInt(rewards);

                        if (value <= 1) {
                            mCircle1.setImageResource(R.drawable.ovel_tick);
                            mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));



                            mCircle2.setImageResource(R.drawable.oval_cross);
                            mCircleView2.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle3.setImageResource(R.drawable.oval_cross);
                            mCircleView3.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle4.setImageResource(R.drawable.oval_cross);
                            mCircleView4.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle5.setImageResource(R.drawable.oval_cross);
                        } else if (value <= 2) {
                            mCircle2.setImageResource(R.drawable.ovel_tick);
                            mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                            mCircle3.setImageResource(R.drawable.oval_cross);
                            mCircleView3.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle4.setImageResource(R.drawable.oval_cross);
                            mCircleView4.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle5.setImageResource(R.drawable.oval_cross);
                        } else if (value <= 3) {
                            mCircle3.setImageResource(R.drawable.ovel_tick);
                            mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                            mCircle4.setImageResource(R.drawable.oval_cross);
                            mCircleView4.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle5.setImageResource(R.drawable.oval_cross);
                        } else if (value <= 4) {
                            mCircle4.setImageResource(R.drawable.ovel_tick);
                            mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));

                            mCircle5.setImageResource(R.drawable.oval_cross);
                        } else if (value < 5) {
                            mCircle5.setImageResource(R.drawable.ovel_tick);
                        }else{
                            mCircle1.setImageResource(R.drawable.oval_cross);
                            mCircleView1.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle2.setImageResource(R.drawable.oval_cross);
                            mCircleView2.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle3.setImageResource(R.drawable.oval_cross);
                            mCircleView3.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle4.setImageResource(R.drawable.oval_cross);
                            mCircleView4.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle5.setImageResource(R.drawable.oval_cross);

                        }


                    }

                }else {
                    mOfferExpireLay.setVisibility(View.VISIBLE);

                    if (duration != null) {
                        mExpDate.setText((duration) + " Days left");
                    } else {
                        mExpDate.setText("Offer Activated");
                    }


                    if (targetDesc.getGoalId() == 3) {

                        mTv1.setText("3 hours watched");
                        mTv2.setText("6 hours watched");
                        mTv3.setText("9 hours watched");
                        mTv4.setText("12 hours watched");
                        mTv5.setText("15 hours watched");
                        getDirectReferCount("MBR"+PreferenceHandler.getInstance(InfluencerProgramDetailScreen.this).getUserId(),targetDesc);
                        mInPgmStage2.setVisibility(View.VISIBLE);

                        int penalty = 0;
                        if(targetDesc.getExtraDescription().contains("+")){

                            penalty = 0;

                        }else{
                            penalty = Integer.parseInt(targetDesc.getExtraDescription());
                        }

                        if(penalty==0){

                            mPenalty.setVisibility(View.GONE);
                        }else{
                            mPenalty.setVisibility(View.VISIBLE);

                            int[] vale = splitToComponentTimes(penalty);

                            if(vale.length!=0&&vale.length==2){
                                mPenalty.setText("Last Week Penalty "+vale[0]+" hr "+vale[1]+" min" );
                            }

                        }


                        if (targetDesc.getStatus().equalsIgnoreCase("Activated")) {

                            String rewards = targetDesc.getRewardsEarned();
                            int value = Integer.parseInt(rewards);

                            int penaltyAmount = 0;
                            if(targetDesc.getExtraDescription().contains("+")){

                                String penaltyValue = targetDesc.getExtraDescription().replaceAll("/+","");

                                penaltyAmount = Integer.parseInt(penaltyValue);

                                value = value-penaltyAmount;

                                if (value >= (10800) && value < (21600) ) {
                                    mCircle1.setImageResource(R.drawable.ovel_tick);
                                    mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));
                                } else if (value >= (21600) && value < (32400)) {

                                    mCircle1.setImageResource(R.drawable.ovel_tick);
                                    mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                    mCircle2.setImageResource(R.drawable.ovel_tick);
                                    mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));
                                } else if (value >= (32400) && value < (43200)) {
                                    mCircle1.setImageResource(R.drawable.ovel_tick);
                                    mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                    mCircle2.setImageResource(R.drawable.ovel_tick);
                                    mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                    mCircle3.setImageResource(R.drawable.ovel_tick);
                                    mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));
                                } else if (value >= (43200) && value < (54000)) {

                                    mCircle1.setImageResource(R.drawable.ovel_tick);
                                    mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                    mCircle2.setImageResource(R.drawable.ovel_tick);
                                    mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                    mCircle3.setImageResource(R.drawable.ovel_tick);
                                    mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                                    mCircle4.setImageResource(R.drawable.ovel_tick);
                                    mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));
                                } else if (value >= (54000) ) {

                                    mCircle1.setImageResource(R.drawable.ovel_tick);
                                    mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                    mCircle2.setImageResource(R.drawable.ovel_tick);
                                    mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                    mCircle3.setImageResource(R.drawable.ovel_tick);
                                    mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                                    mCircle4.setImageResource(R.drawable.ovel_tick);
                                    mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));

                                    mCircle5.setImageResource(R.drawable.ovel_tick);
                                }

                            }else{
                                    penaltyAmount = Integer.parseInt(targetDesc.getExtraDescription());

                                if (value >= (10800+penaltyAmount) && value < (21600+penaltyAmount) ) {
                                    mCircle1.setImageResource(R.drawable.ovel_tick);
                                    mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));
                                } else if (value >= (21600+penaltyAmount) && value < (32400+penaltyAmount)) {

                                    mCircle1.setImageResource(R.drawable.ovel_tick);
                                    mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                    mCircle2.setImageResource(R.drawable.ovel_tick);
                                    mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));
                                } else if (value >= (32400+penaltyAmount) && value < (43200+penaltyAmount)) {
                                    mCircle1.setImageResource(R.drawable.ovel_tick);
                                    mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                    mCircle2.setImageResource(R.drawable.ovel_tick);
                                    mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                    mCircle3.setImageResource(R.drawable.ovel_tick);
                                    mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));
                                } else if (value >= (43200+penaltyAmount) && value < (54000+penaltyAmount)) {

                                    mCircle1.setImageResource(R.drawable.ovel_tick);
                                    mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                    mCircle2.setImageResource(R.drawable.ovel_tick);
                                    mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                    mCircle3.setImageResource(R.drawable.ovel_tick);
                                    mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                                    mCircle4.setImageResource(R.drawable.ovel_tick);
                                    mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));
                                } else if (value >= (54000+penaltyAmount) ) {

                                    mCircle1.setImageResource(R.drawable.ovel_tick);
                                    mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                    mCircle2.setImageResource(R.drawable.ovel_tick);
                                    mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                    mCircle3.setImageResource(R.drawable.ovel_tick);
                                    mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                                    mCircle4.setImageResource(R.drawable.ovel_tick);
                                    mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));

                                    mCircle5.setImageResource(R.drawable.ovel_tick);
                                }
                            }






                        }else if (targetDesc.getStatus().equalsIgnoreCase("Completed")) {

                            String rewards = targetDesc.getRewardsEarned();
                            int penaltyAmount = Integer.parseInt(targetDesc.getExtraDescription());

                            int value = Integer.parseInt(rewards);

                            if (value >= (10800+penaltyAmount) && value < (21600+penaltyAmount) ) {
                                mCircle1.setImageResource(R.drawable.ovel_tick);
                                mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));
                            } else if (value >= (21600+penaltyAmount) && value < (32400+penaltyAmount)) {

                                mCircle1.setImageResource(R.drawable.ovel_tick);
                                mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle2.setImageResource(R.drawable.ovel_tick);
                                mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));
                            } else if (value >= (32400+penaltyAmount) && value < (43200+penaltyAmount)) {
                                mCircle1.setImageResource(R.drawable.ovel_tick);
                                mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle2.setImageResource(R.drawable.ovel_tick);
                                mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle3.setImageResource(R.drawable.ovel_tick);
                                mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));
                            } else if (value >= (43200+penaltyAmount) && value < (54000+penaltyAmount)) {

                                mCircle1.setImageResource(R.drawable.ovel_tick);
                                mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle2.setImageResource(R.drawable.ovel_tick);
                                mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle3.setImageResource(R.drawable.ovel_tick);
                                mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle4.setImageResource(R.drawable.ovel_tick);
                                mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));
                            } else if (value >= (54000+penaltyAmount) ) {

                                mCircle1.setImageResource(R.drawable.ovel_tick);
                                mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle2.setImageResource(R.drawable.ovel_tick);
                                mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle3.setImageResource(R.drawable.ovel_tick);
                                mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle4.setImageResource(R.drawable.ovel_tick);
                                mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle5.setImageResource(R.drawable.ovel_tick);
                            }

                        }else if (targetDesc.getStatus().equalsIgnoreCase("Penalty")) {

                            String rewards = targetDesc.getRewardsEarned();
                            int penaltyAmount = Integer.parseInt(targetDesc.getExtraDescription());

                            int value = Integer.parseInt(rewards);

                            if (value >= (10800+penaltyAmount) && value < (21600+penaltyAmount) ) {
                                mCircle1.setImageResource(R.drawable.ovel_tick);
                                mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));
                            } else if (value >= (21600+penaltyAmount) && value < (32400+penaltyAmount)) {

                                mCircle1.setImageResource(R.drawable.ovel_tick);
                                mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle2.setImageResource(R.drawable.ovel_tick);
                                mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));
                            } else if (value >= (32400+penaltyAmount) && value < (43200+penaltyAmount)) {
                                mCircle1.setImageResource(R.drawable.ovel_tick);
                                mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle2.setImageResource(R.drawable.ovel_tick);
                                mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle3.setImageResource(R.drawable.ovel_tick);
                                mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));
                            } else if (value >= (43200+penaltyAmount) && value < (54000+penaltyAmount)) {

                                mCircle1.setImageResource(R.drawable.ovel_tick);
                                mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle2.setImageResource(R.drawable.ovel_tick);
                                mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle3.setImageResource(R.drawable.ovel_tick);
                                mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle4.setImageResource(R.drawable.ovel_tick);
                                mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));
                            } else if (value >= (54000+penaltyAmount) ) {

                                mCircle1.setImageResource(R.drawable.ovel_tick);
                                mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle2.setImageResource(R.drawable.ovel_tick);
                                mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle3.setImageResource(R.drawable.ovel_tick);
                                mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle4.setImageResource(R.drawable.ovel_tick);
                                mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle5.setImageResource(R.drawable.ovel_tick);
                            }

                        }

                    } else if (targetDesc.getGoalId() == 1) {

                        mTv1.setText("1st Month");
                        mTv2.setText("2nd Month");
                        mTv3.setText("3rd Month");
                        mTv4.setText("4th Month");
                        mTv5.setText("5th Month");

                        mInPgmStage2.setVisibility(View.GONE);

                        String rewards = targetDesc.getRewardsEarned();
                        if (rewards.contains(",")) {

                            String rew[] = rewards.split(",");

                            if (rew[0].equals("1") && Integer.parseInt(rew[1]) >= 100) {

                                mCircle1.setImageResource(R.drawable.ovel_tick);
                                mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                            } else if (rew[0].equals("1") && Integer.parseInt(rew[1]) < 100) {

                                mCircle1.setImageResource(R.drawable.oval_cross);
                                mCircleView1.setBackgroundColor(Color.parseColor("#FF0000"));

                            }

                            if (rew[0].equals("2") && Integer.parseInt(rew[1]) >= 300) {

                                mCircle2.setImageResource(R.drawable.ovel_tick);
                                mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                            } else if (rew[0].equals("2") && Integer.parseInt(rew[1]) < 300) {

                                mCircle2.setImageResource(R.drawable.oval_cross);
                                mCircleView2.setBackgroundColor(Color.parseColor("#FF0000"));

                            }

                            if (rew[0].equals(3) && Integer.parseInt(rew[1]) >= 900) {

                                mCircle3.setImageResource(R.drawable.ovel_tick);
                                mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                            } else if (rew[0].equals("3") && Integer.parseInt(rew[1]) < 900) {

                                mCircle3.setImageResource(R.drawable.oval_cross);
                                mCircleView3.setBackgroundColor(Color.parseColor("#FF0000"));

                            }

                            if (rew[0].equals("4") && Integer.parseInt(rew[1]) >= 2700) {

                                mCircle4.setImageResource(R.drawable.ovel_tick);
                                mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));

                            } else if (rew[0].equals("4") && Integer.parseInt(rew[1]) < 2700) {

                                mCircle4.setImageResource(R.drawable.oval_cross);
                                mCircleView4.setBackgroundColor(Color.parseColor("#FF0000"));

                            }

                            if (rew[0].equals("5") && Integer.parseInt(rew[1]) >= 5000) {

                                mCircle5.setImageResource(R.drawable.ovel_tick);


                            } else if (rew[0].equals("5") && Integer.parseInt(rew[1]) < 5000) {

                                mCircle5.setImageResource(R.drawable.oval_cross);

                            }
                        }

                    }else if (targetDesc.getGoalId() == 2) {

                        mTv1.setText("1st Day");
                        mTv2.setText("2nd Day");
                        mTv3.setText("3rd Day");
                        mTv4.setText("4th Day");
                        mTv5.setText("5th Day");
                        mInPgmStage2.setVisibility(View.GONE);
                        if (targetDesc.getStatus().equalsIgnoreCase("Activated")) {

                            String rewards = targetDesc.getRewardsEarned();

                            int value = Integer.parseInt(rewards);

                            if (value <= 1) {
                                mCircle1.setImageResource(R.drawable.ovel_tick);
                                mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));
                            } else if (value <= 2) {

                                mCircle1.setImageResource(R.drawable.ovel_tick);
                                mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle2.setImageResource(R.drawable.ovel_tick);
                                mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));
                            } else if (value <=3 ) {

                                mCircle1.setImageResource(R.drawable.ovel_tick);
                                mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle2.setImageResource(R.drawable.ovel_tick);
                                mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle3.setImageResource(R.drawable.ovel_tick);
                                mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));
                            } else if (value <= 4) {

                                mCircle1.setImageResource(R.drawable.ovel_tick);
                                mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle2.setImageResource(R.drawable.ovel_tick);
                                mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle3.setImageResource(R.drawable.ovel_tick);
                                mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle4.setImageResource(R.drawable.ovel_tick);
                                mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));
                            } else if (value <= 5) {

                                mCircle1.setImageResource(R.drawable.ovel_tick);
                                mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle2.setImageResource(R.drawable.ovel_tick);
                                mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle3.setImageResource(R.drawable.ovel_tick);
                                mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle4.setImageResource(R.drawable.ovel_tick);
                                mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));

                                mCircle5.setImageResource(R.drawable.ovel_tick);
                            }

                        }

                    }
                }

            }else{

                SnackbarViewer.showSnackbar(findViewById(R.id.influencer_detail_main),"No internet connection ");

            }

            }



        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void showPopUp() {
        final Dialog mPopupDialog;
        mPopupDialog = new Dialog(this);
        mPopupDialog.setContentView(R.layout.popup_terms_condition);

        TextView closePopup = mPopupDialog.findViewById(R.id.popupclose_terms_condition_close);
        TextView termsCondText = mPopupDialog.findViewById(R.id.terms_condition_txt);

        getGoals(targetDesc.getGoalId(),termsCondText);

        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close the popup
                mPopupDialog.dismiss();

            }
        });
        mPopupDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT); // setting popup width to match parent
        mPopupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupDialog.getWindow().setGravity(Gravity.BOTTOM);
        mPopupDialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        mPopupDialog.show();

    }

    public void getGoals(final int ids,final TextView textView)
    {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final GoalAPI categoryAPI = Util.getClient().create(GoalAPI.class);
                Call<Goals> getCat = categoryAPI.getGoalsById(ids);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<Goals>() {

                    @Override
                    public void onResponse(Call<Goals> call, Response<Goals> response) {


                        Goals goals = response.body();

                        if(response.code() == 200 && goals!= null)
                        {

                            textView.setText(""+goals.getGoalImage());
                            mGoalName.setText(""+goals.getGoalName());



                        }else{



                        }
                    }

                    @Override
                    public void onFailure(Call<Goals> call, Throwable t) {


//                        Toast.makeText(InfluencerProgramDetailScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
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
            return  String.valueOf(diffDays+1);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static int[] splitToComponentTimes(int time)
    {

        int hours = (int) time / 3600;
        int remainder = (int) time - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        int[] ints = {hours , mins };
        return ints;
    }

    private void getDirectReferCount(final String code,final SubscribedGoals targetDesc){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getDirectReferedProfile(code);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
                        int statusCode = response.code();


                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();
                            ArrayList<UserProfile> targetProfiles = new ArrayList<>();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                                String expdate = targetDesc.getEndDate();
                                String startdate = targetDesc.getStartDate();
                                Date exp = null;
                                Date start = null;

                                if(expdate.contains("T")){

                                    String[] tDates = expdate.split("T");
                                    try {
                                        exp = dateFormat.parse(tDates[0]);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if(startdate.contains("T")){

                                    String[] tDates = startdate.split("T");
                                    try {
                                        start = dateFormat.parse(tDates[0]);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }


                                for (UserProfile target:responseProfile) {
                                    Date sign=null;
                                    if(target.getSignUpDate().contains("T")){

                                        String[] tDates = target.getSignUpDate().split("T");

                                        try {
                                            sign = dateFormat.parse(tDates[0]);

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    if(sign.getTime()>=start.getTime()&&sign.getTime()<=exp.getTime()){

                                        targetProfiles.add(target);
                                    }

                                }


                                if(targetProfiles!=null&&targetProfiles.size()!=0){

                                    int value = targetProfiles.size();

                                    if (value ==1 ) {
                                        mCircle21.setImageResource(R.drawable.ovel_tick);
                                        mCircleView21.setBackgroundColor(Color.parseColor("#176e0b"));
                                    } else if (value ==2) {

                                        mCircle21.setImageResource(R.drawable.ovel_tick);
                                        mCircleView21.setBackgroundColor(Color.parseColor("#176e0b"));

                                        mCircle22.setImageResource(R.drawable.ovel_tick);
                                        mCircleView22.setBackgroundColor(Color.parseColor("#176e0b"));
                                    } else if (value ==3) {
                                        mCircle21.setImageResource(R.drawable.ovel_tick);
                                        mCircleView21.setBackgroundColor(Color.parseColor("#176e0b"));

                                        mCircle22.setImageResource(R.drawable.ovel_tick);
                                        mCircleView22.setBackgroundColor(Color.parseColor("#176e0b"));

                                        mCircle23.setImageResource(R.drawable.ovel_tick);
                                        mCircleView23.setBackgroundColor(Color.parseColor("#176e0b"));
                                    } else if (value == 4) {

                                        mCircle21.setImageResource(R.drawable.ovel_tick);
                                        mCircleView21.setBackgroundColor(Color.parseColor("#176e0b"));

                                        mCircle22.setImageResource(R.drawable.ovel_tick);
                                        mCircleView22.setBackgroundColor(Color.parseColor("#176e0b"));

                                        mCircle23.setImageResource(R.drawable.ovel_tick);
                                        mCircleView23.setBackgroundColor(Color.parseColor("#176e0b"));

                                        mCircle24.setImageResource(R.drawable.ovel_tick);
                                        mCircleView24.setBackgroundColor(Color.parseColor("#176e0b"));
                                    } else if (value ==5 ) {

                                        mCircle21.setImageResource(R.drawable.ovel_tick);
                                        mCircleView21.setBackgroundColor(Color.parseColor("#176e0b"));

                                        mCircle22.setImageResource(R.drawable.ovel_tick);
                                        mCircleView22.setBackgroundColor(Color.parseColor("#176e0b"));

                                        mCircle23.setImageResource(R.drawable.ovel_tick);
                                        mCircleView23.setBackgroundColor(Color.parseColor("#176e0b"));

                                        mCircle24.setImageResource(R.drawable.ovel_tick);
                                        mCircleView24.setBackgroundColor(Color.parseColor("#176e0b"));

                                        mCircle25.setImageResource(R.drawable.ovel_tick);
                                        mCircleView25.setBackgroundColor(Color.parseColor("#176e0b"));
                                    } else if (value ==6 ) {

                                        mCircle21.setImageResource(R.drawable.ovel_tick);
                                        mCircleView21.setBackgroundColor(Color.parseColor("#176e0b"));

                                        mCircle22.setImageResource(R.drawable.ovel_tick);
                                        mCircleView22.setBackgroundColor(Color.parseColor("#176e0b"));

                                        mCircle23.setImageResource(R.drawable.ovel_tick);
                                        mCircleView23.setBackgroundColor(Color.parseColor("#176e0b"));

                                        mCircle24.setImageResource(R.drawable.ovel_tick);
                                        mCircleView24.setBackgroundColor(Color.parseColor("#176e0b"));

                                        mCircle25.setImageResource(R.drawable.ovel_tick);
                                        mCircleView25.setBackgroundColor(Color.parseColor("#176e0b"));

                                        mCircle26.setImageResource(R.drawable.ovel_tick);
                                        mCircleView26.setBackgroundColor(Color.parseColor("#176e0b"));
                                    } else if (value>=7 ) {

                                        mCircle21.setImageResource(R.drawable.ovel_tick);
                                        mCircleView21.setBackgroundColor(Color.parseColor("#176e0b"));

                                        mCircle22.setImageResource(R.drawable.ovel_tick);
                                        mCircleView22.setBackgroundColor(Color.parseColor("#176e0b"));

                                        mCircle23.setImageResource(R.drawable.ovel_tick);
                                        mCircleView23.setBackgroundColor(Color.parseColor("#176e0b"));

                                        mCircle24.setImageResource(R.drawable.ovel_tick);
                                        mCircleView24.setBackgroundColor(Color.parseColor("#176e0b"));

                                        mCircle25.setImageResource(R.drawable.ovel_tick);
                                        mCircleView25.setBackgroundColor(Color.parseColor("#176e0b"));

                                        mCircle26.setImageResource(R.drawable.ovel_tick);
                                        mCircleView26.setBackgroundColor(Color.parseColor("#176e0b"));

                                        mCircle27.setImageResource(R.drawable.ovel_tick);
                                    }
                                }


                            }
                            else
                            {
                            }
                        }
                        else
                        {



                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {


                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }
}
