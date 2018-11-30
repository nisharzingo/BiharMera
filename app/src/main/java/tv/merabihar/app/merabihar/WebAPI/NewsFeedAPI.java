package tv.merabihar.app.merabihar.WebAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import tv.merabihar.app.merabihar.Model.Category;
import tv.merabihar.app.merabihar.Model.NewsSources;
import tv.merabihar.app.merabihar.Rss.NewsAPIData;

/**
 * Created by ZingoHotels Tech on 29-11-2018.
 */

public interface NewsFeedAPI {

    @GET("top-headlines?country=in&apiKey=50f164cfd717409682b670a71b1c95b6")
    Call<NewsAPIData> getNews();

    @GET("top-headlines?country=in&category=sports&apiKey=50f164cfd717409682b670a71b1c95b6")
    Call<NewsAPIData> getNewsSports();

    @GET("top-headlines?country=in&category=health&apiKey=50f164cfd717409682b670a71b1c95b6")
    Call<NewsAPIData> getNewsHealth();

    @GET("top-headlines?country=in&category=business&apiKey=50f164cfd717409682b670a71b1c95b6")
    Call<NewsAPIData> getNewsBusiness();

    @GET("top-headlines?country=in&category=science&apiKey=50f164cfd717409682b670a71b1c95b6")
    Call<NewsAPIData> getNewsScience();

    @GET("top-headlines?country=in&category=technology&apiKey=50f164cfd717409682b670a71b1c95b6")
    Call<NewsAPIData> getNewsTech();

    @GET("top-headlines?country=in&category=entertainment&apiKey=50f164cfd717409682b670a71b1c95b6")
    Call<NewsAPIData> getNewsEnter();

    @GET("top-headlines?country=in&category=general&apiKey=50f164cfd717409682b670a71b1c95b6")
    Call<NewsAPIData> getGeneralNews();

    @GET("top-headlines?sources=google-news-in&apiKey=50f164cfd717409682b670a71b1c95b6")
    Call<NewsAPIData> getGoogleNews();

    @GET("sources?apiKey=50f164cfd717409682b670a71b1c95b6")
    Call<NewsSources> getSources();
}
