package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexzh.circleimageview.CircleImageView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Lato_Regular;
import tv.merabihar.app.merabihar.CustomFonts.TextViewSFProDisplaySemibold;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.ContentDetailScreen;
import tv.merabihar.app.merabihar.UI.Activity.ContentImageDetailScreen;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ProfileAPI;

/**
 * Created by ZingoHotels Tech on 31-10-2018.
 */

public class ContentRecyclerHorizontal extends RecyclerView.Adapter<ContentRecyclerHorizontal.ViewHolder> {
    private Context context;
    private ArrayList<Contents> list;
    public ContentRecyclerHorizontal(Context context,ArrayList<Contents> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_content_list_design, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Contents contents = list.get(position);

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


                //holder.mIcon.setVisibility(View.VISIBLE);
                if(contents.getContentURL()!=null&&!contents.getContentURL().isEmpty()){
                    String img = "https://img.youtube.com/vi/"+contents.getContentURL()+"/0.jpg";
                    if(img!=null&&!img.isEmpty()){
                        Picasso.with(context).load(img).placeholder(R.drawable.no_image).
                                error(R.drawable.no_image).into(holder.mContentPic);
                    }
                }





            }else{


                //holder.mIcon.setVisibility(View.GONE);
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


            holder.mContentDetail.setOnClickListener(new View.OnClickListener() {
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

        }

    }

    @Override
    public int getItemCount() {

        return list.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder  {


        CircleImageView mProfilePhoto;
        MyTextView_Lato_Regular mProfileName,mDuration,mContentTitle,mContentDesc;

        ImageView mContentPic;
        LinearLayout mContentDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            mProfilePhoto = (CircleImageView) itemView.findViewById(R.id.profile_photo_hor);
            mProfileName = (MyTextView_Lato_Regular) itemView.findViewById(R.id.profile_name_horz);
            mDuration = (MyTextView_Lato_Regular) itemView.findViewById(R.id.duration_horz);
            mContentTitle = (MyTextView_Lato_Regular) itemView.findViewById(R.id.content_title_horz);
            mContentDesc = (MyTextView_Lato_Regular) itemView.findViewById(R.id.content_desc_horz);
            mContentPic = (ImageView) itemView.findViewById(R.id.content_pic_horz);
            mContentDetail = (LinearLayout) itemView.findViewById(R.id.content_detail);

        }


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


}
