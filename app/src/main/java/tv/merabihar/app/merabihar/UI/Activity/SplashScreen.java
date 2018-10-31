package tv.merabihar.app.merabihar.UI.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import tv.merabihar.app.merabihar.BuildConfig;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.TabMainActivity;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;

public class SplashScreen extends AppCompatActivity {

    TextView mVersionName;
    ImageView appLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_splash_screen);

            mVersionName = (TextView) findViewById(R.id.version_name);

            appLogo = (ImageView) findViewById(R.id.bihar_splash);

            init();

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void init(){
        try{

            mVersionName.setText("Version code : "+ BuildConfig.VERSION_NAME+"");

            Animation fade_in = AnimationUtils.loadAnimation(this,R.anim.fade_in);

            appLogo.startAnimation(fade_in);


            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    int profileId = PreferenceHandler.getInstance(SplashScreen.this).getUserId();
                    if(profileId!=0){
                        Intent i = new Intent(SplashScreen.this, TabMainActivity.class);
                        startActivity(i);
                        finish();

                    }else{

                        Intent i = new Intent(SplashScreen.this, SlideOptionScreen.class);
                        startActivity(i);
                        finish();

                    }



                }
            }, 1000);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
