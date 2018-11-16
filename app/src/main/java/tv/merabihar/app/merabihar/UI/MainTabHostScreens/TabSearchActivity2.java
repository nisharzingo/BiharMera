package tv.merabihar.app.merabihar.UI.MainTabHostScreens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.FollowFragmentContentAdapter;
import tv.merabihar.app.merabihar.Adapter.TrendingIntrestAdapter;
import tv.merabihar.app.merabihar.CustomFonts.TextViewSFProDisplaySemibold;
import tv.merabihar.app.merabihar.CustomViews.CustomGridView;
import tv.merabihar.app.merabihar.CustomViews.SnackbarViewer;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Model.InterestContentMapping;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.InterestListScreen;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.SearchScreens.SearchActivity;
import tv.merabihar.app.merabihar.Util.Constants;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ContentAPI;
import tv.merabihar.app.merabihar.WebAPI.InterestAPI;

public class TabSearchActivity2 extends AppCompatActivity {

    RecyclerView contentsView;
    CardView mSearch;
    RecyclerView mTrendingInterest;


    TextViewSFProDisplaySemibold mViewTags;
    ProgressBar progressBar, mCategoryProgressBar;
    LinearLayoutManager verticalLinearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tab_search2);


        /*dummy contents for testing*/

        contentsView = findViewById(R.id.category_layout);
        mSearch = (CardView) findViewById(R.id.linear);
        mTrendingInterest = (RecyclerView) findViewById(R.id.trending_tags);
        mViewTags = (TextViewSFProDisplaySemibold) findViewById(R.id.view_tags);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_content);
        mCategoryProgressBar = (ProgressBar) findViewById(R.id.progressBar_catg);
        mTrendingInterest.setLayoutManager(new LinearLayoutManager(TabSearchActivity2.this, LinearLayoutManager.HORIZONTAL, false));
        mTrendingInterest.setNestedScrollingEnabled(false);
        contentsView.setNestedScrollingEnabled(false);
        verticalLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        Thread interest = new Thread() {
            public void run() {
                getTrendingInterest();
            }
        };

        Thread contents = new Thread() {
            public void run() {
                loadFirstSetOfContents();
            }
        };

        if (Util.isNetworkAvailable(TabSearchActivity2.this)) {
            interest.start();
            contents.start();
        }
        else{
                progressBar.setVisibility(View.GONE);
            mCategoryProgressBar.setVisibility(View.GONE);
            SnackbarViewer.showSnackbar(findViewById(R.id.tab_search_activity_ll),"No Internet connection");
//            Toast.makeText(this, "no connection", Toast.LENGTH_SHORT).show();
//            Log.e("NO Connection found", "xxxxxxxxxxx");
        }


        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent search = new Intent(TabSearchActivity2.this, SearchActivity.class);
                startActivity(search);
            }
        });

        mViewTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent search = new Intent(TabSearchActivity2.this, InterestListScreen.class);
                startActivity(search);
            }
        });

    }

    public void getTrendingInterest()
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final InterestAPI categoryAPI = Util.getClient().create(InterestAPI.class);
                Call<ArrayList<InterestContentMapping>> getCat = categoryAPI.getTrendingInterest();
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<InterestContentMapping>>() {

                    @Override
                    public void onResponse(Call<ArrayList<InterestContentMapping>> call, Response<ArrayList<InterestContentMapping>> response) {



                        if(response.code() == 200 && response.body()!= null)
                        {
                            progressBar.setVisibility(View.GONE);

                            if(response.body().size()!=0){

                                TrendingIntrestAdapter adapters = new TrendingIntrestAdapter(TabSearchActivity2.this,response.body());
                                mTrendingInterest.setAdapter(adapters);

                            }

                        }else{

                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<InterestContentMapping>> call, Throwable t) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(TabSearchActivity2.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public void loadFirstSetOfContents()
    {

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ContentAPI categoryAPI = Util.getClient().create(ContentAPI.class);
                Call<ArrayList<Contents>> getCat = categoryAPI.getContentByCityId(Constants.CITY_ID);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Contents>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {


                        System.out.println("Content inside"+response.code());
                        if(response.code() == 200 && response.body()!= null)
                        {
                            System.out.println("Content inside"+response.body().size());


                            ArrayList<ArrayList<Contents>> contentList = new ArrayList<>();
                            ArrayList<Contents> contents = new ArrayList<>();
                            if( response.body().size()!= 0){

                                System.out.println("Content inside");

                                int count = 0;

                                for (Contents  content:response.body()) {


                                    if(content.getContentType().equalsIgnoreCase("Image")){
                                        contents.add(content);
                                        count = count+1;
                                        if(count==9){
                                            contentList.add(contents);
                                            count=0;
                                            contents = new ArrayList<>();
                                        }
                                    }



                                }

                                if(contentList!=null&&contentList.size()!=0){
                                    FollowFragmentContentAdapter followFragmentContentAdapter = new FollowFragmentContentAdapter(TabSearchActivity2.this, contentList);
                                    contentsView.setLayoutManager(verticalLinearLayoutManager);
                                    contentsView.setHasFixedSize(true);
                                    contentsView.setAdapter(followFragmentContentAdapter);
                                    mCategoryProgressBar.setVisibility(View.INVISIBLE);

                                }else{
                                    mCategoryProgressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(TabSearchActivity2.this, "No Contents", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                mCategoryProgressBar.setVisibility(View.INVISIBLE);

                            }



                        }else{
                            mCategoryProgressBar.setVisibility(View.INVISIBLE);

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Contents>> call, Throwable t) {

                        mCategoryProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(TabSearchActivity2.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

                //System.out.println(TAG+" thread started");
            }

        });

    }
}
