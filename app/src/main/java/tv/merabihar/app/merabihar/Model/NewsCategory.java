package tv.merabihar.app.merabihar.Model;

/**
 * Created by ZingoHotels Tech on 29-11-2018.
 */

public class NewsCategory {

    String title;
    int icon;

    public NewsCategory(String title, int icon)
    {
        this.title = title;
        this.icon = icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }
}
