package tv.merabihar.app.merabihar.WebAPI;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import tv.merabihar.app.merabihar.Model.Comments;

/**
 * Created by ZingoHotels Tech on 03-11-2018.
 */

public interface CommentAPI {

    @POST("CommentsForContents")
    Call<Comments> postComment(@Body Comments body);
}
