package tv.merabihar.app.merabihar.UI.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Model.SubscribedGoals;
import tv.merabihar.app.merabihar.Model.TransactionHistroy;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.SubscribedGoalsAPI;
import tv.merabihar.app.merabihar.WebAPI.TransactionHistroyAPI;

public class TargetWatchTime extends AppCompatActivity {

    TextView mDailyAvgWatchedTime,
            mPastWeekWatchedTime, mYesterdayWatchedTime, mTodayWatchedTime,mHistroy,
            mThisWeek,mThisMonth;
    int penalty=0;
    // Toolbar mToolbar;
    int ProfileId=0;
    String type="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_target_watch_time);

            //  mToolbar = findViewById(R.id.time_watched_toolbar);
            mDailyAvgWatchedTime = findViewById(R.id.daily_average_watched_time);
            mPastWeekWatchedTime = findViewById(R.id.pastweek_watched_time);
            mThisWeek = findViewById(R.id.thisweek_watched_time);
            mThisMonth = findViewById(R.id.thismonth_watched_time);
            mYesterdayWatchedTime = findViewById(R.id.yesterday_watched_time);
            mTodayWatchedTime = findViewById(R.id.today_watched_time);
            mHistroy = findViewById(R.id.watch_history);


            Bundle bundle = getIntent().getExtras();
            if(bundle!=null){

                ProfileId = bundle.getInt("ProfileId");
                type = bundle.getString("FriendType");
            }

            if(ProfileId!=0&&type!=null&&!type.isEmpty()){
                getGoalsByProfileId(ProfileId);
            }else{
                getGoalsByProfileId(PreferenceHandler.getInstance(TargetWatchTime.this).getUserId());
            }



            //getTransactionsByIdType(PreferenceHandler.getInstance(TargetWatchTime.this).getUserId(),"VideoWatched");



