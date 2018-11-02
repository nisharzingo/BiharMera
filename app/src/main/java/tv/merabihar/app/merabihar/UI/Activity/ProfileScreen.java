package tv.merabihar.app.merabihar.UI.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Roboto_Regular;
import tv.merabihar.app.merabihar.Model.BeanClass;
import tv.merabihar.app.merabihar.Model.Contents;
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
    //ListView mPostsList;
    ProgressBar progressBar;

    UserProfile profile;

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

            mFollowingPeoples.setLayoutManager(new LinearLayoutManager(ProfileScreen.this, LinearLayoutManager.HORIZONTAL, false));
            mFollowingPeoples.setNestedScrollingEnabled(false);

            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ProfileScreen.this, 3);
            mPostsList.setLayoutManager(layoutManager);
            mPostsList.setItemAnimator(new DefaultItemAnimator());


           Bundle bundle = getIntent().getExtras();

           if(bundle!=null){

               profile = (UserProfile)bundle.getSerializable("Profile");
           }
            if(profile!=null){

                getProfile(profile.getProfileId());
                getProfileContent(profile.getProfileId());
                getFollowingByProfileId(profile.getProfileId());
                getFollowersByProfileId(profile.getProfileId());


            }else{

            }



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

                        Toast.makeText(ProfileScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

}
