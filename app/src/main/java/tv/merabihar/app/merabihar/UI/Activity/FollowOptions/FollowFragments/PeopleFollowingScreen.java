package tv.merabihar.app.merabihar.UI.Activity.FollowOptions.FollowFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.FollowRequestAdapter;
import tv.merabihar.app.merabihar.Adapter.NonFollowersAdapter;
import tv.merabihar.app.merabihar.Adapter.ProfileListAdapter;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ProfileAPI;
import tv.merabihar.app.merabihar.WebAPI.ProfileFollowAPI;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFollowingScreen extends Fragment {


    RecyclerView recyclerView;
    private ProgressBar mProgressBar;

    ArrayList<UserProfile> userProfiles;
    ArrayList<UserProfile> followings;
    int profileId;

    public PeopleFollowingScreen() {
        // Required empty public constructor
    }

    public static PeopleFollowingScreen newInstance() {
        PeopleFollowingScreen fragment = new PeopleFollowingScreen();
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
            View view = inflater.inflate(R.layout.fragment_people_following_screen, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.people_following_list);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            mProgressBar.setVisibility(View.GONE);

            profileId = PreferenceHandler.getInstance(getActivity()).getUserId();

            if(profileId!=0){


                mProgressBar.setVisibility(View.VISIBLE);

                getNonFollowers(profileId);

            }else{


            }

            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
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

                            mProgressBar.setVisibility(View.GONE);
                            ArrayList<UserProfile> responseProfile = response.body();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {

                                ArrayList<UserProfile> non = new ArrayList<>();

                                for (UserProfile pro :responseProfile) {

                                    if(pro.getProfileId()!=id){
                                        non.add(pro);
                                    }

                                }

                                if(non!=null&&non.size()!=0){

                                    Collections.shuffle(non);
                                    FollowRequestAdapter adapter = new FollowRequestAdapter(getActivity(),non);
                                    recyclerView.setAdapter(adapter);

                                }




                            }
                            else
                            {

                                mProgressBar.setVisibility(View.GONE);

                            }
                        }
                        else
                        {
                            mProgressBar.setVisibility(View.GONE);

                            Toast.makeText(getActivity(),response.message(),Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed

                        mProgressBar.setVisibility(View.GONE);

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }
}
