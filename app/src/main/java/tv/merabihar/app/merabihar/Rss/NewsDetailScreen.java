package tv.merabihar.app.merabihar.Rss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import tv.merabihar.app.merabihar.R;

public class NewsDetailScreen extends AppCompatActivity {

    ImageView mBack,mNewsImage;
    TextView mTitle,mShort,mDescription,mSource,pubDate;
    LinearLayout mRead;

    NewArticles currentArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_news_detail_screen);

            mBack = (ImageView)findViewById(R.id.news_back);
            mNewsImage = (ImageView)findViewById(R.id.news_image);
            mTitle = (TextView)findViewById(R.id.title);
            mShort = (TextView)findViewById(R.id.title_short);
            mDescription = (TextView)findViewById(R.id.title_description);
            mSource = (TextView)findViewById(R.id.source);
            pubDate = (TextView)findViewById(R.id.date_post);
            mRead = (LinearLayout)findViewById(R.id.read_click);

            Bundle bundle = getIntent().getExtras();


            if(bundle!=null){

                currentArticle = (NewArticles)bundle.getSerializable("Articles");
            }

            mBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    NewsDetailScreen.this.finish();
                }
            });
            if(currentArticle!=null){

                Locale.setDefault(Locale.getDefault());
                String date = currentArticle.getPublishedAt();

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

                        if(currentArticle.getAuthor()!=null&&!currentArticle.getAuthor().isEmpty()){

                            pubDate.setText(currentArticle.getAuthor()+"\n"+""+output);

                        }else{
                            pubDate.setText(""+output);
                        }


                        try {
                            Date publish = new SimpleDateFormat("dd MMM,yyyy HH:mm:ss").parse(publishedDate.replaceAll("Z",""));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");



                if(currentArticle.getTitle()!=null){
                    if(currentArticle.getTitle().contains("-")){
                        String titles[] = currentArticle.getTitle().split("-");
                        if(titles.length!=0&&titles.length>1){

                            String remove = titles[titles.length-1];

                            mTitle.setText(""+currentArticle.getTitle().replaceAll(remove,""));
                        }
                    }else{
                        mTitle.setText(currentArticle.getTitle());
                    }
                }




                if(currentArticle.getUrlToImage()!=null){
                    Picasso.with(NewsDetailScreen.this)
                            .load(currentArticle.getUrlToImage())

                            .placeholder(R.drawable.no_image)
                            .error(R.drawable.no_image)
                            .into(mNewsImage);
                }else{
                    mNewsImage.setImageResource(R.drawable.no_image);
                    mNewsImage.setVisibility(View.GONE);
                }




                String categories = "";

                if(currentArticle.getDescription()!=null&&!currentArticle.getDescription().isEmpty()){

                    mShort.setText(currentArticle.getDescription());

                }else{
                    mShort.setVisibility(View.GONE);
                }

                if(currentArticle.getSource().getName()!=null&&!currentArticle.getSource().getName().isEmpty()){

                    mSource.setText(currentArticle.getSource().getName());

                }else{
                    mSource.setVisibility(View.GONE);
                }

                if(currentArticle.getContent()!=null&&!currentArticle.getContent().isEmpty()){

                    mDescription.setText(currentArticle.getContent());

                }else{
                    mDescription.setVisibility(View.GONE);
                }


                mRead.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        //show article content inside a dialog

                        Intent intent = new Intent(NewsDetailScreen.this, NewsSourcePageScreen.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("Source",currentArticle.getUrl());
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                });
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
