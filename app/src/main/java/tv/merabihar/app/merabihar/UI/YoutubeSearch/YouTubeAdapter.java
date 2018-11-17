package tv.merabihar.app.merabihar.UI.YoutubeSearch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.YoutubeSearch.youtubemodels.Video;

class YouTubeAdapter extends RecyclerView.Adapter<YoutubeVideoViewHolder> {
    private List<Video> videos;
    private IFragmentManager fragmentManager;

    YouTubeAdapter(IFragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    void setVideos(List<Video> videos) {
        if (this.videos == null) {
            this.videos = new ArrayList<>(videos.size());
            this.videos.addAll(videos);
            return;
        }
        this.videos.clear();
        this.videos.addAll(videos);
    }

    @Override
    public YoutubeVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.youtube_search_video_item, parent, false);
        return new YoutubeVideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(YoutubeVideoViewHolder holder, int position) {
    }

    @Override
    public void onViewAttachedToWindow(YoutubeVideoViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getAdapterPosition();
        Video video = videos.get(position);
        video.binder.prepare();
        video.binder.bind(holder, fragmentManager);
    }

    @Override
    public void onViewDetachedFromWindow(YoutubeVideoViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
          try{
              int position = holder.getAdapterPosition();
              Video video = videos.get(position);
              video.binder.unBind(holder, fragmentManager);
          }catch (Exception e){
          }
    }

    @Override
    public int getItemCount()
    {
        return this.videos.size();
    }
}
