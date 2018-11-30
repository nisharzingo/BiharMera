package tv.merabihar.app.merabihar.UI.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.CommentsListAdapter;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Lato_Regular;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_SF_Pro_Light;
import tv.merabihar.app.merabihar.CustomFonts.TextViewSFProDisplayRegular;
import tv.merabihar.app.merabihar.DataBase.DataBaseHelper;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Model.FollowsWithMapping;
import tv.merabihar.app.merabihar.Model.Likes;
import tv.merabihar.app.merabihar.Model.ProfileFollowMapping;
import tv.merabihar.app.merabihar.Model.SubscribedGoals;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Service.VideoWatchedService;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.PostContent.UpdateContentUrlScreen;
import tv.merabihar.app.merabihar.Util.Constants;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ContentAPI;
import tv.merabihar.app.merabihar.WebAPI.LikeAPI;
import tv.merabihar.app.merabihar.WebAPI.ProfileAPI;
import tv.merabihar.app.merabihar.WebAPI.ProfileFollowAPI;
import tv.merabihar.app.merabihar.WebAPI.SubscribedGoalsAPI;

public class ContentDetailScreen extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{


    ImageView mback;
    TextViewSFProDisplayRegular mSubCategory,mReadTime,mNocomments;
    MyTextView_SF_Pro_Light mContentTitle,mContentDesc;
    MyTextView_Lato_Regular mProfileName,mFollow;
    CircleImageView mProfilePhoto;
    LinearLayout mProfileContent;
    RecyclerView mCommentsList;
    MyTextView_Lato_Regular mCommentsCount,mLikesCount,mDislikesCount,mLikedId,mDislikedId,mWhatsappShareCount,postWatchedCount;;
    ImageView mLike,mDislike,mComment,mEditOption;
    LinearLayout mWhatsapp, mShare, mLikeLayout, mDislikeLayout, mCommentLayout ;

    int REQUEST_CODE = 101 ;

    private YouTubePlayerFragment playerFragmentTop;
    private YouTubePlayer mPlayer;
    private String YouTubeKey = Constants.YOUTUBE,url,urls;

    Contents contents;
    int mappingId=0;
    long videoTime = 0;
    SubscribedGoals sg;
    int profileId = 0,youtubeWatcheTime=0;
    int totalPostView = 0;
    UserProfile currentProfile;

    String fileNames,type;
    String shareContent = "Save time. Download Mera Bihar,The Only App for Bihar,To Read,Share your Stories and Earn Rs 1000\n\n Use my referal code for Sign-Up MBR"+PreferenceHandler.getInstance(ContentDetailScreen.this).getUserId()+"\n http://bit.ly/2JXcOnw";
    String shareContents = "Save time. Download Mera Bihar,The Only App for Bihar,To Read,Share your Stories and Earn Rs 1000\n\n http://bit.ly/2JXcOnw";

    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;
    int Seconds, Minutes, MilliSeconds ;

