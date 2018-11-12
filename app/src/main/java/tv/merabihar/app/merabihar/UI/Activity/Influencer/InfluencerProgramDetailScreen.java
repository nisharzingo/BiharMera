package tv.merabihar.app.merabihar.UI.Activity.Influencer;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Model.Goals;
import tv.merabihar.app.merabihar.Model.SubscribedGoals;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.GoalAPI;

public class InfluencerProgramDetailScreen extends AppCompatActivity {

    TextView viewMOre,mGoalTc,mGoalName;
    SubscribedGoals targetDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_influencer_program_detail_screen);
            viewMOre = findViewById(R.id.termsCondition_view_more);
            mGoalName = findViewById(R.id.goal_name);
            mGoalTc = findViewById(R.id.goals_tc);

            Bundle bundle = getIntent().getExtras();

            if(bundle!=null){

                targetDesc = (SubscribedGoals)bundle.getSerializable("Goals");

            }
            //open bottom popup box
            viewMOre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopUp();
                }
            });

            if(targetDesc!=null){
                getGoals(targetDesc.getGoalId(),mGoalTc);

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
/*
        LinearLayout linearLayout = mPopupDialog.findViewById(R.id.termsandcondlinearlayout);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.dialog_terms_cond);
        linearLayout.setAnimation(animation);
*/

        getGoals(targetDesc.getGoalId(),termsCondText);

        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close the popup
                mPopupDialog.dismiss();

            }
        });
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
}
