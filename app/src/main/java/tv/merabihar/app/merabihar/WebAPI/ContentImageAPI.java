package tv.merabihar.app.merabihar.WebAPI;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import tv.merabihar.app.merabihar.Model.ContentImages;
import tv.merabihar.app.merabihar.Model.Contents;

/**
 * Created by ZingoHotels Tech on 27-11-2018.
 */

public interface ContentImageAPI {

    @GET("ContentImages/{id}")
    Call<ContentImages> getContentImagesById(@Path("id") int id);

    @DELETE("ContentImages/{id}")
    Call<ContentImages> deleteContentImagesById(@Path("id") int id);

    @POST("ContentImages")
    Call<ContentImages> postContentImages(@Body ContentImages body);

    @PUT("ContentImages/{id}")
    Call<ContentImages> updateContentImages(@Path("id") int id,@Body ContentImages body);
}
