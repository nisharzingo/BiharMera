package tv.merabihar.app.merabihar.WebAPI;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import tv.merabihar.app.merabihar.Model.Likes;

/**
 * Created by ZingoHotels Tech on 01-11-2018.
 */

public interface LikeAPI {

    @POST("Likes")
    Call<Likes> postLikes(@Body Likes body);

    @DELETE("Likes/{id}")
    Call<Likes> deleteLikes(@Path("id") int id);

    @PUT("Likes/{id}")
    Call<Likes> updateLikes(@Path("id") int id,@Body Likes body);
}
