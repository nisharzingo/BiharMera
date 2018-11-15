package tv.merabihar.app.merabihar.WebAPI;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by ZingoHotels Tech on 15-11-2018.
 */

public interface RetrofitInterface {

    @GET()
    @Streaming
    Call<ResponseBody> downloadImage(@Url String fileUrl);
}
