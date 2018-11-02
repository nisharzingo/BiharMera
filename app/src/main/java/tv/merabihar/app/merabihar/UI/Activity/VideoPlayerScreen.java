package tv.merabihar.app.merabihar.UI.Activity;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.TabVideoActivity;
import tv.merabihar.app.merabihar.Util.Constants;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ContentAPI;

public class VideoPlayerScreen extends AppCompatActivity implements YouTubePlayer.OnInitializedListener,YouTubePlayer.PlayerStateChangeListener{

    private YouTubePlayerFragment playerFragmentTop;
    private YouTubePlayer mPlayer;
    private String YouTubeKey = Constants.YOUTUBE,url;

    private static final int PAGE_START =1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 10 ;
    private int currentPage = PAGE_START;

    ArrayList<String> urls = new ArrayList<>();

    int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            setContentView(R.layout.activity_video_player_screen);

            playerFragmentTop = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_player_fragment_top);



            if(urls!=null&&urls.size()!=0){

                new CountDownTimer(5000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        isLoading = true;

                        if(isLastPage){

                        }else{
                            currentPage = currentPage+1;
                            loadNextSetOfItems();
                        }

                    }

                }.start();

            }

            loadFirstSetOfContents();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        mPlayer = player;

        //Enables automatic control of orientation
        mPlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);

        //Show full screen in landscape mode always
        //mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);

        //System controls will appear automatically
        mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);

        if (!wasRestored) {

            mPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
            mPlayer.setFullscreen(true);

            if(urls.size()!=0){

                if(size<urls.size()){
                    mPlayer.loadVideo(urls.get(size));
                    mPlayer.play();
                    size = size+1;
                }
            }

            //player.loadVideo("9rLZYyMbJic");
            //mPlayer.loadVideo("9rLZYyMbJic");
        }
        else
        {

            if(urls.size()!=0){

                if(size<urls.size()){
                    mPlayer.play();
                }
            }

        }
    }

    @Override
    public void onVideoEnded() {

        //Enables automatic control of orientation
        mPlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);

        //Show full screen in landscape mode always
        //mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);

        //System controls will appear automatically
        mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);

        mPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        mPlayer.setFullscreen(true);

        if(urls.size()!=0){

            if(size<urls.size()){
                mPlayer.loadVideo(urls.get(size));
                mPlayer.play();
                size = size+1;
            }
        }

        //player.loadVideo("9rLZYyMbJic");
        //mPlayer.loadVideo("9rLZYyMbJic");

    }

    @Override
    public void onLoaded(String s) {

    }

    @Override
    public void onVideoStarted() {

    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {

    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        mPlayer = null;
        //errorReason.getErrorDialog(VideoPlayerScreen.this,1);
        System.out.println("Failure reason "+errorReason.toString());
    }


    public void loadFirstSetOfContents()
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ContentAPI categoryAPI = Util.getClient().create(ContentAPI.class);
                Call<ArrayList<Contents>> getCat = categoryAPI.getContentByVideoAndCity(Constants.CITY_ID,"Video",1,5);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Contents>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {



                        if(response.code() == 200 && response.body()!= null)
                        {



                            if( response.body().size()!= 0){
                                loadFirstPage(response.body());
                            }else{

                                isLastPage = true;
                                isLoading = true;

                            }



                        }else{


                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Contents>> call, Throwable t) {



                        Toast.makeText(VideoPlayerScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    private void loadFirstPage(ArrayList<Contents> list) {


        if (list != null && list.size() !=0){

            for (Contents content:list) {

                if(content.getContentURL()!=null){

                    urls.add(content.getContentURL());
                }

                
            }

            if(urls!=null&&urls.size()!=0){

                playerFragmentTop.initialize(YouTubeKey, VideoPlayerScreen.this);
            }

        }else{
            isLastPage = true;
        }

    }

    public void loadNextSetOfItems() {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ContentAPI bookingApi = Util.getClient().create(ContentAPI.class);

                Call<ArrayList<Contents>> getAllBookings = bookingApi.
                        getContentByVideoAndCity(Constants.CITY_ID,"Video",currentPage,5);

                getAllBookings.enqueue(new Callback<ArrayList<Contents>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {


                        try{
                            if(response.code() == 200 && response.body()!= null)
                            {
                                if(response.body().size() != 0) {


                                    loadNextPage(response.body());


                                }
                                else
                                {

                                    isLastPage = true;
                                }

                            }
                            else
                            {

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<Contents>> call, Throwable t) {
                    }
                });
                //WebService.getAllBookings(PreferenceHandler.getInstance(getActivity()).getHotelID());
            }
        });

    }

    private void loadNextPage(ArrayList<Contents> list) {
        //Collections.reverse(list);

        isLoading = false;



        if (list != null && list.size() !=0 )
        {

            for (Contents content:list) {

                if(content.getContentURL()!=null){

                    urls.add(content.getContentURL());
                }


            }
        }
        else
        {
            isLastPage = true;

        }
    }
}
