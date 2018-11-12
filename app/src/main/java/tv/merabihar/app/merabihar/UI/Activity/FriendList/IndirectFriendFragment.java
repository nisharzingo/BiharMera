package tv.merabihar.app.merabihar.UI.Activity.FriendList;


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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.ReferalPeopleListAdapter;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ProfileFollowAPI;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndirectFriendFragment  extends Fragment {

    RecyclerView recyclerView;
    private ProgressBar mProgressBar;


    String referalCode = "";


    public IndirectFriendFragment() {
        // Required empty public constructor
    }

    public static IndirectFriendFragment newInstance() {
        IndirectFriendFragment fragment = new IndirectFriendFragment();
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
            View view = inflater.inflate(R.layout.fragment_indirect_friend, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.influnecer_indirect_friend_list);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar_indirect);
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

                               getInDirectRefer(code,responseProfile);


                            }
                            else
                            {

                                getInDirectRefer(code,null);

                            }
                        }
                        else
                        {

                            getInDirectRefer(code,null);

                            Toast.makeText(getActivity(),response.message(),Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed

                        mProgressBar.setVisibility(View.GONE);
                        getInDirectRefer(code,null);


                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    private void getInDirectRefer(final String code,final ArrayList<UserProfile> profileArrayList){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getInDirectReferedProfile(code);

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

                                if(profileArrayList!=null&&profileArrayList.size()!=0){


                                    responseProfile.removeAll(profileArrayList);

                                }

                                if(responseProfile!=null&&responseProfile.size()!=0){

                                    ReferalPeopleListAdapter adapter = new ReferalPeopleListAdapter(getActivity(),responseProfile);
                                    recyclerView.setAdapter(adapter);
                                }



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
