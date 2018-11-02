package tv.merabihar.app.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 02-11-2018.
 */

public class InterestContentResponse  implements Serializable {

    @SerializedName("interest")
    public Interest interest;

    @SerializedName("content")
    public Contents blog;

    @SerializedName("interestcontentMapping")
    public InterestContentMapping interestblogMapping;

    public Interest getInterest() {
        return interest;
    }

    public void setInterest(Interest interest) {
        this.interest = interest;
    }

    public Contents getBlog() {
        return blog;
    }

    public void setBlog(Contents blog) {
        this.blog = blog;
    }

    public InterestContentMapping getInterestblogMapping() {
        return interestblogMapping;
    }

    public void setInterestblogMapping(InterestContentMapping interestblogMapping) {
        this.interestblogMapping = interestblogMapping;
    }
}
