package tv.merabihar.app.merabihar.UI.MainTabHostScreens.HomeFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.ContentAdapterVertical;

import tv.merabihar.app.merabihar.Adapter.ContentRecyclerHorizontal;
import tv.merabihar.app.merabihar.Adapter.NonFollowersAdapter;
import tv.merabihar.app.merabihar.Adapter.ProfileListAdapter;
import tv.merabihar.app.merabihar.CustomViews.SnackbarViewer;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.TabAccountActivity;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ContentAPI;
import tv.merabihar.app.merabihar.WebAPI.InterestProfileAPI;
import tv.merabihar.app.merabihar.WebAPI.ProfileFollowAPI;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForFollowersFragment extends Fragment {

    SwipeRefreshLayout pullToRefresh;
    RecyclerView mFollowerContent,mNonFollowers,mTrendingContents;
    //,mFollowingContent,mInterestContent,mTrendingContents,mNonFollowers
    int profileId = 0;

    ContentAdapterVertical adapter;
    boolean value = false;

    private boolean _hasLoadedOnce = false;


    public ForFollowersFragment() {
        // Required empty public constructor
    }

    public static ForFollowersFragment newInstance() {
        ForFollowersFragment fragment = new ForFollowersFragment();
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

            final View view =  inflater.inflate(R.layout.fragment_for_followers, container, false);


            pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);

            mFollowerContent = (RecyclerView) view.findViewById(R.id.all_followers_contents);
            mFollowerContent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mFollowerContent.setNestedScrollingEnabled(false);

            mNonFollowers = (RecyclerView) view.findViewById(R.id.people_non_follow);
            mNonFollowers.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            mNonFollowers.setNestedScrollingEnabled(false);

            mTrendingContents = (RecyclerView) view.findViewById(R.id.trending_contents);
            mTrendingContents.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mTrendingContents.setNestedScrollingEnabled(false);

           /* mFollowingContent = (RecyclerView)  view.findViewById(R.id.all_following_contents);
            mFollowingContent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mFollowingContent.setNestedScrollingEnabled(false);

            mInterestContent = (RecyclerView)  view.findViewById(R.id.all_interest_contents);
            mInterestContent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mInterestContent.setNestedScrollingEnabled(false);





*/
            profileId = PreferenceHandler.getInstance(getActivity()).getUserId();





            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                int Refreshcounter = 1; //Counting how many times user have refreshed the layout

                @Override
                public void onRefresh() {
                    if(profileId!=0){

                  mFollowerContent.removeAllViews();
                       /* mFollowingContent.removeAllViews();
                        mInterestContent.removeAllViews();*/

                        Thread follower = new Thread(){

                            public void run(){
                                getFollowingByProfileId(profileId);
                            }
                        };

                        if (Util.isNetworkAvailable(getActivity())) {
                            follower.start();
                        }else{
                            SnackbarViewer.showSnackbar(view.findViewById(R.id.frag_follow_main),"No Internet connection");
                        }


                    }else{

                    }

                    pullToRefresh.setRefreshing(false);
                }
            });


            if(profileId!=0){



                mFollowerContent.removeAllViews();
                       /* mFollowingContent.removeAllViews();
                        mInterestContent.removeAllViews();*/

                Thread follower = new Thread(){

                    public void run(){
                        getFollowingByProfileId(profileId);
                    }
                };


                if (Util.isNetworkAvailable(getActivity())) {
                    follower.start();
                }else{
                    SnackbarViewer.showSnackbar(view.findViewById(R.id.frag_follow_main),"No Internet connection");
                }

            }else{

            }

            return  view;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void getFollowingByProfileId(final int id){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getFollowersContentByProfileId(id);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();



                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();
                            ArrayList<Contents> followingContents = new ArrayList<>();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {

                                for (UserProfile profile:responseProfile) {

                                    if(profile.getContents()!=null&&profile.getContents().size()!=0){

                                        for (Contents content:profile.getContents()) {

                                            followingContents.add(content);

                                        }


                                    }

                                }

                                if(followingContents!=null&& followingContents.size()!=0){
                                    Collections.shuffle(followingContents);
                                    value = true;
                                    adapter = new ContentAdapterVertical(getActivity());
                                    mFollowerContent.setAdapter(adapter);
                                    adapter.addAll(followingContents);

                                   // getFollowerByProfileId(profileId);




                                }else{
                                    //getFollowerByProfileId(profileId);

                                    if(value){

                                    }else{
                                        getTrendingContent(id);
                                    }
                                }


                            }
                            else
                            {
                                if(value){

                                }else{
                                    getTrendingContent(id);
                                }

                            }
                        }
                        else
                        {

                            if(value){

                            }else{
                                getTrendingContent(id);
                            }
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed

                        if(value){

                        }else{
                            getTrendingContent(id);
                        }

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

   /* private void getFollowerByProfileId(final int id){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getFollowingContentByProfileId(id);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();



                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();
                            ArrayList<Contents> followingContents = new ArrayList<>();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {

                                for (UserProfile profile:responseProfile) {

                                    if(profile.getContents()!=null&&profile.getContents().size()!=0){

                                        for (Contents content:profile.getContents()) {

                                            followingContents.add(content);

                                        }


                                    }

                                }

                                if(followingContents!=null&& followingContents.size()!=0){
                                    Collections.shuffle(followingContents);
                                    value = true;
                                    adapter = new ContentRecyclerAdapter(getActivity());
                                   // mFollowingContent.setAdapter(adapter);
                                    adapter.addAll(followingContents);

                                    getContentsofInterest(profileId);
                                }


                            }
                            else
                            {
                                getContentsofInterest(profileId);

                            }
                        }
                        else
                        {
                            getContentsofInterest(profileId);

                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed



                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }*/

  /*  public void getContentsofInterest(final int id)
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final InterestProfileAPI categoryAPI = Util.getClient().create(InterestProfileAPI.class);
                Call<ArrayList<Contents>> getCat = categoryAPI.getContentofInterestByProfileId(id);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Contents>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {



                        if(response.code() == 200)
                        {

                            ArrayList<Contents> contentsInterestList = response.body();

                            if(contentsInterestList != null && contentsInterestList.size() != 0)
                            {

                                Collections.shuffle(contentsInterestList);
                                value = true;
                                adapter = new ContentRecyclerAdapter(getActivity());
                               // mInterestContent.setAdapter(adapter);
                                adapter.addAll(contentsInterestList);


                            }
                            else
                            {

                                if(value){

                                }else{
                                    getTrendingContent(id);
                                }



                            }
                        }else{

                            if(value){

                            }else{
                                getTrendingContent(id);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Contents>> call, Throwable t) {


                        if(value){

                        }else{
                            getTrendingContent(id);
                        }
                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }*/

    public void getTrendingContent(final int id)
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

                            mFollowerContent.setVisibility(View.GONE);

                            if(response.body().size()!=0){

                                ContentRecyclerHorizontal adapters = new ContentRecyclerHorizontal(getActivity(),response.body());
                                mTrendingContents.setAdapter(adapters);
                                getNonFollowers(id);
                            }

                        }else{

                            getNonFollowers(id);

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Contents>> call, Throwable t) {

                        getNonFollowers(id);

//                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    private void getNonFollowers(final int id){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getNonFollowersByProfileId(id);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();



                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {

                                Collections.shuffle(responseProfile);
                                NonFollowersAdapter adapter = new NonFollowersAdapter(getActivity(),responseProfile);
                                mNonFollowers.setAdapter(adapter);


                            }
                            else
                            {



                            }
                        }
                        else
                        {

                            Toast.makeText(getActivity(),response.message(),Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed



                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

  /*  @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);

        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isFragmentVisible_ && !_hasLoadedOnce) {
                if (Util.isNetworkAvailable(getActivity())) {

                    if(profileId!=0){


                        Thread follower = new Thread(){

                            public void run(){
                                getFollowingByProfileId(profileId);
                            }
                        };


                        follower.start();


                    }else{

                    }

                }
                _hasLoadedOnce = true;
            }
        }
    }*/


}
