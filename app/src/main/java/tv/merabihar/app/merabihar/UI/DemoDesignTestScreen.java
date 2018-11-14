package tv.merabihar.app.merabihar.UI;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.VideoView;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;
import tv.merabihar.app.merabihar.CustomInterface.DownloadTaskVideo;
import tv.merabihar.app.merabihar.R;

public class DemoDesignTestScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.adapter_content_list_design);
        setContentView(R.layout.activity_demo_design_test_screen);

        final VideoView videoView;
        videoView = (VideoView)findViewById(R.id.videoView);

        String youtubeLink = "http://youtube.com/watch?v=Z8vWQXNCv20";

        YouTubeUriExtractor ytEx = new YouTubeUriExtractor(DemoDesignTestScreen.this) {
            @Override
            public void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> ytFiles) {
                if (ytFiles != null) {
                    int itag = 22;

                    List<Integer> iTags = Arrays.asList(22, 137, 18);

                    for (Integer iTag : iTags) {

                        YtFile ytFile = ytFiles.get(iTag);

                        if (ytFile != null) {

                            String downloadUrl = ytFile.getUrl();

                            if (downloadUrl != null && !downloadUrl.isEmpty()) {

                                videoView.setVideoPath(downloadUrl);
                                videoView.start();

                                break;

                            }

                        }

                    }
// Here you can get download url





                }
            }
        };

        ytEx.execute(youtubeLink);

    }
}
