package tv.merabihar.app.merabihar.WebAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import tv.merabihar.app.merabihar.Model.TransactionHistroy;

/**
 * Created by ZingoHotels Tech on 16-11-2018.
 */

public interface TransactionHistroyAPI {

    @POST("TransactionHistories")
    Call<TransactionHistroy> postTransactionHistories(@Body TransactionHistroy body);

    @DELETE("TransactionHistories/{id}")
    Call<TransactionHistroy> deleteTransactionHistories(@Path("id") int id);

    @PUT("TransactionHistories/{id}")
    Call<TransactionHistroy> updateTransactionHistories(@Path("id") int id,@Body TransactionHistroy body);

    @GET("TransactionHistories")
    Call<ArrayList<TransactionHistroy>> getTransactionHistories();

    @GET("TransactionHistories/{id}")
    Call<TransactionHistroy> getTransactionHistoriesById(@Path("id") int id);

    @GET("TransactionHistories/GettransactionHistoryByProfileId/{ProfileId}")
    Call<ArrayList<TransactionHistroy>> getTransactionHistoriesByProfileId(@Path("ProfileId") int id);

    @GET("TransactionHistories/GettransactionHistoryByProfileIdAndTitle/{ProfileId}/{Title}")
    Call<ArrayList<TransactionHistroy>> getTransactionHistoriesByProfileIdAndGoal(@Path("ProfileId") int id,@Path("Title") String ids);

}
