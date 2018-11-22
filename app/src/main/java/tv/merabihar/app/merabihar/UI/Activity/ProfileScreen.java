package tv.merabihar.app.merabihar.UI.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.ImagePorifleContentAdapter;
import tv.merabihar.app.merabihar.Adapter.ListViewAdapter;
import tv.merabihar.app.merabihar.Adapter.ProfileListAdapter;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Lato_Regular;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Roboto_Regular;
import tv.merabihar.app.merabihar.CustomViews.SnackbarViewer;
import tv.merabihar.app.merabihar.Model.BeanClass;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Model.FollowsWithMapping;
import tv.merabihar.app.merabihar.Model.ProfileFollowMapping;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.TabAccountActivity;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ContentAPI;
import tv.merabihar.app.merabihar.WebAPI.ProfileAPI;
import tv.merabihar.app.merabihar.WebAPI.ProfileFollowAPI;

public class ProfileScreen extends AppCompatActivity {


    CircleImageView mProfilePhoto;
    MyTextView_Roboto_Regular mProfileName,mProfileAbout,mPosts,
            mFollowers,mFollowings,mNoPosts;
    RecyclerView mFollowingPeoples;
    RecyclerView mPostsList;
    AppCompatButton mFollowOption;
    //ListView mPostsList;
    ProgressBar progressBar;

    UserProfile profile;
    int profileId=0,mappingId=0;


    @Override
    protected void onRestart() {
        super.onRestart();



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            setContentView(R.layout.activity_profile_screen);


            mProfilePhoto = (CircleImageView)findViewById(R.id.profile_photo);
            mProfileName = (MyTextView_Roboto_Regular)findViewById(R.id.profile_name);
            mProfileAbout = (MyTextView_Roboto_Regular)findViewById(R.id.profile_about);
            mPosts = (MyTextView_Roboto_Regular)findViewById(R.id.tvPosts);
            mFollowers = (MyTextView_Roboto_Regular)findViewById(R.id.tvFollowers);
            mFollowings = (MyTextView_Roboto_Regular)findViewById(R.id.tvFollowing);
            mNoPosts = (MyTextView_Roboto_Regular)findViewById(R.id.no_post);
            mFollowingPeoples = (RecyclerView)findViewById(R.id.follow_profiles);
            mPostsList = (RecyclerView)findViewById(R.id.listviews);
            progressBar = (ProgressBar)findViewById(R.id.progressBar);
            mFollowOption = (AppCompatButton) findViewById(R.id.follow_unfollow);

            mFollowingPeoples.setLayoutManager(new LinearLayoutManager(ProfileScreen.this, LinearLayoutManager.HORIZONTAL, false));
            mFollowingPeoples.setNestedScrollingEnabled(false);

            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ProfileScreen.this, 3);
            mPostsList.setLayoutManager(layoutManager);
            mPostsList.setItemAnimator(new DefaultItemAnimator());


           Bundle bundle = getIntent().getExtras();

           if(bundle!=null){

               profile = (UserProfile)bundle.getSerializable("Profile");
               profileId = bundle.getInt("ProfileId");
           }

            if (Util.isNetworkAvailable(ProfileScreen.this)) {

                if(profile!=null){

                    profileId = profile.getProfileId();
                    if(PreferenceHandler.getInstance(ProfileScreen.this).getUserId()!=0){
                        if(profileId==PreferenceHandler.getInstance(ProfileScreen.this).getUserId()){
                            mFollowOption.setVisibility(View.GONE);
                        }else{
                            mFollowOption.setVisibility(View.VISIBLE);
                            getFollowingsByProfileId(PreferenceHandler.getInstance(ProfileScreen.this).getUserId(),profile.getProfileId());
                        }
                    }else{
                        mFollowOption.setVisibility(View.GONE);
                    }
                    getProfile(profile.getProfileId());
                    getProfileContent(profile.getProfileId());
                    getFollowingByProfileId(profile.getProfileId());
                    getFollowersByProfileId(profile.getProfileId());



                }else if(profileId!=0){
                    getProfile(profileId);
                    getProfileContent(profileId);
                    getFollowingByProfileId(profileId);
                    getFollowersByProfileId(profileId);
                    if(PreferenceHandler.getInstance(ProfileScreen.this).getUserId()!=0){
                        if(profileId==PreferenceHandler.getInstance(ProfileScreen.this).getUserId()){
                            mFollowOption.setVisibility(View.GONE);
                        }else{
                            mFollowOption.setVisibility(View.VISIBLE);
                            getFollowingsByProfileId(PreferenceHandler.getInstance(ProfileScreen.this).getUserId(),profile.getProfileId());
                        }
                    }else{
                        mFollowOption.setVisibility(View.GONE);
                    }
                }else{

                    SnackbarViewer.showSnackbar(findViewById(R.id.activity_profile_main),"Something went wrong");
                }

            }else{

                SnackbarViewer.showSnackbar(findViewById(R.id.activity_profile_main),"No Internet connection");
                progressBar.setVisibility(View.GONE);
            }


            mFollowOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (Util.isNetworkAvailable(ProfileScreen.this)) {

                        if(profileId!=0){

                            String follow = mFollowOption.getText().toString();

                            if(follow!=null&&!follow.isEmpty()){

                                if(follow.equalsIgnoreCase("Follow")){

                                    ProfileFollowMapping pm = new ProfileFollowMapping();
                                    pm.setFollowerId(profileId);
                                    pm.setProfileId(PreferenceHandler.getInstance(ProfileScreen.this).getUserId());
                                    profileFollow(pm);

                                }else if(follow.equalsIgnoreCase("Unfollow")){


                                    if(mappingId!=0){

                                        deleteFollow(mappingId);

                                    }else{
                                        Toast.makeText(ProfileScreen.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                        }else {
                            new AlertDialog.Builder(ProfileScreen.this)
                                    .setMessage("Please login/Signup to Like the Story")
                                    .setCancelable(true)
                                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            Intent login = new Intent(ProfileScreen.this, LoginScreen.class);
                                            startActivity(login);

                                        }
                                    })
                                    .setNegativeButton("SignUp", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            Intent signUp = new Intent(ProfileScreen.this, SignUpScreen.class);
                                            startActivity(signUp);

                                        }
                                    })
                                    .show();
                        }


                    } else{
                            SnackbarViewer.showSnackbar(findViewById(R.id.activity_profile_main),"Please check your internet connection !");

                    }

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getProfile(final int id){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ProfileAPI subCategoryAPI = Util.getClient().create(ProfileAPI.class);
                Call<UserProfile> getProf = subCategoryAPI.getProfileById(id);
                //Call<ArrayList<Blogs>> getBlog = blogApi.getBlogs();

                getProf.enqueue(new Callback<UserProfile>() {

                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.code() == 200)
                        {
                            System.out.println("Inside api");

                            UserProfile profile = response.body();

                            if(profile.getProfilePhoto()!=null){

                                String base=profile.getProfilePhoto();
                                mProfileName.setText(""+profile.getFullName());

                                if(profile.getPrefix()!=null){
                                    mProfileAbout.setText(""+profile.getPrefix());
                                }


                                if(base != null && !base.isEmpty()){
                                    Picasso.with(ProfileScreen.this).load(base).placeholder(R.drawable.profile_image).
                                            error(R.drawable.profile_image).into(mProfilePhoto);

                                }else{
                                    mProfilePhoto.setImageResource(R.drawable.profile_image);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {

                    }
                });

            }

        });
    }
    private void getFollowerByProfileId(final int id){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getFollowersByProfileId(id);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        progressBar.setVisibility(View.GONE);

                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {

                                Collections.shuffle(responseProfile);
                                ProfileListAdapter adapter = new ProfileListAdapter(ProfileScreen.this,responseProfile);
                                mFollowingPeoples.setAdapter(adapter);


                            }
                            else
                            {

                            }
                        }
                        else
                        {

                            Toast.makeText(ProfileScreen.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed

                        progressBar.setVisibility(View.GONE);

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    private void getFollowersByProfileId(final int id){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getFollowersByProfileId(id);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        progressBar.setVisibility(View.GONE);

                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {

                                mFollowers.setText(""+responseProfile.size());


                            }
                            else
                            {



                            }
                        }
                        else
                        {

                            Toast.makeText(ProfileScreen.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed

                        progressBar.setVisibility(View.GONE);

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    private void getFollowingByProfileId(final int id){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getFollowingByProfileId(id);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        progressBar.setVisibility(View.GONE);

                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {
                                Collections.shuffle(responseProfile);
                                mFollowings.setText(""+responseProfile.size());
                                ProfileListAdapter adapter = new ProfileListAdapter(ProfileScreen.this,responseProfile);
                                mFollowingPeoples.setAdapter(adapter);


                            }
                            else
                            {

                                getFollowerByProfileId(id);
                            }
                        }
                        else
                        {

                            getFollowerByProfileId(id);
                            Toast.makeText(ProfileScreen.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed

                        progressBar.setVisibility(View.GONE);
                        getFollowerByProfileId(id);

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    public void getProfileContent(final int profileId)
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ContentAPI categoryAPI = Util.getClient().create(ContentAPI.class);
                Call<ArrayList<Contents>> getCat = categoryAPI.getContentByProfileId(profileId);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Contents>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {



                        if(response.code() == 200 && response.body()!= null)
                        {
                            progressBar.setVisibility(View.GONE);

                            if(response.body().size()!=0){

                                mPosts.setText(""+response.body().size());
                                ImagePorifleContentAdapter adapters = new ImagePorifleContentAdapter(ProfileScreen.this,response.body());
                                mPostsList.setAdapter(adapters);
                            }

                        }else{

                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Contents>> call, Throwable t) {

                        progressBar.setVisibility(View.GONE);

//                        Toast.makeText(ProfileScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    private void getFollowingsByProfileId(final int id, final int contentProfileId){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<FollowsWithMapping>> call = apiService.getFollowingsWithMappingByProfileId(id);

                call.enqueue(new Callback<ArrayList<FollowsWithMapping>>() {
                    @Override
                    public void onResponse(Call<ArrayList<FollowsWithMapping>> call, Response<ArrayList<FollowsWithMapping>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();


                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<FollowsWithMapping> responseProfile = response.body();
                            boolean value = false;

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {

                                for (FollowsWithMapping profile:responseProfile) {

                                    if(profile.getFollowers().getProfileId()==contentProfileId){

                                        value = true;

                                       mappingId = profile.getFollowMapping().getFollowId();

                                        break;
                                    }

                                }

                                if(value){
                                    mFollowOption.setVisibility(View.VISIBLE);
                                    mFollowOption.setText("Unfollow");

                                }else{
                                    mFollowOption.setVisibility(View.VISIBLE);
                                    mFollowOption.setText("Follow");
                                }



                            }
                            else
                            {
                                mFollowOption.setVisibility(View.VISIBLE);
                                mFollowOption.setText("Follow");

                            }
                        }
                        else
                        {


                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<FollowsWithMapping>> call, Throwable t) {
                        // Log error here since request failed



                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    private void profileFollow(final ProfileFollowMapping intrst) {


        final ProgressDialog dialog = new ProgressDialog(ProfileScreen.this);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ProfileFollowAPI mapApi = Util.getClient().create(ProfileFollowAPI.class);
                Call<ProfileFollowMapping> response = mapApi.postInterest(intrst);
                response.enqueue(new Callback<ProfileFollowMapping>() {
                    @Override
                    public void onResponse(Call<ProfileFollowMapping> call, Response<ProfileFollowMapping> response) {

                        System.out.println(response.code());

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {


                            mFollowOption.setText("Unfollow");
                            mappingId = response.body().getFollowId();

                        }
                        else
                        {

                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileFollowMapping> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
//                        Toast.makeText(ProfileScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    private void deleteFollow(final int mapId) {


        final ProgressDialog dialog = new ProgressDialog(ProfileScreen.this);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ProfileFollowAPI mapApi = Util.getClient().create(ProfileFollowAPI.class);
                Call<ProfileFollowMapping> response = mapApi.deleteIntrs(mapId);
                response.enqueue(new Callback<ProfileFollowMapping>() {
                    @Override
                    public void onResponse(Call<ProfileFollowMapping> call, Response<ProfileFollowMapping> response) {

                        System.out.println(response.code());

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {


                            mFollowOption.setText("Follow");
                            mappingId = 0;

                        }
                        else
                        {
                            Toast.makeText(ProfileScreen.this, "Please try again", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileFollowMapping> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
//                        Toast.makeText(ProfileScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

}
