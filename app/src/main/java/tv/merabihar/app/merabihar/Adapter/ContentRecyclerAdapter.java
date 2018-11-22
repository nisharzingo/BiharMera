package tv.merabihar.app.merabihar.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import junit.framework.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.Date;
import java.util.List;
import java.util.Timer;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Lato_Regular;
import tv.merabihar.app.merabihar.CustomFonts.TextViewSFProDisplaySemibold;
import tv.merabihar.app.merabihar.CustomInterface.OnBottomReachedListener;
import tv.merabihar.app.merabihar.CustomViews.DoubleClickListener;
import tv.merabihar.app.merabihar.CustomViews.DoubleTab;
import tv.merabihar.app.merabihar.Model.CategoryAndContentList;
import tv.merabihar.app.merabihar.Model.ContentImages;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Model.FollowsWithMapping;
import tv.merabihar.app.merabihar.Model.Likes;
import tv.merabihar.app.merabihar.Model.ProfileFollowMapping;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.CommentsScreen;
import tv.merabihar.app.merabihar.UI.Activity.ContentDetailScreen;
import tv.merabihar.app.merabihar.UI.Activity.ContentImageDetailScreen;
import tv.merabihar.app.merabihar.UI.Activity.LoginScreen;
import tv.merabihar.app.merabihar.UI.Activity.ProfileScreen;
import tv.merabihar.app.merabihar.UI.Activity.SignUpScreen;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.TabMainActivity;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.LikeAPI;
import tv.merabihar.app.merabihar.WebAPI.ProfileAPI;
import tv.merabihar.app.merabihar.WebAPI.ProfileFollowAPI;

/**
 * Created by ZingoHotels Tech on 31-10-2018.
 */

public class ContentRecyclerAdapter extends RecyclerView.Adapter {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private boolean isLoadingAdded = false;
    private Context context;
    private ArrayList<Contents> mList;

    int currentPage = 0,start = 0,end = 0;
    Timer timer;
    final long DELAY_MS = 5000;
    final long PERIOD_MS = 3500;

    String url,fileNames;
    boolean isFirstTimePressed = false;
    String shareContent = "Save time. Download Mera Bihar,The Only App for Bihar,To Read,Share your Stories and Earn Rs 1000\n\n Use my referal code for Sign-Up MBR"+PreferenceHandler.getInstance(context).getUserId()+"\n http://bit.ly/2JXcOnw";

    OnBottomReachedListener onBottomReachedListener;

