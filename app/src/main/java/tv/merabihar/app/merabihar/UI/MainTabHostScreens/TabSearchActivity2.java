package tv.merabihar.app.merabihar.UI.MainTabHostScreens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.ContentAdapterVertical;
import tv.merabihar.app.merabihar.Adapter.ContentSearchPaginationAdapter;
import tv.merabihar.app.merabihar.Adapter.FollowFragmentContentAdapter;
import tv.merabihar.app.merabihar.Adapter.TrendingIntrestAdapter;
import tv.merabihar.app.merabihar.CustomFonts.TextViewSFProDisplaySemibold;
import tv.merabihar.app.merabihar.CustomInterface.PageScrollListener;
import tv.merabihar.app.merabihar.CustomViews.CustomGridView;
import tv.merabihar.app.merabihar.CustomViews.SnackbarViewer;
import tv.merabihar.app.merabihar.DataBase.DataBaseHelper;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Model.InterestContentMapping;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Service.ContentDataBaseService;
import tv.merabihar.app.merabihar.UI.Activity.ContentListScreen;
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

        setContentView(R.layout.activity_tab_search2);


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

        contentsView.setLayoutManager(verticalLinearLayoutManager);

        contentsView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ContentSearchPaginationAdapter(TabSearchActivity2.this);
        contentsView.setAdapter(adapter);

        db = new DataBaseHelper(TabSearchActivity2.this);

        waitForGarbageCollector(new Runnable() {
            @Override
            public void run() {

                Thread interest = new Thread() {
                    public void run() {
                        getTrendingInterest();
                    }
                };

                Thread contentss = new Thread() {
                    public void run() {
                        loadFirstSetOfBlogs();
                    }
                };

                if (Util.isNetworkAvailable(TabSearchActivity2.this)) {

                    if(db.getContents()!=null&&db.getContents().size()!=0){

                        ArrayList<ArrayList<Contents>> contentList = new ArrayList<>();
                        ArrayList<Contents> contents = new ArrayList<>();
                        int count = 0;
                        ArrayList<Contents> contentsList = db.getContents();

                        Collections.shuffle(contentsList);

                        for (Contents content :contentsList) {


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
                        Toast.makeText(TabSearchActivity2.this, "No Contents in db", Toast.LENGTH_SHORT).show();
                    }
                    interest.start();
                    contentss.start();
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    mCategoryProgressBar.setVisibility(View.GONE);
                    SnackbarViewer.showSnackbar(findViewById(R.id.tab_search_activity_ll),"No Internet connection");
                    if(db.getContents()!=null&&db.getContents().size()!=0){

                        ArrayList<ArrayList<Contents>> contentList = new ArrayList<>();
                        ArrayList<Contents> contents = new ArrayList<>();
                        int count = 0;
                        ArrayList<Contents> contentsList = db.getContents();

                        Collections.shuffle(contentsList);

                        for (Contents content :contentsList) {


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
                        Toast.makeText(TabSearchActivity2.this, "No Contents in db", Toast.LENGTH_SHORT).show();
                    }
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

                contentsView.addOnScrollListener(new PageScrollListener(verticalLinearLayoutManager) {
                    @Override
                    protected void loadMoreItems() {
                        isLoading = true;

                        currentPage = currentPage+1;

                        if (Util.isNetworkAvailable(TabSearchActivity2.this)) {
                            loadNextSetOfItems();

                        }else{
                            Toast.makeText(TabSearchActivity2.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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

//                        Toast.makeText(TabSearchActivity2.this,t.getMessage(),Toast.LENGTH_SHORT).show();
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


                                    //if(content.getContentType().equalsIgnoreCase("Image")){
                                        contents.add(content);
                                        count = count+1;
                                        if(count==9){
                                            contentList.add(contents);
                                            count=0;
                                            contents = new ArrayList<>();
                                        }
                                   // }



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
//                        Toast.makeText(TabSearchActivity2.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
                //System.out.println(TAG+" thread started");
            }

        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    private void goBack()
    {
        Intent intent = null;

        intent = new Intent(TabSearchActivity2.this,TabMainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        TabSearchActivity2.this.finish();
    }



    public void loadFirstSetOfBlogs() {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ContentAPI bookingApi = Util.getClient().create(ContentAPI.class);

                Call<ArrayList<Contents>> getAllBookings = bookingApi.
                        getContentPageByCityId(Constants.CITY_ID,currentPage,9);

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

                                            if (contentList != null && contentList.size() != 0) {
                                                loadFirstPage(contentList);
                                            }

                                            if(db.getContents()!=null&&db.getContents().size()!=0){

                                                Intent intent = new Intent(TabSearchActivity2.this, ContentDataBaseService.class);
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
                                                Intent intent = new Intent(TabSearchActivity2.this, ContentDataBaseService.class);
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
                        getContentPageByCityId(Constants.CITY_ID,currentPage,9);

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

                                                Intent intent = new Intent(TabSearchActivity2.this, ContentDataBaseService.class);
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
                                                Intent intent = new Intent(TabSearchActivity2.this, ContentDataBaseService.class);
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
