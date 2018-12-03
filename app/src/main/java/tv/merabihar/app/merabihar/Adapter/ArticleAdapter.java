package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Rss.Article;
import tv.merabihar.app.merabihar.Rss.NewArticles;
import tv.merabihar.app.merabihar.Rss.NewsAPIData;
import tv.merabihar.app.merabihar.Rss.NewsDetailScreen;
import tv.merabihar.app.merabihar.Rss.NewsSourcePageScreen;
import tv.merabihar.app.merabihar.UI.Activity.NewsListCategoryScreen;

/**
 * Created by ZingoHotels Tech on 29-11-2018.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private Context context;
    private ArrayList<NewArticles> list;
    private WebView articleView;

    public ArticleAdapter(Context context,ArrayList<NewArticles> list) {

        this.context = context;
        this.list = list;

    }


    public void clearData() {
        if (list != null)
            list.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_content_adapter, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final NewArticles currentArticle = list.get(position);

        Locale.setDefault(Locale.getDefault());
        String date = currentArticle.getPublishedAt();

        if(position%2==0){

            AdRequest adRequest = new AdRequest.Builder().build();
            holder.mAdView.loadAd(adRequest);
        }else{
            holder.mAdView.setVisibility(View.GONE);
        }

        if(date!=null){
            if(date.contains("T")){

                String publishedDate = date.replaceAll("T"," ");

                DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                DateFormat indianFormat = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
                indianFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
                Date timestamp = null;
                try {
                    timestamp = utcFormat.parse(publishedDate.replaceAll("Z",""));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String output = indianFormat.format(timestamp);

                holder.pubDate.setText(""+output);
                try {
                    Date publish = new SimpleDateFormat("dd MMM,yyyy HH:mm:ss").parse(publishedDate.replaceAll("Z",""));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
//        final String pubDateString = sdf.format(date);


        if(currentArticle.getTitle()!=null){
            if(currentArticle.getTitle().contains("-")){
                String titles[] = currentArticle.getTitle().split("-");
                if(titles.length!=0&&titles.length>1){

                    String remove = titles[titles.length-1];

                    holder.title.setText(""+currentArticle.getTitle().replaceAll(remove,""));
                }
            }else{
                holder.title.setText(currentArticle.getTitle());
            }
        }




        if(currentArticle.getUrlToImage()!=null){
            Picasso.with(context)
                    .load(currentArticle.getUrlToImage())

                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(holder.image);
        }else{
            holder.image.setImageResource(R.drawable.no_image);
            holder.image.setVisibility(View.GONE);
        }




        String categories = "";
        /*if (currentArticle.getCategories() != null) {
            for (int i = 0; i < currentArticle.getCategories().size(); i++) {
                if (i == currentArticle.getCategories().size() - 1) {
                    categories = categories + currentArticle.getCategories().get(i);
                } else {
                    categories = categories + currentArticle.getCategories().get(i) + ", ";
                }
            }
        }*/

        if(currentArticle.getDescription()!=null&&!currentArticle.getDescription().isEmpty()){

            holder.category.setText(currentArticle.getDescription());

        }else{
            holder.category.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //show article content inside a dialog

                Intent intent = new Intent(context, NewsDetailScreen.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Articles",currentArticle);
                intent.putExtras(bundle);
                context.startActivity(intent);
                /*articleView = new WebView(context);

                articleView.getSettings().setLoadWithOverviewMode(true);

                String title = list.get(holder.getAdapterPosition()).getTitle();
                String content = list.get(holder.getAdapterPosition()).getContent();

                articleView.getSettings().setJavaScriptEnabled(true);
                articleView.setHorizontalScrollBarEnabled(false);
                articleView.setWebChromeClient(new WebChromeClient());
                articleView.loadDataWithBaseURL(null, "<style>img{display: inline; height: auto; max-width: 100%;} " +

                        "</style>\n" + "<style>iframe{ height: auto; width: auto;}" + "</style>\n" + content, null, "utf-8", null);

                android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(context).create();
                alertDialog.setTitle(title);
                alertDialog.setView(articleView);
                alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                ((TextView) alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());*/
            }
        });

    }

    @Override
    public int getItemCount() {

        return list == null ? 0 : list.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder  {

        TextView title,pubDate,category;
        ImageView image;
        AdView mAdView ;


        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);
            mAdView = (AdView)itemView.findViewById(R.id.adView_news);
            title = itemView.findViewById(R.id.title);
            pubDate = itemView.findViewById(R.id.pubDate);
            image = itemView.findViewById(R.id.image);
            category = itemView.findViewById(R.id.categories);

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
