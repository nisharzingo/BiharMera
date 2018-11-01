package tv.merabihar.app.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 01-11-2018.
 */

public class InterestContentMapping implements Serializable {

    @SerializedName("InterestContentMappingId")
    public int InterestContentMappingId;

    @SerializedName("content")
    public Contents content;

    @SerializedName("ContentId")
    public int ContentId;

    @SerializedName("zingoInterst")
    public Interest zingoInterst;

    @SerializedName("ZingoInterestId")
    public int ZingoInterestId;

    public int getInterestContentMappingId() {
        return InterestContentMappingId;
    }

    public void setInterestContentMappingId(int interestContentMappingId) {
        InterestContentMappingId = interestContentMappingId;
    }

    public Contents getContent() {
        return content;
    }

    public void setContent(Contents content) {
        this.content = content;
    }

    public int getContentId() {
        return ContentId;
    }

    public void setContentId(int contentId) {
        ContentId = contentId;
    }

    public Interest getZingoInterst() {
        return zingoInterst;
    }

    public void setZingoInterst(Interest zingoInterst) {
        this.zingoInterst = zingoInterst;
    }

    public int getZingoInterestId() {
        return ZingoInterestId;
    }

    public void setZingoInterestId(int zingoInterestId) {
        ZingoInterestId = zingoInterestId;
    }
}
