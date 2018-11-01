package tv.merabihar.app.merabihar.WebAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Model.Interest;
import tv.merabihar.app.merabihar.Model.InterestContentMapping;

/**
 * Created by ZingoHotels Tech on 01-11-2018.
 */

public interface InterestAPI {

    @GET("Interest/GetTopFiveInterest")
    Call<ArrayList<InterestContentMapping>> getTrendingInterest();

    @GET("interest")
    Call<ArrayList<Interest>> getInterest();
}
