package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexzh.circleimageview.CircleImageView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Lato_Regular;
import tv.merabihar.app.merabihar.CustomFonts.TextViewSFProDisplaySemibold;
import tv.merabihar.app.merabihar.Model.CategoryAndContentList;
import tv.merabihar.app.merabihar.Model.ContentImages;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ProfileAPI;

/**
 * Created by ZingoHotels Tech on 31-10-2018.
 */

public class ContentRecyclerAdapter extends RecyclerView.Adapter  {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private boolean isLoadingAdded = false;
    private Context context;
    private ArrayList<Contents> mList;

    int currentPage = 0,start = 0,end = 0;
    Timer timer;
    final long DELAY_MS = 5000;
    final long PERIOD_MS = 3500;

    public ContentRecyclerAdapter(Context context) {

        this.context = context;
        mList = new ArrayList<>();


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

                        Contents contents = mList.get(pos);

                        if(contents!=null){

                            String contentTitle =contents.getTitle();
                            String contentDesc =contents.getDescription();
                            String createdBy =contents.getCreatedBy();
                            String createdDate =contents.getCreatedDate();

                            holder.mProfileName.setText(""+createdBy);
                            holder.mContentTitle.setText(""+contentTitle);
                            holder.mContentDesc.setText(""+contentDesc);
                           // holder.mProfileName.setText(""+createdBy);

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
                                    //getFollowingByProfileId(profileId,holder.mFollowing,contents.getProfileId());

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
                                    }
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
        MyTextView_Lato_Regular mProfileName,mDuration,mFollow,mContentTitle,mContentDesc;
        TextViewSFProDisplaySemibold mTags;
        RoundedImageView mContentPic;
        ImageView mIcon;

        public ContentViewHolder(View view) {
            super(view);

            mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_photo);
            mProfileName = (MyTextView_Lato_Regular) view.findViewById(R.id.profile_name);
            mDuration = (MyTextView_Lato_Regular) view.findViewById(R.id.duration);
            mFollow = (MyTextView_Lato_Regular) view.findViewById(R.id.follow_profile);
            mContentTitle = (MyTextView_Lato_Regular) view.findViewById(R.id.content_title);
            mContentDesc = (MyTextView_Lato_Regular) view.findViewById(R.id.content_desc);
            mTags = (TextViewSFProDisplaySemibold) view.findViewById(R.id.content_tags);
            mContentPic = (RoundedImageView) view.findViewById(R.id.content_image);
            mIcon = (ImageView) view.findViewById(R.id.youtube_icon);


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


}
