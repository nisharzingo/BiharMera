package tv.merabihar.app.merabihar.UI.Activity.Influencer;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.CustomViews.SnackbarViewer;
import tv.merabihar.app.merabihar.Model.Goals;
import tv.merabihar.app.merabihar.Model.SubscribedGoals;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.GoalAPI;

public class InfluencerProgramDetailScreen extends AppCompatActivity {

    TextView viewMOre,mGoalTc,mGoalName,mExpDate;
    TextView mTv1,mTv2,mTv3,mTv4,mTv5;
    SubscribedGoals targetDesc;
    ImageView mCircle1,mCircle2,mCircle3,mCircle4,mCircle5;
    View mCircleView1,mCircleView2,mCircleView3,mCircleView4;

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

            mTv1 = findViewById(R.id.tv1);
            mTv2 = findViewById(R.id.tv2);
            mTv3 = findViewById(R.id.tv3);
            mTv4 = findViewById(R.id.tv4);
            mTv5 = findViewById(R.id.tv5);

            mCircleView1 = findViewById(R.id.view1);
            mCircleView2 = findViewById(R.id.view2);
            mCircleView3 = findViewById(R.id.view3);
            mCircleView4 = findViewById(R.id.view4);

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

                    mExpDate.setText("Offer Expired");

                    if(targetDesc.getGoalId()==3){

                        mTv1.setText("3 hours watched");
                        mTv2.setText("6 hours watched");
                        mTv3.setText("9 hours watched");
                        mTv4.setText("12 hours watched");
                        mTv5.setText("15 hours watched");

                        String rewards = targetDesc.getRewardsEarned();

                        int value = Integer.parseInt(rewards);

                        if (value >= 10800) {
                            mCircle1.setImageResource(R.drawable.ovel_tick);
                            mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));



                            mCircle2.setImageResource(R.drawable.oval_cross);
                            mCircleView2.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle3.setImageResource(R.drawable.oval_cross);
                            mCircleView3.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle4.setImageResource(R.drawable.oval_cross);
                            mCircleView4.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle5.setImageResource(R.drawable.oval_cross);
                        } else if (value >= 21600) {
                            mCircle2.setImageResource(R.drawable.ovel_tick);
                            mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                            mCircle3.setImageResource(R.drawable.oval_cross);
                            mCircleView3.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle4.setImageResource(R.drawable.oval_cross);
                            mCircleView4.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle5.setImageResource(R.drawable.oval_cross);
                        } else if (value >= 32400) {
                            mCircle3.setImageResource(R.drawable.ovel_tick);
                            mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                            mCircle4.setImageResource(R.drawable.oval_cross);
                            mCircleView4.setBackgroundColor(Color.parseColor("#FF0000"));

                            mCircle5.setImageResource(R.drawable.oval_cross);
                        } else if (value >= 43200) {
                            mCircle4.setImageResource(R.drawable.ovel_tick);
                            mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));

                            mCircle5.setImageResource(R.drawable.oval_cross);
                        } else if (value >= 54000) {
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

                    if (duration != null) {
                        mExpDate.setText(duration + " Days left");
                    } else {
                        mExpDate.setText("Offer Activated");
                    }


                    if (targetDesc.getGoalId() == 3) {

                        if (targetDesc.getStatus().equalsIgnoreCase("Activated")) {

                            String rewards = targetDesc.getRewardsEarned();

                            int value = Integer.parseInt(rewards);

                            if (value >= 10800) {
                                mCircle1.setImageResource(R.drawable.ovel_tick);
                                mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));
                            } else if (value >= 21600) {
                                mCircle2.setImageResource(R.drawable.ovel_tick);
                                mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));
                            } else if (value >= 32400) {
                                mCircle3.setImageResource(R.drawable.ovel_tick);
                                mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));
                            } else if (value >= 43200) {
                                mCircle4.setImageResource(R.drawable.ovel_tick);
                                mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));
                            } else if (value >= 54000) {
                                mCircle5.setImageResource(R.drawable.ovel_tick);
                            }

                        }

                    } else if (targetDesc.getGoalId() == 1) {

                        mTv1.setText("1st Month");
                        mTv2.setText("2nd Month");
                        mTv3.setText("3rd Month");
                        mTv4.setText("4th Month");
                        mTv5.setText("5th Month");
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


                        Toast.makeText(InfluencerProgramDetailScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
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
