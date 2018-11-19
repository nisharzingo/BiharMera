package tv.merabihar.app.merabihar.UI.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.FollowersListAdapter;
import tv.merabihar.app.merabihar.Adapter.FollowingProfileListAdapter;
import tv.merabihar.app.merabihar.Model.FollowsWithMapping;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ProfileFollowAPI;

public class FollowersListScreen extends AppCompatActivity {

    RecyclerView mFollowingProfileRC,mSearchProfile;
    Toolbar mRecordsToolbar;
    ProgressBar mProgressBar;
    EditText mSearchText;
    int profileId=0;

    ArrayList<FollowsWithMapping> userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_followers_list_screen);

            mFollowingProfileRC = (RecyclerView)findViewById(R.id.followinig_profile_list);
            mSearchProfile = (RecyclerView)findViewById(R.id.following_people_list);
            mRecordsToolbar = findViewById(R.id.following_profiles_toolbar);
            mSearchText = (EditText) findViewById(R.id.following_search_editText);
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar_following);

            setSupportActionBar(mRecordsToolbar);
            getSupportActionBar().setTitle("Follwing");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            mSearchText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    String text = mSearchText.getText().toString();

                    if(text.isEmpty()||text.equalsIgnoreCase("")){


                        mSearchProfile.setVisibility(View.GONE);
                        mFollowingProfileRC.setVisibility(View.VISIBLE);

                    }else{
                        filterProfiles(charSequence.toString().toLowerCase());

                    }




                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            mRecordsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // perform whatever you want on back arrow click
                    FollowersListScreen.this.finish();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void filterProfiles(String s) {

        ArrayList<FollowsWithMapping> filteredList = new ArrayList<>();
        mFollowingProfileRC.setVisibility(View.GONE);
        mSearchProfile.setVisibility(View.VISIBLE);

        try{
            for(int i=0;i<userProfile.size();i++)
            {

                String fullName = "";


                if(userProfile.get(i).getFollows().getFullName()!=null){
                    fullName= userProfile.get(i).getFollows().getFullName().toLowerCase();
                }

                if(fullName.contains(s))
                {
                    filteredList.add(userProfile.get(i));
                }



            }

            FollowersListAdapter adapter = new FollowersListAdapter(FollowersListScreen.this,filteredList);
            mSearchProfile.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("OTA Error "+e.getMessage());

        }




    }

    @Override
    protected void onResume() {
        super.onResume();

        profileId = PreferenceHandler.getInstance(FollowersListScreen.this).getUserId();

        if(profileId!=0){

            if(Util.isNetworkAvailable(FollowersListScreen.this)){

                getFollowingsByProfileId(profileId);

            }else{

                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getFollowingsByProfileId(final int id){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<FollowsWithMapping>> call = apiService.getFollowerssWithMappingByProfileId(id);

                call.enqueue(new Callback<ArrayList<FollowsWithMapping>>() {
                    @Override
                    public void onResponse(Call<ArrayList<FollowsWithMapping>> call, Response<ArrayList<FollowsWithMapping>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        mProgressBar.setVisibility(View.GONE);

                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<FollowsWithMapping> responseProfile = response.body();
                            boolean value = false;

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {


                                mSearchProfile.setVisibility(View.GONE);
                                userProfile = responseProfile;
                                FollowersListAdapter adapter = new FollowersListAdapter(FollowersListScreen.this,response.body());
                                mFollowingProfileRC.setAdapter(adapter);


                            }
                            else
                            {

                                Toast.makeText(FollowersListScreen.this, "No followings", Toast.LENGTH_SHORT).show();

                            }
                        }
                        else
                        {
                            Toast.makeText(FollowersListScreen.this, ""+response.message(), Toast.LENGTH_SHORT).show();

                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<FollowsWithMapping>> call, Throwable t) {
                        // Log error here since request failed

                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(FollowersListScreen.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }
}
