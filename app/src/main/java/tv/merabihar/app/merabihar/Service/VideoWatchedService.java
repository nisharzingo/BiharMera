package tv.merabihar.app.merabihar.Service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Model.TransactionHistroy;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.TransactionHistroyAPI;

/**
 * Created by ZingoHotels Tech on 16-11-2018.
 */

public class VideoWatchedService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private NotificationManager alarmNotificationManager;
    private TransactionHistroy transactionHistroy;

    public VideoWatchedService()
    {
        super("UpdateVideoWatching");
    }
    public VideoWatchedService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


        Bundle bundle = intent.getExtras();

        if(bundle!=null){

            int profileId = bundle.getInt("ProfileId");
            int time = bundle.getInt("Time");


            if(profileId!=0){
                getTransactionsByIdType(profileId,"VideoWatched",time);
            }

        }


    }

    public void getTransactionsByIdType(final int id,final String type,final int time)
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

                            for (TransactionHistroy tc:response.body()) {

                                String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                                if(tc.getTransactionHistoryDate().contains(todayDate)){

                                    transactionHistroy = tc;
                                    break;

                                }

                            }

                            if(transactionHistroy!=null){

                                TransactionHistroy tc = transactionHistroy;
                                tc.setValue((time+transactionHistroy.getValue()));
                                updateVideoWatched(tc);

                            }else{
                                TransactionHistroy tc = new TransactionHistroy();
                                tc.setProfileId(id);
                                tc.setTitle("VideoWatched");
                                tc.setDescription("Stories video");
                                tc.setTransactionHistoryDate(new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").format(new Date()));
                                tc.setValue(time);

                                profileVideoWatched(tc);
                            }



                        }else{

                            TransactionHistroy tc = new TransactionHistroy();
                            tc.setProfileId(id);
                            tc.setTitle("VideoWatched");
                            tc.setDescription("Stories video");
                            tc.setTransactionHistoryDate(new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").format(new Date()));
                            tc.setValue(time);

                            profileVideoWatched(tc);

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
    public void profileVideoWatched(final TransactionHistroy sg) {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                TransactionHistroyAPI mapApi = Util.getClient().create(TransactionHistroyAPI.class);
                Call<TransactionHistroy> response = mapApi.postTransactionHistories(sg);
                response.enqueue(new Callback<TransactionHistroy>() {
                    @Override
                    public void onResponse(Call<TransactionHistroy> call, Response<TransactionHistroy> response) {

                        System.out.println(response.code());



                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {



                        }
                        else
                        {

                        }
                    }

                    @Override
                    public void onFailure(Call<TransactionHistroy> call, Throwable t) {




                    }
                });
            }
        });
    }

    public void updateVideoWatched(final TransactionHistroy sg) {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                TransactionHistroyAPI mapApi = Util.getClient().create(TransactionHistroyAPI.class);
                Call<TransactionHistroy> response = mapApi.updateTransactionHistories(sg.getTransactionHistoryId(),sg);
                response.enqueue(new Callback<TransactionHistroy>() {
                    @Override
                    public void onResponse(Call<TransactionHistroy> call, Response<TransactionHistroy> response) {

                        System.out.println(response.code());



                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {



                        }
                        else
                        {

                        }
                    }

                    @Override
                    public void onFailure(Call<TransactionHistroy> call, Throwable t) {




                    }
                });
            }
        });
    }

}
