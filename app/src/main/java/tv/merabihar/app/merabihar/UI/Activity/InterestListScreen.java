package tv.merabihar.app.merabihar.UI.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.InterestSearchAdapter;
import tv.merabihar.app.merabihar.CustomViews.SnackbarViewer;
import tv.merabihar.app.merabihar.Model.Interest;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.SearchScreens.SearchActivity;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.InterestAPI;

public class InterestListScreen extends AppCompatActivity {

    RecyclerView mInterestList;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_interest_list_screen);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            setTitle("Tags in Mera Bihar");

            mInterestList = (RecyclerView)findViewById(R.id.interest_list);
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);


            if (Util.isNetworkAvailable(InterestListScreen.this)) {
                getInterest();

            }else{

                SnackbarViewer.showSnackbar(mInterestList,"No Internet connection");
                mProgressBar.setVisibility(View.GONE);
            }

        }catch(Exception e){
            e.printStackTrace();
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
                           ArrayList<Interest> categories = response.body();

                            if(categories!=null&&categories.size()!=0){



                                    InterestSearchAdapter adapter = new InterestSearchAdapter(InterestListScreen.this,categories);
                                    mInterestList.setAdapter(adapter);


                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Interest>> call, Throwable t) {
                        mProgressBar.setVisibility(View.GONE);

                        Toast.makeText(InterestListScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                InterestListScreen.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
