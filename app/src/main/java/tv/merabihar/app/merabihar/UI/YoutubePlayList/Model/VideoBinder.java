package tv.merabihar.app.merabihar.UI.YoutubePlayList.Model;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import tv.merabihar.app.merabihar.CustomInterface.IFragmentManager;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.YoutubePlayList.Custom.VideoViewHolder;
import tv.merabihar.app.merabihar.Util.Constants;

/**
 * Created by ZingoHotels Tech on 02-11-2018.
 */

public class VideoBinder {

    private static final String TAG = VideoBinder.class.getSimpleName();
    private static final int HACK_ID_PREFIX = 12331293; //some random number
    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    private static YouTubePlayerSupportFragment youTubePlayerFragment;
    private static YouTubePlayer youTubePlayer;
    private static boolean isFullScreen = false;
    private Video video;

    private ImageRequest imageRequest;
    private Uri uri;

    VideoBinder(Video video) {
        this.video = video;
    }

    public void prepare() {
        if (!TextUtils.isEmpty(video.image.url) && uri == null) {
            try {
                uri = Uri.parse(video.image.url);
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    }

    public void bind(final VideoViewHolder videoViewHolder, final IFragmentManager fragmentManager) {
        videoViewHolder.image.setAspectRatio(16f / 9f);
        if (imageRequest == null) {
            videoViewHolder.image.post(new Runnable() {
                @Override
                public void run() {
                    ImageRequestBuilder builder;
                    if (uri == null) {
                        builder = ImageRequestBuilder.newBuilderWithResourceId(android.R.color.darker_gray);
                    } else {
                        builder = ImageRequestBuilder.newBuilderWithSource(uri);
                    }
                    imageRequest = builder.setResizeOptions(new ResizeOptions(
                            videoViewHolder.videoContainer.getWidth(), videoViewHolder.videoContainer.getHeight()
                    )).build();
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(imageRequest)
                            .setOldController(videoViewHolder.image.getController())
                            .build();
                    videoViewHolder.image.setController(controller);
                }
            });
        } else {
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(imageRequest)
                    .setOldController(videoViewHolder.image.getController())
                    .build();
            videoViewHolder.image.setController(controller);
        }
        bindVideo(videoViewHolder, fragmentManager);
        bindTitle(videoViewHolder);
        bindDescription(videoViewHolder);
    }

    private void bindVideo(final VideoViewHolder viewHolder,
                           final IFragmentManager fragmentManager) {
        View view = viewHolder.itemView.findViewWithTag(viewHolder.itemView.getContext()
                .getString(R.string.video_component_tag));
        if (view != null) {
            view.setId(HACK_ID_PREFIX + viewHolder.getAdapterPosition());
        }
        //handleClick(viewHolder, fragmentManager);

        if (viewHolder.videoContainer.getChildCount() == 0) {
            if (youTubePlayerFragment == null) {
                youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
            }
            if (youTubePlayerFragment.isAdded()) {
                if (VideoBinder.youTubePlayer != null) {
                    try {
                        VideoBinder.youTubePlayer.pause();
                        VideoBinder.youTubePlayer.release();
                    } catch (Exception e) {
                        if (VideoBinder.youTubePlayer != null) {
                            try {
                                VideoBinder.youTubePlayer.release();
                            } catch (Exception ignore) {
                            }

                        }
                    }
                    VideoBinder.youTubePlayer = null;
                }

                fragmentManager.getSupportFragmentManager()
                        .beginTransaction()
                        .remove(youTubePlayerFragment)
                        .commit();
                fragmentManager.getSupportFragmentManager()
                        .executePendingTransactions();
                youTubePlayerFragment = null;
            }
            if (youTubePlayerFragment == null) {
                youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
            }
            fragmentManager.getSupportFragmentManager()
                    .beginTransaction()
                    .add(HACK_ID_PREFIX + viewHolder.getAdapterPosition(), youTubePlayerFragment)
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .commit();
            youTubePlayerFragment.initialize(Constants.KEY,
                    new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                            YouTubePlayer youTubePlayer, boolean b) {
                            VideoBinder.youTubePlayer = youTubePlayer;
                            VideoBinder.youTubePlayer.loadVideo(video.videoId);
                            VideoBinder.youTubePlayer.setFullscreenControlFlags(0);
                            //VideoBinder.youTubePlayer.setFullscreen(true);
                            VideoBinder.youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                                @Override
                                public void onFullscreen(boolean b) {
                                    isFullScreen = b;
                                }
                            });
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                            YouTubeInitializationResult youTubeInitializationResult) {
                            Log.e(VideoBinder.class.getSimpleName(), youTubeInitializationResult.name());
                            if (YouTubeIntents.canResolvePlayVideoIntent(
                                    fragmentManager.getSupportFragment().getContext())) {
                                fragmentManager.getSupportFragment()
                                        .startActivity(YouTubeIntents.createPlayVideoIntent(
                                                fragmentManager.getSupportFragment().getContext(),
                                                video.videoId));
                                return;
                            }
                            Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + video.videoId));
                            fragmentManager.getSupportFragment().startActivity(viewIntent);
                        }
                    });
        }
    }

    private void bindTitle(final VideoViewHolder videoViewHolder) {
        videoViewHolder.title.setText(video.title);
    }

    private void bindDescription(final VideoViewHolder videoViewHolder) {
        videoViewHolder.description.setText(video.description);
    }

    private void handleClick(final VideoViewHolder viewHolder,
                             final IFragmentManager fragmentManager) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (TextUtils.isEmpty(video.videoId)) {
                    return;
                }
                if (!YouTubeIntents.isYouTubeInstalled(view.getContext()) ||
                        YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(view.getContext()) != YouTubeInitializationResult.SUCCESS) {
                    if (YouTubeIntents.canResolvePlayVideoIntent(view.getContext())) {
                        fragmentManager.getSupportFragment().
                                startActivity(YouTubeIntents.createPlayVideoIntent(view.getContext(), video.videoId));
                        return;
                    }
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + video.videoId));
                    fragmentManager.getSupportFragment().startActivity(viewIntent);
                    return;
                }*/
                if (viewHolder.videoContainer.getChildCount() == 0) {
                    if (youTubePlayerFragment == null) {
                        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
                    }
                    if (youTubePlayerFragment.isAdded()) {
                        if (VideoBinder.youTubePlayer != null) {
                            try {
                                VideoBinder.youTubePlayer.pause();
                                VideoBinder.youTubePlayer.release();
                            } catch (Exception e) {
                                if (VideoBinder.youTubePlayer != null) {
                                    try {
                                        VideoBinder.youTubePlayer.release();
                                    } catch (Exception ignore) {
                                    }

                                }
                            }
                            VideoBinder.youTubePlayer = null;
                        }

                        fragmentManager.getSupportFragmentManager()
                                .beginTransaction()
                                .remove(youTubePlayerFragment)
                                .commit();
                        fragmentManager.getSupportFragmentManager()
                                .executePendingTransactions();
                        youTubePlayerFragment = null;
                    }
                    if (youTubePlayerFragment == null) {
                        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
                    }
                    fragmentManager.getSupportFragmentManager()
                            .beginTransaction()
                            .add(HACK_ID_PREFIX + viewHolder.getAdapterPosition(), youTubePlayerFragment)
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .commit();
                    youTubePlayerFragment.initialize(Constants.KEY,
                            new YouTubePlayer.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                                    YouTubePlayer youTubePlayer, boolean b) {
                                    VideoBinder.youTubePlayer = youTubePlayer;
                                    VideoBinder.youTubePlayer.loadVideo(video.videoId);
                                    VideoBinder.youTubePlayer.setFullscreenControlFlags(0);
                                    VideoBinder.youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                                        @Override
                                        public void onFullscreen(boolean b) {
                                            isFullScreen = b;
                                        }
                                    });
                                }

                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                    YouTubeInitializationResult youTubeInitializationResult) {
                                    Log.e(VideoBinder.class.getSimpleName(), youTubeInitializationResult.name());
                                    if (YouTubeIntents.canResolvePlayVideoIntent(
                                            fragmentManager.getSupportFragment().getContext())) {
                                        fragmentManager.getSupportFragment()
                                                .startActivity(YouTubeIntents.createPlayVideoIntent(
                                                        fragmentManager.getSupportFragment().getContext(),
                                                        video.videoId));
                                        return;
                                    }

                                    System.out.println("Reason failure "+youTubeInitializationResult.toString());
                                    Log.e("Reason failure ",youTubeInitializationResult.toString());

                                    /*Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + video.videoId));
                                    fragmentManager.getSupportFragment().startActivity(viewIntent);*/
                                }
                            });
                }
            }
        });
    }

    public void unBind(final VideoViewHolder videoViewHolder, IFragmentManager fragmentManager) {
        if (videoViewHolder.videoContainer.getChildCount() > 0) {
            if (youTubePlayerFragment != null && youTubePlayerFragment.isAdded()) {
                if (VideoBinder.youTubePlayer != null) {
                    try {
                        VideoBinder.youTubePlayer.pause();
                        VideoBinder.youTubePlayer.release();
                    } catch (Exception e) {
                        if (VideoBinder.youTubePlayer != null) {
                            try {
                                VideoBinder.youTubePlayer.release();
                            } catch (Exception ignore) {}
                        }
                    }
                    VideoBinder.youTubePlayer = null;
                }

                fragmentManager.getSupportFragmentManager()
                        .beginTransaction()
                        .remove(youTubePlayerFragment)
                        .commit();
                fragmentManager.getSupportFragmentManager()
                        .executePendingTransactions();
                youTubePlayerFragment = null;
            }
        }
    }
}
