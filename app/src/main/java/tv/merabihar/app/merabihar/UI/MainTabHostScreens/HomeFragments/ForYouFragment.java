package tv.merabihar.app.merabihar.UI.MainTabHostScreens.HomeFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.ContentRecyclerAdapter;
import tv.merabihar.app.merabihar.Adapter.ContentRecyclerHorizontal;
import tv.merabihar.app.merabihar.CustomInterface.OnBottomReachedListener;
import tv.merabihar.app.merabihar.CustomInterface.PageScrollListener;
import tv.merabihar.app.merabihar.Model.CategoryAndContentList;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.Constants;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.CategoryApi;
import tv.merabihar.app.merabihar.WebAPI.ContentAPI;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForYouFragment extends Fragment {

    RecyclerView mCategoryContents,mTrendingContents;
    LinearLayout progressBar;

    ArrayList<CategoryAndContentList> categoryAndContentList;
    ArrayList<Contents> contentsList;

    ContentRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    private static final int PAGE_START =1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 10 ;
    private int currentPage = PAGE_START;

    private String TAG="BlogList";

    private boolean _hasLoadedOnce = false;

    public ForYouFragment() {
        // Required empty public constructor
    }

    public static ForYouFragment newInstance() {
        ForYouFragment fragment = new ForYouFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try{

            View view = inflater.inflate(R.layout.fragment_for_you, container, false);


            progressBar = (LinearLayout) view.findViewById(R.id.progress_loding);
            mCategoryContents = (RecyclerView) view.findViewById(R.id.all_contents);

            adapter = new ContentRecyclerAdapter(getActivity());

            linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
            mCategoryContents.setLayoutManager(linearLayoutManager);
            mCategoryContents.setNestedScrollingEnabled(false);

            mTrendingContents = (RecyclerView) view.findViewById(R.id.trending_contents);
            mTrendingContents.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            mTrendingContents.setNestedScrollingEnabled(false);

            mCategoryContents.setItemAnimator(new DefaultItemAnimator());

            mCategoryContents.setAdapter(adapter);
            mCategoryContents.setOnScrollListener(new PageScrollListener(linearLayoutManager) {
                @Override
                protected void loadMoreItems() {
                    isLoading = true;

                    currentPage = currentPage+1;
                    loadNextSetOfItems();
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



            getTrendingContent();
            loadFirstSetOfContents();


            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }


    public void loadFirstSetOfContents()
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ContentAPI categoryAPI = Util.getClient().create(ContentAPI.class);
                Call<ArrayList<Contents>> getCat = categoryAPI.getContentPageByCityId(Constants.CITY_ID,1,5);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Contents>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {



                        if(response.code() == 200 && response.body()!= null)
                        {
                            progressBar.setVisibility(View.GONE);


                            if( response.body().size()!= 0){
                                System.out.println("Response Body size = "+response.body().size());
                                loadFirstPage(response.body());
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

                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public void getTrendingContent()
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ContentAPI categoryAPI = Util.getClient().create(ContentAPI.class);
                Call<ArrayList<Contents>> getCat = categoryAPI.getTrendingContent();
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Contents>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {



                        if(response.code() == 200 && response.body()!= null)
                        {
                            progressBar.setVisibility(View.GONE);

                            if(response.body().size()!=0){

                                ContentRecyclerHorizontal adapters = new ContentRecyclerHorizontal(getActivity(),response.body());
                                mTrendingContents.setAdapter(adapters);
                            }

                        }else{

                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Contents>> call, Throwable t) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
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

    public void loadNextSetOfItems() {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ContentAPI bookingApi = Util.getClient().create(ContentAPI.class);

                Call<ArrayList<Contents>> getAllBookings = bookingApi.
                        getContentPageByCityId(Constants.CITY_ID,currentPage,5);

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
                                Log.d(TAG, "loadNextPage: " + currentPage+" == "+"FALSE = "+response.body().size());
                                adapter.removeLoadingFooter();
                                isLastPage = true;

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
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);

        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isFragmentVisible_ && !_hasLoadedOnce) {
                if (Util.isNetworkAvailable(getActivity())) {

                    mCategoryContents.setOnScrollListener(new PageScrollListener(linearLayoutManager) {

                        @Override
                        protected void loadMoreItems() {
                            isLoading = true;

                            currentPage = currentPage+1;
                            loadNextSetOfItems();
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
                _hasLoadedOnce = true;
            }
        }
    }


}
