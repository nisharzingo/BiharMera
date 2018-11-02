package tv.merabihar.app.merabihar.UI.MainTabHostScreens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.LoginScreen;
import tv.merabihar.app.merabihar.UI.Activity.SignUpScreen;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.PostContent.PostContentScreen;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.PostContent.PostVideoYoutubeContent;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;

public class TabPostOptionsActivity extends AppCompatActivity {

    CardView mGallery,mYoutube;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_tab_post_options);

            mGallery = (CardView)findViewById(R.id.gallery_post);
            mYoutube = (CardView)findViewById(R.id.youtube_url);

            mGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(PreferenceHandler.getInstance(TabPostOptionsActivity.this).getUserId()!=0){

                        Intent post = new Intent(TabPostOptionsActivity.this, PostContentScreen.class);
                        startActivity(post);

                    }else{

                        new AlertDialog.Builder(TabPostOptionsActivity.this)
                                .setMessage("Please login/Signup to Like the Story")
                                .setCancelable(false)
                                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent login = new Intent(TabPostOptionsActivity.this, LoginScreen.class);
                                        startActivity(login);

                                    }
                                })
                                .setNegativeButton("SignUp", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent signUp = new Intent(TabPostOptionsActivity.this, SignUpScreen.class);
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

                    if(PreferenceHandler.getInstance(TabPostOptionsActivity.this).getUserId()!=0){

                        Intent post = new Intent(TabPostOptionsActivity.this, PostVideoYoutubeContent.class);
                        startActivity(post);

                    }else{

                        new AlertDialog.Builder(TabPostOptionsActivity.this)
                                .setMessage("Please login/Signup to Like the Story")
                                .setCancelable(false)
                                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent login = new Intent(TabPostOptionsActivity.this, LoginScreen.class);
                                        startActivity(login);

                                    }
                                })
                                .setNegativeButton("SignUp", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent signUp = new Intent(TabPostOptionsActivity.this, SignUpScreen.class);
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
}
