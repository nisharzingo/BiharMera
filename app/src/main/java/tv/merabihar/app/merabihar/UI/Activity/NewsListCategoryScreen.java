package tv.merabihar.app.merabihar.UI.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tv.merabihar.app.merabihar.Adapter.ArticleAdapter;
import tv.merabihar.app.merabihar.Adapter.NewsViewPagerAdapter;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Rss.NewArticles;
import tv.merabihar.app.merabihar.Rss.NewsAPIData;
import tv.merabihar.app.merabihar.Util.Constants;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.NewsFeedAPI;

public class NewsListCategoryScreen extends AppCompatActivity {

    private static RecyclerView mtopBlogs;
    ProgressBar progressBar;

    ArrayList<NewArticles> blogsList;
    LinearLayoutManager linearLayoutManager;

    String category="";

    private static Retrofit retrofit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_news_list_category_screen);

            mtopBlogs = (RecyclerView) findViewById(R.id.top_blogs_viewpager);
            progressBar = (ProgressBar) findViewById(R.id.blog_progress);

            linearLayoutManager = new LinearLayoutManager(NewsListCategoryScreen.this,LinearLayoutManager.VERTICAL,false);
            mtopBlogs.setLayoutManager(linearLayoutManager);

            mtopBlogs.setItemAnimator(new DefaultItemAnimator());

            Bundle bundle = getIntent().getExtras();

            if(bundle!=null){

                category = bundle.getString("Category");

            }

            if(category!=null&&!category.isEmpty()){

                if(category.equalsIgnoreCase("Sports")){

                    progressBar.setVisibility(View.GONE);
                    loadFeedSports();

                }else if(category.equalsIgnoreCase("Health")){
                    progressBar.setVisibility(View.GONE);
                    loadFeedHealth();

                }else if(category.equalsIgnoreCase("Business")){
                    progressBar.setVisibility(View.GONE);
                    loadFeedBusiness();

                }else if(category.equalsIgnoreCase("Science")){
                    progressBar.setVisibility(View.GONE);
                    loadFeedScience();

                }else if(category.equalsIgnoreCase("Entertainment")){
                    progressBar.setVisibility(View.GONE);
                    loadFeedEntertainment();

                }else if(category.equalsIgnoreCase("Technology")){
                    progressBar.setVisibility(View.GONE);
                    loadFeedTechnology();

                }else{
                    progressBar.setVisibility(View.GONE);
                    loadFeedGeneral();
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
    public void loadFeedSports()
    {




        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                final NewsFeedAPI categoryAPI = getClientNews().create(NewsFeedAPI.class);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(Constants.CITY_ID);
                Call<NewsAPIData> getCat = categoryAPI.getNewsSports();
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




                                        ArticleAdapter adapter = new ArticleAdapter(NewsListCategoryScreen.this,newsAPIData.getArticles());
                                        mtopBlogs.setAdapter(adapter);

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

    public void loadFeedHealth()
    {




        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                final NewsFeedAPI categoryAPI = getClientNews().create(NewsFeedAPI.class);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(Constants.CITY_ID);
                Call<NewsAPIData> getCat = categoryAPI.getNewsHealth();
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




                                    ArticleAdapter adapter = new ArticleAdapter(NewsListCategoryScreen.this,newsAPIData.getArticles());
                                    mtopBlogs.setAdapter(adapter);

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

    public void loadFeedBusiness()
    {




        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                final NewsFeedAPI categoryAPI = getClientNews().create(NewsFeedAPI.class);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(Constants.CITY_ID);
                Call<NewsAPIData> getCat = categoryAPI.getNewsBusiness();
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




                                    ArticleAdapter adapter = new ArticleAdapter(NewsListCategoryScreen.this,newsAPIData.getArticles());
                                    mtopBlogs.setAdapter(adapter);

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

    public void loadFeedScience()
    {




        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                final NewsFeedAPI categoryAPI = getClientNews().create(NewsFeedAPI.class);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(Constants.CITY_ID);
                Call<NewsAPIData> getCat = categoryAPI.getNewsScience();
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




                                    ArticleAdapter adapter = new ArticleAdapter(NewsListCategoryScreen.this,newsAPIData.getArticles());
                                    mtopBlogs.setAdapter(adapter);

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

    public void loadFeedEntertainment()
    {




        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                final NewsFeedAPI categoryAPI = getClientNews().create(NewsFeedAPI.class);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(Constants.CITY_ID);
                Call<NewsAPIData> getCat = categoryAPI.getNewsEnter();
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




                                    ArticleAdapter adapter = new ArticleAdapter(NewsListCategoryScreen.this,newsAPIData.getArticles());
                                    mtopBlogs.setAdapter(adapter);

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

    public void loadFeedTechnology()
    {




        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                final NewsFeedAPI categoryAPI = getClientNews().create(NewsFeedAPI.class);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(Constants.CITY_ID);
                Call<NewsAPIData> getCat = categoryAPI.getNewsTech();
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




                                    ArticleAdapter adapter = new ArticleAdapter(NewsListCategoryScreen.this,newsAPIData.getArticles());
                                    mtopBlogs.setAdapter(adapter);

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

    public void loadFeedGeneral()
    {




        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                final NewsFeedAPI categoryAPI = getClientNews().create(NewsFeedAPI.class);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(Constants.CITY_ID);
                Call<NewsAPIData> getCat = categoryAPI.getGeneralNews();
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




                                    ArticleAdapter adapter = new ArticleAdapter(NewsListCategoryScreen.this,newsAPIData.getArticles());
                                    mtopBlogs.setAdapter(adapter);

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
