package tv.merabihar.app.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZingoHotels Tech on 31-10-2018.
 */

public class City  implements Serializable {

    @SerializedName("CityId")
    private int CityId;

    @SerializedName("CityName")
    private String CityName;

    @SerializedName("categoriesList")
    private ArrayList<Category> categoriesList;

    public int getCityId() {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public ArrayList<Category> getCategoriesList() {
        return categoriesList;
    }

    public void setCategoriesList(ArrayList<Category> categoriesList) {
        this.categoriesList = categoriesList;
    }
}
