package tv.merabihar.app.merabihar.UI.MainTabHostScreens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;

import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.LoginScreen;
import tv.merabihar.app.merabihar.UI.Activity.SignUpScreen;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.PostContent.PostContentScreen;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.PostContent.PostVideoYoutubeContent;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.YoutubeUpload.YoutubeVideoUploadScreen;

public class TabPostMediaActivity extends AppCompatActivity {

    LinearLayout mGallery,mYoutube;

    @Override
    protected void onResume() {
        super.onResume();
        try{
            animateView();
        }catch (Exception error){
            error.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_tab_story);

            mGallery = (LinearLayout)findViewById(R.id.gallery_post);
            mYoutube = (LinearLayout)findViewById(R.id.youtube_url);

            mGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(PreferenceHandler.getInstance(TabPostMediaActivity.this).getUserId()!=0){

                        Intent post = new Intent(TabPostMediaActivity.this, PostContentScreen.class);
                        startActivity(post);

                    }else{

                        new AlertDialog.Builder(TabPostMediaActivity.this)
                                .setMessage("Please login/Signup to Like the Story")
                                .setCancelable(false)
                                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent login = new Intent(TabPostMediaActivity.this, LoginScreen.class);
                                        startActivity(login);

                                    }
                                })
                                .setNegativeButton("SignUp", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent signUp = new Intent(TabPostMediaActivity.this, SignUpScreen.class);
                                        startActivity(signUp);

                                    }
                                })
                                .show();


                    }

                }
            });

            mYoutube.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(PreferenceHandler.getInstance(TabPostMediaActivity.this).getUserId()!=0){

                        Intent post = new Intent(TabPostMediaActivity.this, PostVideoYoutubeContent.class);
                        startActivity(post);

                    }else{

                        new AlertDialog.Builder(TabPostMediaActivity.this)
                                .setMessage("Please login/Signup to Like the Story")
                                .setCancelable(false)
                                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent login = new Intent(TabPostMediaActivity.this, LoginScreen.class);
                                        startActivity(login);

                                    }
                                })
                                .setNegativeButton("SignUp", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent signUp = new Intent(TabPostMediaActivity.this, SignUpScreen.class);
                                        startActivity(signUp);

                                    }
                                })
                                .show();


                    }

                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void animateView() {

        LinearLayout gal_ll = findViewById(R.id.ll_share_gal);
        gal_ll.setTranslationX(-300);
        gal_ll.animate().translationXBy(300).setDuration(500);

        LinearLayout youtube_ll = findViewById(R.id.youtube_url);
        youtube_ll.setTranslationX(600);
        youtube_ll.animate().translationXBy(-600).setDuration(500);

        TextView share_something = findViewById(R.id.share_something);
        share_something.setTranslationY(-300);
        share_something.animate().translationYBy(300).setDuration(500);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    private void goBack()
    {
        Intent intent = null;

        intent = new Intent(TabPostMediaActivity.this,TabMainActivity.class);
        //intent.putExtra("TABNAME",3);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        TabPostMediaActivity.this.finish();
    }
}
