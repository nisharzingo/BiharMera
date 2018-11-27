package tv.merabihar.app.merabihar.UI.MainTabHostScreens.HomeFragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.ContentAdapterVertical;
import tv.merabihar.app.merabihar.CustomInterface.PageScrollListener;
import tv.merabihar.app.merabihar.CustomViews.SnackbarViewer;
import tv.merabihar.app.merabihar.DataBase.DataBaseHelper;
import tv.merabihar.app.merabihar.Model.CategoryAndContentList;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Service.ContentDataBaseService;
import tv.merabihar.app.merabihar.Service.VideoWatchedService;
import tv.merabihar.app.merabihar.UI.Activity.ContentDetailScreen;
import tv.merabihar.app.merabihar.UI.Activity.ContentListScreen;
import tv.merabihar.app.merabihar.Util.Constants;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ContentAPI;


public class ForYouNewFragment extends Fragment {

    SwipeRefreshLayout pullToRefresh;
    View view;
    private static RecyclerView mtopBlogs;
    ProgressBar progressBar;

    ArrayList<Contents> blogsList;
    LinearLayoutManager linearLayoutManager;

    ContentAdapterVertical adapter;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES ;
    private int currentPage = PAGE_START;

    private String TAG="BlogList";

    DataBaseHelper db ;
    int contentId = 0;

