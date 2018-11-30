package tv.merabihar.app.merabihar.Rss;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tv.merabihar.app.merabihar.R;

public class NewsDetailScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_news_detail_screen);

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