            mHistroy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent wh = new Intent(TargetWatchTime.this,WatchHistroyScreen.class);
                    startActivity(wh);

                }
            });

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
                            int totalWeek = 0;
                            int thisweekWatch = 0;
                            int totalWatch = 0;
                            int thisMonthWatch = 0;

                            for (TransactionHistroy tc:response.body()) {

                                String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                                totalWatch = totalWatch+tc.getValue();

                                final Calendar cal = Calendar.getInstance();
                                cal.add(Calendar.DATE, -1);

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String yesterdate = dateFormat.format(cal.getTime());

                                final Calendar cals = Calendar.getInstance();
                                cals.add(Calendar.DATE, -15);
                                String passweek = dateFormat.format(cals.getTime());

                                final Calendar calse = Calendar.getInstance();
                                calse.add(Calendar.DATE, -7);
                                String passweekEnd = dateFormat.format(calse.getTime());

                                final Calendar calst = Calendar.getInstance();
                                calst.add(Calendar.DATE, -6);
                                String thisweek = dateFormat.format(calst.getTime());

                                final Calendar calstm = Calendar.getInstance();
                                calstm.add(Calendar.DATE, -30);
                                String thisMonth = dateFormat.format(calstm.getTime());

                                Date past = null;
                                Date pastDay = null;
                                Date pastDayEnd = null;
                                Date thisweekDay = null;
                                Date thisMonthDay = null;
                                Date tDay = null;
                                try {
                                    pastDay = new SimpleDateFormat("yyyy-MM-dd").parse(passweek);
                                    pastDayEnd = new SimpleDateFormat("yyyy-MM-dd").parse(passweekEnd);
                                    tDay = new SimpleDateFormat("yyyy-MM-dd").parse(todayDate);
                                    thisweekDay = new SimpleDateFormat("yyyy-MM-dd").parse(thisweek);
                                    thisMonthDay = new SimpleDateFormat("yyyy-MM-dd").parse(thisMonth);
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

                                    int[] vale = splitToComponentTimes(7800-tc.getValue());

                                    if(vale.length!=0&&vale.length==2){

                                        if(vale[0]<=0&&vale[1]<=0){
                                            mTodayWatchedTime.setText("0 hr "+"0 min" );
                                        }else{
                                            mTodayWatchedTime.setText(vale[0]+" hr "+vale[1]+" min" );
                                        }

                                    }


                                }

                                if(tc.getTransactionHistoryDate().contains(yesterdate)){

                                    int[] vale = splitToComponentTimes(7800-tc.getValue());

                                    if(vale[0]<=0&&vale[1]<=0){
                                        mYesterdayWatchedTime.setText("0 hr "+"0 min" );
                                    }else{
                                        mYesterdayWatchedTime.setText(vale[0]+" hr "+vale[1]+" min" );
                                    }



                                }

                                System.out.println("PastDay "+pastDay.getTime());
                                System.out.println("PastDay "+past.getTime());
                                System.out.println("PastDay "+ new Date().getTime());
                                System.out.println("PastDay "+pastDay);
                                System.out.println("PastDay "+past);

                                if ((pastDay.getTime() <= past.getTime() && pastDayEnd.getTime() > past.getTime())) {
                                    totalWeek = totalWeek+tc.getValue();
                                }

                                if ((thisweekDay.getTime() <= past.getTime() && tDay.getTime() >= past.getTime())) {
                                    thisweekWatch = thisweekWatch+tc.getValue();
                                }

                                if ((thisMonthDay.getTime() <= past.getTime() && tDay.getTime() >= past.getTime())) {
                                    thisMonthWatch = thisMonthWatch+tc.getValue();
                                }

                            }

                            int[] vale = splitToComponentTimes(penalty);

                            if(vale.length!=0&&vale.length==2){
                                mPastWeekWatchedTime.setText(vale[0]+" hr "+vale[1]+" min" );
                            }

                            int[] valet = splitToComponentTimes((54000+penalty)-thisweekWatch);

                            if(valet[0]<=0&&valet[1]<=0){
                                mThisWeek.setText("0 hr "+"0 min" );
                            }else{
                                mThisWeek.setText(valet[0]+" hr "+valet[1]+" min" );
                            }

                            /*if(valet.length!=0&&valet.length==2){
                                mThisWeek.setText(valet[0]+" hr "+valet[1]+" min" );
                            }*/


                            int[] valetm = splitToComponentTimes((216000+penalty)-thisMonthWatch);

                            if(valetm[0]<=0&&valetm[1]<=0){
                                mThisMonth.setText("0 hr "+"0 min" );
                            }else{
                                mThisMonth.setText(valetm[0]+" hr "+valetm[1]+" min" );
                            }

                           /* if(valetm.length!=0&&valetm.length==2){
                                mThisMonth.setText(valetm[0]+" hr "+valetm[1]+" min" );
                            }*/


                            int[] vales = splitToComponentTimes(totalWatch/response.body().size());

                            if(vales.length!=0&&vales.length==2){


                                //mDailyAvgWatchedTime.setText(vales[0]+" hr "+vales[1]+" min" );
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

    public void getGoalsByProfileId(final int id)
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final SubscribedGoalsAPI categoryAPI = Util.getClient().create(SubscribedGoalsAPI.class);
                Call<ArrayList<SubscribedGoals>> getCat = categoryAPI.getSubscribedGoalsByProfileIdAndGoal(id,3);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<SubscribedGoals>>() {

                    @Override
                    public void onResponse(Call<ArrayList<SubscribedGoals>> call, Response<ArrayList<SubscribedGoals>> response) {



                        if(response.code() == 200 && response.body()!= null)
                        {

                            if(response.body().size()!=0){

                                SubscribedGoals sg = response.body().get(0);

                                if(sg!=null){

                                    String startDate = sg.getStartDate();
                                    String endDate = sg.getEndDate();
                                    penalty = Integer.parseInt(sg.getExtraDescription());
                                    getTransactionsByIdType(id,"VideoWatched");

                                }else{
                                    getTransactionsByIdType(id,"VideoWatched");
                                }


                            }else{
                                getTransactionsByIdType(id,"VideoWatched");
                            }


                        }else{

                            getTransactionsByIdType(id,"VideoWatched");

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<SubscribedGoals>> call, Throwable t) {


                        getTransactionsByIdType(id,"VideoWatched");

                        //Toast.makeText(TimeWatchedScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }
}
