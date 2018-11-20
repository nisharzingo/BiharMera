package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.ContentDetailScreen;
import tv.merabihar.app.merabihar.UI.Activity.ContentImageDetailScreen;

public class FollowFragmentContentAdapter extends RecyclerView.Adapter<FollowFragmentContentAdapter.MyViewHolder> {

    public int targetWidth = 300;
    public int targetHeight = 300;

    Context context;
    ArrayList<ArrayList<Contents>> mContentList;
    View view;

    public FollowFragmentContentAdapter(Context context , ArrayList<ArrayList<Contents>> mContentList)
    {
        this.context = context;
        this.mContentList = mContentList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_follow_content_single_layout,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final ArrayList<Contents> content = mContentList.get(position);

       /* FrameLayout.LayoutParams paramsMsg = new FrameLayout.
                LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.START);*/
        if(position%2==0){
            view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        }else{

            view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        final ImageView iv1 = holder.iv1 ;
        ImageView iv2 = holder.iv2 ;
        ImageView iv3 = holder.iv3 ;  // large imageview
        ImageView iv4 = holder.iv4 ;
        ImageView iv5 = holder.iv5 ;
        ImageView iv6 = holder.iv6 ;
        ImageView iv7 = holder.iv7 ;
        ImageView iv8 = holder.iv8 ;
        ImageView iv9 = holder.iv9 ;


        if(content!=null&&content.size()!=0&&content.size()==9){


            if(content.get(0).getContentType().equalsIgnoreCase("Video")){

                holder.ivp1.setVisibility(View.VISIBLE);

                if(content.get(0).getContentURL()!=null&&!content.get(0).getContentURL().isEmpty()){
                    final String img = "https://img.youtube.com/vi/"+content.get(0).getContentURL()+"/0.jpg";
                    if(img!=null&&!img.isEmpty()){

                        Picasso.with(context).load(img).resize(targetWidth,targetHeight).placeholder(R.drawable.no_image).
                                error(R.drawable.no_image).into(iv1);

/*                        Picasso.with(context).load(img).into(new com.squareup.picasso.Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                // Cropping the image
                                Bitmap customBitMap =  Bitmap.createBitmap(bitmap, 0, 45, bitmap.getWidth(), bitmap.getHeight()-90);

                                Glide.with(context)
                                        .load(customBitMap)
                                        .apply(new RequestOptions()
                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image))
                                        .into(holder.iv1);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                Picasso.with(context).load(img).placeholder(R.drawable.no_image).
                                        error(R.drawable.no_image).into(iv1);

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });*/

                    }else{
                        holder.iv1.setImageResource(R.drawable.no_image);
                    }
                }

            }else{
                if(content.get(0).getContentImage()!=null&&content.get(0).getContentImage().size()!=0){

                    String urlString1 = content.get(0).getContentImage().get(0).getImages();

                    if(urlString1!=null){
                        Picasso.with(context)
                                .load(Uri.parse(urlString1))
                                .resize(targetWidth,targetHeight)
                                .error(R.drawable.no_image)
                                .placeholder(R.drawable.no_image)
                                .into(holder.iv1);
//                        iv1.setImageURI(Uri.parse(urlString1));
                    }

                }
            }


            if(content.get(1).getContentType().equalsIgnoreCase("Video")){

                holder.ivp2.setVisibility(View.VISIBLE);

                if(content.get(1).getContentURL()!=null&&!content.get(1).getContentURL().isEmpty()){
                    final String img = "https://img.youtube.com/vi/"+content.get(1).getContentURL()+"/0.jpg";
                    if(img!=null&&!img.isEmpty()){

                       Picasso.with(context).load(img).resize(targetWidth,targetHeight).placeholder(R.drawable.no_image).
                                error(R.drawable.no_image).into(holder.iv2);
/*
                        Picasso.with(context).load(img).into(new com.squareup.picasso.Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                // Cropping the image
                                Bitmap customBitMap =  Bitmap.createBitmap(bitmap, 0, 45, bitmap.getWidth(), bitmap.getHeight()-90);

                                Glide.with(context)
                                        .load(customBitMap)
                                        .apply(new RequestOptions()
                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image))
                                        .into(holder.iv2);

                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                                Picasso.with(context).load(img).placeholder(R.drawable.no_image).
                                        error(R.drawable.no_image).into(holder.iv2);


                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });*/

                    }else{
                        holder.iv2.setImageResource(R.drawable.no_image);
                    }
                }

            }else{
                if(content.get(1).getContentImage()!=null&&content.get(1).getContentImage().size()!=0){

                    String urlString1 = content.get(1).getContentImage().get(0).getImages();

                    if(urlString1!=null){

                        Picasso.with(context).load(urlString1).resize(targetWidth,targetHeight).placeholder(R.drawable.no_image).
                                error(R.drawable.no_image).into(holder.iv2);

                    }

                }
            }


            if(content.get(2).getContentType().equalsIgnoreCase("Video")){

                holder.ivp3.setVisibility(View.VISIBLE);

                if(content.get(2).getContentURL()!=null&&!content.get(2).getContentURL().isEmpty()){
                    final String img = "https://img.youtube.com/vi/"+content.get(2).getContentURL()+"/0.jpg";
                    if(img!=null&&!img.isEmpty()){

                        Picasso.with(context).load(img).resize(targetWidth,targetHeight).placeholder(R.drawable.no_image).
                                error(R.drawable.no_image).into(holder.iv3);

                        /*Picasso.with(context).load(img).into(new com.squareup.picasso.Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                // Cropping the image
                                Bitmap customBitMap =  Bitmap.createBitmap(bitmap, 0, 45, bitmap.getWidth(), bitmap.getHeight()-90);

                                Glide.with(context)
                                        .load(customBitMap)
                                        .apply(new RequestOptions()
                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image))
                                        .into(holder.iv3);


                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                                Picasso.with(context).load(img).placeholder(R.drawable.no_image).
                                        error(R.drawable.no_image).into(holder.iv3);


                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });*/


                    }else{
                        holder.iv3.setImageResource(R.drawable.no_image);
                    }
                }

            }else{
                if(content.get(2).getContentImage()!=null&&content.get(2).getContentImage().size()!=0){

                    String urlString1 = content.get(2).getContentImage().get(0).getImages();

                    if(urlString1!=null){
                        Picasso.with(context).load(urlString1).resize(targetWidth,targetHeight).placeholder(R.drawable.no_image).
                                error(R.drawable.no_image).into(holder.iv3);
                    }

                }
            }


            if(content.get(3).getContentType().equalsIgnoreCase("Video")){

                holder.ivp4.setVisibility(View.VISIBLE);

                if(content.get(3).getContentURL()!=null&&!content.get(3).getContentURL().isEmpty()){
                    final String img = "https://img.youtube.com/vi/"+content.get(3).getContentURL()+"/0.jpg";
                    if(img!=null&&!img.isEmpty()){

                        Picasso.with(context).load(img).resize(targetWidth,targetHeight).placeholder(R.drawable.no_image).
                                error(R.drawable.no_image).into(holder.iv4);
/*
                        Picasso.with(context).load(img).into(new com.squareup.picasso.Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                // Cropping the image
                                Bitmap customBitMap =  Bitmap.createBitmap(bitmap, 0, 45, bitmap.getWidth(), bitmap.getHeight()-90);

                                Glide.with(context)
                                        .load(customBitMap)
                                        .apply(new RequestOptions()
                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image))
                                        .into(holder.iv4);

//                mCustomLoader.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                Picasso.with(context).load(img).placeholder(R.drawable.no_image).
                                        error(R.drawable.no_image).into(holder.iv4);

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });*/

                    }else{
                        holder.iv4.setImageResource(R.drawable.no_image);
                    }
                }

            }else{
                if(content.get(3).getContentImage()!=null&&content.get(3).getContentImage().size()!=0){

                    String urlString1 = content.get(3).getContentImage().get(0).getImages();

                    if(urlString1!=null){
                        Picasso.with(context).load(urlString1).resize(targetWidth,targetHeight).placeholder(R.drawable.no_image).
                                error(R.drawable.no_image).into(holder.iv4);
                    }

                }
            }


            if(content.get(4).getContentType().equalsIgnoreCase("Video")){

                holder.ivp5.setVisibility(View.VISIBLE);

                if(content.get(4).getContentURL()!=null&&!content.get(4).getContentURL().isEmpty()){
                    final String img = "https://img.youtube.com/vi/"+content.get(4).getContentURL()+"/0.jpg";
                    if(img!=null&&!img.isEmpty()){


                        Picasso.with(context).load(img).resize(targetWidth,targetHeight).placeholder(R.drawable.no_image).
                                error(R.drawable.no_image).into(holder.iv5);
/*
                        Picasso.with(context).load(img).into(new com.squareup.picasso.Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                // Cropping the image
                                Bitmap customBitMap =  Bitmap.createBitmap(bitmap, 0, 45, bitmap.getWidth(), bitmap.getHeight()-90);

                                Glide.with(context)
                                        .load(customBitMap)
                                        .apply(new RequestOptions()
                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image))
                                        .into(holder.iv5);

//                mCustomLoader.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                                Picasso.with(context).load(img).placeholder(R.drawable.no_image).
                                        error(R.drawable.no_image).into(holder.iv5);


                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
       */
                    }else{
                        holder.iv5.setImageResource(R.drawable.no_image);
                    }
                }

            }else{
                if(content.get(4).getContentImage()!=null&&content.get(4).getContentImage().size()!=0){

                    String urlString1 = content.get(4).getContentImage().get(0).getImages();

                    if(urlString1!=null){
                        Picasso.with(context).load(urlString1).resize(targetWidth,targetHeight).placeholder(R.drawable.no_image).
                                error(R.drawable.no_image).into(holder.iv5);
                    }

                }
            }


            if(content.get(5).getContentType().equalsIgnoreCase("Video")){

                holder.ivp6.setVisibility(View.VISIBLE);

                if(content.get(5).getContentURL()!=null&&!content.get(5).getContentURL().isEmpty()){
                    final String img = "https://img.youtube.com/vi/"+content.get(5).getContentURL()+"/0.jpg";
                    if(img!=null&&!img.isEmpty()){

                        Picasso.with(context).load(img).resize(targetWidth,targetHeight).placeholder(R.drawable.no_image).
                                error(R.drawable.no_image).into(holder.iv6);

/*
                        Picasso.with(context).load(img).into(new com.squareup.picasso.Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                // Cropping the image
                                Bitmap customBitMap =  Bitmap.createBitmap(bitmap, 0, 45, bitmap.getWidth(), bitmap.getHeight()-90);

                                Glide.with(context)
                                        .load(customBitMap)
                                        .apply(new RequestOptions()
                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image))
                                        .into(holder.iv6);

//                mCustomLoader.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                Picasso.with(context).load(img).placeholder(R.drawable.no_image).
                                        error(R.drawable.no_image).into(holder.iv6);
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });

                        */
                    }else{
                        holder.iv6.setImageResource(R.drawable.no_image);
                    }
                }

            }else{
                if(content.get(5).getContentImage()!=null&&content.get(5).getContentImage().size()!=0){

                    String urlString1 = content.get(5).getContentImage().get(0).getImages();

                    if(urlString1!=null){
                        Picasso.with(context).load(urlString1).resize(targetWidth,targetHeight).placeholder(R.drawable.no_image).
                                error(R.drawable.no_image).into(holder.iv6);
//                        iv6.setImageURI(Uri.parse(urlString1));
                    }

                }
            }


            if(content.get(6).getContentType().equalsIgnoreCase("Video")){

                holder.ivp7.setVisibility(View.VISIBLE);

                if(content.get(6).getContentURL()!=null&&!content.get(6).getContentURL().isEmpty()){
                    final String img = "https://img.youtube.com/vi/"+content.get(6).getContentURL()+"/0.jpg";
                    if(img!=null&&!img.isEmpty()){

                        Picasso.with(context).load(img).resize(targetWidth,targetHeight).placeholder(R.drawable.no_image).
                                error(R.drawable.no_image).into(holder.iv7);
                        /*
                        Picasso.with(context).load(img).into(new com.squareup.picasso.Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                // Cropping the image
                                Bitmap customBitMap =  Bitmap.createBitmap(bitmap, 0, 45, bitmap.getWidth(), bitmap.getHeight()-90);

                                Glide.with(context)
                                        .load(customBitMap)
                                        .apply(new RequestOptions()
                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image))
                                        .into(holder.iv7);

                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                Picasso.with(context).load(img).placeholder(R.drawable.no_image).
                                        error(R.drawable.no_image).into(holder.iv7);
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
                        */
                    }else{
                        holder.iv7.setImageResource(R.drawable.no_image);
                    }
                }

            }else{
                if(content.get(6).getContentImage()!=null&&content.get(6).getContentImage().size()!=0){

                    String urlString1 = content.get(6).getContentImage().get(0).getImages();

                    if(urlString1!=null){
                        Picasso.with(context).load(urlString1).resize(targetWidth,targetHeight).placeholder(R.drawable.no_image).
                                error(R.drawable.no_image).into(holder.iv7);
                    }

                }
            }


            if(content.get(7).getContentType().equalsIgnoreCase("Video")){

                holder.ivp8.setVisibility(View.VISIBLE);

                if(content.get(7).getContentURL()!=null&&!content.get(7).getContentURL().isEmpty()){
                    final String img = "https://img.youtube.com/vi/"+content.get(7).getContentURL()+"/0.jpg";
                    if(img!=null&&!img.isEmpty()){


                        Picasso.with(context).load(img).resize(targetWidth,targetHeight).placeholder(R.drawable.no_image).
                                error(R.drawable.no_image).into(holder.iv8);

                        /*
                        Picasso.with(context).load(img).into(new com.squareup.picasso.Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                // Cropping the image
                                Bitmap customBitMap =  Bitmap.createBitmap(bitmap, 0, 45, bitmap.getWidth(), bitmap.getHeight()-90);

                                Glide.with(context)
                                        .load(customBitMap)
                                        .apply(new RequestOptions()
                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image))
                                        .into(holder.iv8);

//                mCustomLoader.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                Picasso.with(context).load(img).placeholder(R.drawable.no_image).
                                        error(R.drawable.no_image).into(holder.iv8);
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });

                        */
                    }else{
                        holder.iv8.setImageResource(R.drawable.no_image);
                    }
                }

            }else{
                if(content.get(7).getContentImage()!=null&&content.get(7).getContentImage().size()!=0){

                    String urlString1 = content.get(7).getContentImage().get(0).getImages();

                    if(urlString1!=null){
                        Picasso.with(context).load(urlString1).resize(targetWidth,targetHeight).placeholder(R.drawable.no_image).
                                error(R.drawable.no_image).into(holder.iv8);

//                        iv8.setImageURI(Uri.parse(urlString1));
                    }

                }
            }


            if(content.get(8).getContentType().equalsIgnoreCase("Video")){

                holder.ivp9.setVisibility(View.VISIBLE);

                if(content.get(8).getContentURL()!=null&&!content.get(8).getContentURL().isEmpty()){
                    final String img = "https://img.youtube.com/vi/"+content.get(8).getContentURL()+"/0.jpg";

                    if(img!=null&&!img.isEmpty()){

                        Picasso.with(context).load(img).resize(targetWidth,targetHeight).placeholder(R.drawable.no_image).
                                error(R.drawable.no_image).into(holder.iv9);
                        /*
                        Picasso.with(context).load(img).into(new com.squareup.picasso.Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                // Cropping the image
                                Bitmap customBitMap =  Bitmap.createBitmap(bitmap, 0, 45, bitmap.getWidth(), bitmap.getHeight()-90);

                                Glide.with(context)
                                        .load(customBitMap)
                                        .apply(new RequestOptions()
                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image))
                                        .into(holder.iv9);

                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                Picasso.with(context).load(img).placeholder(R.drawable.no_image).
                                        error(R.drawable.no_image).into(holder.iv9);
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });

                        */
                    }else{
                        holder.iv9.setImageResource(R.drawable.no_image);
                    }
                }

            }else{

                if(content.get(8).getContentImage()!=null&&content.get(8).getContentImage().size()!=0){

                    String urlString1 = content.get(8).getContentImage().get(0).getImages();

                    if(urlString1!=null){
                        Picasso.with(context).load(urlString1).resize(targetWidth,targetHeight).placeholder(R.drawable.no_image).
                                error(R.drawable.no_image).into(holder.iv9);
//                        iv9.setImageURI(Uri.parse(urlString1));
                    }

                }
            }

        }
        // load image from api

        /*When clicked on the content image*/
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(content.get(0).getContentType().equalsIgnoreCase("Video")){
                    Intent intent = new Intent(context, ContentDetailScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Contents",content.get(0));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, ContentImageDetailScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Contents",content.get(0));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }

            }
        });

        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(content.get(1).getContentType().equalsIgnoreCase("Video")){
                    Intent intent = new Intent(context, ContentDetailScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Contents",content.get(1));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, ContentImageDetailScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Contents",content.get(1));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }

            }
        });

        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(content.get(2).getContentType().equalsIgnoreCase("Video")){
                    Intent intent = new Intent(context, ContentDetailScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Contents",content.get(2));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, ContentImageDetailScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Contents",content.get(2));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }

            }
        });

        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(content.get(3).getContentType().equalsIgnoreCase("Video")){
                    Intent intent = new Intent(context, ContentDetailScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Contents",content.get(3));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, ContentImageDetailScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Contents",content.get(3));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }


            }
        });

        iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(content.get(4).getContentType().equalsIgnoreCase("Video")){
                    Intent intent = new Intent(context, ContentDetailScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Contents",content.get(4));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, ContentImageDetailScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Contents",content.get(4));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }

            }
        });

        iv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(content.get(5).getContentType().equalsIgnoreCase("Video")){
                    Intent intent = new Intent(context, ContentDetailScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Contents",content.get(5));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, ContentImageDetailScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Contents",content.get(5));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }

            }
        });

        iv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(content.get(6).getContentType().equalsIgnoreCase("Video")){
                    Intent intent = new Intent(context, ContentDetailScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Contents",content.get(6));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, ContentImageDetailScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Contents",content.get(6));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }

            }
        });

        iv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(content.get(7).getContentType().equalsIgnoreCase("Video")){
                    Intent intent = new Intent(context, ContentDetailScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Contents",content.get(7));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, ContentImageDetailScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Contents",content.get(7));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }

            }
        });
        iv9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(content.get(8).getContentType().equalsIgnoreCase("Video")){
                    Intent intent = new Intent(context, ContentDetailScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Contents",content.get(8));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, ContentImageDetailScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Contents",content.get(8));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }


            }
        });
    }


    @Override
    public int getItemCount() {
        return mContentList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9,ivp1, ivp2, ivp3, ivp4, ivp5, ivp6, ivp7, ivp8, ivp9 ;

        MyViewHolder(View itemView) {
            super(itemView);
            iv1 = itemView.findViewById(R.id.follow_frag_img_view1);
            iv2 = itemView.findViewById(R.id.follow_frag_img_view2);
            iv3 = itemView.findViewById(R.id.follow_frag_img_view3);
            iv4 = itemView.findViewById(R.id.follow_frag_img_view4);
            iv5 = itemView.findViewById(R.id.follow_frag_img_view5);
            iv6 = itemView.findViewById(R.id.follow_frag_img_view6);
            iv7 = itemView.findViewById(R.id.follow_frag_img_view7);
            iv8 = itemView.findViewById(R.id.follow_frag_img_view8);
            iv9 = itemView.findViewById(R.id.follow_frag_img_view9);

            ivp1 = itemView.findViewById(R.id.videoicon1);
            ivp2 = itemView.findViewById(R.id.videoicon2);
            ivp3 = itemView.findViewById(R.id.videoicon3);
            ivp4 = itemView.findViewById(R.id.videoicon4);
            ivp5 = itemView.findViewById(R.id.videoicon5);
            ivp6 = itemView.findViewById(R.id.videoicon6);
            ivp7 = itemView.findViewById(R.id.videoicon7);
            ivp8 = itemView.findViewById(R.id.videoicon8);
            ivp9 = itemView.findViewById(R.id.videoicon9);

        }

    }

}