    public ContentRecyclerAdapter(Context context) {

        this.context = context;
        mList = new ArrayList<>();


    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

        this.onBottomReachedListener = onBottomReachedListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType)
        {
            case ITEM:
                viewHolder = getViewHolder(parent,inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }

        return viewHolder;

    }

    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent,LayoutInflater inflater)
    {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.adapter_content_lists_design, parent, false);
        viewHolder = new ContentViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holders, final int pos) {

        final int profileId = PreferenceHandler.getInstance(context).getUserId();

        try{
            if(mList.get(pos)!=null){

                switch (getItemViewType(pos)) {

                    case ITEM:
                        final ContentViewHolder holder = (ContentViewHolder) holders;

                        SimpleDateFormat sdfs = new SimpleDateFormat("MMM dd,yyyy");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        final  Contents contents = mList.get(pos);


                        if(contents!=null){

                            String contentTitle =contents.getTitle();
                            final String contentDesc =contents.getDescription();
                            String createdBy =contents.getCreatedBy();
                            String createdDate =contents.getCreatedDate();

                            holder.mProfileName.setText(""+createdBy);
                            holder.mContentTitle.setText(""+contentTitle);
                            holder.mContentDesc.setText(""+contentDesc);
                           
                            int countLine= holder.mContentDesc.getLineHeight();
                            System.out.println("LineCount " + countLine);
                            if (countLine>=25){
                               holder.mMore.setVisibility(View.VISIBLE);
                            }
                           // holder.mProfileName.setText(""+createdBy);

                            holder.mMore.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {


                                    holder.mContentDesc.setMaxLines(5);
                                    holder.mContentDesc.setText(""+contentDesc);

                                    int countLine= holder.mContentDesc.getLineHeight();
                                    System.out.println("LineCount " + countLine);
                                    if (countLine>=50){
                                        holder.mMoreExtnd.setVisibility(View.GONE);
                                        holder.mMoreLine.setVisibility(View.VISIBLE);
                                        holder.mMore.setVisibility(View.GONE);
                                        holder.mLess.setVisibility(View.GONE);
                                    }else{
                                        holder.mMore.setVisibility(View.GONE);
                                        holder.mMoreLine.setVisibility(View.GONE);
                                        holder.mMoreExtnd.setVisibility(View.GONE);
                                        holder.mLess.setVisibility(View.VISIBLE);
                                    }

                                }
                            });

                            holder.mMoreLine.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {


                                    holder.mContentDesc.setMaxLines(10);
                                    holder.mContentDesc.setText(""+contentDesc);

                                    int countLine= holder.mContentDesc.getLineHeight();
                                    System.out.println("LineCount " + countLine);
                                    if (countLine>=70){
                                        holder.mMoreExtnd.setVisibility(View.VISIBLE);
                                        holder.mMore.setVisibility(View.GONE);
                                        holder.mMoreLine.setVisibility(View.GONE);
                                        holder.mLess.setVisibility(View.GONE);
                                    }else{
                                        holder.mMore.setVisibility(View.GONE);
                                        holder.mMoreExtnd.setVisibility(View.GONE);
                                        holder.mMoreLine.setVisibility(View.GONE);
                                        holder.mLess.setVisibility(View.VISIBLE);
                                    }

                                }
                            });


                            holder.mMoreExtnd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if(contents.getContentType().equalsIgnoreCase("Video")){

                                        Intent intent = new Intent(context, ContentDetailScreen.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("Contents",contents);
                                        intent.putExtras(bundle);
                                        context.startActivity(intent);

                                    }else if(contents.getContentType().equalsIgnoreCase("Image")){

                                        Intent intent = new Intent(context, ContentImageDetailScreen.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("Contents",contents);
                                        intent.putExtras(bundle);
                                        context.startActivity(intent);
                                    }
                                }
                            });

                            holder.mLess.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    holder.mLess.setVisibility(View.GONE);
                                    holder.mMoreLine.setVisibility(View.GONE);
                                    holder.mMoreExtnd.setVisibility(View.GONE);
                                    holder.mMore.setVisibility(View.VISIBLE);
                                    holder.mContentDesc.setMaxLines(2);

                                }
                            });
                            if(createdDate!=null||!createdDate.isEmpty()){

                                if(createdDate.contains("T")){
                                  /*  String date = createdDate.replace("T","");
                                    holder.mDuration.setText(""+duration(date));*/

                                  String date[] = createdDate.split("T");
                                  holder.mDuration.setText(""+date[0]);
                                }

                            }else{
                                holder.mDuration.setVisibility(View.GONE);
                            }

                           // ArrayList<ContentImages> contentImage = contents.getContentImage();

                            if(profileId!=0){

                                if(profileId==contents.getProfileId()){

                                    holder.mFollow.setVisibility(View.GONE);
                                }else{
                                    //getFollowingByProfileId(profileId,holder.mFollow,contents.getProfileId());
                                    getFollowingsByProfileId(profileId,holder.mFollow,contents.getProfileId());

                                }

                            }

                            if(contents.getCommentsList()!=null&&contents.getCommentsList().size()!=0){

                                holder.mCommentsCount.setText(""+contents.getCommentsList().size());

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
                                                holder.mLikedId.setText(""+likes.getLikeId());
                                            }
                                        }


                                    }else{
                                        disliked.add(likes);

                                        if(profileId!=0){
                                            if(likes.getProfileId()==profileId){
                                                profileDislike = true;
                                                holder.mDislikedId.setText(""+likes.getLikeId());
                                            }
                                        }

                                    }

                                }


                                if(liked!=null&&liked.size()!=0){

                                    holder.mLikesCount.setText(""+liked.size());

                                }

                                if(disliked!=null&&disliked.size()!=0){

                                    holder.mDislikesCount.setText(""+disliked.size());
                                }

                                if(profileLike){

                                    holder.mLike.setImageResource(R.drawable.liked_icon);
                                }

                                if(profileDislike){

                                    holder.mDislike.setImageResource(R.drawable.unliked_icons);
                                }
                            }


                            if(contents.getProfile()==null){
                                getProfile(contents.getProfileId(),holder.mProfilePhoto);
                            }else{


                                String base=contents.getProfile().getProfilePhoto();
                                if(base != null && !base.isEmpty()){
                                    Picasso.with(context).load(base).placeholder(R.drawable.profile_image).
                                            error(R.drawable.profile_image).into(holder.mProfilePhoto);

                                }else{
                                    holder.mProfilePhoto.setImageResource(R.drawable.profile_image);
                                }
                            }

                            if(contents.getContentType().equalsIgnoreCase("Video")){

                                //url = contents.getContentURL();


                                holder.mIcon.setVisibility(View.VISIBLE);
                                if(contents.getContentURL()!=null&&!contents.getContentURL().isEmpty()){
                                    String img = "https://img.youtube.com/vi/"+contents.getContentURL()+"/0.jpg";
                                    if(img!=null&&!img.isEmpty()){
                                        Picasso.with(context).load(img).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(holder.mContentPic);
                                    }else{
                                        holder.mContentPic.setImageResource(R.drawable.no_image);
                                    }
                                }else{
                                    holder.mContentPic.setImageResource(R.drawable.no_image);
                                }





                            }else{


                                holder.mIcon.setVisibility(View.GONE);
                                if(contents.getContentImage() != null && contents.getContentImage().size()!=0)
                                {

                                    String img = contents.getContentImage().get(0).getImages();

                                    if(img!=null&&!img.isEmpty()){
                                        Picasso.with(context).load(img).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(holder.mContentPic);
                                    }else{
                                        holder.mContentPic.setImageResource(R.drawable.no_image);
                                    }



                                }else{
                                    holder.mContentPic.setImageResource(R.drawable.no_image);
                                }

                            }



                            /*holder.mContentDetail.setOnTouchListener(new View.OnTouchListener() {
                                private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                                    @Override
                                    public boolean onDoubleTap(MotionEvent e) {
                                        Log.d("TEST", "onDoubleTap");
                                        return super.onDoubleTap(e);



                                    }

                                });

                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    Log.d("TEST", "Raw event: " + event.getAction() + ", (" + event.getRawX() + ", " + event.getRawY() + ")");
                                    gestureDetector.onTouchEvent(event);
                                    if(contents.getContentType().equalsIgnoreCase("Video")){

                                        Intent intent = new Intent(context, ContentDetailScreen.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("Contents",contents);
                                        intent.putExtras(bundle);
                                        context.startActivity(intent);

                                    }else if(contents.getContentType().equalsIgnoreCase("Image")){

                                        Intent intent = new Intent(context, ContentImageDetailScreen.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("Contents",contents);
                                        intent.putExtras(bundle);
                                        context.startActivity(intent);
                                    }
                                    return true;
                                }
                            });*/




                            holder.mContentDetail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    new CountDownTimer(500, 500) {
                                        @Override
                                        public void onTick(long l) {
                                            if(!isFirstTimePressed)
                                            {

                                                isFirstTimePressed = true;
                                            }
                                            else
                                            {
                                                //System.out.println("isFirstTimePressed = "+isFirstTimePressed);
                                                isFirstTimePressed = false;
                                                if(profileId!=0 && holder.mLike.getDrawable().getConstantState()!=context.getResources().getDrawable(R.drawable.liked_icon).getConstantState()){

                                                    holder.mLike.setEnabled(false);
                                                    Likes likes = new Likes();
                                                    likes.setContentId(contents.getContentId());
                                                    likes.setProfileId(profileId);
                                                    likes.setLiked(true);

                                                    if (holder.mDislike.getDrawable().getConstantState() == context.getResources().getDrawable( R.drawable.unliked_icons).getConstantState())
                                                    {
                                                        if(holder.mDislikedId.getText().toString()!=null&&!holder.mDislikedId.getText().toString().isEmpty()){


                                                          //  updateLike(likes,holder.mLike,holder.mLikesCount,Integer.parseInt(holder.mDislikedId.getText().toString()),holder.mDislike,holder.mDislikedId,holder.mDislikesCount,holder.mLikedId);
                                                        }
                                                    }
                                                    else
                                                    {

                                                        postLike(likes,holder.mLike,holder.mLikesCount,0,holder.mDislike,holder.mDislikedId,holder.mDislikesCount,holder.mLikedId);
                                                    }


                                                }

                                                //finishAffinity();
                                            }
                                        }

                                        @Override
                                        public void onFinish() {

                                            if(isFirstTimePressed){
                                                if(contents.getContentType().equalsIgnoreCase("Video")){

                                                    Intent intent = new Intent(context, ContentDetailScreen.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putSerializable("Contents",contents);
                                                    intent.putExtras(bundle);
                                                    context.startActivity(intent);

                                                }else if(contents.getContentType().equalsIgnoreCase("Image")){

                                                    Intent intent = new Intent(context, ContentImageDetailScreen.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putSerializable("Contents",contents);
                                                    intent.putExtras(bundle);
                                                    context.startActivity(intent);
                                                }
                                            }
                                            isFirstTimePressed = false;


                                        }
                                    }.start();

                                }
                            });


                            /*holder.mContentDetail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    if(contents.getContentType().equalsIgnoreCase("Video")){

                                        Intent intent = new Intent(context, ContentDetailScreen.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("Contents",contents);
                                        intent.putExtras(bundle);
                                        context.startActivity(intent);

                                    }else if(contents.getContentType().equalsIgnoreCase("Image")){

                                        Intent intent = new Intent(context, ContentImageDetailScreen.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("Contents",contents);
                                        intent.putExtras(bundle);
                                        context.startActivity(intent);
                                    }
                                }
                            });*/

                            holder.mLike.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if(profileId!=0){

                                        holder.mLike.setEnabled(false);
                                        Likes likes = new Likes();
                                        likes.setContentId(contents.getContentId());
                                        likes.setProfileId(profileId);
                                        likes.setLiked(true);

                                        if (holder.mDislike.getDrawable().getConstantState() == context.getResources().getDrawable( R.drawable.unliked_icon).getConstantState())
                                        {
                                            if(holder.mDislikedId.getText().toString()!=null&&!holder.mDislikedId.getText().toString().isEmpty()){


                                                updateLike(likes,holder.mLike,holder.mLikesCount,Integer.parseInt(holder.mDislikedId.getText().toString()),holder.mDislike,holder.mDislikedId,holder.mDislikesCount,holder.mLikedId);
                                            }
                                        }
                                        else
                                        {

                                            postLike(likes,holder.mLike,holder.mLikesCount,0,holder.mDislike,holder.mDislikedId,holder.mDislikesCount,holder.mLikedId);
                                        }




                                       /* }else{

                                            postLike(likes,holder.mLike,holder.mLiked,holder.mLikeCount,holder.mLikedId);
                                        }*/

                                    }else {
                                        new AlertDialog.Builder(context)
                                                .setMessage("Please login/Signup to Like the Story")
                                                .setCancelable(false)
                                                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                        Intent login = new Intent(context, LoginScreen.class);
                                                        context.startActivity(login);

                                                    }
                                                })
                                                .setNegativeButton("SignUp", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                        Intent signUp = new Intent(context, SignUpScreen.class);
                                                        context.startActivity(signUp);

                                                    }
                                                })
                                                .show();
                                    }
                                }
                            });

                            holder.mDislike.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if(profileId!=0){

                                        holder.mDislike.setEnabled(false);
                                        Likes likes = new Likes();
                                        likes.setContentId(contents.getContentId());
                                        likes.setProfileId(profileId);
                                        likes.setLiked(false);

                                        if (holder.mLike.getDrawable().getConstantState() == context.getResources().getDrawable( R.drawable.liked_icon).getConstantState())
                                        {
                                            if(holder.mLikedId.getText().toString()!=null&&!holder.mLikedId.getText().toString().isEmpty()){


                                                updatedisLike(likes,holder.mDislike,holder.mDislikesCount,Integer.parseInt(holder.mLikedId.getText().toString()),holder.mLike,holder.mLikedId,holder.mLikesCount,holder.mDislikedId);
                                            }
                                        }
                                        else
                                        {

                                            postDislike(likes,holder.mLike,holder.mLikesCount,0,holder.mDislike,holder.mDislikedId,holder.mDislikesCount,holder.mLikedId);
                                        }




                                       /* }else{

                                            postLike(likes,holder.mLike,holder.mLiked,holder.mLikeCount,holder.mLikedId);
                                        }*/

                                    }else {
                                        new AlertDialog.Builder(context)
                                                .setMessage("Please login/Signup to Like the Story")
                                                .setCancelable(false)
                                                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                        Intent login = new Intent(context, LoginScreen.class);
                                                        context.startActivity(login);

                                                    }
                                                })
                                                .setNegativeButton("SignUp", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                        Intent signUp = new Intent(context, SignUpScreen.class);
                                                        context.startActivity(signUp);

                                                    }
                                                })
                                                .show();
                                    }
                                }
                            });

                            holder.mFollow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if(profileId!=0){

                                        if(holder.mFollow.getText().toString().equalsIgnoreCase("Follow")){
                                            holder.mFollow.setEnabled(false);
                                            ProfileFollowMapping pm = new ProfileFollowMapping();
                                            pm.setFollowerId(contents.getProfileId());
                                            pm.setProfileId(PreferenceHandler.getInstance(context).getUserId());
                                            profileFollow(pm,holder.mFollow);
                                        }else{

                                            Toast.makeText(context, "Already you followed "+contents.getCreatedBy(), Toast.LENGTH_SHORT).show();
                                        }

                                    }else{

                                        new AlertDialog.Builder(context)
                                                .setMessage("Please login/Signup to Like the Story")
                                                .setCancelable(false)
                                                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                        Intent login = new Intent(context, LoginScreen.class);
                                                        context.startActivity(login);

                                                    }
                                                })
                                                .setNegativeButton("SignUp", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                        Intent signUp = new Intent(context, SignUpScreen.class);
                                                        context.startActivity(signUp);

                                                    }
                                                })
                                                .show();

                                    }

                                }
                            });

                            holder.mProfileContent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(context, ProfileScreen.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Profile",contents.getProfile());
                                    bundle.putInt("ProfileId",contents.getProfileId());
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);

                                }
                            });

                            holder.mProfilePhoto.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(context, ProfileScreen.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Profile",contents.getProfile());
                                    bundle.putInt("ProfileId",contents.getProfileId());
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);

                                }
                            });

                            holder.mWhatsapp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    fileNames = contents.getContentId()+""+contents.getProfileId();

                                    AsyncTask mMyTask;
                                    if(contents.getContentType().equalsIgnoreCase("Video")) {

                                        url = contents.getContentURL();


                                        if (url != null && !url.isEmpty()) {

                                            mMyTask = new DownloadTask()
                                                    .execute(stringToURL(
                                                            "https://img.youtube.com/vi/"+url+"/0.jpg"
                                                    ));

                                        }

                                    }else{

                                        mMyTask = new DownloadTask()
                                                .execute(stringToURL(
                                                        ""+contents.getContentImage().get(0).getImages()
                                                ));
                                    }

                                    //shareApplication();
                                }
                            });

                            holder.mMoreShare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    shareApplication();
                                }
                            });
                            holder.mShare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    fileNames = contents.getContentId()+""+contents.getProfileId();

                                    AsyncTask mMyTask;
                                    if(contents.getContentType().equalsIgnoreCase("Video")) {

                                        url = contents.getContentURL();


                                        if (url != null && !url.isEmpty()) {

                                            mMyTask = new DownloadTasks()
                                                    .execute(stringToURL(
                                                            "https://img.youtube.com/vi/"+url+"/0.jpg"
                                                    ));

                                        }

                                    }else{

                                        mMyTask = new DownloadTasks()
                                                .execute(stringToURL(
                                                        ""+contents.getContentImage().get(0).getImages()
                                                ));
                                    }

                                    //shareApplication();
                                }
                            });


                            holder.mComment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent comments = new Intent(context, CommentsScreen.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Contents",contents);
                                    bundle.putInt("Position",pos);
                                    comments.putExtras(bundle);
                                    context.startActivity(comments);
                                }
                            });


                        }



                        break;

                    case LOADING:
