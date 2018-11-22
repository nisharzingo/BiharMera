package tv.merabihar.app.merabihar.UI.Activity.FollowOptions.FollowFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.CategoryFollowingList;
import tv.merabihar.app.merabihar.CustomViews.SnackbarViewer;
import tv.merabihar.app.merabihar.Model.Category;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.Constants;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.CategoryApi;


public class CategoryFollowScreen extends Fragment {

    RecyclerView recyclerView;
    private ProgressBar mProgressBar;

    ArrayList<Category> categories;
    int profileId;


    public CategoryFollowScreen() {
        // Required empty public constructor
    }

    public static CategoryFollowScreen newInstance() {
        CategoryFollowScreen fragment = new CategoryFollowScreen();
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
        // Inflate the layout for this fragment

        try{
            View view = inflater.inflate(R.layout.fragment_category_follow_screen, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.category_following_list);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            mProgressBar.setVisibility(View.GONE);

            profileId = PreferenceHandler.getInstance(getActivity()).getUserId();

            getCategories();

            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void getCategories()
    {
        mProgressBar.setVisibility(View.VISIBLE);

        if (Util.isNetworkAvailable(getActivity())) {

            new ThreadExecuter().execute(new Runnable() {
                @Override
                public void run() {
                    final CategoryApi categoryAPI = Util.getClient().create(CategoryApi.class);
                    //Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(Constants.CITY_ID);
                    Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(Constants.CITY_ID);
                    //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                    getCat.enqueue(new Callback<ArrayList<Category>>() {

                        @Override
                        public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                            mProgressBar.setVisibility(View.GONE);

                            if(response.code() == 200)
                            {
                                categories = response.body();

                                if(categories!=null&&categories.size()!=0){

                                    CategoryFollowingList adapter = new CategoryFollowingList(getActivity(),categories);
                                    recyclerView.setAdapter(adapter);
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                            mProgressBar.setVisibility(View.GONE);

//                            Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });


                    //System.out.println(TAG+" thread started");

                }
            });
        }
        else{

            SnackbarViewer.showSnackbar(recyclerView,"No Internet Connection found");
            mProgressBar.setVisibility(View.GONE);

        }



    }
}
