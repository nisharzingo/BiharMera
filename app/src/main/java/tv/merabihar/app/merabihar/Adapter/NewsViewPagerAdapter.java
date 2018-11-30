package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Rss.NewArticles;

/**
 * Created by ZingoHotels Tech on 29-11-2018.
 */

public class NewsViewPagerAdapter extends PagerAdapter {

    private Context context;

    private ArrayList<NewArticles> mList = new ArrayList<>();
    private LayoutInflater inflater;


    public NewsViewPagerAdapter(Context context,ArrayList<NewArticles> mList)
    {
        this.context = context;
        this.mList = mList;
        inflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object) ;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int pos) {

        try{
            View view = inflater.inflate(R.layout.news_main_design_adapter,container,false);

            TextView mBlogName = (TextView)view.findViewById(R.id.news_title_adapter);
            TextView mBlogShort = (TextView)view.findViewById(R.id.news_provider);
            TextView mBlogDescription = (TextView)view.findViewById(R.id.news_description);
            TextView mCreateBy = (TextView)view.findViewById(R.id.created_by_adapter);
            TextView mCreateDate = (TextView)view.findViewById(R.id.created_date_adapter);
            TextView mduration = (TextView)view.findViewById(R.id.duration);
            Button mReadMore = (Button) view.findViewById(R.id.blog_read_adapter);
            ImageView mBlogImage = (ImageView) view.findViewById(R.id.news_image_adapter);


            if(mList.get(pos)!=null){

                NewArticles currentArticle = mList.get(pos);

                Locale.setDefault(Locale.getDefault());
                String date = currentArticle.getPublishedAt();

                if(date!=null){
                    if(date.contains("T")){

                        String publishedDate = date.replaceAll("T"," ");

                        mCreateDate.setText(publishedDate.replaceAll("Z",""));
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

                            mBlogName.setText(""+currentArticle.getTitle().replaceAll(remove,""));
                        }
                    }else{
                        mBlogName.setText(currentArticle.getTitle());
                    }
                }




                if(currentArticle.getUrlToImage()!=null){
                    Picasso.with(context)
                            .load(currentArticle.getUrlToImage())

                            .placeholder(R.drawable.no_image)
                            .error(R.drawable.no_image)
                            .into(mBlogImage);
                }else{
                    mBlogImage.setImageResource(R.drawable.no_image);

                }




                String categories = "";


                mBlogDescription.setText(currentArticle.getDescription());

                /*holder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        //show article content inside a dialog
                        articleView = new WebView(context);

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

                        ((TextView) alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                    }
                });*/

            }

            container.addView(view);
            return view;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