    public ForYouNewFragment() {
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

        db = new DataBaseHelper(getActivity());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try{

             view = inflater.inflate(R.layout.fragment_for_you_new, container, false);


            mtopBlogs = (RecyclerView) view.findViewById(R.id.top_blogs_viewpager);
            progressBar = (ProgressBar) view.findViewById(R.id.blog_progress);
           pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.follow_for_u_new);

            linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
            mtopBlogs.setLayoutManager(linearLayoutManager);

            mtopBlogs.setItemAnimator(new DefaultItemAnimator());
            adapter = new ContentAdapterVertical(getActivity());
            mtopBlogs.setAdapter(adapter);


            /*SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(mtopBlogs);*/



            //getBlogs();





            if(Util.isNetworkAvailable(getActivity())){

                if(db.getContents()!=null&&db.getContents().size()!=0){

                    Collections.sort(db.getContents(),Contents.compareContent);
                    loadNextPage(db.getContents());
                    progressBar.setVisibility(View.GONE);
                }else{
                    //Toast.makeText(getActivity(), "No Contents in db", Toast.LENGTH_SHORT).show();
                }

                loadFirstSetOfBlogs();
//                System.out.println("Db size"+db.getContents().size());
            }else{
                System.out.println("Db size"+db.getContents().size());
                if(db.getContents()!=null&&db.getContents().size()!=0){
                    loadNextPage(db.getContents());
                    progressBar.setVisibility(View.GONE);
                }else{
                    Toast.makeText(getActivity(), "No Contents in db", Toast.LENGTH_SHORT).show();
                }
            }

            mtopBlogs.setOnScrollListener(new PageScrollListener(linearLayoutManager) {
                @Override
                protected void loadMoreItems() {
                    isLoading = false;

                    currentPage = currentPage+1;
                    loadNextSetOfItems();

                  /*  if (Util.isNetworkAvailable(getActivity())) {
                        loadNextSetOfItems();

                    }else{
                        SnackbarViewer.showSnackbar(view.findViewById(R.id.follow_for_u_new),"No Internet connection");
                        progressBar.setVisibility(View.GONE);
                    }*/
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

            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                int Refreshcounter = 1; //Counting how many times user have refreshed the layout

                @Override
                public void onRefresh() {

                    pullToRefresh.setRefreshing(false);
                }
            });
            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public void loadFirstSetOfBlogs() {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ContentAPI bookingApi = Util.getClient().create(ContentAPI.class);

                Call<ArrayList<Contents>> getAllBookings = bookingApi.
                        getContentPageByCityId(Constants.CITY_ID,currentPage,5);

                getAllBookings.enqueue(new Callback<ArrayList<Contents>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {

                        progressBar.setVisibility(View.GONE);
                        try{
                            if(response.code() == 200 && response.body()!= null)
                            {
                                if(response.body().size() != 0) {
                                    Log.d(TAG, "loadFirstPage: "+response.message());
                                    ArrayList<Contents> approvedBlogs = response.body();

                                    if(approvedBlogs!=null&&approvedBlogs.size()!=0){
                                        loadFirstPage(approvedBlogs);
                                        contentId = approvedBlogs.get(0).getContentId();

                                        if(db.getContents()!=null&&db.getContents().size()!=0){



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
                                        }
                                    }else{
                                        isLoading = true;

                                        currentPage = currentPage+1;
                                        loadNextSetOfItems();
                                    }

                                }
                                else
                                {
                                    adapter.removeLoadingFooter();
                                    isLastPage = true;
                                    isLoading = true;
                                    progressBar.setVisibility(View.GONE);
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
                        progressBar.setVisibility(View.GONE);
                    }
                });

                //WebService.getAllBookings(PreferenceHandler.getInstance(getActivity()).getHotelID());
            }
        });

    }

    public void loadFirstSetOfBlogse() {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ContentAPI bookingApi = Util.getClient().create(ContentAPI.class);

                Call<ArrayList<Contents>> getAllBookings = bookingApi.
                        getContentPageByCityId(Constants.CITY_ID,currentPage,5);

                getAllBookings.enqueue(new Callback<ArrayList<Contents>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {

                        progressBar.setVisibility(View.GONE);
                        try{
                            if(response.code() == 200 && response.body()!= null)
                            {
                                if(response.body().size() != 0) {
                                    Log.d(TAG, "loadFirstPage: "+response.message());
                                    ArrayList<Contents> approvedBlogs = response.body();

                                    if(approvedBlogs!=null&&approvedBlogs.size()!=0){
                                        loadFirstPage(approvedBlogs);
                                        contentId = approvedBlogs.get(0).getContentId();

                                        if(db.getContents()!=null&&db.getContents().size()!=0){



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
                                        }
                                    }else{
                                        isLoading = true;

                                        currentPage = currentPage+1;
                                        loadNextSetOfItems();
                                    }

                                }
                                else
                                {
                                    adapter.removeLoadingFooter();
                                    isLastPage = true;
                                    isLoading = true;
                                    progressBar.setVisibility(View.GONE);
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
                        progressBar.setVisibility(View.GONE);
                    }
                });

                //WebService.getAllBookings(PreferenceHandler.getInstance(getActivity()).getHotelID());
            }
        });

    }

    private void loadFirstPage(ArrayList<Contents> list) {
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
                        getContentPageByCityId(Constants.CITY_ID,currentPage,5);

                getAllBookings.enqueue(new Callback<ArrayList<Contents>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {

                        try{
                            if(response.code() == 200 && response.body()!= null)
                            {
                                if(response.body().size() != 0) {

                                    ArrayList<Contents> approvedBlogs = response.body();


                                    if(approvedBlogs!=null&&approvedBlogs.size()!=0){
                                        isLoading = false;
                                        loadNextPage(approvedBlogs);

                                        if(db.getContents()!=null&&db.getContents().size()!=0){



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

                                        }

                                    }else{
                                        isLoading = false;

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

    private void loadNextPage(ArrayList<Contents> list) {
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
            isLastPage = false;
            Log.d(TAG, "loadNextPage: " + currentPage+" == "+isLastPage);
        }
    }

    private void loadNextPageDb(ArrayList<Contents> list) {
        //Collections.reverse(list);
        adapter.removeLoadingFooter();
        isLoading = false;

        adapter.addAll(list);

        if (list != null && list.size() !=0)
        {
            //adapter.addLoadingFooter();
            Log.d(TAG, "loadNextPage: " + currentPage+" == "+isLastPage);
        }

    }

}