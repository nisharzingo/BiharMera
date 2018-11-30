package tv.merabihar.app.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import tv.merabihar.app.merabihar.Rss.NewsSource;

/**
 * Created by ZingoHotels Tech on 30-11-2018.
 */

public class NewsSources implements Serializable {

    @SerializedName("status")
    public String status;

    @SerializedName("sources")
    public ArrayList<NewsSource> sources;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<NewsSource> getSources() {
        return sources;
    }

    public void setSources(ArrayList<NewsSource> sources) {
        this.sources = sources;
    }
}