//                Do nothing
                        break;


                }

            }
        }catch (Exception w){
            w.printStackTrace();
        }

    }

    @Override
    public int getItemViewType(int position) {
        System.out.println("inside getItemViewType = "+position);
        if(position == mList.size() - 1 && isLoadingAdded)
        {
            return LOADING;
        }
        else
        {
            return ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    public class ContentViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mProfilePhoto;
        MyTextView_Lato_Regular mProfileName,mDuration,mFollow,mContentTitle,mContentDesc
                ,mCommentsCount,mLikesCount,mDislikesCount,mLikedId,mDislikedId;
        TextViewSFProDisplaySemibold mTags;
        RoundedImageView mContentPic;
        ImageView mIcon;
        LinearLayout mProfileContent;
        FrameLayout mContentDetail;
        TextView mMore,mLess,mMoreExtnd,mMoreLine;


        ImageView mLike,mDislike,mComment,mWhatsapp,mShare,mMoreShare;

        public ContentViewHolder(View view) {
            super(view);

            mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_photo);
            mProfileName = (MyTextView_Lato_Regular) view.findViewById(R.id.profile_name);
            mDuration = (MyTextView_Lato_Regular) view.findViewById(R.id.duration);
            mFollow = (MyTextView_Lato_Regular) view.findViewById(R.id.follow_profile);
            mContentTitle = (MyTextView_Lato_Regular) view.findViewById(R.id.content_title);
            mContentDesc = (MyTextView_Lato_Regular) view.findViewById(R.id.content_desc);
            mCommentsCount = (MyTextView_Lato_Regular) view.findViewById(R.id.comments_count);
            mLikesCount = (MyTextView_Lato_Regular) view.findViewById(R.id.likes_count);
            mDislikesCount = (MyTextView_Lato_Regular) view.findViewById(R.id.unlikes_count);
            mLikedId = (MyTextView_Lato_Regular) view.findViewById(R.id.like_id);
            mDislikedId = (MyTextView_Lato_Regular) view.findViewById(R.id.dislike_id);
            mTags = (TextViewSFProDisplaySemibold) view.findViewById(R.id.content_tags);
            mContentPic = (RoundedImageView) view.findViewById(R.id.content_image);
            mIcon = (ImageView) view.findViewById(R.id.youtube_icon);
            mMore = (TextView) view.findViewById(R.id.read_more);
            mMoreExtnd = (TextView) view.findViewById(R.id.read_more_extend);
            mMoreLine = (TextView) view.findViewById(R.id.read_more_extend_line);
            mLess = (TextView) view.findViewById(R.id.read_less);
            mContentDetail = (FrameLayout) view.findViewById(R.id.content_detail);
            mProfileContent = (LinearLayout) view.findViewById(R.id.profile_lay_content);
            mLike = (ImageView) view.findViewById(R.id.likes_image);
            mDislike = (ImageView) view.findViewById(R.id.unlikes_image);
            mComment = (ImageView) view.findViewById(R.id.comments_image);
            mWhatsapp = (ImageView) view.findViewById(R.id.whatsapp_share);
            mShare = (ImageView) view.findViewById(R.id.share_image);
            mMoreShare = (ImageView) view.findViewById(R.id.more_icons);


        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public void add(Contents mc) {
        //System.out.println("BookingAndTraveller = "+mc.getRoomBooking().getTotalAmount());
        mList.add(mc);
        //System.out.println("BookingAndTraveller = "+list.size());
        notifyItemInserted(mList.size() );
    }

    public void addAll(List<Contents> mcList) {
        for (Contents mc : mcList) {
            add(mc);
        }
    }

    public void remove(Contents city) {
        int position = mList.indexOf(city);
        if (position > -1) {
            mList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        //add(new Movie());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        if(mList != null && mList.size() !=0)
        {
            int position = mList.size() - 1;
            Contents item = getItem(position);
            notifyItemChanged(position);

            /*if (item != null) {
                list.remove(position);
                notifyItemRemoved(position);
            }*/
        }
    }

    public Contents getItem(int position) {
        return mList.get(position);
    }

    public ArrayList<Contents> getAllBookings()
    {
        return mList;
    }


    public void getProfile(final int id,final CircleImageView cv){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ProfileAPI subCategoryAPI = Util.getClient().create(ProfileAPI.class);
                Call<UserProfile> getProf = subCategoryAPI.getProfileById(id);
                //Call<ArrayList<Blogs>> getBlog = blogApi.getBlogs();

                getProf.enqueue(new Callback<UserProfile>() {

                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {

                        if (response.code() == 200)
                        {
                            System.out.println("Inside api");

                            UserProfile profile = response.body();

                            if(profile.getProfilePhoto()!=null){

                                String base=profile.getProfilePhoto();

                                if(base != null && !base.isEmpty()){
                                    Picasso.with(context).load(base).placeholder(R.drawable.profile_image).
                                            error(R.drawable.profile_image).into(cv);

                                }else{
                                    cv.setImageResource(R.drawable.profile_image);
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


    public String duration(String notifyDate){
        //2018-08-28T00:00:00
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        Date fd = null,td = null;
        long diffDays=0,diffMinutes=0,diffHours=0 ;
        String duration = "" ;
        try {
            if(notifyDate.contains("-")){
                fd = sdf.parse(notifyDate);
            }

            System.out.println("Text=="+notifyDate+" Date"+fd);
            //td = sdf.parse(book_to_date.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        try {
            long diff = new Date().getTime() - fd.getTime();
            diffMinutes = diff / (60 * 1000) % 60;
            diffHours = diff / (60 * 60 * 1000) % 24;
            diffDays = diff / (24 * 60 * 60 * 1000);

        }catch(Exception e){
            e.printStackTrace();
        }
        if(diffDays!=0){
            duration =  diffDays+" day ago";
        }else if(diffHours!=0){
            duration =  diffHours+" hours ago";
        }else if(diffMinutes!=0){
            duration =  diffMinutes+" mins ago";
        }

        return duration;
    }

    private void postLike(final Likes likes, final ImageView like,final MyTextView_Lato_Regular likeCount,final int dislikedId,final ImageView dislike,final MyTextView_Lato_Regular dislikeId,final MyTextView_Lato_Regular dislikeCount,final MyTextView_Lato_Regular likedId) {

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LikeAPI mapApi = Util.getClient().create(LikeAPI.class);
                Call<Likes> response = mapApi.postLikes(likes);
                response.enqueue(new Callback<Likes>() {
                    @Override
                    public void onResponse(Call<Likes> call, Response<Likes> response) {

                        System.out.println(response.code());
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

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


                        }
                        else
                        {
                            if(dialog != null)
                            {
                                dialog.dismiss();
                            }
                            like.setEnabled(false);

                        }
                    }

                    @Override
                    public void onFailure(Call<Likes> call, Throwable t) {

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
//                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                        like.setEnabled(true);

                    }
                });
            }
        });
    }

    private void updateLike(final Likes likes, final ImageView like,final MyTextView_Lato_Regular likeCount,final int dislikedId,final ImageView dislike,final MyTextView_Lato_Regular dislikeId,final MyTextView_Lato_Regular dislikeCount,final MyTextView_Lato_Regular likedId) {

        likes.setLikeId(dislikedId);

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LikeAPI mapApi = Util.getClient().create(LikeAPI.class);
                Call<Likes> response = mapApi.updateLikes(dislikedId,likes);
                response.enqueue(new Callback<Likes>() {
                    @Override
                    public void onResponse(Call<Likes> call, Response<Likes> response) {

                        System.out.println(response.code());

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

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
                            }

                        }
                        else
                        {
                            if(dialog != null)
                            {
                                dialog.dismiss();
                            }
                            like.setEnabled(false);

                        }
                    }

                    @Override
                    public void onFailure(Call<Likes> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

//                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                        like.setEnabled(true);

                    }
                });
            }
        });
    }

    private void updatedisLike(final Likes likes, final ImageView like,final MyTextView_Lato_Regular likeCount,final int dislikedId,final ImageView dislike,final MyTextView_Lato_Regular dislikeId,final MyTextView_Lato_Regular dislikeCount,final MyTextView_Lato_Regular likedId) {

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();

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

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
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
                            }

                        }
                        else
                        {
                            like.setEnabled(false);
                            if(dialog != null)
                            {
                                dialog.dismiss();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<Likes> call, Throwable t) {

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
//                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                        like.setEnabled(true);

                    }
                });
            }
        });
    }


    private void postDislike(final Likes likes, final ImageView like,final MyTextView_Lato_Regular likeCount,final int dislikedId,final ImageView dislike,final MyTextView_Lato_Regular dislikeId,final MyTextView_Lato_Regular dislikeCount,final MyTextView_Lato_Regular likedId) {

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LikeAPI mapApi = Util.getClient().create(LikeAPI.class);
                Call<Likes> response = mapApi.postLikes(likes);
                response.enqueue(new Callback<Likes>() {
                    @Override
                    public void onResponse(Call<Likes> call, Response<Likes> response) {

                        System.out.println(response.code());
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                            if(dialog != null)
                            {
                                dialog.dismiss();
                            }
                            dislike.setImageResource(R.drawable.unliked_icons);
                            like.setImageResource(R.drawable.non_like);

                            dislikeId.setText(""+response.body().getLikeId());
                            likedId.setText("");
                            String likeText = dislikeCount.getText().toString();
                            if(likeText!=null&&!likeText.isEmpty()){

                                int count = Integer.parseInt(likeText);
                                dislikeCount.setText(""+(count+1));
                            }


                        }
                        else
                        {
                            if(dialog != null)
                            {
                                dialog.dismiss();
                            }
                            dislike.setEnabled(false);

                        }
                    }

                    @Override
                    public void onFailure(Call<Likes> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

//                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                        like.setEnabled(true);

                    }
                });
            }
        });
    }

    private void getFollowingByProfileId(final int id, final MyTextView_Lato_Regular button, final int contentProfileId){

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


                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {

                                for (UserProfile profile:responseProfile) {

                                    if(profile.getProfileId()==contentProfileId){

                                        button.setText("Following");
                                        button.setVisibility(View.GONE);
                                        button.setEnabled(false);
                                        break;
                                    }

                                }



                            }
                            else
                            {


                            }
                        }
                        else
                        {


                        }
