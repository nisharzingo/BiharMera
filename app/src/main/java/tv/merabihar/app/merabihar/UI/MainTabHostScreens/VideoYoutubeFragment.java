package tv.merabihar.app.merabihar.UI.MainTabHostScreens;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import tv.merabihar.app.merabihar.CustomInterface.IFragmentManager;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.YoutubePlayList.Model.Video;
import tv.merabihar.app.merabihar.UI.YoutubePlayList.Transform.ItemsTransform;
import tv.merabihar.app.merabihar.UI.YoutubePlayList.YouTubeAdapter;
import tv.merabihar.app.merabihar.UI.YoutubePlayList.YouTubeFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoYoutubeFragment  extends Fragment implements IFragmentManager {
    private static final String TAG = VideoYoutubeFragment.class.getSimpleName();

    RecyclerView videos;
    ProgressBar progressBar;

    YouTubeAdapter adapter = new YouTubeAdapter(this);
    LinearLayoutManager linearLayoutManager;
    float scale;

    public VideoYoutubeFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        scale = getResources().getDisplayMetrics().density;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_you_tube, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        videos = (RecyclerView) view.findViewById(R.id.videos);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);
        videos.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(videos);
      /*  videos.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int adapterPosition = parent.getChildAdapterPosition(view);
                if (adapterPosition == 0) {
                    outRect.top = (int) (4 * scale);
                }
                outRect.bottom = (int) (10 * scale);
            }
        });*/
    }

    public void setSearchString(ArrayList<Contents> contents) {

        progressBar.setVisibility(View.GONE);
        List<Video> videos = ItemsTransform.from(contents);
        adapter.setVideos(videos);
        VideoYoutubeFragment.this.videos.setAdapter(adapter);
    }

    @Override
    public FragmentManager getSupportFragmentManager() {
        return getFragmentManager();
    }

    @Override
    public Fragment getSupportFragment() {
        return this;
    }
}
