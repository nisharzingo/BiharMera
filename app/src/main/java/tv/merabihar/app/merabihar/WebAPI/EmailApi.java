package tv.merabihar.app.merabihar.WebAPI;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import tv.merabihar.app.merabihar.Model.Email;

public interface EmailApi {

    @POST("Operation/SendInvoice")
    Call<String> sendEmail(@Body Email email);
}
