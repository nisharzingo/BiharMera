package tv.merabihar.app.merabihar.Rss;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tv.merabihar.app.merabihar.Adapter.ArticleAdapter;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.Constants;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.WebAPI.NewsFeedAPI;

public class SourceBasedNews extends AppCompatActivity {

    ImageView mBack;

    RecyclerView mSourceNews;
    private RecyclerView.LayoutManager layoutManager;

    NewsSource sources;

    private static Retrofit retrofit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_source_based_news);
            mBack = (ImageView)findViewById(R.id.news_back);
            mSourceNews = (RecyclerView)findViewById(R.id.source_news);


            layoutManager = new LinearLayoutManager(SourceBasedNews.this, LinearLayoutManager.VERTICAL, false);
            mSourceNews.setLayoutManager(layoutManager);
            mSourceNews.setItemAnimator(new DefaultItemAnimator());

            Bundle bundle = getIntent().getExtras();


            if(bundle!=null){

                sources = (NewsSource)bundle.getSerializable("Sources");
            }


            mBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SourceBasedNews.this.finish();
                }
            });


            if(sources!=null){

                try{

                    loadFeedNews(sources);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, "No News in this source", Toast.LENGTH_SHORT).show();
                }


            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static Retrofit getClientNews() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.NEWSAPI_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        //System.out.println("Retrofit = "+retrofit.get);
        return retrofit;
    }


    public void loadFeedNews(final NewsSource sources) throws Exception
    {




        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                final NewsFeedAPI categoryAPI = getClientNews().create(NewsFeedAPI.class);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(Constants.CITY_ID);
                Call<NewsAPIData> getCat = categoryAPI.getNewsBySource(sources.getId(),Constants.NEWSAPI_TOKEN);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<NewsAPIData>() {

                    @Override
                    public void onResponse(Call<NewsAPIData> call, Response<NewsAPIData> response) {


                        if(response.code() == 200)
                        {
                            NewsAPIData newsAPIData = response.body();
                            ArrayList<NewArticles> newArticlesOne = new ArrayList<>();
                            ArrayList<NewArticles> newArticlesTwo = new ArrayList<>();

                            int count =0;

                            if(newsAPIData!=null){

                                if(newsAPIData.getArticles()!=null&&newsAPIData.getArticles().size()!=0){




                                    ArticleAdapter adapter = new ArticleAdapter(SourceBasedNews.this,newsAPIData.getArticles());
                                    mSourceNews.setAdapter(adapter);

                                }


                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<NewsAPIData> call, Throwable t) {


//                            Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }
        });




    }

}
