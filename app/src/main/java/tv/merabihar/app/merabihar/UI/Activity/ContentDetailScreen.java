package tv.merabihar.app.merabihar.UI.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Lato_Regular;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_SF_Pro_Light;
import tv.merabihar.app.merabihar.CustomFonts.TextViewSFProDisplayRegular;
import tv.merabihar.app.merabihar.Model.ContentImages;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Model.FollowsWithMapping;
import tv.merabihar.app.merabihar.Model.ProfileFollowMapping;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.Constants;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ProfileAPI;
import tv.merabihar.app.merabihar.WebAPI.ProfileFollowAPI;

public class ContentDetailScreen extends AppCompatActivity implements YouTubePlayer.OnInitializedListener{


    ImageView mback;
    TextViewSFProDisplayRegular mSubCategory,mReadTime;
    MyTextView_SF_Pro_Light mContentTitle,mContentDesc;
    MyTextView_Lato_Regular mProfileName,mFollow;
    CircleImageView mProfilePhoto;
    LinearLayout mProfileContent;

    private YouTubePlayerFragment playerFragmentTop;
    private YouTubePlayer mPlayer;
    private String YouTubeKey = Constants.YOUTUBE,url;

    Contents contents;
    int mappingId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_content_detail_screen);
            //GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);


            mback = (ImageView)findViewById(R.id.back_view);
            mSubCategory = (TextViewSFProDisplayRegular)findViewById(R.id.subcategory_of_content);
            mReadTime = (TextViewSFProDisplayRegular)findViewById(R.id.read_time);
            mContentTitle = (MyTextView_SF_Pro_Light)findViewById(R.id.content_title);
            mContentDesc = (MyTextView_SF_Pro_Light)findViewById(R.id.content_desc);
            mProfileName = (MyTextView_Lato_Regular)findViewById(R.id.profile_name);
            mFollow = (MyTextView_Lato_Regular)findViewById(R.id.follow_profile);
            mProfilePhoto = (CircleImageView) findViewById(R.id.profile_photo);
            mProfileContent = (LinearLayout) findViewById(R.id.profile_lay_content);
            playerFragmentTop = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_player_fragment_top);

            final Bundle bundle = getIntent().getExtras();
            if(bundle!=null){
                contents = (Contents) bundle.getSerializable("Contents");
            }

            if(contents!=null){
                setViewPager();
            }else{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            mback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ContentDetailScreen.this.finish();
                }
            });

            mProfileContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(contents!=null){
                        Intent intent = new Intent(ContentDetailScreen.this, ProfileScreen.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Profile",contents.getProfile());
                        bundle.putInt("ProfileId",contents.getProfileId());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }


                }
            });

            mProfilePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(contents!=null){
                        Intent intent = new Intent(ContentDetailScreen.this, ProfileScreen.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Profile",contents.getProfile());
                        bundle.putInt("ProfileId",contents.getProfileId());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }


                }
            });

            mFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String follow = mFollow.getText().toString();

                    if(contents!=null){
                        if(follow!=null&&!follow.isEmpty()){

                            if(follow.equalsIgnoreCase("Follow")){

                                ProfileFollowMapping pm = new ProfileFollowMapping();
                                pm.setFollowerId(contents.getProfileId());
                                pm.setProfileId(PreferenceHandler.getInstance(ContentDetailScreen.this).getUserId());
                                profileFollow(pm);

                            }else if(follow.equalsIgnoreCase("Unfollow")){


                                if(mappingId!=0){

                                    deleteFollow(mappingId);

                                }else{
                                    Toast.makeText(ContentDetailScreen.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }


                }
            });

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void setViewPager() {

        try{

            final int profileId = PreferenceHandler.getInstance(ContentDetailScreen.this).getUserId();

            if(contents!=null) {

                String vWatch = "W" + contents.getContentId();
                getFollowingsByProfileId(profileId,contents.getProfileId());


                if (contents.getProfile() == null) {
                    getProfile(contents.getProfileId());
                } else {


                }

                if (contents.getSubCategories() == null) {
                   mSubCategory.setText(""+contents.getSubCategories().getSubCategoriesName());
                } else {


                }

                if(profileId!=0){

                    if(profileId==contents.getProfileId()){

                        mFollow.setVisibility(View.GONE);
                    }else{
                        //getFollowingByProfileId(profileId,mFollowLay,blogDataModel.getProfileId());

                    }

                }

                SimpleDateFormat sdfs = new SimpleDateFormat("MMM dd,yyyy");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                String blogName = contents.getTitle();
                String blogShort = contents.getDescription();
                String createdDate = contents.getCreatedDate();

                if(createdDate!=null||!createdDate.isEmpty()){

                    if(createdDate.contains("T")){
                                  /*  String date = createdDate.replace("T","");
                                    holder.mDuration.setText(""+duration(date));*/

                        String date[] = createdDate.split("T");
                        mReadTime.setText(""+date[0]);
                    }

                }else{

                }

                if(contents.getContentURL()!=null&&!contents.getContentURL().isEmpty()){

                    url = contents.getContentURL();

                    playerFragmentTop.initialize(YouTubeKey, ContentDetailScreen.this);

                }


                mContentTitle.setText(blogName + "");
                mContentDesc.setText(blogShort + "");


            }






        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void getProfile(final int id){

      /*  final ProgressDialog dialog = new ProgressDialog(ActivityDetail.this);
        dialog.setMessage("Loading Packages");
        dialog.setCancelable(false);
        dialog.show();*/

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ProfileAPI subCategoryAPI = Util.getClient().create(ProfileAPI.class);
                Call<UserProfile> getProf = subCategoryAPI.getProfileById(id);
                //Call<ArrayList<Blogs>> getBlog = blogApi.getBlogs();

                getProf.enqueue(new Callback<UserProfile>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }
*/
                        if (response.code() == 200)
                        {
                            System.out.println("Inside api");

                            UserProfile profile = response.body();

                            mProfileName.setText(""+profile.getFullName());

                            if(profile.getProfilePhoto()!=null){

                                String base=profile.getProfilePhoto();
                                if(base != null && !base.isEmpty()){
                                    Picasso.with(ContentDetailScreen.this).load(base).placeholder(R.drawable.profile_image).
                                            error(R.drawable.profile_image).into(mProfilePhoto);
                                    //mImage.setImageBitmap(
                                    //      BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

                                }
                            }




                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }
*/
                        //Toast.makeText(.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        mPlayer = player;

        //Enables automatic control of orientation
        mPlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);

        //Show full screen in landscape mode always
        //mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);

        //System controls will appear automatically
        mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);

        if (!wasRestored) {
            player.loadVideo(url);
            //mPlayer.loadVideo("9rLZYyMbJic");
        }
        else
        {
            mPlayer.play();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        mPlayer = null;
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
                                    mFollow.setVisibility(View.VISIBLE);
                                    mFollow.setText("Unfollow");

                                }else{
                                    mFollow.setVisibility(View.VISIBLE);
                                    mFollow.setText("Follow");
                                }



                            }
                            else
                            {
                                mFollow.setVisibility(View.VISIBLE);
                                mFollow.setText("Follow");

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


        final ProgressDialog dialog = new ProgressDialog(ContentDetailScreen.this);
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


                            mFollow.setText("Unfollow");
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
                        Toast.makeText(ContentDetailScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    private void deleteFollow(final int mapId) {


        final ProgressDialog dialog = new ProgressDialog(ContentDetailScreen.this);
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


                            mFollow.setText("Follow");
                            mappingId = 0;

                        }
                        else
                        {
                            Toast.makeText(ContentDetailScreen.this, "Please try again", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileFollowMapping> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(ContentDetailScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
}
