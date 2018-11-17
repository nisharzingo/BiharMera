package tv.merabihar.app.merabihar.UI.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

public class SignUpScreen extends AppCompatActivity {

    FrameLayout mCreateAccount;
    TextInputEditText mName,mEmail,mMobile,mPassword,mConfirmPassword,mReferalCode;
    TextView mLoginScreen,mPrivacy;
    RadioButton mMale,mFemale,mOther;
    CheckBox mTerms;

    boolean refered = false;
    int referedProfileId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_sign_up_screen);

            mCreateAccount = (FrameLayout)findViewById(R.id.createAccount);
            mName = (TextInputEditText)findViewById(R.id.full_name);
            mEmail = (TextInputEditText)findViewById(R.id.email);
            mMobile = (TextInputEditText)findViewById(R.id.mobile_number);
            mPassword = (TextInputEditText)findViewById(R.id.password);
            mConfirmPassword = (TextInputEditText)findViewById(R.id.confirm_password);
            mReferalCode = (TextInputEditText)findViewById(R.id.referal_code);
            mLoginScreen = (TextView)findViewById(R.id.login_screen);
            mPrivacy = (TextView)findViewById(R.id.termsConditionText);
            mMale = (RadioButton)findViewById(R.id.sign_up_male);
            mFemale = (RadioButton)findViewById(R.id.sign_up_female);
            mOther = (RadioButton)findViewById(R.id.sign_up_other);
            mTerms = (CheckBox)findViewById(R.id.termsCondition);

            mCreateAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(mTerms.isChecked()){
                        try {
                            mCreateAccount.setEnabled(false);
                            validate();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{

                        Toast.makeText(SignUpScreen.this, "Agree Privacy Policy", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            mLoginScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent create = new Intent(SignUpScreen.this,LoginScreen.class);
                    startActivity(create);
                    SignUpScreen.this.finish();
                }
            });

            mPrivacy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent create = new Intent(SignUpScreen.this,PrivacyPolicyScreen.class);
                    startActivity(create);

                }
            });


            mName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    if( mName.getText().length()>0)
                    {
                        mName.setError(null);
                    }

                }
            });

            mEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    if( mEmail.getText().length()>0)
                    {
                        mEmail.setError(null);
                    }

                }
            });

            mMobile.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    if( mMobile.getText().length()>0)
                    {
                        mMobile.setError(null);
                    }

                }
            });

            mPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    if( mPassword.getText().length()>0)
                    {
                        mPassword.setError(null);
                    }

                }
            });

            mConfirmPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    if( mConfirmPassword.getText().length()>0)
                    {
                        mConfirmPassword.setError(null);
                    }

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }


    }


    public void validate() throws Exception{

        String name = mName.getText().toString();
        String email = mEmail.getText().toString();
        String mobile = mMobile.getText().toString();
        String password = mPassword.getText().toString();
        String confirmPassword = mConfirmPassword.getText().toString();
        String referal = mReferalCode.getText().toString();

        if(name==null||name.isEmpty()){

            //Toast.makeText(this, "Please enter Name", Toast.LENGTH_SHORT).show();
            mName.requestFocus();
            mName.setError("Please enter Name");

        }else if(!isValidMail(email)){

            //Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show();
            mEmail.requestFocus();
            mEmail.setError("Please enter Email");

        }else if(!isValidMobile(mobile)){

           // Toast.makeText(this, "Please enter Mobile", Toast.LENGTH_SHORT).show();
            mMobile.requestFocus();
            mMobile.setError("Please enter valid Mobile");

        }else if(password==null||password.isEmpty()){

           // Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
            mPassword.requestFocus();
            mPassword.setError("Please enter Password");

        }else if(password!=null&&!password.isEmpty()&&password.length()<8){

            // Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
            mPassword.requestFocus();
            mPassword.setError("Password length is 8");

        }else if(password!=null&&!password.isEmpty()&&password.contains(" ")){

            // Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
            mPassword.requestFocus();
            mPassword.setError("Password should not contain space");

        }else if(confirmPassword==null||confirmPassword.isEmpty()){

            //Toast.makeText(this, "Please enter Confirm Password", Toast.LENGTH_SHORT).show();
            mConfirmPassword.requestFocus();
            mConfirmPassword.setError("Please enter Confirm Password");

        }else if(!password.equals(confirmPassword)){

            //Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show();
            mConfirmPassword.requestFocus();
            mConfirmPassword.setError("Password is not matching");
        }else if(!mMale.isChecked()&&!mFemale.isChecked()&&!mOther.isChecked()){

            Toast.makeText(this, "Please select Gender", Toast.LENGTH_SHORT).show();

        }else{

            UserProfile profiles = new UserProfile();
            profiles.setFullName(name);
            profiles.setPassword(password);
            if(mMale.isChecked()){
                profiles.setGender("Male");
            }else if(mFemale.isChecked()){
                profiles.setGender("Female");
            }else if(mOther.isChecked()){
                profiles.setGender("Others");
            }
            profiles.setEmail(email);
            profiles.setPhoneNumber(mobile);
            profiles.setUserRoleId(1);
            profiles.setMemberType("Beginner");

            profiles.setStatus("Active");
            profiles.setAuthType("Normal");
            profiles.setAuthId("Normal");


            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            profiles.setSignUpDate(sdf.format(new Date()));


            if(referal!=null&&!referal.isEmpty()){

                String stringFromBase = new String(Base64.decode(referal, Base64.DEFAULT));
                if(referal.contains("MBR")){
                    profiles.setReferralCodeUsed(referal);
                    profiles.setReferralAmount(50.0);
                    profiles.setReferralAmountForOtherProfile(0.5);
                    String mbr = referal.replace("MBR","");
                    checkUserByReferalCode(profiles,"Normal",mbr);
                }else{
                    profiles.setReferralCodeUsed(stringFromBase);
                    profiles.setReferralAmount(50.0);
                    profiles.setReferralAmountForOtherProfile(0.5);
                    String mbr = stringFromBase.replace("MBR","");
                    checkUserByReferalCode(profiles,"Normal",mbr);
                }



            }else{
                checkUserByEmailId(profiles,"Normal");
            }

        }

    }

    private boolean isValidMobile(String phone) {

        System.out.println("Mobile "+phone);
        System.out.println("Mobile valid "+android.util.Patterns.PHONE.matcher(phone).matches());
        return android.util.Patterns.PHONE.matcher(phone).matches();

    }

    private boolean isValidMail(String email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    private void checkUserByReferalCode(final UserProfile userProfile,final String type,final String i){




        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileAPI apiService =
                        Util.getClient().create(ProfileAPI.class);

                Call<UserProfile> call = apiService.getProfileById(Integer.parseInt(i));

                call.enqueue(new Callback<UserProfile>() {
                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        if(statusCode == 200 || statusCode == 204)
                        {
                            UserProfile responseProfile = response.body();
                            if(responseProfile != null )
                            {

                                refered = true;
                                referedProfileId = responseProfile.getProfileId();

                                String referUsed = responseProfile.getReferralCodeUsed();
                                String referParent = responseProfile.getReferralCodeOfParents();
                                String referSuper = responseProfile.getReferralCodeOfSuperParents();

                                if(referUsed!=null&&!referUsed.isEmpty()){

                                    userProfile.setReferralCodeOfParents(referUsed);


                                }else{
                                    userProfile.setReferralCodeOfParents(userProfile.getReferralCodeUsed());
                                }

                                if(referSuper!=null&&!referSuper.isEmpty()){

                                    userProfile.setReferralCodeOfSuperParents(referSuper);

                                }else{
                                    if(referParent!=null&&!referParent.isEmpty()){

                                        userProfile.setReferralCodeOfSuperParents(referParent);

                                    }else{
                                        userProfile.setReferralCodeOfSuperParents(userProfile.getReferralCodeUsed());
                                    }
                                }

                                checkUserByEmailId(userProfile,type);


                            }
                            else
                            {

                                mReferalCode.setError("Invalid Referal Code");
                                mReferalCode.requestFocus();
                                mCreateAccount.setEnabled(true);
                            }
                        }
                        else
                        {

                            Toast.makeText(SignUpScreen.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    private void checkUserByEmailId(final UserProfile userProfile,final String type){


        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Please wait..");
        dialog.show();


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileAPI apiService =
                        Util.getClient().create(ProfileAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getUserByEmail(userProfile);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();
                            if(responseProfile != null && responseProfile.size()!=0 )
                            {

                                if(type!=null&&!type.isEmpty()){
                                    if(type.equalsIgnoreCase("Google")){
                                        Toast.makeText(SignUpScreen.this, "Email already registered with us", Toast.LENGTH_SHORT).show();

                                        /*if(mGoogleApiClient!=null){

                                            if (mGoogleApiClient.isConnected()) {
                                                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                                                mGoogleApiClient.disconnect();
                                                mGoogleApiClient.connect();
                                            }
                                        }*/
                                    }else if(type.equalsIgnoreCase("Facebook")){
                                        /*if(mFbLoginManager!=null){

                                            AccessToken accessToken = AccessToken.getCurrentAccessToken();

                                            if(accessToken!=null){

                                                mFbLoginManager.logOut();
                                            }


                                        }//lv-uw03*/

                                        Toast.makeText(SignUpScreen.this, "Email Already Exists", Toast.LENGTH_SHORT).show();
                                    }else{
                                        mEmail.setError("Email Already Exists");
                                        mCreateAccount.setEnabled(true);
                                    }
                                }else{
                                    mEmail.setError("Email Already Exists");
                                    mCreateAccount.setEnabled(true);
                                }

                            }
                            else
                            {

                                if(userProfile.getPhoneNumber()==null|| userProfile.getPhoneNumber().isEmpty()){
                                    postProfile(userProfile);
                                }else{
                                    checkUserByPhone(userProfile,type);
                                }


                            }
                        }
                        else
                        {

                            Toast.makeText(SignUpScreen.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    private void checkUserByPhone(final UserProfile userProfile,final String type){


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileAPI apiService =
                        Util.getClient().create(ProfileAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getUserByPhone(userProfile);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        if(statusCode == 200 || statusCode == 204)
                        {
                            ArrayList<UserProfile> responseProfile = response.body();
                            if(responseProfile != null && responseProfile.size()!=0 )
                            {

                                if(type!=null&&!type.isEmpty()){
                                    if(type.equalsIgnoreCase("Google")){
                                        Toast.makeText(SignUpScreen.this, "Mobile number already registered with us", Toast.LENGTH_SHORT).show();
                                        mCreateAccount.setEnabled(true);
                                    }else{
                                        mMobile.setError("Number Already Exists");
                                        mCreateAccount.setEnabled(true);
                                    }
                                }else{
                                    mMobile.setError("Number Already Exists");
                                    mCreateAccount.setEnabled(true);
                                }



                            }
                            else
                            {

                                postProfile(userProfile);
                            }
                        }
                        else
                        {

                            Toast.makeText(SignUpScreen.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    private void postProfile(final UserProfile userProfile) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Please wait..");
        dialog.show();



        //System.out.println(sub.getCategoriesName()+","+sub.getDescription()+","+sub.getOrderNo()+","+sub.getCityId());

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ProfileAPI auditApi = Util.getClient().create(ProfileAPI.class);
                Call<UserProfile> response = auditApi.postProfile(userProfile);
                response.enqueue(new Callback<UserProfile>() {
                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());

                        if(response.code() == 201)
                        {

                            if(response.body()!=null){

                                UserProfile dto = response.body();
                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SignUpScreen.this);
                                SharedPreferences.Editor spe = sp.edit();
                                spe.putInt(Constants.USER_ID, dto.getProfileId());
                                PreferenceHandler.getInstance(SignUpScreen.this).setUserId(dto.getProfileId());
                                PreferenceHandler.getInstance(SignUpScreen.this).setUserName(dto.getEmail());
                                PreferenceHandler.getInstance(SignUpScreen.this).setUserFullName(dto.getFullName());
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
                                    PreferenceHandler.getInstance(SignUpScreen.this).setUserRoleUniqueID(userRole.getUserRoleUniqueId());
                                }

                               /* if(mGoogleApiClient!=null){

                                    if (mGoogleApiClient.isConnected()) {
                                        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                                        mGoogleApiClient.disconnect();
                                        mGoogleApiClient.connect();
                                    }
                                }
*/
                                /*if(mFbLoginManager!=null){

                                    AccessToken accessToken = AccessToken.getCurrentAccessToken();

                                    if(accessToken!=null){

                                        mFbLoginManager.logOut();
                                    }
                                }*/




                                Toast.makeText(SignUpScreen.this,"Profile created Successfull",Toast.LENGTH_SHORT).show();
                                //Intent intent = new Intent(SignUpScreen.this, PickInterestsScreenForProfile.class);
                                Intent intent = new Intent(SignUpScreen.this, PickInterestsScreenForProfile.class);
                                intent.putExtra("Activity","SignUp");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                SignUpScreen.this.finish();

                            }

                        }
                        else
                        {
                            mCreateAccount.setEnabled(true);
                            Toast.makeText(SignUpScreen.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        mCreateAccount.setEnabled(true);
                        Toast.makeText(SignUpScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

}
