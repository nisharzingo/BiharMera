package tv.merabihar.app.merabihar.WebAPI;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import tv.merabihar.app.merabihar.Model.InterestContentMapping;

/**
 * Created by ZingoHotels Tech on 02-11-2018.
 */

public interface InterestContentAPI {

    @POST("InterestContentMappings")
    Call<InterestContentMapping> postInterestContent(@Body InterestContentMapping body);
}
