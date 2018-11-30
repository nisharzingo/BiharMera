package tv.merabihar.app.merabihar.Rss;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZingoHotels Tech on 29-11-2018.
 */

public class NewsAPIData implements Serializable {

    @SerializedName("status")
    public String status;

    @SerializedName("totalResults")
    public int totalResults;

    @SerializedName("articles")
    public ArrayList<NewArticles> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public ArrayList<NewArticles> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<NewArticles> articles) {
        this.articles = articles;
    }
}
