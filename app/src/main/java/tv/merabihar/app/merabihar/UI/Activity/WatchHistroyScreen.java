package tv.merabihar.app.merabihar.UI.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.WatchHistroyAdapter;
import tv.merabihar.app.merabihar.Model.TransactionHistroy;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.TransactionHistroyAPI;

public class WatchHistroyScreen extends AppCompatActivity {
    RecyclerView mWatchHistroy;
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_watch_histroy_screen);

            mWatchHistroy = (RecyclerView)findViewById(R.id.watch_history_list);
            mToolbar = findViewById(R.id.time_watched_toolbar);

            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("Watch Histroy");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);



            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // perform whenever back arrow click
                    WatchHistroyScreen.this.finish();
                }
            });
            getTransactionsByIdType(PreferenceHandler.getInstance(WatchHistroyScreen.this).getUserId(),"VideoWatched");

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void getTransactionsByIdType(final int id,final String type)
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final TransactionHistroyAPI categoryAPI = Util.getClient().create(TransactionHistroyAPI.class);
                Call<ArrayList<TransactionHistroy>> getCat = categoryAPI.getTransactionHistoriesByProfileIdAndGoal(id,type);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<TransactionHistroy>>() {

                    @Override
                    public void onResponse(Call<ArrayList<TransactionHistroy>> call, Response<ArrayList<TransactionHistroy>> response) {



                        if(response.code() == 200 && response.body()!= null&&response.body().size()!=0)
                        {

                            WatchHistroyAdapter adapter = new WatchHistroyAdapter(WatchHistroyScreen.this,response.body());
                            mWatchHistroy.setAdapter(adapter);


                        }else{

                            Toast.makeText(WatchHistroyScreen.this, "No Histroy", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TransactionHistroy>> call, Throwable t) {





                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }
}
