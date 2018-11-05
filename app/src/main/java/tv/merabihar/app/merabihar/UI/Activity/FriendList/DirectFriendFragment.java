package tv.merabihar.app.merabihar.UI.Activity.FriendList;

import android.content.Context;
import android.net.Uri;
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
import tv.merabihar.app.merabihar.Adapter.ProfileListAdapter;
import tv.merabihar.app.merabihar.Adapter.ReferalPeopleListAdapter;
import tv.merabihar.app.merabihar.Adapter.TargetInfluencerAdapter;
import tv.merabihar.app.merabihar.Model.TargetDes;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.Influencer.InfluencerTargetFragment;
import tv.merabihar.app.merabihar.UI.Activity.ProfileScreen;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ProfileFollowAPI;


public class DirectFriendFragment extends Fragment {

    RecyclerView recyclerView;
    private ProgressBar mProgressBar;


    String referalCode = "";


    public DirectFriendFragment() {
        // Required empty public constructor
    }

    public static DirectFriendFragment newInstance() {
        DirectFriendFragment fragment = new DirectFriendFragment();
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
            View view = inflater.inflate(R.layout.fragment_direct_friend, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.influnecer_direct_friend_list);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar_direct);
            mProgressBar.setVisibility(View.GONE);

            referalCode = PreferenceHandler.getInstance(getActivity()).getReferalcode();

            if(referalCode!=null&&!referalCode.isEmpty()){
                getDirectRefer(referalCode);
            }


            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    private void getDirectRefer(final String code){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getDirectReferedProfile(code);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        mProgressBar.setVisibility(View.GONE);

                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {
                                //Collections.shuffle(responseProfile);

                                ReferalPeopleListAdapter adapter = new ReferalPeopleListAdapter(getActivity(),responseProfile);
                                recyclerView.setAdapter(adapter);


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

                        mProgressBar.setVisibility(View.GONE);


                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

}
