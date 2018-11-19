package tv.merabihar.app.merabihar.UI.MainTabHostScreens.SearchScreens.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.ProfileSearchAdapter;
import tv.merabihar.app.merabihar.Model.FollowerNonFollowers;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ProfileAPI;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleSearchFragment extends Fragment {

    RecyclerView recyclerViewFolloers,recyclerViewNonFolloers,recyclerProfile;
    private ProgressBar mProgressBar;
    EditText mSearchText;
    NestedScrollView mPeopleScroll;
    LinearLayout mNon,mOn;

    ArrayList<UserProfile> userProfile;
    ArrayList<UserProfile> followings;
    int profileId;
    public PeopleSearchFragment() {
        // Required empty public constructor
    }

    public static PeopleSearchFragment newInstance() {
        PeopleSearchFragment fragment = new PeopleSearchFragment();
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
            View view = inflater.inflate(R.layout.fragment_people_search, container, false);
            recyclerViewFolloers = (RecyclerView) view.findViewById(R.id.people_following_list);
            recyclerViewNonFolloers = (RecyclerView) view.findViewById(R.id.people_non_following_list);
            recyclerProfile = (RecyclerView) view.findViewById(R.id.people_list);
            mPeopleScroll = (NestedScrollView) view.findViewById(R.id.scroll_profile);
            mNon = (LinearLayout) view.findViewById(R.id.non_layout);
            mOn = (LinearLayout) view.findViewById(R.id.on_layout);
            mSearchText = (EditText) view.findViewById(R.id.search_editText);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            mProgressBar.setVisibility(View.GONE);

            profileId = PreferenceHandler.getInstance(getActivity()).getUserId();

            if(profileId!=0){


                mProgressBar.setVisibility(View.VISIBLE);

                userProfile = new ArrayList<>();
                getOtherProfiles(profileId);

            }else{
                mProgressBar.setVisibility(View.VISIBLE);
                userProfile = new ArrayList<>();
                getProfileByUserRoleId();
            }

            mSearchText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    String text = mSearchText.getText().toString();

                    if(text.isEmpty()||text.equalsIgnoreCase("")){

                        mPeopleScroll.setVisibility(View.VISIBLE);
                        recyclerProfile.setVisibility(View.GONE);

                    }else{
                        filterProfiles(charSequence.toString().toLowerCase());

                    }




                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void filterProfiles(String s) {

        ArrayList<UserProfile> filteredList = new ArrayList<>();
        mPeopleScroll.setVisibility(View.GONE);
        recyclerProfile.setVisibility(View.VISIBLE);

        try{
            for(int i=0;i<userProfile.size();i++)
            {

                String fullName = "";


                if(userProfile.get(i).getFullName()!=null){
                    fullName= userProfile.get(i).getFullName().toLowerCase();
                }

                if(fullName.contains(s))
                {
                    filteredList.add(userProfile.get(i));
                }



            }

            ProfileSearchAdapter adapter = new ProfileSearchAdapter(getActivity(),filteredList);
            recyclerProfile.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("OTA Error "+e.getMessage());

        }




    }

    private void getOtherProfiles(final int id){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileAPI apiService =
                        Util.getClient().create(ProfileAPI.class);

                Call<FollowerNonFollowers> call = apiService.getOtherProfiles(id);

                call.enqueue(new Callback<FollowerNonFollowers>() {
                    @Override
                    public void onResponse(Call<FollowerNonFollowers> call, Response<FollowerNonFollowers> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        mProgressBar.setVisibility(View.GONE);

                        if(statusCode == 200 || statusCode == 204)
                        {

                            FollowerNonFollowers userProfiles = response.body();

                            if(userProfiles!=null){


                                ArrayList<UserProfile> folloers = new ArrayList<>();
                                ArrayList<UserProfile> nonfolloers = new ArrayList<>();

                                if (userProfiles.getFollowers()!=null&&userProfiles.getFollowers().size()!=0){


                                    System.out.println("User 1Size "+userProfile.size());
                                    folloers = userProfiles.getFollowers();
                                    for (UserProfile usr:folloers) {

                                        userProfile.add(usr);

                                    }
                                    ProfileSearchAdapter adapter = new ProfileSearchAdapter(getActivity(),folloers);
                                    recyclerViewFolloers.setAdapter(adapter);

                                }else{

                                    mOn.setVisibility(View.GONE);

                                }

                                if (userProfiles.getNonFollowers()!=null&&userProfiles.getNonFollowers().size()!=0){

                                    System.out.println("User 2Size "+userProfile.size());
                                    nonfolloers = userProfiles.getNonFollowers();
                                    for (UserProfile usr:nonfolloers) {

                                        userProfile.add(usr);

                                    }
                                    ProfileSearchAdapter adapter = new ProfileSearchAdapter(getActivity(),nonfolloers);
                                    recyclerViewNonFolloers.setAdapter(adapter);

                                }else{

                                    mNon.setVisibility(View.GONE);

                                }


                            }

                        }
                        else
                        {

                            Toast.makeText(getActivity(),response.message(),Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<FollowerNonFollowers> call, Throwable t) {
                        // Log error here since request failed

                        mProgressBar.setVisibility(View.GONE);

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    private void getProfileByUserRoleId(){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileAPI apiService =
                        Util.getClient().create(ProfileAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getUserByUserRoleId(1);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        mProgressBar.setVisibility(View.GONE);

                        if(statusCode == 200 || statusCode == 204)
                        {
                            ArrayList<UserProfile> userProfiles = response.body();
                            if (userProfiles!=null&&userProfiles.size()!=0){



                                for (UserProfile usr:userProfiles) {

                                    userProfile.add(usr);

                                }
                                                             ProfileSearchAdapter adapter = new ProfileSearchAdapter(getActivity(),userProfile);
                                recyclerViewFolloers.setAdapter(adapter);

                            }else{

                                mOn.setVisibility(View.GONE);

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

