package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.alexzh.circleimageview.CircleImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Lato_Regular;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Model.InterestContentMapping;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.InterestContentListScreen;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ProfileAPI;

/**
 * Created by ZingoHotels Tech on 01-11-2018.
 */

public class TrendingIntrestAdapter extends RecyclerView.Adapter<TrendingIntrestAdapter.ViewHolder> {
    private Context context;
    private ArrayList<InterestContentMapping> list;
    public TrendingIntrestAdapter(Context context,ArrayList<InterestContentMapping> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_interest_trending, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final InterestContentMapping content = list.get(position);

        if(content!=null){

            if(content.getZingoInterst()!=null){

                holder.mInterestName.setText(""+content.getZingoInterst().getInterestName());
            }

            if(content.getContent()!=null){

                Contents contents = content.getContent();
                if(contents.getContentType().equalsIgnoreCase("Video")){

                    //url = contents.getContentURL();

                    //holder.mIcon.setVisibility(View.VISIBLE);
                    if(contents.getContentURL()!=null&&!contents.getContentURL().isEmpty()){
                        String img = "https://img.youtube.com/vi/"+contents.getContentURL()+"/0.jpg";

                        if(img!=null&&!img.isEmpty()){
                            // crop the image & load
//                            loadCroppedImage(img,holder.mContentImage);

                            Glide.with(context)
                                    .load(img)
                                    .apply(new RequestOptions()
                                            .placeholder(R.drawable.no_image)
                                            .error(R.drawable.no_image))
                                    .into(holder.mContentImage);


                        }
                    }




                }else{


                    if(contents.getContentImage() != null && contents.getContentImage().size()!=0)
                    {

                        String img = contents.getContentImage().get(0).getImages();

                        if(img!=null&&!img.isEmpty()){
                            Picasso.with(context).load(img).placeholder(R.drawable.no_image).
                                    error(R.drawable.no_image).into(holder.mContentImage);

                        }else{
                            holder.mContentImage.setImageResource(R.drawable.no_image);
                        }



                    }else{
                        holder.mContentImage.setImageResource(R.drawable.no_image);
                    }

                }
            }

            holder.mInterestLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, InterestContentListScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Interest",content.getZingoInterst());
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }
            });



        }

    }

    @Override
    public int getItemCount() {

        return list.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder  {

        RoundedImageView mContentImage;
        MyTextView_Lato_Regular mInterestName;
        LinearLayout mInterestLay;





        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            mInterestName = (MyTextView_Lato_Regular) itemView.findViewById(R.id.interest_name);
            mContentImage = (RoundedImageView) itemView.findViewById(R.id.content_image);
            mInterestLay = (LinearLayout) itemView.findViewById(R.id.collection_lay);


        }

    }


    // loading the cropped image from youtube
    private void loadCroppedImage(String urlString, final ImageView imageView) {
        Picasso.with(context).load(urlString).into(new com.squareup.picasso.Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                // Cropping the image
                Bitmap customBitMap =  Bitmap.createBitmap(bitmap, 0, 45, bitmap.getWidth(), bitmap.getHeight()-90);

                Glide.with(context)
                        .load(customBitMap)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.no_image)
                                .error(R.drawable.no_image))
                        .into(imageView);

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }



}
