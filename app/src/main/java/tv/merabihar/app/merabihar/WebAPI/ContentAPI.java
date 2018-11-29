package tv.merabihar.app.merabihar.WebAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Model.InterestAndContents;
import tv.merabihar.app.merabihar.Model.InterestContentResponse;

/**
 * Created by ZingoHotels Tech on 31-10-2018.
 */

public interface ContentAPI {

    @Streaming
    @GET("Pagination/GetContentByCityId/{CityId}")
    Call<ArrayList<Contents>> getContentPageByCityId(@Path("CityId") int id, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

    @Streaming
    @GET("Pagination/GetContentByCategoryId/{CategoryId}")
    Call<ArrayList<Contents>> getContentPageByCategoryId(@Path("CategoryId") int id, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

    @Streaming
    @GET("Pagination/GetContentByInterestId/{InterestId}")
    Call<ArrayList<Contents>> getContentPageByInterestId(@Path("InterestId") int id, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

    @Streaming
    @GET("Contents/GetTopFiveContent")
    Call<ArrayList<Contents>> getTrendingContent();

    @Streaming
    @GET("Contents/{id}")
    Call<Contents> getContentsById(@Path("id") int id);

    @Streaming
    @DELETE("Contents/{id}")
    Call<Contents> deleteContentsById(@Path("id") int id);

    @Streaming
    @POST("Contents")
    Call<Contents> postContent(@Body Contents body);


    @PUT("Contents/{id}")
    Call<Contents> updateContent(@Path("id") int id,@Body Contents body);

    @Streaming
    @GET("Contents/GetContentByProfileId/{ProfileId}")
    Call<ArrayList<Contents>> getContentByProfileId(@Path("ProfileId") int id);

    @Streaming
    @GET("Contents/GetContentByCityId/{CityId}")
    Call<ArrayList<Contents>> getContentByCityId(@Path("CityId") int id);

    @Streaming
    @GET("Pagination/GetContentByCityIdAndContentType/{CityId}/{ContentType}")
    Call<ArrayList<Contents>> getContentByVideoAndCity(@Path("CityId") int id,@Path("ContentType") String type, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

    @Streaming
    @POST("InterestContentMapping/AddMultipleInterestWithContentAndMapping")
    Call<ArrayList<InterestContentResponse>> postContentsWithMultipleNewInterest(@Body InterestAndContents body);


}
