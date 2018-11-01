package tv.merabihar.app.merabihar.WebAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Model.InterestProfileMapping;

/**
 * Created by ZingoHotels Tech on 01-11-2018.
 */

public interface InterestProfileAPI {

    @POST("ProfileInterestMappings")
    Call<InterestProfileMapping> postInterest(@Body InterestProfileMapping body);

    @POST("ProfileInterestMappings/AddMultipleMapping")
    Call<ArrayList<InterestProfileMapping>> postMultipleInterest(@Body ArrayList<InterestProfileMapping> body);

    @DELETE("ProfileInterestMappings/{id}")
    Call<InterestProfileMapping> deleteIntrs(@Path("id") int id);

    @GET("ProfileInterestMappings")
    Call<ArrayList<InterestProfileMapping>> getInterest();

    @GET("ProfileInterestMappings/GetInterestByProfileId/{ProfileId}")
    Call<ArrayList<InterestProfileMapping>> getInterestByProfileId(@Path("ProfileId") int id);

    @GET("ProfileInterestMappings/GetContentByProfileId/{ProfileId}")
    Call<ArrayList<Contents>> getContentofInterestByProfileId(@Path("ProfileId") int id);

    @GET("ProfileInterestMappings/GetProfileByInterestId/{InterestId}")
    Call<ArrayList<InterestProfileMapping>> getProfileByInterestId(@Path("InterestId") int id);

    @PUT("ProfileInterestMappings/{id}")
    Call<InterestProfileMapping> updateProfile(@Path("id") int id, @Body InterestProfileMapping body);

    @GET("ProfileInterestMappings/{id}")
    Call<InterestProfileMapping> getInterestById(@Path("id") int id);
}
