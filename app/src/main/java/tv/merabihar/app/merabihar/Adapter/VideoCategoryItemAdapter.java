package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.ContentDetailScreen;

public class VideoCategoryItemAdapter extends RecyclerView.Adapter<VideoCategoryItemAdapter.MyViewHolder> {

    Context context;
    ArrayList<Contents> mContentsList; /* Contents  Type*/
    MKLoader mCustomLoader;


    public VideoCategoryItemAdapter(Context context , ArrayList<Contents> mContentsList)
    {
        this.context = context;
        this.mContentsList = mContentsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.video_category_style,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final  Contents mContent =  mContentsList.get(position);
        ImageView content_poster = holder.content_poster;
        mCustomLoader = holder.customLoader;

        // load image from api
        String urlString = "https://img.youtube.com/vi/"+mContent.getContentURL()+"/0.jpg";
        loadCroppedImage(urlString, content_poster);

        /*When clicked on the content image*/
        content_poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context, position + "", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, ContentDetailScreen.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Contents",mContent);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }


    // loading the cropped image from youtube
    private void loadCroppedImage(String urlString, final ImageView content_poster) {
        Picasso.with(context).load(urlString).into(new com.squareup.picasso.Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                // Cropping the image
                Bitmap customBitMap =  Bitmap.createBitmap(bitmap, 0, 45, bitmap.getWidth(), bitmap.getHeight()-90);
                Glide.with(context).load(customBitMap).into(content_poster);
//                mCustomLoader.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
               // Log.e("Cropping Failed", errorDrawable.toString());
//                mCustomLoader.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mContentsList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView content_poster;
        MKLoader customLoader;

        MyViewHolder(View itemView) {
            super(itemView);
            content_poster = itemView.findViewById(R.id.cat_content_poster);
            customLoader = itemView.findViewById(R.id.custom_loader_content);
//            customLoader.setVisibility(View.VISIBLE);
        }

    }

}