    DataBaseHelper db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_content_detail_screen);
            //GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
            profileId = PreferenceHandler.getInstance(ContentDetailScreen.this).getUserId();
            db = new DataBaseHelper(ContentDetailScreen.this);
            handler = new Handler() ;
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
            mWhatsappShareCount = (MyTextView_Lato_Regular) findViewById(R.id.whatsapp_share_count);

            mCommentsCount = (MyTextView_Lato_Regular) findViewById(R.id.comments_count);
            mLikesCount = (MyTextView_Lato_Regular) findViewById(R.id.likes_count);
            mDislikesCount = (MyTextView_Lato_Regular) findViewById(R.id.unlikes_count);
            mLikedId = (MyTextView_Lato_Regular) findViewById(R.id.like_id);
            mDislikedId = (MyTextView_Lato_Regular) findViewById(R.id.dislike_id);
            postWatchedCount = (MyTextView_Lato_Regular) findViewById(R.id.post_total_watched_count);
            mLike = (ImageView) findViewById(R.id.likes_image);
            mDislike = (ImageView) findViewById(R.id.unlikes_image);
            mComment = (ImageView) findViewById(R.id.comments_image);
            mEditOption = (ImageView) findViewById(R.id.edit_option_icon);
            mWhatsapp = (LinearLayout) findViewById(R.id.whatsapp_share);
            mShare = (LinearLayout) findViewById(R.id.share_image);
            mLikeLayout = (LinearLayout) findViewById(R.id.like_ll_cds);
            mDislikeLayout = (LinearLayout) findViewById(R.id.dislike_ll_cds);
            mCommentLayout = (LinearLayout) findViewById(R.id.comment_ll_cds);
            mNocomments = (TextViewSFProDisplayRegular)findViewById(R.id.no_comments);
            //mMoreShare = (ImageView) findViewById(R.id.more_icons);
            mCommentsList = (RecyclerView) findViewById(R.id.comments_list);

            final Bundle bundle = getIntent().getExtras();
            if(bundle!=null){
                contents = (Contents) bundle.getSerializable("Contents");
                type = bundle.getString("Edit");
            }

            if(type!=null&&type.equalsIgnoreCase("Edit")){

                mEditOption.setVisibility(View.VISIBLE);
            }else{

                mEditOption.setVisibility(View.GONE);
            }

            mEditOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(contents!=null){
                        showAlertBox();
                    }else{
                        Toast.makeText(ContentDetailScreen.this, "Not valid content", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            if(contents!=null){


                setViewPager();
            }else{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }




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

                    if (Util.isNetworkAvailable(ContentDetailScreen.this)) {

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
                    }else{
                        Toast.makeText(ContentDetailScreen.this, "No Internet connection", Toast.LENGTH_SHORT).show();


                    }

                }
            });


            mLikeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (Util.isNetworkAvailable(ContentDetailScreen.this)) {


                        if(profileId!=0){

                            mLikeLayout.setEnabled(false);
                            Likes likes = new Likes();
                            likes.setContentId(contents.getContentId());
                            likes.setProfileId(profileId);
                            likes.setLiked(true);

                            if (mDislike.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.unliked_icons).getConstantState())
                            {
                                if(mDislikedId.getText().toString()!=null&&!mDislikedId.getText().toString().isEmpty()){


                                    updateLike(likes,mLike,mLikesCount,Integer.parseInt(mDislikedId.getText().toString()),mDislike,mDislikedId,mDislikesCount,mLikedId, mLikeLayout, mDislikeLayout);
                                }
                            }
                            else
                            {

                                postLike(likes,mLike,mLikesCount,0,mDislike,mDislikedId,mDislikesCount,mLikedId, mLikeLayout, mDislikeLayout);
                            }

                                       /* }else{

                                            postLike(likes,holder.mLike,holder.mLiked,holder.mLikeCount,holder.mLikedId);
                                        }*/

                        }else {
                            new AlertDialog.Builder(ContentDetailScreen.this)
                                    .setMessage("Please login/Signup to Like the Story")
                                    .setCancelable(false)
                                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            Intent login = new Intent(ContentDetailScreen.this, LoginScreen.class);
                                            startActivity(login);

                                        }
                                    })
                                    .setNegativeButton("SignUp", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            Intent signUp = new Intent(ContentDetailScreen.this, SignUpScreen.class);
                                            startActivity(signUp);

                                        }
                                    })
                                    .show();
                        }

                    }else{
                        Toast.makeText(ContentDetailScreen.this, "No Internet connection", Toast.LENGTH_SHORT).show();

                    }

                }
            });

            mDislikeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (Util.isNetworkAvailable(ContentDetailScreen.this)) {

                        if(profileId!=0){

                            mDislikeLayout.setEnabled(false);
                            Likes likes = new Likes();
                            likes.setContentId(contents.getContentId());
                            likes.setProfileId(profileId);
                            likes.setLiked(false);

                            if (mLike.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.liked_icon).getConstantState())
                            {
                                if(mLikedId.getText().toString()!=null&&!mLikedId.getText().toString().isEmpty()){


                                    updatedisLike(likes,mDislike,mDislikesCount,Integer.parseInt(mLikedId.getText().toString()),mLike,mLikedId,mLikesCount,mDislikedId, mDislikeLayout, mLikeLayout);
                                }
                            }
                            else
                            {

                                postDislike(likes,mLike,mLikesCount,0,mDislike,mDislikedId,mDislikesCount,mLikedId, mDislikeLayout, mLikeLayout);
                            }

                                       /* }else{

                                            postLike(likes,holder.mLike,holder.mLiked,holder.mLikeCount,holder.mLikedId);
                                        }*/

                        }else {
                            new AlertDialog.Builder(ContentDetailScreen.this)
                                    .setMessage("Please login/Signup to Like the Story")
                                    .setCancelable(false)
                                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            Intent login = new Intent(ContentDetailScreen.this, LoginScreen.class);
                                            startActivity(login);

                                        }
                                    })
                                    .setNegativeButton("SignUp", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            Intent signUp = new Intent(ContentDetailScreen.this, SignUpScreen.class);
                                            startActivity(signUp);

                                        }
                                    })
                                    .show();
                        }



                    }else{

                        Toast.makeText(ContentDetailScreen.this, "No Internet connection", Toast.LENGTH_SHORT).show();


                    }



                }
            });


            mWhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(Util.isNetworkAvailable(ContentDetailScreen.this)){

                        fileNames = contents.getContentId()+""+contents.getProfileId();

                        AsyncTask mMyTask;
                        if(contents.getContentType().equalsIgnoreCase("Video")) {

                            urls = contents.getContentURL();


                            if (urls != null && !urls.isEmpty()) {

                                mMyTask = new DownloadTask()
                                        .execute(stringToURL(
                                                "https://img.youtube.com/vi/"+urls+"/0.jpg"
                                        ));

                            }

                        }else{

                            mMyTask = new DownloadTask()
                                    .execute(stringToURL(
                                            ""+contents.getContentImage().get(0).getImages()
                                    ));
                        }

                        //shareApplication();

                    }else{
                        Toast.makeText(ContentDetailScreen.this, "No Internet connection", Toast.LENGTH_SHORT).show();
                    }




                }
            });


            mShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (Util.isNetworkAvailable(ContentDetailScreen.this)) {

                        fileNames = contents.getContentId() + "" + contents.getProfileId();

                        AsyncTask mMyTask;
                        if (contents.getContentType().equalsIgnoreCase("Video")) {

                            urls = contents.getContentURL();

                            if (urls != null && !urls.isEmpty()) {

                                mMyTask = new DownloadTasks()
                                        .execute(stringToURL(
                                                "https://img.youtube.com/vi/" + urls + "/0.jpg"
                                        ));


                            } else {

                                mMyTask = new DownloadTasks()
                                        .execute(stringToURL(
                                                "" + contents.getContentImage().get(0).getImages()
                                        ));
                            }

                            //shareApplication();

                        } else {

                            Toast.makeText(ContentDetailScreen.this, "No Internet connection", Toast.LENGTH_SHORT).show();
                        }


                    }
                }
            });

            mCommentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent comments = new Intent(ContentDetailScreen.this, CommentsScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Contents",contents);

                    comments.putExtras(bundle);
                    startActivity(comments);
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
                mProfileName.setText(""+contents.getCreatedBy());

//                if (Util.isNetworkAvailable(ContentDetailScreen.this)) {

                if(contents.getCreatedBy()!=null){
                    mProfileName.setText(""+contents.getCreatedBy());
                }else{

                }


                    if(profileId!=0){

                        if(profileId==contents.getProfileId()){

                            mFollow.setVisibility(View.GONE);
                        }else{
                            getFollowingsByProfileId(profileId,contents.getProfileId());

                        }

                    }
                    getGoalsByProfileId(profileId);

                    if (contents.getProfile() == null) {
                        getProfile(contents.getProfileId());

                    } else {


                    }


//                }else{

//                    "No internet"

//                }

                if(contents.getCreatedDate().contains(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))){



                    if(contents.getCreditName()==null){

                        contents.setCreditName(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));

                        if(contents.getViews()==null){



                            postWatchedCount.setText("1");
                            contents.setViews("1");
                            if(db.getContentById(contents.getContentId())!=null){

                                db.updateContents(contents);
                                System.out.println("Data Base Update Service");

                            }
                            updateContent(contents);

                        }else{

                            int total = Integer.parseInt(contents.getViews());
//                        postWatchedCount.setText(++total + "");
                            contents.setViews((total+1)+"");
                       /* if(total+1>=1000){
                            Double newformat = ( (total+1) * 1.0)/1000  ;
                            postWatchedCount.setText(String.format("%.1f", newformat) + "k");
                        }else {
                            postWatchedCount.setText((total+1) +"");
                        }*/
                            postWatchedCount.setText((total+1) +"");
                            if(db.getContentById(contents.getContentId())!=null){
                                db.updateContents(contents);
                                System.out.println("Data Base Update Service");

                            }
                            updateContent(contents);

                        }

                    }else{

                        String creditDate = contents.getCreditName();
                        if(creditDate!=null){
                            String todayDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
                            try{

                                if(todayDate.equals(creditDate)){

                                    if(contents.getViews()==null){

                                        postWatchedCount.setText("1");



                                        contents.setViews("1");
                                        if(db.getContentById(contents.getContentId())!=null){

                                            db.updateContents(contents);
                                            System.out.println("Data Base Update Service");

                                        }
                                        updateContent(contents);

                                    }else{
                                        int total = Integer.parseInt(contents.getViews());
                                   /* if(total+1>=1000){
                                        Double newformat = ( (total+1) * 1.0)/1000  ;
                                        postWatchedCount.setText(String.format("%.1f", newformat) + "k");
                                    }else {
                                        postWatchedCount.setText(++total +"");
                                    }*/
//                                    postWatchedCount.setText(++total + "");
                                        postWatchedCount.setText(++total +"");
                                        contents.setViews(total+"");
                                        if(db.getContentById(contents.getContentId())!=null){

                                            db.updateContents(contents);
                                            System.out.println("Data Base Update Service");

                                        }
                                        updateContent(contents);

                                    }
                                }else{
                                    contents.setCreditName(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));

                                    if(contents.getViews()==null){



                                        postWatchedCount.setText("1");
                                        contents.setViews("1");
                                        if(db.getContentById(contents.getContentId())!=null){

                                            db.updateContents(contents);
                                            System.out.println("Data Base Update Service");

                                        }
                                        updateContent(contents);

                                    }else{
                                        int total = Integer.parseInt(contents.getViews());
                                   /* if(total+1>=1000){
                                        Double newformat = ( (total+1) * 1.0)/1000  ;
                                        postWatchedCount.setText(String.format("%.1f", newformat) + "k");
                                    }else {
                                        postWatchedCount.setText(++total +"");
                                    }*/
                                        postWatchedCount.setText(++total +"");
//                                    postWatchedCount.setText(++total + "");
                                        contents.setViews(total+"");
                                        if(db.getContentById(contents.getContentId())!=null){

                                            db.updateContents(contents);
                                            System.out.println("Data Base Update Service");

                                        }
                                        updateContent(contents);

                                    }


                                }


                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                    }

                }else{

                    if(contents.getCreditName()==null){

                        contents.setCreditName(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));

                        if(contents.getViews()==null){


                            int max = 100;
                            int min = 1;
                            Random randomNum = new Random();
                            int showMe = min + randomNum.nextInt(max);
                            postWatchedCount.setText(""+showMe);
                            contents.setViews(showMe+"");
                            if(db.getContentById(contents.getContentId())!=null){

                                db.updateContents(contents);
                                System.out.println("Data Base Update Service");

                            }
                            updateContent(contents);

                        }else{

                            int total = Integer.parseInt(contents.getViews());
//                        postWatchedCount.setText(++total + "");
                            contents.setViews((total+1)+"");
                       /* if(total+1>=1000){
                            Double newformat = ( (total+1) * 1.0)/1000  ;
                            postWatchedCount.setText(String.format("%.1f", newformat) + "k");
                        }else {
                            postWatchedCount.setText((total+1) +"");
                        }*/
                            postWatchedCount.setText((total+1) +"");
                            if(db.getContentById(contents.getContentId())!=null){
                                db.updateContents(contents);
                                System.out.println("Data Base Update Service");

                            }
                            updateContent(contents);

                        }

                    }else{

                        String creditDate = contents.getCreditName();
                        if(creditDate!=null){
                            String todayDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
                            try{

                                if(todayDate.equals(creditDate)){

                                    if(contents.getViews()==null){

                                        postWatchedCount.setText("1");

                                        int max = 100;
                                        int min = 1;
                                        Random randomNum = new Random();
                                        int showMe = min + randomNum.nextInt(max);

                                        contents.setViews(showMe+"");
                                        if(db.getContentById(contents.getContentId())!=null){

                                            db.updateContents(contents);
                                            System.out.println("Data Base Update Service");

                                        }
                                        updateContent(contents);

                                    }else{
                                        int total = Integer.parseInt(contents.getViews());
                                   /* if(total+1>=1000){
                                        Double newformat = ( (total+1) * 1.0)/1000  ;
                                        postWatchedCount.setText(String.format("%.1f", newformat) + "k");
                                    }else {
                                        postWatchedCount.setText(++total +"");
                                    }*/
//                                    postWatchedCount.setText(++total + "");
                                        postWatchedCount.setText(++total +"");
                                        contents.setViews(total+"");
                                        if(db.getContentById(contents.getContentId())!=null){

                                            db.updateContents(contents);
                                            System.out.println("Data Base Update Service");

                                        }
                                        updateContent(contents);

                                    }
                                }else{
                                    contents.setCreditName(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));

                                    if(contents.getViews()==null){


                                        int max = 100;
                                        int min = 1;
                                        Random randomNum = new Random();
                                        int showMe = min + randomNum.nextInt(max);
                                        postWatchedCount.setText(showMe+"");
                                        contents.setViews(showMe+"");
                                        if(db.getContentById(contents.getContentId())!=null){

                                            db.updateContents(contents);
                                            System.out.println("Data Base Update Service");

                                        }
                                        updateContent(contents);

                                    }else{
                                        int total = Integer.parseInt(contents.getViews());
                                   /* if(total+1>=1000){
                                        Double newformat = ( (total+1) * 1.0)/1000  ;
                                        postWatchedCount.setText(String.format("%.1f", newformat) + "k");
                                    }else {
                                        postWatchedCount.setText(++total +"");
                                    }*/
                                        postWatchedCount.setText(++total +"");
//                                    postWatchedCount.setText(++total + "");
                                        contents.setViews(total+"");
                                        if(db.getContentById(contents.getContentId())!=null){

                                            db.updateContents(contents);
                                            System.out.println("Data Base Update Service");

                                        }
                                        updateContent(contents);

                                    }


                                }


                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                    }

                }





                if (contents.getSubCategories() != null) {
                   mSubCategory.setText(""+contents.getSubCategories().getSubCategoriesName());
                } else {


                }

                if(contents.getCommentsList()!=null&&contents.getCommentsList().size()!=0){

                    mCommentsCount.setText(""+contents.getCommentsList().size());
                    CommentsListAdapter adapter = new CommentsListAdapter(ContentDetailScreen.this,contents.getCommentsList());
                    mCommentsList.setAdapter(adapter);

                }else{
                    mNocomments.setVisibility(View.VISIBLE);
                }

                if(contents.getLikes()!=null&&contents.getLikes().size()!=0){

                    ArrayList<Likes> liked = new ArrayList<>();
                    ArrayList<Likes> disliked = new ArrayList<>();

                    boolean profileLike = false;
                    boolean profileDislike = false;

                    for (Likes likes:contents.getLikes()) {

                        if(likes.isLiked()){
                            liked.add(likes);

                            if(profileId!=0){
                                if(likes.getProfileId()==profileId){
                                    profileLike = true;
                                    mLikedId.setText(""+likes.getLikeId());
                                }
                            }


                        }else{
                            disliked.add(likes);

                            if(profileId!=0){
                                if(likes.getProfileId()==profileId){
                                    profileDislike = true;
                                    mDislikedId.setText(""+likes.getLikeId());
                                }
                            }

                        }

                    }


                    if(liked!=null&&liked.size()!=0){

                        mLikesCount.setText(""+liked.size());

                    }

                    if(disliked!=null&&disliked.size()!=0){

                        mDislikesCount.setText(""+disliked.size());
                    }

                    if(profileLike){

                        mLike.setImageResource(R.drawable.liked_icon);
                    }

                    if(profileDislike){

                        mDislike.setImageResource(R.drawable.unliked_icons);
                    }
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

/*                String uName = PreferenceHandler.getInstance(ContentDetailScreen.this).getUserFullName();
                if(uName!=null){
                    mProfileName.setText(uName);
                }else {
                    mProfileName.setText("Guest");

                }*/




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


                if(blogName!=null&&!blogName.isEmpty()){
                    mContentTitle.setText(blogName + "");
                    mContentTitle.setVisibility(View.VISIBLE);
                }else {
                    mContentTitle.setVisibility(View.GONE);
                }

                if(blogShort!=null&&!blogShort.isEmpty()){
                    mContentDesc.setText(blogShort + "");
                    mContentDesc.setVisibility(View.VISIBLE);
                }else {
                    mContentDesc.setVisibility(View.GONE);
                }



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


//                            mProfileName.setText(""+profile.getFullName());

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

    public void getProfiles(final int id){

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

                            currentProfile = response.body();






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
        mPlayer.setPlaybackEventListener(playbackEventListener);

        if (!wasRestored) {
            player.loadVideo(url);
            videoTime = player.getCurrentTimeMillis();
            System.out.println(" Time of video "+videoTime);
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


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ProfileFollowAPI mapApi = Util.getClient().create(ProfileFollowAPI.class);
                Call<ProfileFollowMapping> response = mapApi.postInterest(intrst);
                response.enqueue(new Callback<ProfileFollowMapping>() {
                    @Override
                    public void onResponse(Call<ProfileFollowMapping> call, Response<ProfileFollowMapping> response) {

//                        System.out.println(response.code());


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

                        //Toast.makeText(ContentDetailScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    private void deleteFollow(final int mapId) {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ProfileFollowAPI mapApi = Util.getClient().create(ProfileFollowAPI.class);
                Call<ProfileFollowMapping> response = mapApi.deleteIntrs(mapId);
                response.enqueue(new Callback<ProfileFollowMapping>() {
                    @Override
                    public void onResponse(Call<ProfileFollowMapping> call, Response<ProfileFollowMapping> response) {

                        System.out.println(response.code());


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

                     //   Toast.makeText(ContentDetailScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }


    /*@Override
    public void onPlaying() {

        videoTime = mPlayer.getDurationMillis();
        System.out.println("System time  vide "+videoTime);
    }

    @Override
    public void onPaused() {
        videoTime = mPlayer.getCurrentTimeMillis();
        System.out.println("System time  vide "+mPlayer.getCurrentTimeMillis());
    }

    @Override
    public void onStopped() {

    }

    @Override
    public void onBuffering(boolean b) {

    }

    @Override
    public void onSeekTo(int i) {

    }*/

    @Override
    public void finish() {

        try{
           /* Intent data = new Intent();
            int t = (mPlayer.getCurrentTimeMillis()/1000);
            data.putExtra("tiempo",t );
            System.out.println("Value youtube "+t);
            setResult(0, data);*/
            //youtubeWatcheTime = t;

            System.out.println("Youtube Watch time "+youtubeWatcheTime);
          //  Toast.makeText(this, "Youtube Watch time "+youtubeWatcheTime, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ContentDetailScreen.this, VideoWatchedService.class);
            intent.putExtra("ProfileId",PreferenceHandler.getInstance(ContentDetailScreen.this).getUserId());
            intent.putExtra("Time",youtubeWatcheTime);
            startService(intent);

            if(sg!=null){

                int value = Integer.parseInt(sg.getRewardsEarned());
                int penaltyAmount = Integer.parseInt(sg.getExtraDescription());

                sg.setRewardsEarned(""+(youtubeWatcheTime+value));
                if(sg.getStatus().equals("Activated")){

                    String endDate = sg.getEndDate();

                    if(endDate!=null&&!endDate.isEmpty()){

                        if(endDate.contains("T")){

                            String dats[] = endDate.split("T");
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dats[0]);

                                if(new Date().getTime()-date.getTime()>0){

                                    int updateValue =((54000+penaltyAmount)-(youtubeWatcheTime+value));

                                    if(updateValue>0){

                                        updateValue = updateValue*2;
                                        sg.setExtraDescription(""+updateValue);
                                    }else{
                                        updateValue = 54000;
                                        sg.setExtraDescription("+"+value);
                                    }


                                    sg.setStatus("Penalty");
                                    sg.setStartDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));

                                    Calendar c = Calendar.getInstance();
                                    c.setTime(new Date()); // Now use today date.
                                    c.add(Calendar.DATE, 7); // Adding 5 days
                                    sg.setEndDate(new SimpleDateFormat("MM/dd/yyyy").format(c.getTime()));
                                    sg.setActiveDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));

                                   //double amount = currentProfile.getReferralAmount();
                                    double valuea = (youtubeWatcheTime+value)*.20;
                                  //  currentProfile.setReferralAmount(valuea);
                                    sg.setActiveDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
                                    profileSubScribed(sg);
                                }else{
                                    if((youtubeWatcheTime+value)>=(54000+penaltyAmount)){
                                        sg.setStatus("Completed");
                                        sg.setActiveDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
                                        profileSubScribed(sg);
                                    }else{
                                        sg.setActiveDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
                                        profileSubScribed(sg);
                                    }
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }else if(sg.getStatus().equals("Penalty")){

                    String endDate = sg.getEndDate();

                    if(endDate!=null&&!endDate.isEmpty()){

                        if(endDate.contains("T")){

                            String dats[] = endDate.split("T");
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dats[0]);

                                if(new Date().getTime()-date.getTime()>0){

                                    int updateValue =((54000+penaltyAmount)-(youtubeWatcheTime+value));

                                    if(updateValue>0){

                                        updateValue = updateValue*2;
                                        sg.setExtraDescription(""+updateValue);
                                    }else{
                                        updateValue = 54000;
                                        sg.setExtraDescription("+"+value);
                                    }



                                    sg.setStatus("Penalty");
                                    sg.setStartDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));

                                    Calendar c = Calendar.getInstance();
                                    c.setTime(new Date()); // Now use today date.
                                    c.add(Calendar.DATE, 7); // Adding 5 days
                                    sg.setEndDate(new SimpleDateFormat("MM/dd/yyyy").format(c.getTime()));
                                    sg.setActiveDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));

//                                    double amount = currentProfile.getReferralAmount();
                                    double valuea = (youtubeWatcheTime+value)*.20;
//                                    currentProfile.setReferralAmount(valuea);
                                    sg.setActiveDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
                                    profileSubScribed(sg);
                                }else{
                                    if((youtubeWatcheTime+value)>=(54000+penaltyAmount)){
                                        sg.setStatus("Completed");
                                        sg.setActiveDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
                                        profileSubScribed(sg);
                                    }else{
                                        sg.setActiveDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
                                        profileSubScribed(sg);
                                    }
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }else if(sg.getStatus().equals("Completed")){

                    String endDate = sg.getEndDate();

                    if(endDate!=null&&!endDate.isEmpty()){

                        if(endDate.contains("T")){

                            String dats[] = endDate.split("T");
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dats[0]);

                                if(new Date().getTime()-date.getTime()>0){

                                    int updateValue =((54000+penaltyAmount)-(youtubeWatcheTime+value));

                                    if(updateValue>0){

                                        updateValue = updateValue*2;
                                        sg.setExtraDescription(""+updateValue);
                                    }else{
                                        updateValue = 54000;
                                        sg.setExtraDescription("+"+value);
                                    }

                                    sg.setStatus("Penalty");
                                    sg.setStartDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));

                                    Calendar c = Calendar.getInstance();
                                    c.setTime(new Date()); // Now use today date.
                                    c.add(Calendar.DATE, 7); // Adding 5 days
                                    sg.setEndDate(new SimpleDateFormat("MM/dd/yyyy").format(c.getTime()));
                                    sg.setActiveDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));

                                   // double amount = currentProfile.getReferralAmount();
                                    double valuea = (youtubeWatcheTime+value)*.20;
                                   //currentProfile.setReferralAmount(valuea);
                                    sg.setActiveDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
                                    profileSubScribed(sg);
                                }else{
                                    if((youtubeWatcheTime+value)>=(54000+penaltyAmount)){
                                        sg.setStatus("Completed");
                                        sg.setActiveDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
                                        profileSubScribed(sg);
                                    }else{
                                        sg.setStatus("Activated");
                                        sg.setActiveDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
                                        profileSubScribed(sg);
                                    }
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }

            }
            super.finish();

        }catch (Exception e){
            e.printStackTrace();
            super.finish();
        }


    }
    public void getGoalsByProfileId(final int id)
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final SubscribedGoalsAPI categoryAPI = Util.getClient().create(SubscribedGoalsAPI.class);
                Call<ArrayList<SubscribedGoals>> getCat = categoryAPI.getSubscribedGoalsByProfileIdAndGoal(id,3);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<SubscribedGoals>>() {

                    @Override
                    public void onResponse(Call<ArrayList<SubscribedGoals>> call, Response<ArrayList<SubscribedGoals>> response) {



                        if(response.code() == 200 && response.body()!= null)
                        {

                            if(response.body().size()!=0){

                                 sg = response.body().get(0);

                                 if(sg!=null){


                                 }


                            }


                        }else{



                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<SubscribedGoals>> call, Throwable t) {




                       // Toast.makeText(ContentDetailScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }



    private void updateContent(final Contents content) {

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ContentAPI contentAPI = Util.getClient().create(ContentAPI.class);
                Call<Contents> response = contentAPI.updateContent(content.getContentId(),content);
                response.enqueue(new Callback<Contents>() {
                    @Override
                    public void onResponse(Call<Contents> call, Response<Contents> response) {

                        System.out.println(response.code());



                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {



                        }
                        else
                        {

                        }
                    }

                    @Override
                    public void onFailure(Call<Contents> call, Throwable t) {


                        // Toast.makeText(ContentDetailScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }


    private void profileSubScribed(final SubscribedGoals sg) {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                SubscribedGoalsAPI mapApi = Util.getClient().create(SubscribedGoalsAPI.class);
                Call<SubscribedGoals> response = mapApi.updateSubscribedGoals(sg.getSubscribedGoalId(),sg);
                response.enqueue(new Callback<SubscribedGoals>() {
                    @Override
                    public void onResponse(Call<SubscribedGoals> call, Response<SubscribedGoals> response) {

                        System.out.println(response.code());



                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {



                        }
                        else
                        {

                        }
                    }

                    @Override
                    public void onFailure(Call<SubscribedGoals> call, Throwable t) {


                       // Toast.makeText(ContentDetailScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    private void postLike(final Likes likes, final ImageView like, final MyTextView_Lato_Regular likeCount, final int dislikedId, final ImageView dislike, final MyTextView_Lato_Regular dislikeId, final MyTextView_Lato_Regular dislikeCount, final MyTextView_Lato_Regular likedId, final LinearLayout mLikeLayout, final LinearLayout mDislikeLayout) {

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LikeAPI mapApi = Util.getClient().create(LikeAPI.class);
                Call<Likes> response = mapApi.postLikes(likes);
                response.enqueue(new Callback<Likes>() {
                    @Override
                    public void onResponse(Call<Likes> call, Response<Likes> response) {

                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                            like.setImageResource(R.drawable.liked_icon);
                            dislike.setImageResource(R.drawable.unlike_icons);
                            likedId.setText(""+response.body().getLikeId());
                            dislikeId.setText("");
                            String likeText = likeCount.getText().toString();
                            if(likeText!=null&&!likeText.isEmpty()){

                                int count = Integer.parseInt(likeText);
                                likeCount.setText(""+(count+1));
                            }

                            mDislikeLayout.setEnabled(true);

                        }
                        else
                        {

                            mLikeLayout.setEnabled(true);

                        }
                    }

                    @Override
                    public void onFailure(Call<Likes> call, Throwable t) {

                        //Toast.makeText(ContentDetailScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        mLikeLayout.setEnabled(true);


                    }
                });
            }
        });
    }

    private void updateLike(final Likes likes, final ImageView like, final MyTextView_Lato_Regular likeCount, final int dislikedId, final ImageView dislike, final MyTextView_Lato_Regular dislikeId, final MyTextView_Lato_Regular dislikeCount, final MyTextView_Lato_Regular likedId, final LinearLayout mLikeLayout, final LinearLayout mDislikeLayout) {

        likes.setLikeId(dislikedId);

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LikeAPI mapApi = Util.getClient().create(LikeAPI.class);
                Call<Likes> response = mapApi.updateLikes(dislikedId,likes);
                response.enqueue(new Callback<Likes>() {
                    @Override
                    public void onResponse(Call<Likes> call, Response<Likes> response) {

//                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                            like.setImageResource(R.drawable.liked_icon);
                            dislike.setImageResource(R.drawable.unlike_icons);
                            likedId.setText(""+dislikedId);
                            dislikeId.setText("");
                            String likeText = likeCount.getText().toString();
                            if(likeText!=null&&!likeText.isEmpty()){

                                int count = Integer.parseInt(likeText);
                                likeCount.setText(""+(count+1));
                            }
                            String dislikeText = dislikeCount.getText().toString();
                            if(dislikeText!=null&&!dislikeText.isEmpty()){

                                int count = Integer.parseInt(dislikeText);
                                dislikeCount.setText(""+(count-1));
                                mDislikeLayout.setEnabled(true);

                            }

                        }
                        else
                        {

                            mLikeLayout.setEnabled(true);

                        }
                    }

                    @Override
                    public void onFailure(Call<Likes> call, Throwable t) {

                        //Toast.makeText(ContentDetailScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        mLikeLayout.setEnabled(true);


                    }
                });
            }
        });
    }

    private void updatedisLike(final Likes likes, final ImageView like, final MyTextView_Lato_Regular likeCount, final int dislikedId, final ImageView dislike, final MyTextView_Lato_Regular dislikeId, final MyTextView_Lato_Regular dislikeCount, final MyTextView_Lato_Regular likedId, final LinearLayout mDislikeLayout, final LinearLayout mLikeLayout) {

        likes.setLikeId(dislikedId);

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LikeAPI mapApi = Util.getClient().create(LikeAPI.class);
                Call<Likes> response = mapApi.updateLikes(dislikedId,likes);
                response.enqueue(new Callback<Likes>() {
                    @Override
                    public void onResponse(Call<Likes> call, Response<Likes> response) {

                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                            like.setImageResource(R.drawable.unliked_icons);
                            dislike.setImageResource(R.drawable.non_like);
                            likedId.setText(""+dislikedId);
                            dislikeId.setText("");
                            String likeText = likeCount.getText().toString();
                            if(likeText!=null&&!likeText.isEmpty()){

                                int count = Integer.parseInt(likeText);
                                likeCount.setText(""+(count+1));
                            }
                            String dislikeText = dislikeCount.getText().toString();
                            if(dislikeText!=null&&!dislikeText.isEmpty()){

                                int count = Integer.parseInt(dislikeText);
                                dislikeCount.setText(""+(count-1));
                                mLikeLayout.setEnabled(true);

                            }

                        }
                        else
                        {
                            mDislikeLayout.setEnabled(false);

                        }
                    }

                    @Override
                    public void onFailure(Call<Likes> call, Throwable t) {

                        mDislikeLayout.setEnabled(false);

                      //  Toast.makeText(ContentDetailScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        like.setEnabled(true);

                    }
                });
            }
        });
    }


    private void postDislike(final Likes likes, final ImageView like, final MyTextView_Lato_Regular likeCount, final int dislikedId, final ImageView dislike, final MyTextView_Lato_Regular dislikeId, final MyTextView_Lato_Regular dislikeCount, final MyTextView_Lato_Regular likedId, final LinearLayout mDislikeLayout, final LinearLayout mLikeLayout) {

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LikeAPI mapApi = Util.getClient().create(LikeAPI.class);
                Call<Likes> response = mapApi.postLikes(likes);
                response.enqueue(new Callback<Likes>() {
                    @Override
                    public void onResponse(Call<Likes> call, Response<Likes> response) {

//                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                            dislike.setImageResource(R.drawable.unliked_icons);
                            like.setImageResource(R.drawable.non_like);

                            dislikeId.setText(""+response.body().getLikeId());
                            likedId.setText("");
                            String likeText = dislikeCount.getText().toString();
                            if(likeText!=null&&!likeText.isEmpty()){

                                int count = Integer.parseInt(likeText);
                                dislikeCount.setText(""+(count+1));
                            }

                            mLikeLayout.setEnabled(true);

                        }
                        else
                        {

                            mDislikeLayout.setEnabled(true);


                        }
                    }

                    @Override
                    public void onFailure(Call<Likes> call, Throwable t) {

                       // Toast.makeText(ContentDetailScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        mDislikeLayout.setEnabled(true);


                    }
                });
            }
        });
    }


    private class DownloadTask extends AsyncTask<URL,Void,Bitmap> {
        // Before the tasks execution
        ProgressDialog progressDialog = new ProgressDialog(ContentDetailScreen.this);

        protected void onPreExecute(){

            // Display the progress dialog on async task start
            //mProgressDialog.show();
            //Toast.makeText(context, "Downloading image...", Toast.LENGTH_SHORT).show();

            progressDialog.setTitle("please wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        // Do the task in background/non UI thread
        protected Bitmap doInBackground(URL...urls){
            URL url = urls[0];
            HttpURLConnection connection = null;

            try{
                // Initialize a new http url connection
                connection = (HttpURLConnection) url.openConnection();

                // Connect the http url connection
                connection.connect();

                // Get the input stream from http url connection
                InputStream inputStream = connection.getInputStream();

                /*
                    BufferedInputStream
                        A BufferedInputStream adds functionality to another input stream-namely,
                        the ability to buffer the input and to support the mark and reset methods.
                */
                /*
                    BufferedInputStream(InputStream in)
                        Creates a BufferedInputStream and saves its argument,
                        the input stream in, for later use.
                */
                // Initialize a new BufferedInputStream from InputStream
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                /*
                    decodeStream
                        Bitmap decodeStream (InputStream is)
                            Decode an input stream into a bitmap. If the input stream is null, or
                            cannot be used to decode a bitmap, the function returns null. The stream's
                            position will be where ever it was after the encoded data was read.

                        Parameters
                            is InputStream : The input stream that holds the raw data
                                              to be decoded into a bitmap.
                        Returns
                            Bitmap : The decoded bitmap, or null if the image data could not be decoded.
                */
                // Convert BufferedInputStream to Bitmap object
                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);

                // Return the downloaded bitmap
                connection.disconnect();
                return bmp;

            }catch(IOException e){
                e.printStackTrace();
            }finally{
                // Disconnect the http url connection
                connection.disconnect();
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result){
            // Hide the progress dialog
            // mProgressDialog.dismiss();
            if(result!=null){
                // Display the downloaded image into ImageView
                //mImageView.setImageBitmap(result);

                // Save bitmap to internal storage

                // Set the ImageView image from internal storage
                //mImageViewInternal.setImageURI(imageInternalUri);

                try{



                    File sd = Environment.getExternalStorageDirectory();
                    String fileName = fileNames+ ".jpg";

                    File directory = new File(sd.getAbsolutePath()+"/Mera Bihar App/.Share/");
                    //create directory if not exist
                    if (!directory.exists() && !directory.isDirectory()) {
                        directory.mkdirs();
                    }


                    File file = new File(directory, fileName);

                    //if(file.exists())

                    Intent shareIntent;


                    OutputStream out = null;
                    try {
                        out = new FileOutputStream(file);
                        mark(result).compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    fileName=file.getPath();

                    Uri bmpUri = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        bmpUri = FileProvider.getUriForFile(ContentDetailScreen.this, "tv.merabihar.app.merabihar.fileprovider", file);
                    }else{
                        bmpUri = Uri.parse("file://"+fileName);
                    }
                    // Uri bmpUri = Uri.parse("file://"+path);
                    shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    shareIntent.setPackage("com.whatsapp");
                    /*String sAux = "\n"+mBlogName.getText().toString()+"\n\n";
                    sAux = sAux + "to read more click here "+shortUrl+" \n\n"+"To get the latest update about Bihar Download our Bihar Tourism official mobile app by clicking goo.gl/rZfotV";*/
                    if(PreferenceHandler.getInstance(ContentDetailScreen.this).getUserId()!=0){
                        shareIntent.putExtra(Intent.EXTRA_TEXT,shareContent);
                    }else{
                        shareIntent.putExtra(Intent.EXTRA_TEXT,shareContents);
                    }
                    shareIntent.setType("image/*");
                    try{

//                        startActivityForResult(shareIntent, REQUEST_CODE); // REQUEST_CODE = 101
                         startActivity(shareIntent);

                    }catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(ContentDetailScreen.this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                    }
                    //context.startActivity(Intent.createChooser(shareIntent,"Share with"));

                }catch (Exception we)
                {
                    try{


                        File sd = Environment.getExternalStorageDirectory();
                        String fileName = fileNames+ ".jpg";

                        File directory = new File(sd.getAbsolutePath()+"/Mera Bihar App/.Share/");
                        //create directory if not exist
                        if (!directory.exists() && !directory.isDirectory()) {
                            directory.mkdirs();
                        }


                        File file = new File(directory, fileName);

                        //if(file.exists())

                        Intent shareIntent;


                        OutputStream out = null;
                        try {
                            out = new FileOutputStream(file);
                            mark(result).compress(Bitmap.CompressFormat.JPEG, 100, out);
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        fileName=file.getPath();

                        Uri bmpUri = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            bmpUri = FileProvider.getUriForFile(ContentDetailScreen.this, "tv.merabihar.app.merabihar.fileprovider", file);
                        }else{
                            bmpUri = Uri.parse("file://"+fileName);
                        }
                        // Uri bmpUri = Uri.parse("file://"+path);
                        shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);

                    /*String sAux = "\n"+mBlogName.getText().toString()+"\n\n";
                    sAux = sAux + "to read more click here "+shortUrl+" \n\n"+"To get the latest update about Bihar Download our Bihar Tourism official mobile app by clicking goo.gl/rZfotV";*/
                        if(PreferenceHandler.getInstance(ContentDetailScreen.this).getUserId()!=0){
                            shareIntent.putExtra(Intent.EXTRA_TEXT,shareContent);
                        }else{
                            shareIntent.putExtra(Intent.EXTRA_TEXT,shareContents);
                        }
                        shareIntent.setType("image/*");
                   /* try{

                        context.startActivity(shareIntent);

                    }catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                    }*/
                        startActivity(Intent.createChooser(shareIntent,"Share with"));

                    }catch (Exception wes)
                    {
                        wes.printStackTrace();
                        Toast.makeText(ContentDetailScreen.this, ""+we.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    /*
                    we.printStackTrace();
                    Toast.makeText(ContentDetailScreen.this, "Unable to send,Check Permission", Toast.LENGTH_SHORT).show();*/
                }

            }else {
                // Notify user that an error occurred while downloading image
                //Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
            }

            progressDialog.dismiss();

        }
    }


/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        *//*Toast.makeText(this, ""+ requestCode, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, ""+ resultCode, Toast.LENGTH_SHORT).show();
        *//*

        if(resultCode==RESULT_OK) {
            Toast.makeText(this, ""+ requestCode, Toast.LENGTH_SHORT).show();
            if (requestCode == REQUEST_CODE) {
                // Increase the whatsapp count and update api
                int count = Integer.parseInt(mWhatsappShareCount.getText().toString()) ;
//                mWhatsappShareCount.setText("Increased");

            }
        }else{
//            Toast.makeText(this, "Response invalid", Toast.LENGTH_SHORT).show();
        }

    }*/

    private class DownloadTasks extends AsyncTask<URL,Void,Bitmap> {
        // Before the tasks execution
        protected void onPreExecute(){
            // Display the progress dialog on async task start
            //mProgressDialog.show();
            //Toast.makeText(context, "Downloading image...", Toast.LENGTH_SHORT).show();
        }

        // Do the task in background/non UI thread
        protected Bitmap doInBackground(URL...urls){
            URL url = urls[0];
            HttpURLConnection connection = null;

            try{
                // Initialize a new http url connection
                connection = (HttpURLConnection) url.openConnection();

                // Connect the http url connection
                connection.connect();

                // Get the input stream from http url connection
                InputStream inputStream = connection.getInputStream();

                /*
                    BufferedInputStream
                        A BufferedInputStream adds functionality to another input stream-namely,
                        the ability to buffer the input and to support the mark and reset methods.
                */
                /*
                    BufferedInputStream(InputStream in)
                        Creates a BufferedInputStream and saves its argument,
                        the input stream in, for later use.
                */
                // Initialize a new BufferedInputStream from InputStream
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                /*
                    decodeStream
                        Bitmap decodeStream (InputStream is)
                            Decode an input stream into a bitmap. If the input stream is null, or
                            cannot be used to decode a bitmap, the function returns null. The stream's
                            position will be where ever it was after the encoded data was read.

                        Parameters
                            is InputStream : The input stream that holds the raw data
                                              to be decoded into a bitmap.
                        Returns
                            Bitmap : The decoded bitmap, or null if the image data could not be decoded.
                */
                // Convert BufferedInputStream to Bitmap object
                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);

                // Return the downloaded bitmap
                return bmp;

            }catch(IOException e){
                e.printStackTrace();
            }finally{
                // Disconnect the http url connection
                connection.disconnect();
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result){
            // Hide the progress dialog
            // mProgressDialog.dismiss();

            if(result!=null){
                // Display the downloaded image into ImageView
                //mImageView.setImageBitmap(result);

                // Save bitmap to internal storage

                // Set the ImageView image from internal storage
                //mImageViewInternal.setImageURI(imageInternalUri);

                try{



                    File sd = Environment.getExternalStorageDirectory();
                    String fileName = fileNames+ ".jpg";

                    File directory = new File(sd.getAbsolutePath()+"/Mera Bihar App/.Share/");
                    //create directory if not exist
                    if (!directory.exists() && !directory.isDirectory()) {
                        directory.mkdirs();
                    }


                    File file = new File(directory, fileName);

                    //if(file.exists())

                    Intent shareIntent;


                    OutputStream out = null;
                    try {
                        out = new FileOutputStream(file);
                        mark(result).compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    fileName=file.getPath();

                    Uri bmpUri = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        bmpUri = FileProvider.getUriForFile(ContentDetailScreen.this, "tv.merabihar.app.merabihar.fileprovider", file);
                    }else{
                        bmpUri = Uri.parse("file://"+fileName);
                    }
                    // Uri bmpUri = Uri.parse("file://"+path);
                    shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);

                    /*String sAux = "\n"+mBlogName.getText().toString()+"\n\n";
                    sAux = sAux + "to read more click here "+shortUrl+" \n\n"+"To get the latest update about Bihar Download our Bihar Tourism official mobile app by clicking goo.gl/rZfotV";*/
                    if(PreferenceHandler.getInstance(ContentDetailScreen.this).getUserId()!=0){
                        shareIntent.putExtra(Intent.EXTRA_TEXT,shareContent);
                    }else{
                        shareIntent.putExtra(Intent.EXTRA_TEXT,shareContents);
                    }
                    shareIntent.setType("image/*");
                   /* try{

                        context.startActivity(shareIntent);

                    }catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                    }*/
                    startActivity(Intent.createChooser(shareIntent,"Share with"));

                }catch (Exception we)
                {
                    we.printStackTrace();
                    Toast.makeText(ContentDetailScreen.this, "Unable to send,Check Permission", Toast.LENGTH_SHORT).show();
                }

            }else {
                // Notify user that an error occurred while downloading image
                //Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
            }
        }
    }

    // Custom method to convert string to url
    protected URL stringToURL(String urlString){
        try{
            URL url = new URL(urlString);
            return url;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

    // Custom method to save a bitmap into internal storage
    public Bitmap mark(Bitmap src) {

        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.logo_mbtv);
        int w = src.getWidth();
        int h = src.getHeight();
        int pw=w-w;
        int ph=h-h;
        int nw = (w * 10)/100;
        int nh = (h * 10)/100;
        Bitmap result = Bitmap.createBitmap(w, h, icon.getConfig());
        Canvas canvas = new Canvas(result);
        Bitmap resized = Bitmap.createScaledBitmap(icon, 25, 25, true);

        canvas.drawBitmap(src, 0, 0, null);
        Paint paint = new Paint();

        paint.setTextSize(30);
        paint.setAntiAlias(true);
        paint.setUnderlineText(false);
        canvas.drawBitmap(resized,pw,ph,paint);
        return result;
    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener()    {

        @Override
        public void onBuffering(boolean arg0) {

            //Toast.makeText(ContentDetailScreen.this, "Video Buffering", Toast.LENGTH_SHORT).show();
            System.out.println("Video Buffering");
        }

        @Override
        public void onPaused() {

            //Toast.makeText(ContentDetailScreen.this, "Video Pause", Toast.LENGTH_SHORT).show();
            System.out.println("Video Pause");

            TimeBuff += MillisecondTime;

            handler.removeCallbacks(runnable);
        }

        @Override
        public void onPlaying() {

            //Toast.makeText(ContentDetailScreen.this, "Video Play", Toast.LENGTH_SHORT).show();

            StartTime = SystemClock.uptimeMillis();
            handler.postDelayed(runnable, 0);
            System.out.println("Video Play");
        }

        @Override
        public void onSeekTo(int arg0) {

            //Toast.makeText(ContentDetailScreen.this, "Video Seeking", Toast.LENGTH_SHORT).show();
            System.out.println("Video Seeking");
        }

        @Override
        public void onStopped() {

            //Toast.makeText(ContentDetailScreen.this, "Video Stop", Toast.LENGTH_SHORT).show();
            System.out.println("Video Stop");
            TimeBuff += MillisecondTime;

            handler.removeCallbacks(runnable);
        }

    };

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

           /* Minutes = Seconds / 60;

            Seconds = Seconds % 60;*/

            //MilliSeconds = (int) (UpdateTime % 1000);

            youtubeWatcheTime = Seconds;

            handler.postDelayed(this, 0);
        }

    };

    public void showAlertBox(){

        try{

            final String[] imageSelectionArray = {"Edit","Delete","Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(ContentDetailScreen.this);

            builder.setCancelable(false);
            builder.setItems(imageSelectionArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(imageSelectionArray[which].equals("Edit"))
                    {

                        Intent intent = new Intent(ContentDetailScreen.this, UpdateContentUrlScreen.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("UpdateContents",contents);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }else  if(imageSelectionArray[which].equals("Delete"))
                    {
                        deleteAlertBox();

                    }else
                    {
                        dialog.dismiss();
                    }
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteAlertBox(){

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                //set icon
                .setIcon(android.R.drawable.ic_dialog_alert)
                //set title
                .setTitle("Are you sure to Delete your post?")
                //set message
                .setMessage("After delete it will not be longer anywhere")
                //set positive button
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        deleteContents(contents.getContentId());
                    }
                })
                //set negative button
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked

                    }
                })
                .show();
    }

    public void deleteContents(final int id)
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ContentAPI categoryAPI = Util.getClient().create(ContentAPI.class);
                Call<Contents> getCat = categoryAPI.deleteContentsById(id);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<Contents>() {

                    @Override
                    public void onResponse(Call<Contents> call, Response<Contents> response) {



                        if(response.code() == 200||response.code()==201||response.code()==204)
                        {

                            Contents contents = response.body();

                            if(contents != null )
                            {

                                ContentDetailScreen.this.finish();



                            }
                            else
                            {
                                ContentDetailScreen.this.finish();


                            }
                        }else{

                            Toast.makeText(ContentDetailScreen.this, "Unable to delete due to "+response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Contents> call, Throwable t) {

                        Toast.makeText(ContentDetailScreen.this, "Unable to delete", Toast.LENGTH_SHORT).show();


//                        Toast.makeText(CommentsScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }
}
