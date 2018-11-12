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

import tv.merabihar.app.merabihar.R;

public class InfluencerProgramDetailScreen extends AppCompatActivity {

    TextView viewMOre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_influencer_program_detail_screen);
            viewMOre = findViewById(R.id.termsCondition_view_more);

            //open bottom popup box
            viewMOre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopUp();
                }
            });



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

}
