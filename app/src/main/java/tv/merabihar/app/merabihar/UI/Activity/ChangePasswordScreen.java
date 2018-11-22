package tv.merabihar.app.merabihar.UI.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ProfileAPI;

public class ChangePasswordScreen extends AppCompatActivity {

    TextInputEditText mNewPassword,mConfirm;
    AppCompatButton mSave;


    UserProfile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_change_password_screen);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Change Password");


            mNewPassword = (TextInputEditText)findViewById(R.id.newpwd);
            mConfirm = (TextInputEditText)findViewById(R.id.confirmpwd);
            mSave = (AppCompatButton) findViewById(R.id.savePassword);

            Bundle bundle = getIntent().getExtras();

            if(bundle!=null){

                profile = (UserProfile)bundle.getSerializable("Profile");
            }

            mSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(profile!=null){
                        validate();
                    }else{
                        Toast.makeText(ChangePasswordScreen.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void validate(){

        String newPwd = mNewPassword.getText().toString();
        String cnfrmPwd = mConfirm.getText().toString();

        if(newPwd.isEmpty()){

            Toast.makeText(this, "New Password is required", Toast.LENGTH_SHORT).show();

        }else if(cnfrmPwd.isEmpty()){

            Toast.makeText(this, "Confirm password is required", Toast.LENGTH_SHORT).show();

        }else if(!newPwd.isEmpty()&&!cnfrmPwd.isEmpty()&&!newPwd.equalsIgnoreCase(cnfrmPwd)){

            Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();

        }else{
            UserProfile profiles = profile;
            profiles.setPassword(cnfrmPwd);
            updateProfile(profiles);
        }
    }


    public void updateProfile(final UserProfile employee){

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Updaitng Password");
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ProfileAPI subCategoryAPI = Util.getClient().create(ProfileAPI.class);
                Call<UserProfile> getProf = subCategoryAPI.updateProfile(employee.getProfileId(),employee);
                //Call<ArrayList<Blogs>> getBlog = blogApi.getBlogs();

                getProf.enqueue(new Callback<UserProfile>() {

                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        if (response.code() == 200||response.code()==201||response.code()==204)
                        {
                            PreferenceHandler.getInstance(ChangePasswordScreen.this).clear();

                            Intent log = new Intent(ChangePasswordScreen.this, LoginScreen.class);
                            log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            Toast.makeText(ChangePasswordScreen.this,"Logout",Toast.LENGTH_SHORT).show();
                            startActivity(log);
                            finish();

                        }else{
                            Toast.makeText(ChangePasswordScreen.this, "Failed due to status code"+response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(ChangePasswordScreen.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(ChangePasswordScreen.this, "Something went wrong due to "+t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:

                ChangePasswordScreen.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
