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
import tv.merabihar.app.merabihar.Model.SubscribedGoals;

/**
 * Created by ZingoHotels Tech on 06-11-2018.
 */

public interface SubscribedGoalsAPI {


    @POST("SubscribedGoals")
    Call<SubscribedGoals> postSubscribedGoals(@Body SubscribedGoals body);

    @DELETE("SubscribedGoals/{id}")
    Call<SubscribedGoals> deleteSubscribedGoals(@Path("id") int id);

    @PUT("SubscribedGoals/{id}")
    Call<SubscribedGoals> updateSubscribedGoals(@Path("id") int id,@Body SubscribedGoals body);


    @GET("SubscribedGoals")
    Call<ArrayList<SubscribedGoals>> getSubscribedGoals();

    @GET("SubscribedGoals/GetsubscribedGoalsByProfileId/{ProfileId}")
    Call<ArrayList<SubscribedGoals>> getSubscribedGoalsByProfileId(@Path("ProfileId") int id);

    @GET("SubscribedGoals/GetsubscribedGoalsByProfileIdAndGoalId/{ProfileId}/{GoalId}")
    Call<ArrayList<SubscribedGoals>> getSubscribedGoalsByProfileIdAndGoal(@Path("ProfileId") int id,@Path("GoalId") int ids);

}
