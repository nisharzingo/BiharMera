package tv.merabihar.app.merabihar.UI.MainTabHostScreens.HomeFragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.CategoryGridAdapter;
import tv.merabihar.app.merabihar.Adapter.ContentImageAdapter;
import tv.merabihar.app.merabihar.Adapter.ContentSearchPaginationAdapter;
import tv.merabihar.app.merabihar.Adapter.FollowFragmentCategoriesAdapter;
import tv.merabihar.app.merabihar.Adapter.FollowFragmentContentAdapter;
import tv.merabihar.app.merabihar.CustomInterface.PageScrollListener;
import tv.merabihar.app.merabihar.CustomViews.SnackbarViewer;
import tv.merabihar.app.merabihar.DataBase.DataBaseHelper;
import tv.merabihar.app.merabihar.Model.Category;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Service.ContentDataBaseService;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.TabSearchActivity2;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.TabVideoNewDesign;
import tv.merabihar.app.merabihar.Util.Constants;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.CategoryApi;
import tv.merabihar.app.merabihar.WebAPI.ContentAPI;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowFragments extends Fragment {

    Context context;
    ProgressBar mContentProgressBar, mCategoryProgressBar;
    ArrayList<Category> categoryList;

    View mFragmentView;
    RecyclerView categoryRecyclerView, contentRecyclerView ;
    LinearLayoutManager horizontalLinearLayoutManager,verticalLinearLayoutManager;

    ContentSearchPaginationAdapter adapter;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES ;
    private int currentPage = PAGE_START;

    private String TAG="BlogList";

    DataBaseHelper db ;
    public FollowFragments() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context; // to retrieve the context of the  parent class

        categoryList = new ArrayList<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DataBaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mFragmentView =  inflater.inflate(R.layout.fragment_follow_fragments, container, false);
        categoryRecyclerView = mFragmentView.findViewById(R.id.follow_frag_categories_recycler_view);
        contentRecyclerView = mFragmentView.findViewById(R.id.follow_frag_content_recycler_view);
        mCategoryProgressBar = mFragmentView.findViewById(R.id.follow_cat_progressbar);
        mContentProgressBar = mFragmentView.findViewById(R.id.follow_contents_progressbar);

        categoryRecyclerView.setNestedScrollingEnabled(false);
        contentRecyclerView.setNestedScrollingEnabled(false);

        horizontalLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        verticalLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);



        contentRecyclerView.setLayoutManager(verticalLinearLayoutManager);

        contentRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ContentSearchPaginationAdapter(getActivity());
        contentRecyclerView.setAdapter(adapter);


        if (Util.isNetworkAvailable(getActivity())) {

            if(db.getContents()!=null&&db.getContents().size()!=0){

                ArrayList<Contents> contents = db.getContents();

                if(contents!=null && contents.size()!=0) {

                    ArrayList<ArrayList<Contents>> contentList = new ArrayList<>();

                    int count = contents.size();
                    int init = count - 1;

                    while (count >= 9) {

                        ArrayList<Contents> list = new ArrayList<>();

                        for (int i = 0; i < 9; i++) {
                            list.add(contents.get(init));
                            init--;
                        }
                        contentList.add(list);
                        count = count - 9;
                    }

                    if (contentList.size() != 0) {
                        loadNextPageDb(contentList);
                    }
                }




            }

            if(db.getCategories()!=null&&db.getCategories().size()!=0){

                FollowFragmentCategoriesAdapter followCategoriesAdapter = new FollowFragmentCategoriesAdapter(context, db.getCategories());
                categoryRecyclerView.setLayoutManager(horizontalLinearLayoutManager);
                categoryRecyclerView.setHasFixedSize(true);
                categoryRecyclerView.setAdapter(followCategoriesAdapter);

                mCategoryProgressBar.setVisibility(View.INVISIBLE);
            }
            mContentProgressBar.setVisibility(View.GONE);
            mCategoryProgressBar.setVisibility(View.GONE);
            loadFirstSetOfBlogs();
            getCategories();

        }else{

            SnackbarViewer.showSnackbar(mFragmentView.findViewById(R.id.follow_frag_ll_main),"No Internet connection");
//            Toast.makeText(context, "No internet Connection", Toast.LENGTH_SHORT).show();
            mContentProgressBar.setVisibility(View.GONE);
            mCategoryProgressBar.setVisibility(View.GONE);

            if(db.getContents()!=null&&db.getContents().size()!=0){

                ArrayList<Contents> contents = db.getContents();

                if(contents!=null && contents.size()!=0) {

                    ArrayList<ArrayList<Contents>> contentList = new ArrayList<>();

                    int count = contents.size();
                    int init = count - 1;

                    while (count >= 9) {

                        ArrayList<Contents> list = new ArrayList<>();

                        for (int i = 0; i < 9; i++) {
                            list.add(contents.get(init));
                            init--;
                        }
                        contentList.add(list);
                        count = count - 9;
                    }

                    if (contentList.size() != 0) {
                        loadNextPageDb(contentList);
                    }
                }
                mContentProgressBar.setVisibility(View.GONE);


            }else{
                Toast.makeText(getActivity(), "No Contents in db", Toast.LENGTH_SHORT).show();
            }

            if(db.getCategories()!=null&&db.getCategories().size()!=0){

                FollowFragmentCategoriesAdapter followCategoriesAdapter = new FollowFragmentCategoriesAdapter(context, db.getCategories());
                categoryRecyclerView.setLayoutManager(horizontalLinearLayoutManager);
                categoryRecyclerView.setHasFixedSize(true);
                categoryRecyclerView.setAdapter(followCategoriesAdapter);

                mCategoryProgressBar.setVisibility(View.INVISIBLE);
            }else{
                Toast.makeText(getActivity(), "No Category in db", Toast.LENGTH_SHORT).show();
            }
        }

        // content recyclerview will be vertical

        contentRecyclerView.addOnScrollListener(new PageScrollListener(verticalLinearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;

                currentPage = currentPage+1;

                if (Util.isNetworkAvailable(getActivity())) {
                    loadNextSetOfItems();

                }else{
                    Toast.makeText(context, "You are offline", Toast.LENGTH_SHORT).show();
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
        return mFragmentView;
    }


    public void getCategories()
    {
/*        final ProgressDialog dialog = new ProgressDialog(TabSearchActivity.this);
        dialog.setMessage("Loading Categories");
        dialog.setCancelable(false);
        dialog.show();*/

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final CategoryApi categoryAPI = Util.getClient().create(CategoryApi.class);
                Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(Constants.CITY_ID);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Category>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/

                        mCategoryProgressBar.setVisibility(View.GONE);
                        if(response.code() == 200)
                        {

                            if(response.body() != null && response.body().size() != 0)
                            {

                                categoryList = response.body();
                                FollowFragmentCategoriesAdapter followCategoriesAdapter = new FollowFragmentCategoriesAdapter(context, response.body());
                                categoryRecyclerView.setLayoutManager(horizontalLinearLayoutManager);
                                categoryRecyclerView.setHasFixedSize(true);
                                categoryRecyclerView.setAdapter(followCategoriesAdapter);

                                mCategoryProgressBar.setVisibility(View.INVISIBLE);

                                if(db.getCategories()!=null&&db.getCategories().size()!=0){



                                    for (Category category:response.body()) {

                                        if(db.getContentById(category.getCategoriesId())!=null){

                                            db.updateCategory(category);
                                            System.out.println("Data Base Update Service");

                                        }else{
                                            db.addCategories(category);
                                            System.out.println("Data Base add Service");

                                        }

                                    }

                                }else{

                                    for (Category category:response.body()) {
                                        db.addCategories(category);
                                    }

                                }


                            }
                            else
                            {
                                categoryRecyclerView.setVisibility(View.GONE);
                                mCategoryProgressBar.setVisibility(View.INVISIBLE);

                            }
                        }else{
                            mCategoryProgressBar.setVisibility(View.INVISIBLE);
                            categoryRecyclerView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/
                        mCategoryProgressBar.setVisibility(View.INVISIBLE);
//                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                        categoryRecyclerView.setVisibility(View.GONE);
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
                        mContentProgressBar.setVisibility(View.GONE);

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
                                    categoryRecyclerView.removeAllViews();
                                    FollowFragmentContentAdapter followFragmentContentAdapter = new FollowFragmentContentAdapter(context, contentList);
                                    contentRecyclerView.setLayoutManager(verticalLinearLayoutManager);
                                    contentRecyclerView.setHasFixedSize(true);
                                    contentRecyclerView.setAdapter(followFragmentContentAdapter);
                                    mContentProgressBar.setVisibility(View.INVISIBLE);

                                }else{
                                    mContentProgressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(context, "No Contents", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                mContentProgressBar.setVisibility(View.INVISIBLE);

                            }

                        }else{

                            mContentProgressBar.setVisibility(View.INVISIBLE);

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Contents>> call, Throwable t) {
                        mContentProgressBar.setVisibility(View.INVISIBLE);
//                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

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

                        mContentProgressBar.setVisibility(View.GONE);
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



                                        } else {
                                            isLoading = true;

                                            currentPage = currentPage + 1;
                                            loadNextSetOfItems();
                                        }

                                    } else {
                                        adapter.removeLoadingFooter();
                                        isLastPage = true;
                                        isLoading = true;
                                        mContentProgressBar.setVisibility(View.GONE);
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
        mContentProgressBar.setVisibility(View.GONE);
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
        //Collections.reverse(list);
        Log.d(TAG, "loadFirstPage: "+list.size());
        //Collections.reverse(list);
        mContentProgressBar.setVisibility(View.GONE);
        adapter.addAll(list);

        if (list != null && list.size() !=0)
            adapter.addLoadingFooter();
        else
            isLastPage = true;

    }

}
