package tv.merabihar.app.merabihar.UI.YoutubePlayList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tv.merabihar.app.merabihar.CustomInterface.IFragmentManager;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.YoutubePlayList.Custom.VideoViewHolder;
import tv.merabihar.app.merabihar.UI.YoutubePlayList.Model.Video;

/**
 * Created by ZingoHotels Tech on 02-11-2018.
 */

public class YouTubeAdapter  extends RecyclerView.Adapter<VideoViewHolder> {
    private List<Video> videos;
    private IFragmentManager fragmentManager;

    public YouTubeAdapter(IFragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setVideos(List<Video> videos) {
        if (this.videos == null) {
            this.videos = new ArrayList<>(videos.size());
            this.videos.addAll(videos);
            return;
        }
        this.videos.clear();
        this.videos.addAll(videos);
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
    }

    @Override
    public void onViewAttachedToWindow(VideoViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getAdapterPosition();
        Video video = videos.get(position);
        video.binder.prepare();
        video.binder.bind(holder, fragmentManager);
    }

    @Override
    public void onViewDetachedFromWindow(VideoViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        int position = holder.getAdapterPosition();
        Video video = videos.get(position);
        video.binder.unBind(holder, fragmentManager);
    }

    @Override
    public int getItemCount() {
        return this.videos.size();
    }
}
