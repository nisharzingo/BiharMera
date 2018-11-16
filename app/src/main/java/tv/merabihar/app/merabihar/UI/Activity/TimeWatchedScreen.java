package tv.merabihar.app.merabihar.UI.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Model.TransactionHistroy;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.Influencer.Income;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.TransactionHistroyAPI;

public class TimeWatchedScreen extends AppCompatActivity {

    TextView mDailyAvgWatchedTime,
            mPastWeekWatchedTime, mYesterdayWatchedTime, mTodayWatchedTime,mHistroy ;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_watched_screen);

        mToolbar = findViewById(R.id.time_watched_toolbar);
        mDailyAvgWatchedTime = findViewById(R.id.daily_average_watched_time);
        mPastWeekWatchedTime = findViewById(R.id.pastweek_watched_time);
        mYesterdayWatchedTime = findViewById(R.id.yesterday_watched_time);
        mTodayWatchedTime = findViewById(R.id.today_watched_time);
        mHistroy = findViewById(R.id.watch_history);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Time watched");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getTransactionsByIdType(PreferenceHandler.getInstance(TimeWatchedScreen.this).getUserId(),"VideoWatched");

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whenever back arrow click
                TimeWatchedScreen.this.finish();
            }
        });

        mHistroy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent wh = new Intent(TimeWatchedScreen.this,WatchHistroyScreen.class);
                startActivity(wh);

            }
        });

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
                            int totalWeek = 0;
                            int totalWatch = 0;

                            for (TransactionHistroy tc:response.body()) {

                                String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                                totalWatch = totalWatch+tc.getValue();

                                final Calendar cal = Calendar.getInstance();
                                cal.add(Calendar.DATE, -1);

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String yesterdate = dateFormat.format(cal.getTime());

                                final Calendar cals = Calendar.getInstance();
                                cals.add(Calendar.DATE, -7);
                                String passweek = dateFormat.format(cals.getTime());

                                Date past = null;
                                Date pastDay = null;
                                Date tDay = null;
                                try {
                                    pastDay = new SimpleDateFormat("yyyy-MM-dd").parse(passweek);
                                    tDay = new SimpleDateFormat("yyyy-MM-dd").parse(todayDate);
                                    System.out.println("Pass "+passweek);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                                String tdate = tc.getTransactionHistoryDate();

                                if(tdate.contains("T")){

                                    String[] tDates = tdate.split("T");
                                    try {
                                        past = dateFormat.parse(tDates[0]);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if(tc.getTransactionHistoryDate().contains(todayDate)){

                                    int[] vale = splitToComponentTimes(tc.getValue());

                                    if(vale.length!=0&&vale.length==2){
                                        mTodayWatchedTime.setText(vale[0]+" hr "+vale[1]+" min" );
                                    }


                                }

                                if(tc.getTransactionHistoryDate().contains(yesterdate)){

                                    int[] vale = splitToComponentTimes(tc.getValue());

                                    if(vale.length!=0&&vale.length==2){
                                        mYesterdayWatchedTime.setText(vale[0]+" hr "+vale[1]+" min" );
                                    }


                                }

                                System.out.println("PastDay "+pastDay.getTime());
                                System.out.println("PastDay "+past.getTime());
                                System.out.println("PastDay "+ new Date().getTime());
                                System.out.println("PastDay "+pastDay);
                                System.out.println("PastDay "+past);

                                if ((pastDay.getTime() <= past.getTime() && tDay.getTime() > past.getTime())) {
                                    totalWeek = totalWeek+tc.getValue();
                                }

                            }

                            int[] vale = splitToComponentTimes(totalWeek);

                            if(vale.length!=0&&vale.length==2){
                                mPastWeekWatchedTime.setText(vale[0]+" hr "+vale[1]+" min" );
                            }


                            int[] vales = splitToComponentTimes(totalWatch/response.body().size());

                            if(vales.length!=0&&vales.length==2){


                                mDailyAvgWatchedTime.setText(vales[0]+" hr "+vales[1]+" min" );
                            }
                        }else{



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


    public static int[] splitToComponentTimes(int time)
    {

        int hours = (int) time / 3600;
        int remainder = (int) time - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        int[] ints = {hours , mins };
        return ints;
    }
}
