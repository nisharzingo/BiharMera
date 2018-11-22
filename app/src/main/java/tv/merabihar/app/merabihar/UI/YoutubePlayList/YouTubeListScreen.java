package tv.merabihar.app.merabihar.UI.YoutubePlayList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.VideoPlayerScreen;
import tv.merabihar.app.merabihar.Util.Constants;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ContentAPI;

public class YouTubeListScreen extends AppCompatActivity {

    private static final String FRAGMENT_TAG = YouTubeFragment.class.getSimpleName();

    YouTubeFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            Fresco.initialize(this);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            setContentView(R.layout.activity_you_tube_list_screen);

            fragment =
                    (YouTubeFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);

            Bundle bundle = getIntent().getExtras();

            int postion = 0;
            if(bundle!=null){

                postion = bundle.getInt("Position");
            }

            loadFirstSetOfContents(postion);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content, new YouTubeFragment(), FRAGMENT_TAG)
                    .commit();


        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void loadFirstSetOfContents(final int position)
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ContentAPI categoryAPI = Util.getClient().create(ContentAPI.class);
                Call<ArrayList<Contents>> getCat = categoryAPI.getContentByVideoAndCity(Constants.CITY_ID,"Video",1,50);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Contents>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {



                        if(response.code() == 200 && response.body()!= null)
                        {



                            if( response.body().size()!= 0){
                               // loadFirstPage(response.body());
                                fragment = (YouTubeFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);


                                    fragment.setSearchString(response.body(),position);



                            }else{

                               /* isLastPage = true;
                                isLoading = true;*/

                            }



                        }else{


                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Contents>> call, Throwable t) {



//                        Toast.makeText(YouTubeListScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }
}
