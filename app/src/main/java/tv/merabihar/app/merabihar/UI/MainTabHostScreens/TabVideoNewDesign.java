package tv.merabihar.app.merabihar.UI.MainTabHostScreens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.CategoryGridAdapter;
import tv.merabihar.app.merabihar.Adapter.ContentImageAdapter;
import tv.merabihar.app.merabihar.Adapter.MultiContentImageAdapter;
import tv.merabihar.app.merabihar.Adapter.TrendingIntrestAdapter;
import tv.merabihar.app.merabihar.CustomFonts.TextViewSFProDisplaySemibold;
import tv.merabihar.app.merabihar.CustomViews.CustomGridView;
import tv.merabihar.app.merabihar.Model.Category;
import tv.merabihar.app.merabihar.Model.ContentImages;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Model.InterestContentMapping;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.InterestListScreen;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.SearchScreens.SearchActivity;
import tv.merabihar.app.merabihar.UI.YoutubePlayList.YouTubeFragment;
import tv.merabihar.app.merabihar.UI.YoutubePlayList.YouTubeListScreen;
import tv.merabihar.app.merabihar.Util.Constants;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.CategoryApi;
import tv.merabihar.app.merabihar.WebAPI.ContentAPI;
import tv.merabihar.app.merabihar.WebAPI.InterestAPI;

public class TabVideoNewDesign extends AppCompatActivity {


    RecyclerView mTrendingInterest;
    CustomGridView mCategories;
    LinearLayout mCategoryLayout;
    RecyclerView mImagesList;

    ProgressBar progressBar;

    private static final String FRAGMENT_TAG = VideoYoutubeFragment.class.getSimpleName();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_tab_video_new_design);

            mCategoryLayout = (LinearLayout) findViewById(R.id.category_layout);
            mTrendingInterest = (RecyclerView)findViewById(R.id.trending_videoes);
            mCategories = (CustomGridView) findViewById(R.id.category_grid_view);

            mImagesList = (RecyclerView) findViewById(R.id.image_list);
            mImagesList.setLayoutManager(new LinearLayoutManager(TabVideoNewDesign.this, LinearLayoutManager.VERTICAL, false));
            mImagesList.setNestedScrollingEnabled(false);

            progressBar = (ProgressBar) findViewById(R.id.progressBar_content);
            mTrendingInterest.setLayoutManager(new LinearLayoutManager(TabVideoNewDesign.this, LinearLayoutManager.HORIZONTAL, false));
            mTrendingInterest.setNestedScrollingEnabled(false);
            Fresco.initialize(this);



            Thread category = new Thread() {
                public void run() {
                    getCategories();
                }
            };

            Thread video = new Thread() {
                public void run() {
                    loadFirstSetOfContents();
                }
            };


            category.start();
            video.start();


        mTrendingInterest.requestFocus();


        }catch (Exception e){
            e.printStackTrace();
        }

    }
        //tv.merabihar.app.merabihar tv.merabihar.app.merabiharvideos

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

                                TrendingIntrestAdapter adapters = new TrendingIntrestAdapter(TabVideoNewDesign.this,response.body());
                                mTrendingInterest.setAdapter(adapters);

                            }

                        }else{

                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<InterestContentMapping>> call, Throwable t) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(TabVideoNewDesign.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public void getCategories()
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final CategoryApi categoryAPI = Util.getClient().create(CategoryApi.class);
                Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(Constants.CITY_ID);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Category>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                        if(response.code() == 200)
                        {

                            if(response.body() != null && response.body().size() != 0)
                            {

                                CategoryGridAdapter adapter = new CategoryGridAdapter(TabVideoNewDesign.this,response.body());
                                mCategories.setAdapter(adapter);


                            }
                            else
                            {
                                mCategoryLayout.setVisibility(View.GONE);

                            }
                        }else{
                            mCategoryLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Category>> call, Throwable t) {

                        Toast.makeText(TabVideoNewDesign.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        mCategoryLayout.setVisibility(View.GONE);
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
                Call<ArrayList<Contents>> getCat = categoryAPI.getContentByVideoAndCity(Constants.CITY_ID,"Video",1,50);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Contents>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {

                        ArrayList<String> contentImage = new ArrayList<>();
                        int count = 0;
                        ArrayList<ArrayList<String>> contentImageList = new ArrayList<>();

                        if(response.code() == 200 && response.body()!= null)
                        {

                            progressBar.setVisibility(View.GONE);

                            if( response.body().size()!= 0){
                                // loadFirstPage(response.body());

                                for (Contents content:response.body()) {

                                        String contentImages = "https://img.youtube.com/vi/"+content.getContentURL()+"/0.jpg";

                                            contentImage.add(contentImages);
                                            count++;
                                            if(count==5){

                                                contentImageList.add(contentImage);

                                                count= 0;
                                                contentImage = new ArrayList<>();

                                            }


                                }



                                if(contentImageList!=null&&contentImageList.size()!=0){

                                    MultiContentImageAdapter blogAdapter = new MultiContentImageAdapter(TabVideoNewDesign.this,contentImageList);//,pagerModelArrayList);
                                    mImagesList.setAdapter(blogAdapter);
                                    //setListViewHeightBasedOnChildren(mImagesList);
                                    mTrendingInterest.requestFocus();
                                    //mImagesList.smoothScrollToPosition(0);

                                }

                                ContentImageAdapter blogAdapters = new ContentImageAdapter(TabVideoNewDesign.this,response.body());//,pagerModelArrayList);
                                mTrendingInterest.setAdapter(blogAdapters);


                            }else{

                               /* isLastPage = true;
                                isLoading = true;*/
                                progressBar.setVisibility(View.GONE);

                            }



                        }else{

                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Contents>> call, Throwable t) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(TabVideoNewDesign.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public  void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))+20;
        listView.setLayoutParams(params);
        listView.requestLayout();
        //mImagesList.getChildAt(0).requestFocus();
    }

}
