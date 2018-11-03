package tv.merabihar.app.merabihar.WebAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import tv.merabihar.app.merabihar.Model.ProfileCategoryMapping;

/**
 * Created by ZingoHotels Tech on 03-11-2018.
 */

public interface ProfileCategoryAPI {

    @POST("ProfileCategoryMappings")
    Call<ProfileCategoryMapping> postInterest(@Body ProfileCategoryMapping body);

    @DELETE("ProfileCategoryMappings/{id}")
    Call<ProfileCategoryMapping> deleteIntrs(@Path("id") int id);

    @GET("ProfileCategoryMappings")
    Call<ArrayList<ProfileCategoryMapping>> getInterest();

    /*@GET("ProfileInterestMappings/GetInterestByProfileId/{ProfileId}")
    Call<ArrayList<InterestProfileMapping>> getInterestByProfileId(@Path("ProfileId") int id);

    @GET("ProfileInterestMappings/GetProfileByInterestId/{InterestId}")
    Call<ArrayList<InterestProfileMapping>> getProfileByInterestId(@Path("InterestId") int id);*/

    @PUT("ProfileCategoryMappings/{id}")
    Call<ProfileCategoryMapping> updateProfile(@Path("id") int id, @Body ProfileCategoryMapping body);

    @GET("ProfileCategoryMappings/{id}")
    Call<ProfileCategoryMapping> getInterestById(@Path("id") int id);

}
