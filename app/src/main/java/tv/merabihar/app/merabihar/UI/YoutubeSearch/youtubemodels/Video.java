package tv.merabihar.app.merabihar.UI.YoutubeSearch.youtubemodels;

public class Video {
    public String videoId;
    public Image image = new Image();
    public String title;
    public String description;

    public transient VideoBinder binder = new VideoBinder(this);
}
