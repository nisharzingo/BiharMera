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

   // SwipeRefreshLayout pullToRefresh;
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

 //  DataBaseHelper db ;

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

      //  db = new DataBaseHelper(getActivity());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try{

             view = inflater.inflate(R.layout.fragment_for_you_new, container, false);


            mtopBlogs = (RecyclerView) view.findViewById(R.id.top_blogs_viewpager);
            progressBar = (ProgressBar) view.findViewById(R.id.blog_progress);
          //  pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);

            linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
            mtopBlogs.setLayoutManager(linearLayoutManager);

            mtopBlogs.setItemAnimator(new DefaultItemAnimator());
            adapter = new ContentAdapterVertical(getActivity());
            mtopBlogs.setAdapter(adapter);


            /*SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(mtopBlogs);*/

            mtopBlogs.setOnScrollListener(new PageScrollListener(linearLayoutManager) {
                @Override
                protected void loadMoreItems() {
                    isLoading = true;

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

            //getBlogs();



            /*pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                int Refreshcounter = 1; //Counting how many times user have refreshed the layout

                @Override
                public void onRefresh() {

                    if (Util.isNetworkAvailable(getActivity())) {

                        //adapter.re
                        mtopBlogs.removeAllViews();
                        adapter = new ContentAdapterVertical(getActivity());
                        currentPage = PAGE_START;
                        loadFirstSetOfBlogs();
                    }else{
                        progressBar.setVisibility(View.GONE);
                        SnackbarViewer.showSnackbar(view.findViewById(R.id.follow_for_u_new),"No Internet connection");

                        //System.out.println("Database Size = "+db.getContents().size());

                       *//* if(db.getContents()!=null&&db.getContents().size()!=0){
                            progressBar.setVisibility(View.GONE);
                            adapter.addAll(db.getContents());

                            if (db.getContents() != null && db.getContents().size() !=0)
                                adapter.addLoadingFooter();
                            else
                                isLastPage = true;
                        }else{

                        }*//*
                    }

                    pullToRefresh.setRefreshing(false);
                }
            });
*/

            loadFirstSetOfBlogs();
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
                        getContentPageByCityId(Constants.CITY_ID,currentPage,3);

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

                                        /*if(db.getContents()!=null&&db.getContents().size()!=0){

                                            Intent intent = new Intent(getActivity(), ContentDataBaseService.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("ContentList",approvedBlogs);
                                            getActivity().startService(intent);

                                        }else{

                                            //db.addContents();
                                            Intent intent = new Intent(getActivity(), ContentDataBaseService.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("ContentList",approvedBlogs);
                                            getActivity().startService(intent);
                                        }*/
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
                        getContentPageByCityId(Constants.CITY_ID,currentPage,3);

                getAllBookings.enqueue(new Callback<ArrayList<Contents>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {

                        try{
                            if(response.code() == 200 && response.body()!= null)
                            {
                                if(response.body().size() != 0) {

                                    ArrayList<Contents> approvedBlogs = response.body();


                                    if(approvedBlogs!=null&&approvedBlogs.size()!=0){
                                        loadNextPage(approvedBlogs);

                                        /*if(db.getContents()!=null&&db.getContents().size()!=0){

                                            Intent intent = new Intent(getActivity(), ContentDataBaseService.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("ContentList",approvedBlogs);
                                            getActivity().startService(intent);

                                        }else{

                                            //db.addContents();
                                            Intent intent = new Intent(getActivity(), ContentDataBaseService.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("ContentList",approvedBlogs);
                                            getActivity().startService(intent);
                                        }*/

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
            isLastPage = true;
            Log.d(TAG, "loadNextPage: " + currentPage+" == "+isLastPage);
        }
    }

}