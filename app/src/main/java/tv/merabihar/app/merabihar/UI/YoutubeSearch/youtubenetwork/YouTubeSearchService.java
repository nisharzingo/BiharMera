package tv.merabihar.app.merabihar.UI.YoutubeSearch.youtubenetwork;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tv.merabihar.app.merabihar.UI.YoutubeSearch.Constants;
import tv.merabihar.app.merabihar.UI.YoutubeSearch.youtubenetwork.youtubenetworkmodels.Items;

public class YouTubeSearchService {

    public static Call<Items> search (String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IYouTubeSearchService service = retrofit.create(IYouTubeSearchService.class);
        return service.search(query);
    }

    interface IYouTubeSearchService {
        @GET("youtube/v3/search?part=snippet&type=video&key=" + Constants.KEY)
        Call<Items> search(@Query("q") String query);
    }
}
