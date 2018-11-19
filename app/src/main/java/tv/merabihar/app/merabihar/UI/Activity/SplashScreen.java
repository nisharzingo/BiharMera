package tv.merabihar.app.merabihar.UI.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.HashMap;

import tv.merabihar.app.merabihar.BuildConfig;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.TabMainActivity;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;

public class SplashScreen extends AppCompatActivity {

    TextView mVersionName;
    ImageView appLogo;
    public static final int MY_PERMISSIONS_REQUEST_RESULT = 1;

    public static final int R_PERM = 2822;
    private static final int REQUEST= 112;
    final String MainPP_SP = "MainPP_data";

    Context mContext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try{
//            Fresco.initialize(this);

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_splash_screen);

            mVersionName = (TextView) findViewById(R.id.version_name);

            appLogo = (ImageView) findViewById(R.id.bihar_splash);

            SharedPreferences settings = getSharedPreferences(MainPP_SP, 0);
            HashMap<String, String> map = (HashMap<String, String>) settings.getAll();

            if (Build.VERSION.SDK_INT >= 23) {
                Log.d("TAG","@@@ IN IF Build.VERSION.SDK_INT >= 23");
                String[] PERMISSIONS = {android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_PHONE_STATE,
                        android.Manifest.permission.INTERNET,
                        android.Manifest.permission.ACCESS_NETWORK_STATE,
                        android.Manifest.permission.ACCESS_WIFI_STATE,
                        android. Manifest.permission.NFC,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                };


                if (!hasPermissions(mContext, PERMISSIONS)) {
                    Log.d("TAG","@@@ IN IF hasPermissions");
                    ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST );
                } else {
                    Log.d("TAG","@@@ IN ELSE hasPermissions");
                    callNextActivity();
                }
            } else {
                Log.d("TAG","@@@ IN ELSE  Build.VERSION.SDK_INT >= 23");
                callNextActivity();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void init(){
        try{



           // appLogo.startAnimation(fade_in);







        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public boolean checkPermission() throws Exception{
        if ((ContextCompat.checkSelfPermission(SplashScreen.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(SplashScreen.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)&&
                (ContextCompat.checkSelfPermission(SplashScreen.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(SplashScreen.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this, android.Manifest.permission.ACCESS_FINE_LOCATION))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this, android.Manifest.permission.CALL_PHONE))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this, android.Manifest.permission.CAMERA))) {

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(SplashScreen.this,
                        new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CALL_PHONE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_RESULT);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(SplashScreen.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CALL_PHONE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_RESULT);


            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG","@@@ PERMISSIONS grant");
                    callNextActivity();
                } else {
                    Log.d("TAG","@@@ PERMISSIONS Denied");
                    Toast.makeText(mContext, "PERMISSIONS Denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void callNextActivity()
    {

        mVersionName.setText("Version code : "+ BuildConfig.VERSION_NAME+"");

        Animation fade_in = AnimationUtils.loadAnimation(this,R.anim.fade_in);

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
    }

}
