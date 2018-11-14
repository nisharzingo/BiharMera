package tv.merabihar.app.merabihar.UI.MainTabHostScreens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Downloader;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.CategoryGridAdapter;
import tv.merabihar.app.merabihar.Adapter.ContentRecyclerHorizontal;
import tv.merabihar.app.merabihar.Adapter.TrendingIntrestAdapter;
import tv.merabihar.app.merabihar.CustomFonts.TextViewSFProDisplaySemibold;
import tv.merabihar.app.merabihar.CustomViews.CustomGridView;
import tv.merabihar.app.merabihar.Model.Category;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Model.Interest;
import tv.merabihar.app.merabihar.Model.InterestContentMapping;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.InterestListScreen;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.SearchScreens.SearchActivity;
import tv.merabihar.app.merabihar.Util.Constants;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.CategoryApi;
import tv.merabihar.app.merabihar.WebAPI.ContentAPI;
import tv.merabihar.app.merabihar.WebAPI.InterestAPI;

public class TabSearchActivity extends AppCompatActivity {

    CardView mSearch;
    RecyclerView mTrendingInterest;
    CustomGridView mCategories;
    LinearLayout mCategoryLayout;
    TextViewSFProDisplaySemibold mViewTags;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_tab_search);

            mSearch = (CardView)findViewById(R.id.linear);
            mCategoryLayout = (LinearLayout) findViewById(R.id.category_layout);
            mTrendingInterest = (RecyclerView)findViewById(R.id.trending_tags);
            mCategories = (CustomGridView) findViewById(R.id.category_grid_view);
            mViewTags = (TextViewSFProDisplaySemibold)findViewById(R.id.view_tags);
            progressBar = (ProgressBar) findViewById(R.id.progressBar_content);
            mTrendingInterest.setLayoutManager(new LinearLayoutManager(TabSearchActivity.this, LinearLayoutManager.HORIZONTAL, false));
            mTrendingInterest.setNestedScrollingEnabled(false);


            Thread interest = new Thread() {
                public void run() {
                    getTrendingInterest();
                }
            };

            Thread category = new Thread() {
                public void run() {
                    getCategories();
                }
            };

            interest.start();
            category.start();

            mSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent search = new Intent(TabSearchActivity.this, SearchActivity.class);
                    startActivity(search);
                }
            });

            mViewTags.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent search = new Intent(TabSearchActivity.this, InterestListScreen.class);
                    startActivity(search);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getTrendingInterest()
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final InterestAPI categoryAPI = Util.getClient().create(InterestAPI.class);
                Call<ArrayList<InterestContentMapping>> getCat = categoryAPI.getTrendingInterest();
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<InterestContentMapping>>() {

                    @Override
                    public void onResponse(Call<ArrayList<InterestContentMapping>> call, Response<ArrayList<InterestContentMapping>> response) {



                        if(response.code() == 200 && response.body()!= null)
                        {
                            progressBar.setVisibility(View.GONE);

                            if(response.body().size()!=0){

                                TrendingIntrestAdapter adapters = new TrendingIntrestAdapter(TabSearchActivity.this,response.body());
                                mTrendingInterest.setAdapter(adapters);

                            }

                        }else{

                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<InterestContentMapping>> call, Throwable t) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(TabSearchActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

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
                        if(response.code() == 200)
                        {

                            if(response.body() != null && response.body().size() != 0)
                            {

                                CategoryGridAdapter adapter = new CategoryGridAdapter(TabSearchActivity.this,response.body());
                                mCategories.setAdapter(adapter);


                            }
                            else
                            {
                                mCategoryLayout.setVisibility(View.GONE);

                            }
                        }else{
                            mCategoryLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/
                        Toast.makeText(TabSearchActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        mCategoryLayout.setVisibility(View.GONE);
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    private void goBack()
    {
        Intent intent = null;

            intent = new Intent(TabSearchActivity.this,TabMainActivity.class);
            //intent.putExtra("TABNAME",3);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        TabSearchActivity.this.finish();
    }
}
