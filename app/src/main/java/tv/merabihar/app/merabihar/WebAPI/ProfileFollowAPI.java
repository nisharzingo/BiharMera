package tv.merabihar.app.merabihar.WebAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import tv.merabihar.app.merabihar.Model.FollowsWithMapping;
import tv.merabihar.app.merabihar.Model.ProfileFollowMapping;
import tv.merabihar.app.merabihar.Model.UserProfile;

/**
 * Created by ZingoHotels Tech on 01-11-2018.
 */

public interface ProfileFollowAPI {

    @POST("Follows")
    Call<ProfileFollowMapping> postInterest(@Body ProfileFollowMapping body);

    @DELETE("Follows/{id}")
    Call<ProfileFollowMapping> deleteIntrs(@Path("id") int id);

    @GET("Follows")
    Call<ArrayList<ProfileFollowMapping>> getInterest();

    @GET("Follow/GetFollowingByProfileId/{ProfileId}")
    Call<ArrayList<UserProfile>> getFollowersByProfileId(@Path("ProfileId") int id);

    @GET("Follow/GetNonFollowersByProfileId/{ProfileId}")
    Call<ArrayList<UserProfile>> getNonFollowersByProfileId(@Path("ProfileId") int id);

    @GET("Follow/GetFollowersByProfileId/{ProfileId}")
    Call<ArrayList<UserProfile>> getFollowingByProfileId(@Path("ProfileId") int id);

    @GET("Follow/GetFollowersContentByProfileId/{ProfileId}")
    Call<ArrayList<UserProfile>> getFollowersContentByProfileId(@Path("ProfileId") int id);

    @GET("Follow/GetFollowersWithMappingByProfileId/{ProfileId}")
    Call<ArrayList<FollowsWithMapping>> getFollowingsWithMappingByProfileId(@Path("ProfileId") int id);

    @GET("Follow/GetFollowingContentByProfileId/{ProfileId}")
    Call<ArrayList<UserProfile>> getFollowingContentByProfileId(@Path("ProfileId") int id);

    @PUT("Follows/{id}")
    Call<ProfileFollowMapping> updateProfile(@Path("id") int id, @Body ProfileFollowMapping body);

    @GET("Follows/{id}")
    Call<ProfileFollowMapping> getInterestById(@Path("id") int id);

    @GET("Profiles/GetAllProfileByReferralCodeUsed/{ReferralCodeUsed}")
    Call<ArrayList<UserProfile>> getDirectReferedProfile(@Path("ReferralCodeUsed") String id);

    @GET("Profiles/GetAllProfileByReferralCodeOfSuperParents/{ReferralCodeOfSuperParents}")
    Call<ArrayList<UserProfile>> getInDirectReferedProfile(@Path("ReferralCodeOfSuperParents") String id);

    @GET("Profiles/GetAllProfileByReferralCodeOfParents/{ReferralCodeOfParents}")
    Call<ArrayList<UserProfile>> getInDirectReferedParentProfile(@Path("ReferralCodeOfParents") String id);

}
