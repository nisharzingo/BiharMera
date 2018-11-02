package tv.merabihar.app.merabihar.WebAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import tv.merabihar.app.merabihar.Model.SubCategories;

/**
 * Created by ZingoHotels Tech on 02-11-2018.
 */

public interface SubCategoryAPI {

    @GET("SubCategories")
    Call<ArrayList<SubCategories>> getSubCategories();

    @GET("SubCategories/{id}")
    Call<SubCategories> getSubCategoryById(@Path("id") int id);

    @GET("GetSubCategoryByCityId/{CityId}")
    Call<ArrayList<SubCategories>> getSubCategoriesByCityId(@Path("CityId") int id);
}
