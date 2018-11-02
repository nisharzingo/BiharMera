package tv.merabihar.app.merabihar.UI.YoutubePlayList.Transform;

import java.util.ArrayList;
import java.util.List;

import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.UI.YoutubePlayList.Model.Item;
import tv.merabihar.app.merabihar.UI.YoutubePlayList.Model.Items;
import tv.merabihar.app.merabihar.UI.YoutubePlayList.Model.Video;

/**
 * Created by ZingoHotels Tech on 02-11-2018.
 */

public class ItemsTransform {

    public static List<Video> from(ArrayList<Contents> items) {
        if (items == null || items.size() == 0) {
            return null;
        }
        ArrayList<Video> videos = new ArrayList<>(items.size());
        for (Contents item : items) {
            Video video = new Video();
            video.videoId = item.getContentURL();
            video.image.url = "https://img.youtube.com/vi/"+item.getContentURL()+"/0.jpg";
            video.image.width = (float) 100.0;
            video.image.height = (float)100.0;
            video.title = item.getTitle();
            video.description = item.getDescription();
            videos.add(video);
        }
        return videos;
    }

}
