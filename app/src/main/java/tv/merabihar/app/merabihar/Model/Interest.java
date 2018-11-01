package tv.merabihar.app.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 01-11-2018.
 */

public class Interest implements Serializable {

    @SerializedName("ZingoInterestId")
    public int ZingoInterestId;

    @SerializedName("InterestName")
    public String InterestName;

    @SerializedName("Description")
    public String Description;

    public int getZingoInterestId() {
        return ZingoInterestId;
    }

    public void setZingoInterestId(int zingoInterestId) {
        ZingoInterestId = zingoInterestId;
    }

    public String getInterestName() {
        return InterestName;
    }

    public void setInterestName(String interestName) {
        InterestName = interestName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
