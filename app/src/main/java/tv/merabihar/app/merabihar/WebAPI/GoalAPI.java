package tv.merabihar.app.merabihar.WebAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import tv.merabihar.app.merabihar.Model.Goals;
import tv.merabihar.app.merabihar.Model.Likes;

/**
 * Created by ZingoHotels Tech on 06-11-2018.
 */

public interface GoalAPI {

    @POST("Goals")
    Call<Goals> postGoals(@Body Goals body);

    @DELETE("Goals/{id}")
    Call<Goals> deleteGoals(@Path("id") int id);

    @GET("Goals/{id}")
    Call<Goals> getGoalsById(@Path("id") int id);

    @PUT("Goals/{id}")
    Call<Goals> updateGoals(@Path("id") int id,@Body Goals body);


    @GET("Goals")
    Call<ArrayList<Goals>> getGoals();
}