//                callGetStartEnd();
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

    private void getFollowingsByProfileId(final int id, final MyTextView_Lato_Regular button, final int contentProfileId){

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

                                       // mappingId = profile.getFollowMapping().getFollowId();

                                        break;
                                    }

                                }

                                if(value){
                                    button.setVisibility(View.GONE);
                                    //mFollowOption.setText("Unfollow");

                                }else{
                                    button.setVisibility(View.VISIBLE);
                                    button.setText("Follow");
                                }



                            }
                            else
                            {
                                button.setVisibility(View.VISIBLE);
                                button.setText("Follow");

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

    private void profileFollow(final ProfileFollowMapping intrst, final MyTextView_Lato_Regular tv) {


         final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Following");
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

                            tv.setText("Following");
                            tv.setVisibility(View.GONE);
                            tv.setEnabled(true);
                            notifyDataSetChanged();

                        }
                        else
                        {
                            tv.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileFollowMapping> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
//                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                        tv.setEnabled(true);
                    }
                });
            }
        });
    }

    private class DownloadTask extends AsyncTask<URL,Void,Bitmap> {
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
                    String fileName = fileNames+ ".png";

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
                        mark(result).compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    fileName=file.getPath();

                    Uri bmpUri = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        bmpUri = FileProvider.getUriForFile(context, "tv.merabihar.app.merabihar.fileprovider", file);
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
                    shareIntent.putExtra(Intent.EXTRA_TEXT,shareContent);
                    shareIntent.setType("image/png");
                    try{

                        context.startActivity(shareIntent);

                    }catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                    }
                    //context.startActivity(Intent.createChooser(shareIntent,"Share with"));

                }catch (Exception we)
                {
                    we.printStackTrace();
                    Toast.makeText(context, "Unable to send,Check Permission", Toast.LENGTH_SHORT).show();
                }

            }else {
                // Notify user that an error occurred while downloading image
                //Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
            }
        }
    }

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
                    String fileName = fileNames+ ".png";

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
                        mark(result).compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    fileName=file.getPath();

                    Uri bmpUri = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        bmpUri = FileProvider.getUriForFile(context, "tv.merabihar.app.merabihar.fileprovider", file);
                    }else{
                        bmpUri = Uri.parse("file://"+fileName);
                    }
                    // Uri bmpUri = Uri.parse("file://"+path);
                    shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);

                    /*String sAux = "\n"+mBlogName.getText().toString()+"\n\n";
                    sAux = sAux + "to read more click here "+shortUrl+" \n\n"+"To get the latest update about Bihar Download our Bihar Tourism official mobile app by clicking goo.gl/rZfotV";*/
                    shareIntent.putExtra(Intent.EXTRA_TEXT,shareContent);
                    shareIntent.setType("image/png");
                   /* try{

                        context.startActivity(shareIntent);

                    }catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                    }*/
                    context.startActivity(Intent.createChooser(shareIntent,"Share with"));

                }catch (Exception we)
                {
                    we.printStackTrace();
                    Toast.makeText(context, "Unable to send,Check Permission", Toast.LENGTH_SHORT).show();
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

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.app_logo);
        int w = src.getWidth();
        int h = src.getHeight();
        int pw=w-w;
        int ph=h-h;
        int nw = (w * 10)/100;
        int nh = (h * 10)/100;
        Bitmap result = Bitmap.createBitmap(w, h, icon.getConfig());
        Canvas canvas = new Canvas(result);
        Bitmap resized = Bitmap.createScaledBitmap(icon, nw, nh, true);

        canvas.drawBitmap(src, 0, 0, null);
        Paint paint = new Paint();

        paint.setTextSize(30);
        paint.setAntiAlias(true);
        paint.setUnderlineText(false);
        canvas.drawBitmap(resized,pw,ph,paint);
        return result;
    }

    public Bitmap marks(Bitmap src) {

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.app_logo);
        Bitmap layoutSrc = BitmapFactory.decodeResource(context.getResources(),R.drawable.layout_canvas);
        int w = layoutSrc.getWidth();
        int h = layoutSrc.getHeight();
        int pw=w-w;
        int ph=h-h;
        int nw = (w * 10)/100;
        int nh = (h * 10)/100;
        Bitmap result = Bitmap.createBitmap(w, h, layoutSrc.getConfig());
        Canvas canvas = new Canvas(result);
        Bitmap resized = Bitmap.createScaledBitmap(icon, nw, nh, true);
        Bitmap resizeds = Bitmap.createScaledBitmap(src, w, h/2, true);

        // canvas.drawBitmap(src, 0, 0, null);
        canvas.drawBitmap(resizeds, 0, 0, null);
        canvas.drawBitmap(layoutSrc,0,0,null);
        Paint paint = new Paint();

        paint.setTextSize(15);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setUnderlineText(false);
        canvas.drawText("Content title", w+5, h+5, paint);
        canvas.drawBitmap(resizeds,pw,ph,paint);
        canvas.drawBitmap(resized,10,10,paint);

        return result;
    }

    private void shareApplication() {
        ApplicationInfo app = context.getApplicationInfo();
        String filePath = app.sourceDir;
        System.out.println("File path apk "+filePath);
        //System.out.println("File path apk "+context.getResources().);

        Intent intent = new Intent(Intent.ACTION_SEND);

        // MIME of .apk is "application/vnd.android.package-archive".
        // but Bluetooth does not accept this. Let's use "*/*" instead.
        intent.setType("*/*");

        // Append file and send Intent
        File originalApk = new File(filePath);

        try {
            //Make new directory in new location
            File tempFile = new File( Environment.getExternalStorageDirectory() + "/ExtractedApk");
            //If directory doesn't exists create new
            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;
            //Get application's name and convert to lowercase
            tempFile = new File(tempFile.getPath() + "/" + context.getString(app.labelRes).replace(" ","").toLowerCase() + ".apk");
            //If file doesn't exists create new
            if (!tempFile.exists()) {
                if (!tempFile.createNewFile()) {
                    return;
                }
            }
            //Copy file to new location
            InputStream in = new FileInputStream(originalApk);
            OutputStream out = new FileOutputStream(tempFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
            //Open share dialog
            Uri bmpUri = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                bmpUri = FileProvider.getUriForFile(context, "tv.merabihar.app.merabihar.fileprovider", tempFile);
            }else{
                bmpUri = Uri.parse("file://"+tempFile);
            }
            intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            context.startActivity(Intent.createChooser(intent, "Share app via"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
