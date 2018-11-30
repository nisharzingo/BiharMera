package tv.merabihar.app.merabihar.UI.MainTabHostScreens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.CategoryGridAdapter;
import tv.merabihar.app.merabihar.Adapter.ContentImageAdapter;
import tv.merabihar.app.merabihar.Adapter.ContentSearchPaginationAdapter;
import tv.merabihar.app.merabihar.Adapter.FollowFragmentContentAdapter;
import tv.merabihar.app.merabihar.Adapter.MultiContentImageAdapter;
import tv.merabihar.app.merabihar.Adapter.TrendingIntrestAdapter;
import tv.merabihar.app.merabihar.Adapter.VideoFragmentAdapter;
import tv.merabihar.app.merabihar.CustomFonts.TextViewSFProDisplaySemibold;
import tv.merabihar.app.merabihar.CustomInterface.PageScrollListener;
import tv.merabihar.app.merabihar.CustomViews.CustomGridView;
import tv.merabihar.app.merabihar.CustomViews.SnackbarViewer;
import tv.merabihar.app.merabihar.DataBase.DataBaseHelper;
import tv.merabihar.app.merabihar.Model.Category;
import tv.merabihar.app.merabihar.Model.ContentImages;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Model.InterestContentMapping;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Service.ContentDataBaseService;
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
    LinearLayoutManager verticalLinearLayoutManager;

    ContentSearchPaginationAdapter adapter;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES ;
    private int currentPage = PAGE_START;

    private String TAG="BlogList";

    DataBaseHelper db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_tab_video_new_design);

            mCategoryLayout = (LinearLayout) findViewById(R.id.category_layout);
            mCategoryLayout.setVisibility(View.GONE);
            mTrendingInterest = (RecyclerView)findViewById(R.id.trending_videoes);
            mCategories = (CustomGridView) findViewById(R.id.category_grid_view);

            mImagesList = (RecyclerView) findViewById(R.id.image_list);
            verticalLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            //mImagesList.setLayoutManager(new LinearLayoutManager(TabVideoNewDesign.this, LinearLayoutManager.VERTICAL, false));
            mImagesList.setNestedScrollingEnabled(false);

            progressBar = (ProgressBar) findViewById(R.id.progressBar_content);
            mTrendingInterest.setLayoutManager(new LinearLayoutManager(TabVideoNewDesign.this, LinearLayoutManager.HORIZONTAL, false));
            mTrendingInterest.setNestedScrollingEnabled(false);

            mImagesList.setLayoutManager(verticalLinearLayoutManager);

            mImagesList.setItemAnimator(new DefaultItemAnimator());
            adapter = new ContentSearchPaginationAdapter(TabVideoNewDesign.this);
            mImagesList.setAdapter(adapter);

            db = new DataBaseHelper(TabVideoNewDesign.this);


            waitForGarbageCollector(new Runnable() {
                @Override
                public void run() {

                    Thread category = new Thread() {
                        public void run() {
                            getCategories();
                        }
                    };

                    Thread video = new Thread() {
                        public void run() {

                            loadFirstSetOfBlogs();
                        }
                    };



                    if (Util.isNetworkAvailable(TabVideoNewDesign.this)) {

                        //video.start();
                        progressBar.setVisibility(View.GONE);
                        if(db.getContentByType("Video")!=null&&db.getContentByType("Video").size()!=0){

                            ArrayList<ArrayList<Contents>> contentList = new ArrayList<>();
                            ArrayList<Contents> contents = new ArrayList<>();
                            int count = 0;

                            ArrayList<Contents> contentsList = db.getContentByType("Video");
                            Collections.shuffle(contentsList);

                            for (Contents content : contentsList) {


                                //if(content.getContentType().equalsIgnoreCase("Image")){
                                contents.add(content);
                                count = count + 1;
                                if (count == 9) {
                                    contentList.add(contents);
                                    ContentImageAdapter blogAdapters = new ContentImageAdapter(TabVideoNewDesign.this,contents);//,pagerModelArrayList);
                                    mTrendingInterest.setAdapter(blogAdapters);
                                    mTrendingInterest.requestFocus();
                                    count = 0;
                                    contents = new ArrayList<>();
                                }
                                // }


                            }

                            if (contentList != null && contentList.size() != 0) {

                                loadNextPageDb(contentList);


                            }


                            progressBar.setVisibility(View.GONE);
                        }
                        loadFirstSetOfBlogs();

                        //category.start();

                    }else{

                        SnackbarViewer.showSnackbar(findViewById(R.id.tab_new_design_ll),"No Internet connection");
                        if(db.getContentByType("Video")!=null&&db.getContentByType("Video").size()!=0){

                            ArrayList<ArrayList<Contents>> contentList = new ArrayList<>();
                            ArrayList<Contents> contents = new ArrayList<>();
                            int count = 0;

                            ArrayList<Contents> contentsList = db.getContentByType("Video");
                            Collections.shuffle(contentsList);

                            for (Contents content : contentsList) {


                                //if(content.getContentType().equalsIgnoreCase("Image")){
                                contents.add(content);
                                count = count + 1;
                                if (count == 9) {
                                    contentList.add(contents);
                                    count = 0;
                                    contents = new ArrayList<>();
                                }
                                // }


                            }

                            if (contentList != null && contentList.size() != 0) {

                                loadNextPageDb(contentList);
                            }


                            progressBar.setVisibility(View.GONE);
                        }else{
                            Toast.makeText(TabVideoNewDesign.this, "No Contents in db", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }



                    mTrendingInterest.requestFocus();

                    mImagesList.addOnScrollListener(new PageScrollListener(verticalLinearLayoutManager) {
                        @Override
                        protected void loadMoreItems() {
                            isLoading = true;

                            currentPage = currentPage+1;

                            if (Util.isNetworkAvailable(TabVideoNewDesign.this)) {
                                loadNextSetOfItems();

                            }else{
                                // SnackbarViewer.showSnackbar(findViewById(R.id.content_list_screen_ll),"No Internet connection");
                                Toast.makeText(TabVideoNewDesign.this, "You are offline", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public int getTotalPageCount() {
                            return currentPage;
                        }

                        @Override
                        public boolean isLastPage() {
                            return isLastPage;
                        }

                        @Override
                        public boolean isLoading() {
                            return isLoading;
                        }
                    });


                }
            });



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

//                        Toast.makeText(TabVideoNewDesign.this,t.getMessage(),Toast.LENGTH_SHORT).show();
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

//                        Toast.makeText(TabVideoNewDesign.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        mCategoryLayout.setVisibility(View.GONE);
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public void loadFirstSetOfContent()
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
//                                    mImagesList.getChildAt(0).requestFocus();
                                    //setListViewHeightBasedOnChildren(mImagesList);

                                    //mImagesList.smoothScrollToPosition(0);

                                }

                                ContentImageAdapter blogAdapters = new ContentImageAdapter(TabVideoNewDesign.this,response.body());//,pagerModelArrayList);
                                mTrendingInterest.setAdapter(blogAdapters);
                                mTrendingInterest.requestFocus();


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

//                        Toast.makeText(TabVideoNewDesign.this,t.getMessage(),Toast.LENGTH_SHORT).show();
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

                        progressBar.setVisibility(View.GONE);
                        System.out.println("Content inside"+response.code());
                        if(response.code() == 200 && response.body()!= null)
                        {
                            System.out.println("Content inside"+response.body().size());


                            ArrayList<ArrayList<Contents>> contentList = new ArrayList<>();
                            ArrayList<Contents> contents = new ArrayList<>();
                            ArrayList<Contents> contentss = new ArrayList<>();
                            if( response.body().size()!= 0){

                                System.out.println("Content inside");

                                int count = 0;

                                for (Contents  content:response.body()) {


                                    if(content.getContentType().equalsIgnoreCase("Video")){
                                        contents.add(content);
                                        contentss.add(content);
                                        count = count+1;
                                        if(count==9){
                                            contentList.add(contents);
                                            count=0;
                                            contents = new ArrayList<>();
                                        }
                                    }



                                }

                                if(contentList!=null&&contentList.size()!=0){
                                    VideoFragmentAdapter followFragmentContentAdapter = new VideoFragmentAdapter(TabVideoNewDesign.this, contentList);
                                    mImagesList.setLayoutManager(verticalLinearLayoutManager);
                                    mImagesList.setHasFixedSize(true);
                                    mImagesList.setAdapter(followFragmentContentAdapter);



                                }else{
                                    Toast.makeText(TabVideoNewDesign.this, "No Contents", Toast.LENGTH_SHORT).show();
                                }

                                if(contentss!=null&&contentss.size()!=0){

                                    System.out.println("Content size "+contentss.size());

                                    ContentImageAdapter blogAdapters = new ContentImageAdapter(TabVideoNewDesign.this,contentss);//,pagerModelArrayList);
                                    mTrendingInterest.setAdapter(blogAdapters);
                                    mTrendingInterest.requestFocus();

                                }else{
                                    Toast.makeText(TabVideoNewDesign.this, "No Contents", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                progressBar.setVisibility(View.GONE);
                            }



                        }else{
                            progressBar.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Contents>> call, Throwable t) {

                        progressBar.setVisibility(View.GONE);

//                        Toast.makeText(TabVideoNewDesign.this,t.getMessage(),Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    private void goBack()
    {
        Intent intent = null;

        intent = new Intent(TabVideoNewDesign.this,TabMainActivity.class);
        //intent.putExtra("TABNAME",3);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        TabVideoNewDesign.this.finish();
    }

    public void loadFirstSetOfBlogs() {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ContentAPI bookingApi = Util.getClient().create(ContentAPI.class);

                Call<ArrayList<Contents>> getAllBookings = bookingApi.
                        getContentByVideoAndCity(Constants.CITY_ID,"Video",currentPage,9);

                getAllBookings.enqueue(new Callback<ArrayList<Contents>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {


                        try {
                            if (response.code() == 200 && response.body() != null) {
                                if (response.body().size() != 0) {
                                    Log.d(TAG, "loadFirstPage: " + response.message());
                                    ArrayList<Contents> approvedBlogs = response.body();

                                    if (approvedBlogs != null && approvedBlogs.size() != 0) {


                                        ArrayList<ArrayList<Contents>> contentList = new ArrayList<>();
                                        ArrayList<Contents> contents = new ArrayList<>();
                                        if (response.body().size() != 0) {

                                            System.out.println("Content inside");

                                            int count = 0;

                                            for (Contents content : response.body()) {


                                                //if(content.getContentType().equalsIgnoreCase("Image")){
                                                contents.add(content);
                                                count = count + 1;
                                                if (count == 9) {
                                                    contentList.add(contents);
                                                    count = 0;
                                                    contents = new ArrayList<>();
                                                }
                                                // }


                                            }

                                            mTrendingInterest.removeAllViews();
                                            ContentImageAdapter blogAdapters = new ContentImageAdapter(TabVideoNewDesign.this,response.body());//,pagerModelArrayList);
                                            mTrendingInterest.setAdapter(blogAdapters);
                                            mTrendingInterest.requestFocus();

                                            if (contentList != null && contentList.size() != 0) {
                                                loadFirstPage(contentList);
                                            }

                                            if(db.getContents()!=null&&db.getContents().size()!=0){

                                                Intent intent = new Intent(TabVideoNewDesign.this, ContentDataBaseService.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("ContentList",approvedBlogs);
                                                startService(intent);

                                                for (Contents content:approvedBlogs) {

                                                    if(db.getContentById(content.getContentId())!=null){

                                                        db.updateContents(content);
                                                        System.out.println("Data Base Update Service");

                                                    }else{
                                                        db.addContents(content);
                                                        System.out.println("Data Base add Service");

                                                    }

                                                }

                                            }else{

                                                for (Contents content:approvedBlogs) {
                                                    db.addContents(content);
                                                }
                                                Intent intent = new Intent(TabVideoNewDesign.this, ContentDataBaseService.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("ContentList",approvedBlogs);
                                                startService(intent);
                                            }



                                        } else {
                                            isLoading = true;

                                            currentPage = currentPage + 1;
                                            loadNextSetOfItems();
                                        }

                                    } else {
                                        adapter.removeLoadingFooter();
                                        isLastPage = true;
                                        isLoading = true;
                                        progressBar.setVisibility(View.GONE);
                                    }

                                } else {

                                }
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

    private void loadFirstPage(ArrayList<ArrayList<Contents>> list) {
        Log.d(TAG, "loadFirstPage: "+list.size());
        //Collections.reverse(list);
        progressBar.setVisibility(View.GONE);
        adapter.addAlls(list);

        if (list != null && list.size() !=0)
            adapter.addLoadingFooter();
        else
            isLastPage = true;

    }

    public void loadNextSetOfItems() {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ContentAPI bookingApi = Util.getClient().create(ContentAPI.class);

                Call<ArrayList<Contents>> getAllBookings = bookingApi.
                        getContentByVideoAndCity(Constants.CITY_ID,"Video",currentPage,9);

                getAllBookings.enqueue(new Callback<ArrayList<Contents>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {


                        try{
                            if(response.code() == 200 && response.body()!= null)
                            {
                                if(response.body().size() != 0) {

                                    ArrayList<Contents> approvedBlogs = response.body();


                                    if(approvedBlogs!=null&&approvedBlogs.size()!=0){

                                        ArrayList<ArrayList<Contents>> contentList = new ArrayList<>();
                                        ArrayList<Contents> contents = new ArrayList<>();
                                        if (response.body().size() != 0) {

                                            System.out.println("Content inside");

                                            int count = 0;

                                            for (Contents content : response.body()) {


                                                //if(content.getContentType().equalsIgnoreCase("Image")){
                                                contents.add(content);
                                                count = count + 1;
                                                if (count == 9) {
                                                    contentList.add(contents);
                                                    count = 0;
                                                    contents = new ArrayList<>();
                                                }
                                                // }


                                            }

                                            if (contentList != null && contentList.size() != 0) {
                                                loadNextPage(contentList);
                                            }

                                            if(db.getContents()!=null&&db.getContents().size()!=0){

                                                Intent intent = new Intent(TabVideoNewDesign.this, ContentDataBaseService.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("ContentList",approvedBlogs);
                                                startService(intent);

                                                for (Contents content:approvedBlogs) {

                                                    if(db.getContentById(content.getContentId())!=null){

                                                        db.updateContents(content);
                                                        System.out.println("Data Base Update Service");

                                                    }else{
                                                        db.addContents(content);
                                                        System.out.println("Data Base add Service");

                                                    }

                                                }

                                            }else{

                                                for (Contents content:approvedBlogs) {
                                                    db.addContents(content);
                                                }
                                                Intent intent = new Intent(TabVideoNewDesign.this, ContentDataBaseService.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("ContentList",approvedBlogs);
                                                startService(intent);
                                            }
                                        }
                                    }else{
                                        isLoading = true;

                                        currentPage = currentPage+1;
                                        loadNextSetOfItems();
                                    }

                                }
                                else
                                {
                                    Log.d(TAG, "loadNextPage: " + currentPage+" == "+"FALSE = "+response.body().size());
                                    adapter.removeLoadingFooter();
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

    private void loadNextPage(ArrayList<ArrayList<Contents>> list) {
        //Collections.reverse(list);
        adapter.removeLoadingFooter();
        isLoading = false;

        adapter.addAll(list);

        if (list != null && list.size() !=0)
        {
            adapter.addLoadingFooter();
            Log.d(TAG, "loadNextPage: " + currentPage+" == "+isLastPage);
        }
        else
        {
            isLastPage = true;
            Log.d(TAG, "loadNextPage: " + currentPage+" == "+isLastPage);
        }
    }

    private void loadNextPageDb(ArrayList<ArrayList<Contents>> list) {
        Log.d(TAG, "loadFirstPage: "+list.size());
        //Collections.reverse(list);
        progressBar.setVisibility(View.GONE);
        adapter.addAll(list);

        if (list != null && list.size() !=0)
            adapter.addLoadingFooter();
        else
            isLastPage = true;

    }

    public static void waitForGarbageCollector(final Runnable callback) {

        Runtime runtime;
        long maxMemory;
        long usedMemory;
        double availableMemoryPercentage = 1.0;
        final double MIN_AVAILABLE_MEMORY_PERCENTAGE = 0.1;
        final int DELAY_TIME = 5 * 1000;

        runtime =
                Runtime.getRuntime();

        maxMemory =
                runtime.maxMemory();

        usedMemory =
                runtime.totalMemory() -
                        runtime.freeMemory();

        availableMemoryPercentage =
                1 -
                        (double) usedMemory /
                                maxMemory;

        if (availableMemoryPercentage < MIN_AVAILABLE_MEMORY_PERCENTAGE) {

            try {
                Thread.sleep(DELAY_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            waitForGarbageCollector(
                    callback);
        } else {

            // Memory resources are availavle, go to next operation:

            callback.run();
        }
    }

}
