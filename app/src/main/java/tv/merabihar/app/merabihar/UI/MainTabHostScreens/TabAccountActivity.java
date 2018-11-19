package tv.merabihar.app.merabihar.UI.MainTabHostScreens;

import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.ContentAdapterVertical;
import tv.merabihar.app.merabihar.Adapter.ContentRecyclerHorizontal;
import tv.merabihar.app.merabihar.Adapter.ImagePorifleContentAdapter;
import tv.merabihar.app.merabihar.Adapter.ProfileListAdapter;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Roboto_Regular;
import tv.merabihar.app.merabihar.CustomViews.SnackbarViewer;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Model.FollowersWithProfileData;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.FollowOptions.FollowOptionsActivity;
import tv.merabihar.app.merabihar.UI.Activity.FollowersListScreen;
import tv.merabihar.app.merabihar.UI.Activity.FollowingProfileListScreen;
import tv.merabihar.app.merabihar.UI.Activity.LoginScreen;
import tv.merabihar.app.merabihar.UI.Activity.SettingScreen;
import tv.merabihar.app.merabihar.UI.Activity.SignUpScreen;
import tv.merabihar.app.merabihar.Util.Constants;
import tv.merabihar.app.merabihar.Util.Permission;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ContentAPI;
import tv.merabihar.app.merabihar.WebAPI.ProfileAPI;
import tv.merabihar.app.merabihar.WebAPI.ProfileFollowAPI;
import tv.merabihar.app.merabihar.WebAPI.UploadApi;

public class TabAccountActivity extends AppCompatActivity {

    ImageView mSettings,mFollow;
    CircleImageView mProfilePhoto;
    MyTextView_Roboto_Regular mProfileName,mProfileAbout,mPosts,
            mFollowers,mFollowings,mNoPosts;
    RecyclerView mFollowingPeoples;
    RecyclerView mPostsList;
    //ListView mPostsList;
    ProgressBar progressBar;
    AppCompatButton mLogin;
    TextView mSignUp;

    NestedScrollView mProfileLoginLay;
    LinearLayout mNonProfileLay;

    LinearLayout applinear,linearlinear,mFollowingLay,mFollowersLay;
    ImageView app,linear;
    ImagePorifleContentAdapter adapters;
    ContentAdapterVertical adapter;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES ;
    private int currentPage = PAGE_START;

    private String TAG="BlogList";

    UserProfile userProfile;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String status,selectedImage;

    ArrayList<Contents> profileContents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            setContentView(R.layout.activity_tab_account);

            mLogin = (AppCompatButton) findViewById(R.id.loginAccount);
            mSignUp = (TextView) findViewById(R.id.link_signup);
            mSettings = (ImageView)findViewById(R.id.settings);
            mFollow = (ImageView)findViewById(R.id.follow_peopls);
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

            applinear = findViewById(R.id.applinear);
            linearlinear = findViewById(R.id.linearlinear);

            mProfileLoginLay = findViewById(R.id.profile_login_layout);
            mNonProfileLay = findViewById(R.id.not_login_layout);

            mFollowingLay = findViewById(R.id.following_layout);
            mFollowersLay = findViewById(R.id.followers_layout);

            app = findViewById(R.id.apptool);
            linear = findViewById(R.id.lineartool);

            mFollowingPeoples.setLayoutManager(new LinearLayoutManager(TabAccountActivity.this, LinearLayoutManager.VERTICAL, false));
            mFollowingPeoples.setNestedScrollingEnabled(false);

            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(TabAccountActivity.this, 3);
            mPostsList.setLayoutManager(layoutManager);
            mPostsList.setItemAnimator(new DefaultItemAnimator());

           final int profileId = PreferenceHandler.getInstance(TabAccountActivity.this).getUserId();

           if(profileId!=0){

               mProfileLoginLay.setVisibility(View.VISIBLE);
               mNonProfileLay.setVisibility(View.GONE);

           }else{

               mProfileLoginLay.setVisibility(View.GONE);
               mNonProfileLay.setVisibility(View.VISIBLE);
           }


            mLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent login = new Intent(TabAccountActivity.this, LoginScreen.class);
                    startActivity(login);
                }
            });

            mSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent login = new Intent(TabAccountActivity.this,SignUpScreen.class);
                    startActivity(login);
                }
            });

           mFollowersLay.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   if(profileId!=0){

                       String followingCount = mFollowers.getText().toString();

                       try{
                           int follw = Integer.parseInt(followingCount);

                           if(mFollowers.getText().toString().equalsIgnoreCase("0")||follw==0){

                               Toast.makeText(TabAccountActivity.this, "No followings", Toast.LENGTH_SHORT).show();

                           }else{

                               if(Util.isNetworkAvailable(TabAccountActivity.this)){
                                   Intent follow = new Intent(TabAccountActivity.this, FollowersListScreen.class);
                                   startActivity(follow);
                               }else{
                                   Toast.makeText(TabAccountActivity.this, "You are offline", Toast.LENGTH_SHORT).show();
                               }


                           }
                       }catch (Exception e){
                           e.printStackTrace();
                       }



                   }else{

                       Toast.makeText(TabAccountActivity.this, "Please login first", Toast.LENGTH_SHORT).show();
                   }

               }
           });

            mFollowingLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(profileId!=0){

                        String followingCount = mFollowings.getText().toString();

                        try{
                            int follw = Integer.parseInt(followingCount);

                            if(mFollowings.getText().toString().equalsIgnoreCase("0")||follw==0){

                                Toast.makeText(TabAccountActivity.this, "No followings", Toast.LENGTH_SHORT).show();

                            }else{

                                if(Util.isNetworkAvailable(TabAccountActivity.this)){
                                    Intent follow = new Intent(TabAccountActivity.this, FollowingProfileListScreen.class);
                                    startActivity(follow);
                                }else{
                                    Toast.makeText(TabAccountActivity.this, "You are offline", Toast.LENGTH_SHORT).show();
                                }


                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }



                    }else{

                        Toast.makeText(TabAccountActivity.this, "Please login first", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            applinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    app.setImageResource(R.drawable.ic_apps_blue);
                    linear.setImageResource(R.drawable.ic_linear_grey);
                    mPostsList.setAdapter(null);
                    mFollowingPeoples.setAdapter(null);
                    mPostsList.setVisibility(View.VISIBLE);
                    mFollowingPeoples.setVisibility(View.GONE);
                    if(profileId!=0){

                        if (Util.isNetworkAvailable(TabAccountActivity.this)) {

                            if(profileContents!=null&&profileContents.size()!=0){
                                adapters = new ImagePorifleContentAdapter(TabAccountActivity.this,profileContents);
                                mPostsList.setAdapter(adapters);
                            }else{
                                getProfileContent(profileId);
                            }


                        }else{

                            SnackbarViewer.showSnackbar(findViewById(R.id.main_activity_tab_account),"No Internet connection");
                            progressBar.setVisibility(View.GONE);
                        }

                    }else{

                        SnackbarViewer.showSnackbar(findViewById(R.id.main_activity_tab_account),"Something went wrong.Please login again");
//                        Toast.makeText(TabAccountActivity.this, "Something went wrong.Please login again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            linearlinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    app.setImageResource(R.drawable.ic_apps_grey);
                    linear.setImageResource(R.drawable.ic_linear_blue);
                    mPostsList.setAdapter(null);
                    mFollowingPeoples.setAdapter(null);
                    mPostsList.setVisibility(View.GONE);
                    mFollowingPeoples.setVisibility(View.VISIBLE);

                    if(profileId!=0){
                        adapter = new ContentAdapterVertical(TabAccountActivity.this);
                        mFollowingPeoples.setAdapter(adapter);

                        if (Util.isNetworkAvailable(TabAccountActivity.this)) {

                            if(profileContents!=null&&profileContents.size()!=0){

                                progressBar.setVisibility(View.GONE);
                                adapter.addAll(profileContents);

                                if (profileContents != null && profileContents.size() !=0)
                                    adapter.addLoadingFooter();
                                else
                                    isLastPage = true;

                            }else{
                                loadFirstSetOfBlogs(profileId);
                            }

                        }else{

                            SnackbarViewer.showSnackbar(findViewById(R.id.main_activity_tab_account),"No Internet connection");
                        }

                    }else{

//                        Toast.makeText(TabAccountActivity.this, "Something went wrong.Please login again", Toast.LENGTH_SHORT).show();
                        SnackbarViewer.showSnackbar(findViewById(R.id.main_activity_tab_account),"Something went wrong.Please login again");

                    }


                }
            });





            //int profileId = 49;
            if(profileId!=0){

                if (Util.isNetworkAvailable(TabAccountActivity.this)) {

                    getProfile(profileId);

                    //getFollowingByProfileId(profileId);

                }else{

                    SnackbarViewer.showSnackbar(findViewById(R.id.main_activity_tab_account),"No Internet connection");
                    progressBar.setVisibility(View.GONE);
                }

            }else{

//                SnackbarViewer.showSnackbar(findViewById(R.id.main_activity_tab_account),"Something went wrong.Please login again");


            }

            mFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent follow = new Intent(TabAccountActivity.this, FollowOptionsActivity.class);
                    startActivity(follow);
                }
            });

            mSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent follow = new Intent(TabAccountActivity.this, SettingScreen.class);
                    startActivity(follow);
                }
            });


            mProfilePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(profileId!=0&&userProfile!=null){


                        selectImage();


                    }else{

                        SnackbarViewer.showSnackbar(findViewById(R.id.main_activity_tab_account),"Something went wrong.Please login again");

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
                Call<FollowersWithProfileData> getProf = subCategoryAPI.getProfileFollow(id);
                //Call<ArrayList<Blogs>> getBlog = blogApi.getBlogs();

                getProf.enqueue(new Callback<FollowersWithProfileData>() {

                    @Override
                    public void onResponse(Call<FollowersWithProfileData> call, Response<FollowersWithProfileData> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.code() == 200)
                        {
                            System.out.println("Inside api");

                            FollowersWithProfileData data = response.body();


                            UserProfile profile = data.getProfile();
                            userProfile = profile;
                            mProfileName.setText(""+profile.getFullName());
                            mFollowers.setText(""+data.getFollowing());
                            mFollowings.setText(""+data.getFollowers());
                            if(profile.getPrefix()!=null){

                                mProfileAbout.setText(""+profile.getPrefix());
                            }

                            String ref = "MBR"+profile.getProfileId();
                            String referCodeText = Base64.encodeToString(ref.getBytes(), Base64.DEFAULT);
                            PreferenceHandler.getInstance(TabAccountActivity.this).setReferalcode(ref);

                            if(profile.getProfilePhoto()!=null){

                                String base=profile.getProfilePhoto();



                                if(base != null && !base.isEmpty()){
                                    Picasso.with(TabAccountActivity.this).load(base).placeholder(R.drawable.profile_image).
                                            error(R.drawable.profile_image).into(mProfilePhoto);

                                }else{
                                    mProfilePhoto.setImageResource(R.drawable.profile_image);
                                }
                            }

                            if(profile.getContents()!=null&&profile.getContents().size()!=0){

                                mPosts.setText(""+data.getNoOfPost());
                                profileContents = profile.getContents();
                                adapters = new ImagePorifleContentAdapter(TabAccountActivity.this,profile.getContents());
                                mPostsList.setAdapter(adapters);
                            }else{
                                if(Util.isNetworkAvailable(TabAccountActivity.this)){
                                    getProfileContent(id);
                                }else{
                                    Toast.makeText(TabAccountActivity.this, "You are offline", Toast.LENGTH_SHORT).show();
                                }

                            }




                        }
                    }

                    @Override
                    public void onFailure(Call<FollowersWithProfileData> call, Throwable t) {

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
                                ProfileListAdapter adapter = new ProfileListAdapter(TabAccountActivity.this,responseProfile);
                                mFollowingPeoples.setAdapter(adapter);


                            }
                            else
                            {



                            }
                        }
                        else
                        {
                            SnackbarViewer.showSnackbar(findViewById(R.id.main_activity_tab_account),response.message());

//                            Toast.makeText(TabAccountActivity.this,response.message(),Toast.LENGTH_SHORT).show();
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
                            SnackbarViewer.showSnackbar(findViewById(R.id.main_activity_tab_account),response.message());
//                            Toast.makeText(TabAccountActivity.this,response.message(),Toast.LENGTH_SHORT).show();
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
                                ProfileListAdapter adapter = new ProfileListAdapter(TabAccountActivity.this,responseProfile);
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
                            SnackbarViewer.showSnackbar(findViewById(R.id.main_activity_tab_account),response.message());

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
                                profileContents = response.body();
                                adapters = new ImagePorifleContentAdapter(TabAccountActivity.this,response.body());
                                mPostsList.setAdapter(adapters);
                            }

                        }else{

                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Contents>> call, Throwable t) {

                        progressBar.setVisibility(View.GONE);

                        SnackbarViewer.showSnackbar(findViewById(R.id.main_activity_tab_account),t.getMessage());

                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    private void goBack()
    {
        Intent intent = null;

        intent = new Intent(TabAccountActivity.this,TabMainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        TabAccountActivity.this.finish();
    }

    public void loadFirstSetOfBlogs(final int id) {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ContentAPI bookingApi = Util.getClient().create(ContentAPI.class);

                Call<ArrayList<Contents>> getAllBookings = bookingApi.
                        getContentByProfileId(id);

                getAllBookings.enqueue(new Callback<ArrayList<Contents>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {


                        try{
                            if(response.code() == 200 && response.body()!= null)
                            {
                                if(response.body().size() != 0) {
                                    Log.d(TAG, "loadFirstPage: "+response.message());
                                    ArrayList<Contents> approvedBlogs = response.body();

                                    if(approvedBlogs!=null&&approvedBlogs.size()!=0){
                                        loadFirstPage(approvedBlogs);
                                    }else{
                                        isLoading = true;

                                        currentPage = currentPage+1;

                                    }

                                }
                                else
                                {
                                    adapter.removeLoadingFooter();
                                    isLastPage = true;
                                    isLoading = true;
                                    progressBar.setVisibility(View.GONE);
                                }

                            }
                            else
                            {

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<Contents>> call, Throwable t) {
                    }
                });

                //WebService.getAllBookings(PreferenceHandler.getInstance(getActivity()).getHotelID());
            }
        });

    }

    private void loadFirstPage(ArrayList<Contents> list) {

        //Collections.reverse(list);
        progressBar.setVisibility(View.GONE);
        adapter.addAll(list);

        if (list != null && list.size() !=0)
            adapter.addLoadingFooter();
        else
            isLastPage = true;

    }

    private void selectImage() {
        final CharSequence[] items = {"Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(TabAccountActivity.this);
        builder.setTitle("Add Image!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Permission.checkPermission(TabAccountActivity.this);
                if (items[item].equals("Choose from Library")) {

                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);

        }
    }

    private void onSelectFromGalleryResult(Intent data) {

        try{


            Uri selectedImageUri = data.getData( );
            String picturePath = getPath( TabAccountActivity.this, selectedImageUri );
            Log.d("Picture Path", picturePath);
            String[] all_path = {picturePath};
            selectedImage = all_path[0];
            System.out.println("allpath === "+data.getPackage());
            for (String s:all_path)
            {
                //System.out.println(s);
                File imgFile = new  File(s);
                if(imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //addView(null,Util.getResizedBitmap(myBitmap,400));
                    addImage(null,Util.getResizedBitmap(myBitmap,700));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    public void addImage(String uri,Bitmap bitmap)
    {
        try{


            if(uri != null)
            {

            }
            else if(bitmap != null)
            {
                mProfilePhoto.setImageBitmap(bitmap);

                if(selectedImage != null && !selectedImage.isEmpty())
                {
                    File file = new File(selectedImage);

                    if(file.length() <= 1*1024*1024)
                    {
                        FileOutputStream out = null;
                        String[] filearray = selectedImage.split("/");
                        final String filename = getFilename(filearray[filearray.length-1]);

                        out = new FileOutputStream(filename);
                        Bitmap myBitmap = BitmapFactory.decodeFile(selectedImage);

//          write the compressed bitmap at the destination specified by filename.
                        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                        uploadImage(filename,userProfile);



                    }
                    else
                    {
                        compressImage(selectedImage,userProfile);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void uploadImage(final String filePath,final UserProfile userProfile)
    {
        //String filePath = getRealPathFromURIPath(uri, ImageUploadActivity.this);

        final File file = new File(filePath);
        int size = 1*1024*1024;

        if(file != null)
        {
            if(file.length() > size)
            {
                System.out.println(file.length());
                compressImage(filePath,userProfile);
            }
            else
            {
                final ProgressDialog dialog = new ProgressDialog(TabAccountActivity.this);
                dialog.setCancelable(false);
                dialog.setTitle("Uploading Image..");
                dialog.show();
                Log.d("Image Upload", "Filename " + file.getName());

                RequestBody mFile = RequestBody.create(MediaType.parse("image"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
                UploadApi uploadImage = Util.getClient().create(UploadApi.class);

                Call<String> fileUpload = uploadImage.uploadProfileImages(fileToUpload, filename);
                fileUpload.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        userProfile.setProfilePhoto(Constants.IMAGE_URL+response.body().toString());


                        updateProfile(userProfile);



                        if(filePath.contains("MyFolder/Images"))
                        {
                            file.delete();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("UpdateCate", "Error " + t.getMessage());
                    }
                });
            }
        }
    }

    public String compressImage(String filePath,final  UserProfile userProfile) {

        //String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = actualHeight/2;//2033.0f;
        float maxWidth = actualWidth/2;//1011.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String[] filearray = filePath.split("/");
        final String filename = getFilename(filearray[filearray.length-1]);
        try {
            out = new FileOutputStream(filename);


//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            uploadImage(filename,userProfile);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public String getFilename(String filePath) {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        System.out.println("getFilePath = "+filePath);
        String uriSting;
        if(filePath.contains(".jpg"))
        {
            uriSting = (file.getAbsolutePath() + "/" + filePath);
        }
        else
        {
            uriSting = (file.getAbsolutePath() + "/" + filePath+".jpg" );
        }
        return uriSting;

    }

    private void updateProfile(final UserProfile userProfile) {


        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Updating Image..");
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ProfileAPI auditApi = Util.getClient().create(ProfileAPI.class);
                Call<UserProfile> response = auditApi.updateProfile(userProfile.getProfileId(),userProfile);
                response.enqueue(new Callback<UserProfile>() {
                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                            SnackbarViewer.showSnackbar(findViewById(R.id.main_activity_tab_account),"Profile Image Updated");
//                            Toast.makeText(TabAccountActivity.this,"Profile Image Updated",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            SnackbarViewer.showSnackbar(findViewById(R.id.main_activity_tab_account),"Error while updating profile picture");
//                            Toast.makeText(TabAccountActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
//                        Toast.makeText(TabAccountActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        SnackbarViewer.showSnackbar(findViewById(R.id.main_activity_tab_account),"Error while updating profile picture");

                    }
                });
            }
        });
    }
}
