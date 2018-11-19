package tv.merabihar.app.merabihar.UI.Activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;
import tv.merabihar.app.merabihar.Adapter.SlideAdapter;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Roboto_Regular;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.TabMainActivity;

public class SlideOptionScreen extends AppCompatActivity {

    private ViewPager viewPager;
    private CircleImageView mAppLogo;
    private SlideAdapter a;
    private CircleIndicator indicator;

    MyTextView_Roboto_Regular mSignUp,mLogin, mLoginAsGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_slide_option_screen);

            viewPager = (ViewPager)findViewById(R.id.view_pager_slide);
            mAppLogo = (CircleImageView) findViewById(R.id.app_logos);
            indicator = (CircleIndicator)findViewById(R.id.indicator);
            mSignUp = (MyTextView_Roboto_Regular)findViewById(R.id.signUp);
            mLogin = (MyTextView_Roboto_Regular)findViewById(R.id.login);
            mLoginAsGuest = (MyTextView_Roboto_Regular)findViewById(R.id.login_user_asguest_btn);


            a = new SlideAdapter(getSupportFragmentManager());
            viewPager.setAdapter(a);
            indicator.setViewPager(viewPager);
            a.registerDataSetObserver(indicator.getDataSetObserver());

            mLoginAsGuest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent guestLogin = new Intent(SlideOptionScreen.this, TabMainActivity.class);
                    startActivity(guestLogin);
                    SlideOptionScreen.this.finish();
                }
            });



            mLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent login = new Intent(SlideOptionScreen.this,LoginScreen.class);
                    startActivity(login);

                }
            });

            mSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent signUp = new Intent(SlideOptionScreen.this,SignUpScreen.class);
                    startActivity(signUp);

                }
            });

            mAppLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                }
            });

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
