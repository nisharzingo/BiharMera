package tv.merabihar.app.merabihar.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.commit451.youtubeextractor.YouTubeExtractor;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//import java.net.HttpURLConnection;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Lato_Regular;
import tv.merabihar.app.merabihar.CustomFonts.TextViewSFProDisplaySemibold;
import tv.merabihar.app.merabihar.CustomInterface.DownloadTaskVideo;
import tv.merabihar.app.merabihar.CustomInterface.OnBottomReachedListener;
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
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.HomeFragments.ForYouNewFragment;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ContentAPI;
import tv.merabihar.app.merabihar.WebAPI.LikeAPI;
import tv.merabihar.app.merabihar.WebAPI.ProfileAPI;
import tv.merabihar.app.merabihar.WebAPI.ProfileFollowAPI;

/**
 * Created by ZingoHotels Tech on 13-11-2018.
 */

public class ContentAdapterVertical  extends RecyclerView.Adapter  implements ActivityCompat.OnRequestPermissionsResultCallback {

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
    String shareContents = "Save time. Download Mera Bihar,The Only App for Bihar,To Read,Share your Stories and Earn Rs 1000\n\n http://bit.ly/2JXcOnw";

    OnBottomReachedListener onBottomReachedListener;

    public ContentAdapterVertical(Context context) {

        this.context = context;
        mList = new ArrayList<>();
        setHasStableIds(true);


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
        viewHolder = new BlogViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holders, final int pos) {

        final int profileId = PreferenceHandler.getInstance(context).getUserId();


        try{
            if(mList.get(pos)!=null){





                switch (getItemViewType(pos)) {

                    case ITEM:
                        final BlogViewHolder holder = (BlogViewHolder) holders;

                        SimpleDateFormat sdfs = new SimpleDateFormat("MMM dd,yyyy");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        final  Contents contents = mList.get(pos);

                        if(pos%2==0){

                            AdRequest adRequest = new AdRequest.Builder().build();
                            holder.mAdView.loadAd(adRequest);
                        }else{
                            holder.mAdView.setVisibility(View.GONE);
                        }


                        if(contents!=null){

                            holder.mProfilePhoto.setImageResource(R.drawable.profile_image);

                            String contentTitle =contents.getTitle();
                            final String contentDesc =contents.getDescription();
                            String createdBy =contents.getCreatedBy();
                            String createdDate =contents.getCreatedDate();

                            holder.mProfileName.setText(""+createdBy);
                            holder.mContentTitle.setText(""+contentTitle);
                            String total_views = contents.getViews();
                            if(total_views!=null){
                                int no  = Integer.parseInt(total_views);
                                if(no>=1000){
                                    Double newformat = (no*1.0)/1000  ;
                                    holder.mTotalWatchedPost.setText(String.format("%.1f", newformat) + "k");
                                }else {
                                    holder.mTotalWatchedPost.setText(total_views);
                                }
                            }else {

                                if(contents.getCreatedDate().contains(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))){
                                    holder.mTotalWatchedPost.setText("1");
                                }else{

                                    int max = 100;
                                    int min = 1;
                                    Random randomNum = new Random();
                                    int showMe = min + randomNum.nextInt(max);
                                    holder.mTotalWatchedPost.setText(showMe+"");


                                }

                            }




                            if(contents.getContentType().equalsIgnoreCase("Video")){

                                //url = contents.getContentURL();

                               // holder.mContentPic.setScaleType(ImageView.ScaleType.FIT_XY);
                                holder.mIcon.setVisibility(View.VISIBLE);
                                if(contents.getContentURL()!=null&&!contents.getContentURL().isEmpty()){
                                    String img = "https://img.youtube.com/vi/"+contents.getContentURL()+"/0.jpg";

                                    /*if(contents.getViews()==null){

                                        // We are updating like multiple times

                                        holder.mTotalWatchedPost.setText(1 + "");
                                        contents.setViews( 1 +"");

                                        updateContent(contents);
                                    }else{
                                        int total = Integer.parseInt(contents.getViews());
                                        holder.mTotalWatchedPost.setText(++total + "");

                                        contents.setViews(++total + "");
                                        updateContent(contents);
                                    }*/


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

                                        /*URL url = new URL(img);
                                        Bitmap loadedImage = BitmapFactory.decodeStream(url.openConnection().getInputStream());


                                        // Gets the width you want it to be
                                        int intendedWidth = holder.mContentPic.getWidth();

                                        // Gets the downloaded image dimensions
                                        int originalWidth = loadedImage.getWidth();
                                        int originalHeight = loadedImage.getHeight();

                                        // Calculates the new dimensions
                                        float scale = (float) intendedWidth / originalWidth;
                                        int newHeight = (int) Math.round(originalHeight * scale);

                                        // Resizes mImageView. Change "FrameLayout" to whatever layout mImageView is located in.
                                        holder.mContentPic.setLayoutParams(new FrameLayout.LayoutParams(
                                                FrameLayout.LayoutParams.WRAP_CONTENT,
                                                FrameLayout.LayoutParams.WRAP_CONTENT));
                                        holder.mContentPic.getLayoutParams().width = intendedWidth;
                                        holder.mContentPic.getLayoutParams().height = newHeight;*/

                                        if(img.contains(" ")){
                                            Picasso.with(context)
                                                    .load(img.replaceAll(" ","%20"))

                                                    .placeholder(R.drawable.no_image)
                                                    .error(R.drawable.no_image)
                                                    .into(holder.mContentPic);
                                        }else{
                                            Picasso.with(context)
                                                    .load(img)

                                                    .placeholder(R.drawable.no_image)
                                                    .error(R.drawable.no_image)
                                                    .into(holder.mContentPic);
                                        }


                                        holder.mContentPic.setScaleType(ImageView.ScaleType.CENTER_CROP);


                                    }else{
                                        holder.mContentPic.setImageResource(R.drawable.no_image);
                                    }



                                }else{

                                    getContents(contents.getContentId(),holder.mContentPic);

                                }

                            }

                            if(contentDesc!=null&&!contentDesc.isEmpty()){

                                holder.mContentDesc.setText(""+contentDesc);

                                int countLine= holder.mContentDesc.getLineHeight();
//                            System.out.println("LineCount " + countLine);
                                if (countLine>=25){
                                    holder.mReadOption.setVisibility(View.VISIBLE);
                                    holder.mMore.setVisibility(View.VISIBLE);
                                }else{
                                    holder.mReadOption.setVisibility(View.GONE);
                                }

                            }else{
                                holder.mContentDesc.setVisibility(View.GONE);
                                holder.mReadOption.setVisibility(View.GONE);
                            }

                            // holder.mProfileName.setText(""+createdBy);


                            holder.mMore.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    holder.mContentDesc.setMaxLines(5);
                                    holder.mContentDesc.setText(""+contentDesc);

                                    int countLine= holder.mContentDesc.getLineHeight();
//                                    System.out.println("LineCount " + countLine);
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
//                                    System.out.println("LineCount " + countLine);
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
                                    holder.mContentDesc.setMaxLines(1);

                                }
                            });
                            if(createdDate!=null||!createdDate.isEmpty()){

                                if(createdDate.contains("T")){
                                  /*  String date = createdDate.replace("T","");
                                    holder.mDuration.setText(""+duration(date));*/

                                    String date[] = createdDate.split("T");
                                    holder.mDuration.setText(""+date[0]+"        "+durationDate(date[0]));
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

                                    new CountDownTimer(300, 300) {
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

                                                    holder.mLikeLayout.setEnabled(false);
                                                    Likes likes = new Likes();
                                                    likes.setContentId(contents.getContentId());
                                                    likes.setProfileId(profileId);
                                                    likes.setLiked(true);

                                                    if (holder.mDislike.getDrawable().getConstantState() == context.getResources().getDrawable( R.drawable.unliked_icons).getConstantState())
                                                    {
                                                        if(holder.mDislikedId.getText().toString()!=null&&!holder.mDislikedId.getText().toString().isEmpty()){

                                                            updateLike(likes,holder.mLike,holder.mLikesCount,Integer.parseInt(holder.mDislikedId.getText().toString()),holder.mDislike,holder.mDislikedId,holder.mDislikesCount,holder.mLikedId,holder.mLikeLayout,holder.mDislikeLayout);
                                                            // updateLike(likes,holder.mLike,holder.mLikesCount,Integer.parseInt(holder.mDislikedId.getText().toString()),holder.mDislike,holder.mDislikedId,holder.mDislikesCount,holder.mLikedId);
                                                        }
                                                    }
                                                    else
                                                    {

                                                        postLike(likes,holder.mLike,holder.mLikesCount,0,holder.mDislike,holder.mDislikedId,holder.mDislikesCount,holder.mLikedId, holder.mLikeLayout,holder.mDislikeLayout);
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


                                                    /*fileNames = contents.getContentId()+""+contents.getProfileId();

                                                   downloadVideo(contents.getContentURL());*/
                                                    System.out.println("Content Url "+contents.getContentURL());


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

                            holder.mLikeLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (Util.isNetworkAvailable(context)) {

                                        if(profileId!=0){

                                            holder.mLikeLayout.setEnabled(false);
                                            Likes likes = new Likes();
                                            likes.setContentId(contents.getContentId());
                                            likes.setProfileId(profileId);
                                            likes.setLiked(true);

                                            if (holder.mDislike.getDrawable().getConstantState() == context.getResources().getDrawable( R.drawable.unliked_icons).getConstantState())
                                            {
                                                if(holder.mDislikedId.getText().toString()!=null&&!holder.mDislikedId.getText().toString().isEmpty()){

                                                    updateLike(likes,holder.mLike,holder.mLikesCount,Integer.parseInt(holder.mDislikedId.getText().toString()),holder.mDislike,holder.mDislikedId,holder.mDislikesCount,holder.mLikedId,holder.mLikeLayout,holder.mDislikeLayout);
                                                }
                                            }
                                            else
                                            {
                                                postLike(likes,holder.mLike,holder.mLikesCount,0,holder.mDislike,holder.mDislikedId,holder.mDislikesCount,holder.mLikedId,holder.mLikeLayout,holder.mDislikeLayout);
                                            }




                                       /* }else{

                                            postLike(likes,holder.mLike,holder.mLiked,holder.mLikeCount,holder.mLikedId);
                                        }*/

                                        }else {
                                            new AlertDialog.Builder(context)
                                                    .setMessage("Please login/Signup to Like the Story")
                                                    .setCancelable(true)
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

                                    }else{

                                        Toast.makeText(context, "No internet connection !", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                            holder.mDislikeLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (Util.isNetworkAvailable(context)) {

                                        if(profileId!=0){

                                            holder.mDislikeLayout.setEnabled(false);
                                            Likes likes = new Likes();
                                            likes.setContentId(contents.getContentId());
                                            likes.setProfileId(profileId);
                                            likes.setLiked(false);

                                            if (holder.mLike.getDrawable().getConstantState() == context.getResources().getDrawable( R.drawable.liked_icon).getConstantState())
                                            {
                                                if(holder.mLikedId.getText().toString()!=null&&!holder.mLikedId.getText().toString().isEmpty()){


                                                    updatedisLike(likes,holder.mDislike,holder.mDislikesCount,Integer.parseInt(holder.mLikedId.getText().toString()),holder.mLike,holder.mLikedId,holder.mLikesCount,holder.mDislikedId, holder.mDislikeLayout,holder.mLikeLayout);
                                                }
                                            }
                                            else
                                            {

                                                postDislike(likes,holder.mLike,holder.mLikesCount,0,holder.mDislike,holder.mDislikedId,holder.mDislikesCount,holder.mLikedId, holder.mDislikeLayout, holder.mLikeLayout);
                                            }

                                       /* }else{

                                            postLike(likes,holder.mLike,holder.mLiked,holder.mLikeCount,holder.mLikedId);
                                        }*/

                                        }else {
                                            new AlertDialog.Builder(context)
                                                    .setMessage("Please login/Signup to Like the Story")
                                                    .setCancelable(true)
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

                                    }else{

                                        Toast.makeText(context, "No internet connection !", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                            holder.mFollow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    // check whether internet is available
//                                    mParentRelativeLayout
                                    if (Util.isNetworkAvailable(context)) {

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
                                                    .setCancelable(true)
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

                                    }else{

                                        Toast.makeText(context, "No internet connection !", Toast.LENGTH_SHORT).show();
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

                                        if(contents.getContentImage()!=null&&contents.getContentImage().size()!=0){
                                            mMyTask = new DownloadTask()
                                                    .execute(stringToURL(
                                                            ""+contents.getContentImage().get(0).getImages()
                                                    ));
                                        }else{

                                            Toast.makeText(context, "No Image Resources", Toast.LENGTH_SHORT).show();
                                        }


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


                            holder.mCommentLayout.setOnClickListener(new View.OnClickListener() {
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
            //System.out.println("inside getItemViewType LOADING = "+position);
            return LOADING;
        }
        else
        {
            //System.out.println("inside getItemViewType ITEM = "+position);
            return ITEM;
        }
        //return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }


    public class BlogViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mProfilePhoto;
        MyTextView_Lato_Regular mProfileName,mDuration,mFollow,mContentTitle,mContentDesc
                ,mCommentsCount,mLikesCount,mDislikesCount,mLikedId,mDislikedId, mWhatsappShareCount;
        TextViewSFProDisplaySemibold mTags;
        RoundedImageView mContentPic;
        ImageView mIcon;
        LinearLayout mProfileContent,mReadOption;
        FrameLayout mContentDetail;
        TextView mMore,mLess,mMoreExtnd,mMoreLine,mTotalWatchedPost;

        ImageView mMoreShare,mLike,mDislike,mComment;

        LinearLayout mWhatsapp,mDownLoad,mShare, mLikeLayout, mDislikeLayout, mCommentLayout ;


        AdView mAdView ;
        public BlogViewHolder(View view) {
            super(view);

            mAdView = (AdView)view.findViewById(R.id.adView_recycler);
          //  mAdClose = (TextView)view.findViewById(R.id.ad_close);
            mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_photo);
            mProfileName = (MyTextView_Lato_Regular) view.findViewById(R.id.profile_name);
            mDuration = (MyTextView_Lato_Regular) view.findViewById(R.id.duration);
            mFollow = (MyTextView_Lato_Regular) view.findViewById(R.id.follow_profile);
            mContentTitle = (MyTextView_Lato_Regular) view.findViewById(R.id.content_title);
            mContentDesc = (MyTextView_Lato_Regular) view.findViewById(R.id.content_desc);
            mCommentsCount = (MyTextView_Lato_Regular) view.findViewById(R.id.comments_count);
            mWhatsappShareCount = (MyTextView_Lato_Regular) view.findViewById(R.id.whatsapp_share_count);
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
            mTotalWatchedPost = (TextView) view.findViewById(R.id.total_watched_post);
            mContentDetail = (FrameLayout) view.findViewById(R.id.content_detail);
            mProfileContent = (LinearLayout) view.findViewById(R.id.profile_lay_content);
            mReadOption = (LinearLayout) view.findViewById(R.id.read_option);
            mLikeLayout = (LinearLayout) view.findViewById(R.id.like_ll);
            mDislikeLayout = (LinearLayout) view.findViewById(R.id.disLike_ll);
            mCommentLayout = (LinearLayout) view.findViewById(R.id.comment_ll);
            mLike = (ImageView) view.findViewById(R.id.likes_image);
            mDislike = (ImageView) view.findViewById(R.id.unlikes_image);
            mComment = (ImageView) view.findViewById(R.id.comments_image);
            mWhatsapp = (LinearLayout) view.findViewById(R.id.whatsapp_share);
            mShare = (LinearLayout) view.findViewById(R.id.share_image);
            mMoreShare = (ImageView) view.findViewById(R.id.more_icons);
            mDownLoad = (LinearLayout) view.findViewById(R.id.download_screen);

            //mAdView = view.findViewById(R.id.adView);




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
        notifyItemInserted(mList.size() - 1);
    }

    public void addAll(List<Contents> mcList) {
        for (Contents mc : mcList) {
            add(mc);
        }
    }

    public void adds(Contents mc) {
        //System.out.println("BookingAndTraveller = "+mc.getRoomBooking().getTotalAmount());
        mList.add(0,mc);
        //System.out.println("BookingAndTraveller = "+list.size());
        notifyItemInserted(0);
    }

    public void addAlls(ArrayList<Contents> mcList) {
        for (Contents mc : mcList) {
            adds(mc);
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
                            }else{
                                cv.setImageResource(R.drawable.profile_image);
                            }




                        }else{
                            cv.setImageResource(R.drawable.profile_image);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {
                        cv.setImageResource(R.drawable.profile_image);
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

    private void postLike(final Likes likes, final ImageView like,final MyTextView_Lato_Regular likeCount,final int dislikedId,final ImageView dislike,final MyTextView_Lato_Regular dislikeId,final MyTextView_Lato_Regular dislikeCount,final MyTextView_Lato_Regular likedId,final LinearLayout likeLyout,final LinearLayout dislikeLayout) {

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

                            like.setImageResource(R.drawable.liked_icon);
                            dislike.setImageResource(R.drawable.unlike_icons);
                            likedId.setText(""+response.body().getLikeId());
                            dislikeId.setText("");
                            String likeText = likeCount.getText().toString();
                            if(likeText!=null&&!likeText.isEmpty()){

                                int count = Integer.parseInt(likeText);
                                likeCount.setText(""+(count+1));
                            }

                            dislikeLayout.setEnabled(true);

                        }
                        else
                        {
                            likeLyout.setEnabled(true);

                        }
                    }

                    @Override
                    public void onFailure(Call<Likes> call, Throwable t) {

//                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                        likeLyout.setEnabled(true);

                    }
                });
            }
        });
    }

    private void updateLike(final Likes likes, final ImageView like, final MyTextView_Lato_Regular likeCount, final int dislikedId, final ImageView dislike, final MyTextView_Lato_Regular dislikeId, final MyTextView_Lato_Regular dislikeCount, final MyTextView_Lato_Regular likedId, final LinearLayout likeLayout, final LinearLayout mDislikeLayout) {

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

                            mDislikeLayout.setEnabled(true);

                        }
                        else
                        {

                            likeLayout.setEnabled(true);

                        }
                    }

                    @Override
                    public void onFailure(Call<Likes> call, Throwable t) {

//                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                        likeLayout.setEnabled(true);

                    }
                });
            }
        });
    }

    private void updatedisLike(final Likes likes, final ImageView like, final MyTextView_Lato_Regular likeCount, final int dislikedId, final ImageView dislike, final MyTextView_Lato_Regular dislikeId, final MyTextView_Lato_Regular dislikeCount, final MyTextView_Lato_Regular likedId, final LinearLayout dislikeLayout, final LinearLayout mLikeLayout) {

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
                            }

                            mLikeLayout.setEnabled(true);

                        }
                        else
                        {
                            dislikeLayout.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<Likes> call, Throwable t) {
//                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                        dislikeLayout.setEnabled(true);

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

                        System.out.println(response.code());

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
//                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                        mDislikeLayout.setEnabled(true);

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


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ProfileFollowAPI mapApi = Util.getClient().create(ProfileFollowAPI.class);
                Call<ProfileFollowMapping> response = mapApi.postInterest(intrst);
                response.enqueue(new Callback<ProfileFollowMapping>() {
                    @Override
                    public void onResponse(Call<ProfileFollowMapping> call, Response<ProfileFollowMapping> response) {

                        System.out.println(response.code());

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
//                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                        tv.setEnabled(true);
                    }
                });
            }
        });
    }

    private class DownloadTask extends AsyncTask<URL,Void,Bitmap> {
        ProgressDialog progressDialog = new ProgressDialog(context);
        // Before the tasks execution
        protected void onPreExecute(){
            // Display the progress dialog on async task start
//            final ProgressDialog progressDialog = new ProgressDialog(context);
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
                //connection.
            }
            return null;
        }




        // When all async task done
        protected void onPostExecute(Bitmap result){
            // Hide the progress dialog
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

                    if(PreferenceHandler.getInstance(context).getUserId()!=0){
                        shareIntent.putExtra(Intent.EXTRA_TEXT,shareContent);
                    }else{
                        shareIntent.putExtra(Intent.EXTRA_TEXT,shareContents);
                    }

                    shareIntent.setPackage("com.whatsapp");
                    shareIntent.setType("image/*");
                    try{

                        context.startActivity(shareIntent);

                    }catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                    }

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
                        if(PreferenceHandler.getInstance(context).getUserId()!=0){
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
                        context.startActivity(Intent.createChooser(shareIntent,"Share with"));

                    }catch (Exception wes)
                    {
                        wes.printStackTrace();
                        Toast.makeText(context, ""+we.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }



            }else {
                // Notify user that an error occurred while downloading image
                //Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();

            }
            progressDialog.dismiss();

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
                    if(PreferenceHandler.getInstance(context).getUserId()!=0){
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

    private class DownloadImage extends AsyncTask<URL,Void,Bitmap> {
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


                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);


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


                try{



                    File sd = Environment.getExternalStorageDirectory();
                    String fileName = fileNames+ ".jpg";

                    File directory = new File(sd.getAbsolutePath()+"/MeraBihar App/Download/Images");
                    //create directory if not exist
                    if (!directory.exists() && !directory.isDirectory()) {
                        directory.mkdirs();
                    }


                    File file = new File(directory, fileName);

                    //if(file.exists())




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
                        bmpUri = FileProvider.getUriForFile(context, "tv.merabihar.app.merabihar.fileprovider", file);
                    }else{
                        bmpUri = Uri.parse("file://"+fileName);
                    }

                    Toast.makeText(context, "Image Downloaded", Toast.LENGTH_SHORT).show();

                }catch (Exception we)
                {
                    we.printStackTrace();
                    Toast.makeText(context, "Unable to send,Check Permission", Toast.LENGTH_SHORT).show();
                }

            }else {
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

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.logo_mbtv);
        int w = src.getWidth();
        int h = src.getHeight();
        int pw=w-w;
        int ph=h-h;
        int nw = (w * 10)/100;
        int nh = (h * 10)/100;
        Bitmap result = Bitmap.createBitmap(w, h, icon.getConfig());
        Canvas canvas = new Canvas(result);
        Bitmap resized = Bitmap.createScaledBitmap(icon, 40, 40, true);

        canvas.drawBitmap(src, 0, 0, null);
        Paint paint = new Paint();

        paint.setTextSize(30);
        paint.setAntiAlias(true);
        paint.setUnderlineText(false);
        canvas.drawBitmap(resized,pw,ph,paint);
        return result;
    }

    public Bitmap marks(Bitmap src) {

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.logo_mbtv);
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


    public void downloadVideo(final String YOUTUBE_ID){



        final YouTubeExtractor mExtractor = YouTubeExtractor.create();

        //mExtractor.extract(YOUTUBE_ID).enqueue(mExtractionCallback);

       /* new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                YouTubeExtractor mExtractor = YouTubeExtractor.create();

                mExtractor.extract(YOUTUBE_ID).enqueue(new Callback<YouTubeExtractionResult>() {
                    @Override
                    public void onResponse(Call<YouTubeExtractionResult> call, Response<YouTubeExtractionResult> response) {
                        bindVideoResult(response.body());
                    }

                    @Override
                    public void onFailure(Call<YouTubeExtractionResult> call, Throwable t) {
                        onError(t);
                    }
                });
            }
        });*/


        String youtubeLink = "http://youtube.com/watch?v="+YOUTUBE_ID;

        YouTubeUriExtractor ytEx = new YouTubeUriExtractor(context) {
            @Override
            public void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> ytFiles) {
                if (ytFiles != null) {
                    int itag = 22;

                    List<Integer> iTags = Arrays.asList(22, 137, 18);

                    for (Integer iTag : iTags) {

                        YtFile ytFile = ytFiles.get(iTag);

                        if (ytFile != null) {

                            String downloadUrl = ytFile.getUrl();

                            if (downloadUrl != null && !downloadUrl.isEmpty()) {

                                File sd = Environment.getExternalStorageDirectory();
                                String fileName = fileNames+ ".mp4";

                                File directory = new File(sd.getAbsolutePath()+"/MeraBihar App/Download/Video/");
                                //create directory if not exist
                                if (!directory.exists() && !directory.isDirectory()) {
                                    directory.mkdirs();
                                }


                                File file = new File(directory, fileName);
                                System.out.println("Download url "+downloadUrl);
                                new DownloadTaskVideo( fileNames, downloadUrl);


                                break;

                            }

                        }

                    }
// Here you can get download url





                }
            }
        };

        ytEx.execute(youtubeLink);


    }

    private static void downloadFile(String url, File outputFile) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();
        } catch(FileNotFoundException e) {
            return; // swallow a 404
        } catch (IOException e) {
            return; // swallow a 404
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }



    private void requestPermission() {

        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

    }

    public void getContents(final int id,final ImageView iv)
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ContentAPI categoryAPI = Util.getClient().create(ContentAPI.class);
                Call<Contents> getCat = categoryAPI.getContentsById(id);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<Contents>() {

                    @Override
                    public void onResponse(Call<Contents> call, Response<Contents> response) {



                        if(response.code() == 200)
                        {

                            Contents contents = response.body();

                            if(contents != null )
                            {

                                if(contents.getContentImage()!=null&&contents.getContentImage().size()!=0){

                                    ArrayList<ContentImages> blogImages = contents.getContentImage();


                                    if (blogImages != null && blogImages.size() != 0) {



                                        String img = contents.getContentImage().get(0).getImages();

                                        if(img!=null&&!img.isEmpty()){

                                        /*URL url = new URL(img);
                                        Bitmap loadedImage = BitmapFactory.decodeStream(url.openConnection().getInputStream());


                                        // Gets the width you want it to be
                                        int intendedWidth = holder.mContentPic.getWidth();

                                        // Gets the downloaded image dimensions
                                        int originalWidth = loadedImage.getWidth();
                                        int originalHeight = loadedImage.getHeight();

                                        // Calculates the new dimensions
                                        float scale = (float) intendedWidth / originalWidth;
                                        int newHeight = (int) Math.round(originalHeight * scale);

                                        // Resizes mImageView. Change "FrameLayout" to whatever layout mImageView is located in.
                                        holder.mContentPic.setLayoutParams(new FrameLayout.LayoutParams(
                                                FrameLayout.LayoutParams.WRAP_CONTENT,
                                                FrameLayout.LayoutParams.WRAP_CONTENT));
                                        holder.mContentPic.getLayoutParams().width = intendedWidth;
                                        holder.mContentPic.getLayoutParams().height = newHeight;*/

                                            if(img.contains(" ")){
                                                Picasso.with(context)
                                                        .load(img.replaceAll(" ","%20"))

                                                        .placeholder(R.drawable.no_image)
                                                        .error(R.drawable.no_image)
                                                        .into(iv);
                                            }else{
                                                Picasso.with(context)
                                                        .load(img)

                                                        .placeholder(R.drawable.no_image)
                                                        .error(R.drawable.no_image)
                                                        .into(iv);
                                            }


                                            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);


                                        }else{
                                            iv.setImageResource(R.drawable.no_image);
                                        }





                                    }


                                }



                            }
                            else
                            {


                            }
                        }else{

                        }
                    }

                    @Override
                    public void onFailure(Call<Contents> call, Throwable t) {




//                        Toast.makeText(CommentsScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
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

    public String durationDate(String notifyDate){
        //2018-08-28T00:00:00
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
        Date fd = null,td = null;
        long diffDays=0,diffMinutes=0,diffHours=0 ;
        String duration = "" ;
        try {
            if(notifyDate.contains("-")){
                fd = sdf.parse(notifyDate);
            }


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
        }else if(diffDays==0){
            duration = "Today";
        }

        return duration;
    }



}
