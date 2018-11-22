package tv.merabihar.app.merabihar.UI.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.ContentAdapterVertical;

import tv.merabihar.app.merabihar.CustomInterface.PageScrollListener;
import tv.merabihar.app.merabihar.CustomViews.SnackbarViewer;
import tv.merabihar.app.merabihar.Model.Category;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.Constants;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ContentAPI;

public class CategoryContentList extends AppCompatActivity {

    RecyclerView mInterestList;
    private ProgressBar progressBar;

    ArrayList<Contents> categoryContents;
    Category category;

    ContentAdapterVertical adapter;
    LinearLayoutManager linearLayoutManager;

    private static final int PAGE_START =1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 10 ;
    private int currentPage = PAGE_START;

    private String TAG="BlogList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_category_content_list);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);


            mInterestList = (RecyclerView)findViewById(R.id.interest_list);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);

            adapter = new ContentAdapterVertical(CategoryContentList.this);

            linearLayoutManager = new LinearLayoutManager(CategoryContentList.this,LinearLayoutManager.VERTICAL,false);
            mInterestList.setLayoutManager(linearLayoutManager);
            mInterestList.setNestedScrollingEnabled(false);

            final Bundle bundle = getIntent().getExtras();
            if(bundle!=null){
                category = (Category) bundle.getSerializable("Category");
            }



            mInterestList.setItemAnimator(new DefaultItemAnimator());

            mInterestList.setAdapter(adapter);

            if(category!=null){
                setTitle("Stories in "+category.getCategoriesName());


                if (Util.isNetworkAvailable(CategoryContentList.this)) {

                    loadFirstSetOfContents(category.getCategoriesId());

                }else{

                    SnackbarViewer.showSnackbar(findViewById(R.id.categ_cont_main_fl),"No Internet connection");
                    progressBar.setVisibility(View.GONE);
                }


                mInterestList.addOnScrollListener(new PageScrollListener(linearLayoutManager) {

                    @Override
                    protected void loadMoreItems() {
                        isLoading = true;

                        currentPage = currentPage+1;
                        if (Util.isNetworkAvailable(CategoryContentList.this)) {

                            loadNextSetOfItems(category.getCategoriesId());


                        }else{

                            SnackbarViewer.showSnackbar(findViewById(R.id.categ_cont_main_fl),"No Internet connection");
                            progressBar.setVisibility(View.GONE);
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
            }else{

                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void loadFirstSetOfContents(final int id)
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ContentAPI categoryAPI = Util.getClient().create(ContentAPI.class);
                Call<ArrayList<Contents>> getCat = categoryAPI.getContentPageByCategoryId(id,1,5);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Contents>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {



                        if(response.code() == 200 && response.body()!= null)
                        {
                            progressBar.setVisibility(View.GONE);


                            if( response.body().size()!= 0){

                                if (Util.isNetworkAvailable(CategoryContentList.this)) {
                                    loadFirstPage(response.body());

                                }else{

                                    SnackbarViewer.showSnackbar(findViewById(R.id.categ_cont_main_fl),"No Internet connection");
                                    progressBar.setVisibility(View.GONE);
                                }


                            }else{
                                adapter.removeLoadingFooter();
                                isLastPage = true;
                                isLoading = true;
                                progressBar.setVisibility(View.GONE);
                            }



                        }else{

                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Contents>> call, Throwable t) {

                        progressBar.setVisibility(View.GONE);

//                        Toast.makeText(CategoryContentList.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    private void loadFirstPage(ArrayList<Contents> list) {
        Log.d(TAG, "loadFirstPage: "+list.size());
        //Collections.reverse(list);
        progressBar.setVisibility(View.GONE);
        //ArrayList<Contents> contents = Contents.
        adapter.addAll(list);

        if (list != null && list.size() !=0)
            adapter.addLoadingFooter();
        else
            isLastPage = true;

    }

    public void loadNextSetOfItems(final int id) {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ContentAPI bookingApi = Util.getClient().create(ContentAPI.class);

                Call<ArrayList<Contents>> getAllBookings = bookingApi.
                        getContentPageByCategoryId(id,currentPage,5);

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

    private void loadNextPage(ArrayList<Contents> list) {
        //Collections.reverse(list);
        adapter.removeLoadingFooter();
        isLoading = false;

        adapter.addAll(list);

        if (list != null && list.size() !=0 )
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                CategoryContentList.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
