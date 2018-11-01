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
import tv.merabihar.app.merabihar.Adapter.InterestSearchAdapter;
import tv.merabihar.app.merabihar.Model.Interest;
import tv.merabihar.app.merabihar.Model.InterestProfileMapping;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.InterestAPI;
import tv.merabihar.app.merabihar.WebAPI.InterestProfileAPI;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterestSearchFragment extends Fragment {

    RecyclerView recyclerViewFolloers,recyclerViewNonFolloers,recyclerProfile;
    private ProgressBar mProgressBar;
    EditText mSearchText;
    NestedScrollView mInterestScroll;
    LinearLayout mNon,mOn;

    ArrayList<Interest> categories;
    ArrayList<Interest> followings;
    int profileId;
    public InterestSearchFragment() {
        // Required empty public constructor
    }

    public static InterestSearchFragment newInstance() {
        InterestSearchFragment fragment = new InterestSearchFragment();
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
            View view = inflater.inflate(R.layout.fragment_interest_search, container, false);
            recyclerViewFolloers = (RecyclerView) view.findViewById(R.id.interest_following_list);
            recyclerViewNonFolloers = (RecyclerView) view.findViewById(R.id.interest_non_following_list);
            recyclerProfile = (RecyclerView) view.findViewById(R.id.interest_list);
            mInterestScroll = (NestedScrollView) view.findViewById(R.id.scroll_interest);
            mNon = (LinearLayout) view.findViewById(R.id.non_layout);
            mOn = (LinearLayout) view.findViewById(R.id.on_layout);
            mSearchText = (EditText) view.findViewById(R.id.search_editText);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            mProgressBar.setVisibility(View.GONE);

            profileId = PreferenceHandler.getInstance(getActivity()).getUserId();

            if(profileId!=0){


                mProgressBar.setVisibility(View.VISIBLE);
                getInterestByProfileId(profileId);


            }else{

                mProgressBar.setVisibility(View.VISIBLE);
                followings = new ArrayList<>();
                getInterest();
            }

            mSearchText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    String text = mSearchText.getText().toString();

                    if(text.isEmpty()||text.equalsIgnoreCase("")){

                        mInterestScroll.setVisibility(View.VISIBLE);
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

        ArrayList<Interest> filteredList = new ArrayList<>();
        mInterestScroll.setVisibility(View.GONE);
        recyclerProfile.setVisibility(View.VISIBLE);

        try{
            for(int i=0;i<categories.size();i++)
            {

                String fullName = "";


                if(categories.get(i).getInterestName()!=null){
                    fullName= categories.get(i).getInterestName().toLowerCase();
                }

                if(fullName.contains(s))
                {
                    filteredList.add(categories.get(i));
                }



            }

            InterestSearchAdapter adapter = new InterestSearchAdapter(getActivity(),filteredList);
            recyclerProfile.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("OTA Error "+e.getMessage());

        }




    }



    public void getInterest()
    {
        mProgressBar.setVisibility(View.VISIBLE);
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                final InterestAPI categoryAPI = Util.getClient().create(InterestAPI.class);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(Constants.CITY_ID);
                Call<ArrayList<Interest>> getCat = categoryAPI.getInterest();
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Interest>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Interest>> call, Response<ArrayList<Interest>> response) {
                        mProgressBar.setVisibility(View.GONE);

                        if(response.code() == 200)
                        {
                            categories = response.body();

                            if(categories!=null&&categories.size()!=0){
                                if(followings.size()!=0){
                                    categories.removeAll(followings);
                                }

                                if(categories!=null&&categories.size()!=0){
                                    InterestSearchAdapter adapter = new InterestSearchAdapter(getActivity(),categories);
                                    recyclerViewNonFolloers.setAdapter(adapter);
                                }

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Interest>> call, Throwable t) {
                        mProgressBar.setVisibility(View.GONE);

                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    private void getInterestByProfileId(final int id){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                InterestProfileAPI apiService =
                        Util.getClient().create(InterestProfileAPI.class);

                Call<ArrayList<InterestProfileMapping>> call = apiService.getInterestByProfileId(id);

                call.enqueue(new Callback<ArrayList<InterestProfileMapping>>() {
                    @Override
                    public void onResponse(Call<ArrayList<InterestProfileMapping>> call, Response<ArrayList<InterestProfileMapping>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        mProgressBar.setVisibility(View.GONE);

                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<InterestProfileMapping> responseProfile = response.body();
                            followings = new ArrayList<>();

                            for(int i=0;i<responseProfile.size();i++){

                                followings.add(responseProfile.get(i).getZingoInterest());
                            }
                            if(followings != null && followings.size()!=0 )
                            {

                                getInterest();

                            }
                            else
                            {
                                followings = new ArrayList<>();
                                getInterest();

                            }
                        }
                        else
                        {

                            Toast.makeText(getActivity(),response.message(),Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<InterestProfileMapping>> call, Throwable t) {
                        // Log error here since request failed

                        mProgressBar.setVisibility(View.GONE);

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }


}

