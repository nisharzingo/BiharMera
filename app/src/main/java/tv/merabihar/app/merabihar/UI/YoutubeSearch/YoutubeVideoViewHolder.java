package tv.merabihar.app.merabihar.UI.YoutubeSearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import tv.merabihar.app.merabihar.R;

public class YoutubeVideoViewHolder extends RecyclerView.ViewHolder {
    public SimpleDraweeView image;
    public TextView title;
    public TextView description;
    public FrameLayout videoContainer;

    public YoutubeVideoViewHolder(View itemView) {
        super(itemView);
        image = (SimpleDraweeView) itemView.findViewById(R.id.youtube_Search_img);
        title = (TextView) itemView.findViewById(R.id.youtube_Search_title);
        description = (TextView) itemView.findViewById(R.id.youtube_search_description);
        videoContainer = (FrameLayout) itemView.findViewById(R.id.youtube_search_video_container);
    }
}
