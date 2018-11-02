package tv.merabihar.app.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZingoHotels Tech on 02-11-2018.
 */

public class InterestAndContents implements Serializable {

    @SerializedName("interestList")
    private ArrayList<Interest> interestList;

    @SerializedName("content")
    private Contents content;

    public ArrayList<Interest> getInterestList() {
        return interestList;
    }

    public void setInterestList(ArrayList<Interest> interestList) {
        this.interestList = interestList;
    }

    public Contents getContent() {
        return content;
    }

    public void setContent(Contents content) {
        this.content = content;
    }
}
