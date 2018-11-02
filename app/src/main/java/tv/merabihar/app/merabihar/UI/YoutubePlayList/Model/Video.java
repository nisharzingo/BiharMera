package tv.merabihar.app.merabihar.UI.YoutubePlayList.Model;



/**
 * Created by ZingoHotels Tech on 02-11-2018.
 */

public class Video {

    public String videoId;
    public Image image = new Image();
    public String title;
    public String description;

    public transient VideoBinder binder = new VideoBinder(this);
}
