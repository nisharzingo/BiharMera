package tv.merabihar.app.merabihar.UI.MainTabHostScreens.HomeFragments;


import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.exoplayer2.upstream.ParsingLoadable;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tv.merabihar.app.merabihar.Adapter.ArticleAdapter;
import tv.merabihar.app.merabihar.Adapter.CategoryFollowingList;
import tv.merabihar.app.merabihar.Adapter.NavigationListAdapter;
import tv.merabihar.app.merabihar.Adapter.NewsCategoryAdapter;
import tv.merabihar.app.merabihar.Adapter.NewsViewPagerAdapter;
import tv.merabihar.app.merabihar.CustomViews.SnackbarViewer;
import tv.merabihar.app.merabihar.DataBase.DataBaseHelper;
import tv.merabihar.app.merabihar.Model.Category;
import tv.merabihar.app.merabihar.Model.NavBarItems;
import tv.merabihar.app.merabihar.Model.NewsCategory;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Rss.Article;
import tv.merabihar.app.merabihar.Rss.NewArticles;
import tv.merabihar.app.merabihar.Rss.NewsAPIData;
import tv.merabihar.app.merabihar.Rss.NewsSearchActivity;
import tv.merabihar.app.merabihar.Rss.Parser;
import tv.merabihar.app.merabihar.UI.Activity.NewsListCategoryScreen;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.SearchScreens.SearchActivity;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.TabSearchActivity2;
import tv.merabihar.app.merabihar.Util.Constants;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.CategoryApi;
import tv.merabihar.app.merabihar.WebAPI.NewsFeedAPI;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends Fragment {

    RecyclerView mCategory,mNewsList;
    ViewPager mHeadLinesOne,mHeadLinesTwo;
    ViewPager mSportsOne,mSportsTwo;
    CardView mSearch;
    private RecyclerView.LayoutManager layoutManager,verticalLayoutManager;
    ArticleAdapter mAdapter;
    TypedArray icons ;
    String[] title;
    ArrayList<NewsCategory> newCategoryList;
    private static Retrofit retrofit = null;


    private String urlString = "https://www.androidauthority.com/feed";
    private String topNews = "https://newsapi.org/v2/top-headlines?" +
            "country=in&" +
            "apiKey="+ Constants.NEWSAPI_TOKEN;


    public NewsFeedFragment() {
        // Required empty public constructor
    }

    public static NewsFeedFragment newInstance() {
        NewsFeedFragment fragment = new NewsFeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        try{
            View view = inflater.inflate(R.layout.fragment_news_feed, container, false);

            mCategory = (RecyclerView)view.findViewById(R.id.category_news);
            mNewsList = (RecyclerView)view.findViewById(R.id.news_list_recycler);

            mHeadLinesOne = (ViewPager) view.findViewById(R.id.top_headlines_pager_one);
            mHeadLinesTwo = (ViewPager)view.findViewById(R.id.top_headlines_pager_two);

            mSportsOne = (ViewPager) view.findViewById(R.id.sports_pager_one);
            mSportsTwo = (ViewPager)view.findViewById(R.id.sports_pager_two);

            mSearch = (CardView) view.findViewById(R.id.news_linear);

            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mCategory.setLayoutManager(layoutManager);
            mCategory.setItemAnimator(new DefaultItemAnimator());

            mNewsList.setLayoutManager(verticalLayoutManager);
            mNewsList.setItemAnimator(new DefaultItemAnimator());

            icons = getResources().obtainTypedArray(R.array.news_images);
            title  = getResources().getStringArray(R.array.news_title);

            newCategoryList = new ArrayList<>();

            for (int i=0;i<title.length;i++)
            {
                NewsCategory navbarItem = new NewsCategory(title[i],icons.getResourceId(i, -1));
                newCategoryList.add(navbarItem);
            }

            if(newCategoryList!=null&&newCategoryList.size()!=0){


                NewsCategoryAdapter adapter = new NewsCategoryAdapter(getActivity(),newCategoryList);
                mCategory.setAdapter(adapter);

            }
            mHeadLinesOne.setClipToPadding(false);
            mHeadLinesOne.setPageMargin(18);

            mHeadLinesTwo.setClipToPadding(false);
            mHeadLinesTwo.setPageMargin(18);

            mSportsOne.setClipToPadding(false);
            mSportsOne.setPageMargin(18);

            mSportsTwo.setClipToPadding(false);
            mSportsTwo.setPageMargin(18);


            loadFeedNews();

            mSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent search = new Intent(getActivity(), NewsSearchActivity.class);
                    startActivity(search);
                }
            });

            //loadFeedSports();


            return  view;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

   /* public void loadFeed() {



        Parser parser = new Parser();
        parser.execute(urlString);
        parser.onFinish(new Parser.OnTaskCompleted() {
            //what to do when the parsing is done
            @Override
            public void onTaskCompleted(ArrayList<Article> list) {
                //list is an Array List with all article's information
                //set the adapter to recycler view
                mAdapter = new ArticleAdapter(getActivity(),list);
                mNewsList.setAdapter(mAdapter);
               *//* progressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);*//*

            }

            //what to do in case of error
            @Override
            public void onError() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       *//* progressBar.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);*//*
                        Toast.makeText(getActivity(), "Unable to load data.",
                                Toast.LENGTH_LONG).show();
                        Log.i("Unable to load ", "articles");
                    }
                });
            }
        });
    }*/

    public void loadFeed()
    {




            new ThreadExecuter().execute(new Runnable() {
                @Override
                public void run() {
                    final NewsFeedAPI categoryAPI = Util.getClientNews().create(NewsFeedAPI.class);
                    //Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(Constants.CITY_ID);
                    Call<NewsAPIData> getCat = categoryAPI.getNews();
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

                                        if(newsAPIData.getArticles().size()>=6&&newsAPIData.getArticles().size()<12){

                                            for (NewArticles articles:newsAPIData.getArticles()) {

                                                if(count>=6){
                                                    newArticlesTwo.add(articles);
                                                    count=count+1;
                                                }else{
                                                    newArticlesOne.add(articles);
                                                    count=count+1;
                                                }

                                            }

                                        }else if(newsAPIData.getArticles().size()>=12){
                                            int counts=0;

                                            for (NewArticles articles:newsAPIData.getArticles()) {

                                                if(count>=6&&count<=12&&counts<12){
                                                    newArticlesTwo.add(articles);
                                                    count=count+1;
                                                    counts = count;
                                                }else if(count<6){
                                                    newArticlesOne.add(articles);
                                                    count=count+1;
                                                }else{
                                                    break;

                                                }

                                            }

                                        }

                                        if(newArticlesOne!=null&&newArticlesOne.size()!=0){
                                            NewsViewPagerAdapter adapter = new NewsViewPagerAdapter(getActivity(),newArticlesOne);
                                            mHeadLinesOne.setAdapter(adapter);
                                        }

                                        if(newArticlesTwo!=null&&newArticlesTwo.size()!=0){
                                            NewsViewPagerAdapter adapter = new NewsViewPagerAdapter(getActivity(),newArticlesTwo);
                                            mHeadLinesTwo.setAdapter(adapter);
                                        }

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

    public void loadFeedSports()
    {




        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                final NewsFeedAPI categoryAPI = Util.getClientNews().create(NewsFeedAPI.class);
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

                                    if(newsAPIData.getArticles().size()>=6&&newsAPIData.getArticles().size()<12){

                                        for (NewArticles articles:newsAPIData.getArticles()) {

                                            if(count>=6){
                                                newArticlesTwo.add(articles);
                                                count=count+1;
                                            }else{
                                                newArticlesOne.add(articles);
                                                count=count+1;
                                            }

                                        }

                                    }else if(newsAPIData.getArticles().size()>=12){
                                        int counts=0;

                                        for (NewArticles articles:newsAPIData.getArticles()) {

                                            if(count>=6&&count<=12&&counts<12){
                                                newArticlesTwo.add(articles);
                                                count=count+1;
                                                counts = count;
                                            }else if(count<6){
                                                newArticlesOne.add(articles);
                                                count=count+1;
                                            }else{
                                                break;

                                            }

                                        }

                                    }

                                    if(newArticlesOne!=null&&newArticlesOne.size()!=0){
                                        NewsViewPagerAdapter adapter = new NewsViewPagerAdapter(getActivity(),newArticlesOne);
                                        mSportsOne.setAdapter(adapter);
                                    }

                                    if(newArticlesTwo!=null&&newArticlesTwo.size()!=0){
                                        NewsViewPagerAdapter adapter = new NewsViewPagerAdapter(getActivity(),newArticlesTwo);
                                        mSportsTwo.setAdapter(adapter);
                                    }

                                }


                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<NewsAPIData> call, Throwable t) {


//                            Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });




            }
        });




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

    public void loadFeedNews()
    {




        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                final NewsFeedAPI categoryAPI = getClientNews().create(NewsFeedAPI.class);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(Constants.CITY_ID);
                Call<NewsAPIData> getCat = categoryAPI.getGoogleNews();
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




                                    ArticleAdapter adapter = new ArticleAdapter(getActivity(),newsAPIData.getArticles());
                                    mNewsList.setAdapter(adapter);

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
