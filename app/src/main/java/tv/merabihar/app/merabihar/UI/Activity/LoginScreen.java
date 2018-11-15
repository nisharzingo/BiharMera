package tv.merabihar.app.merabihar.UI.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.Model.UserRole;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.TabMainActivity;
import tv.merabihar.app.merabihar.Util.Constants;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ProfileAPI;

public class LoginScreen extends AppCompatActivity {

    TextView mLoginAccount,mFPwd;
    EditText mUserName,mPassword;
    LinearLayout mSignUpScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_login_screen);

            mLoginAccount = (TextView)findViewById(R.id.loginAccount);
            mFPwd = (TextView)findViewById(R.id.forgot_pwd);
            mUserName = (EditText)findViewById(R.id.user_name);
            mPassword = (EditText)findViewById(R.id.pwd);
            mSignUpScreen = (LinearLayout)findViewById(R.id.sign_up_screen);

            mLoginAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    validate();
                }
            });

            mSignUpScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent create = new Intent(LoginScreen.this,SignUpScreen.class);
                    startActivity(create);
                }
            });

            mFPwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent create = new Intent(LoginScreen.this,PhoneNumberVerficationActivity.class);
                    startActivity(create);

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void validate(){

        String username = mUserName.getText().toString();
        String pwd = mPassword.getText().toString();

        if(textValidate(username)||textValidate(pwd)){

            Toast.makeText(this, "Field should not be empty", Toast.LENGTH_SHORT).show();
        }else{

            UserProfile profiles = new UserProfile();
            profiles.setPassword(pwd);

            profiles.setEmail(username);
            loginProfile(profiles);
        }
    }


    public boolean textValidate(final String text){

        if(text==null||text.isEmpty()){
            return true;
        }else{
            return false;
        }

    }

    private void loginProfile( final UserProfile p){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                ProfileAPI apiService = Util.getClient().create(ProfileAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getProfileforLogin(p);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {

                        int statusCode = response.code();
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        if (statusCode == 200 || statusCode == 201) {

                            ArrayList<UserProfile> dto1 = response.body();
                            if (dto1!=null && dto1.size()!=0) {

                                UserProfile dto = dto1.get(0);

                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginScreen.this);
                                SharedPreferences.Editor spe = sp.edit();
                                spe.putInt(Constants.USER_ID, dto.getProfileId());
                                PreferenceHandler.getInstance(LoginScreen.this).setUserId(dto.getProfileId());
                                PreferenceHandler.getInstance(LoginScreen.this).setUserName(dto.getEmail());
                                PreferenceHandler.getInstance(LoginScreen.this).setUserFullName(dto.getFullName());
                                spe.putString("FullName", dto.getFullName());
                                spe.putString("Password", dto.getPassword());
                                spe.putString("Email", dto.getEmail());
                                spe.putString("PhoneNumber", dto.getPhoneNumber());
                                spe.putInt("UserRoleId", dto.getUserRoleId());
                                spe.apply();

                                UserRole userRole = dto.getUserRoles();
                                if(userRole != null)
                                {
                                    System.out.println("Unique id = "+userRole.getUserRoleUniqueId());
                                    PreferenceHandler.getInstance(LoginScreen.this).setUserRoleUniqueID(userRole.getUserRoleUniqueId());
                                }

                                Toast.makeText(LoginScreen.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LoginScreen.this, TabMainActivity.class);
                                i.putExtra("Profile",dto);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();

                            }else{
                                if (progressDialog != null)
                                    progressDialog.dismiss();

                                Toast.makeText(LoginScreen.this, "Login credentials are wrong..", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            if (progressDialog!=null)
                                progressDialog.dismiss();

                            Toast.makeText(LoginScreen.this, "Login failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {

                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }
}
