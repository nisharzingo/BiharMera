package tv.merabihar.app.merabihar.Service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.DataBase.DataBaseHelper;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Model.TransactionHistroy;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.TransactionHistroyAPI;

/**
 * Created by ZingoHotels Tech on 20-11-2018.
 */

public class ContentDataBaseService  extends Service {



    private NotificationManager alarmNotificationManager;
    private ArrayList<Contents> contentsList;



    DataBaseHelper db ;

    @Override
    public void onCreate() {

        db = new DataBaseHelper(getApplicationContext());

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Bundle bundle = intent.getExtras();

        if(bundle!=null){

            contentsList = (ArrayList<Contents>)bundle.getSerializable("ContentList");

            System.out.println("Data Base Service");

            if(contentsList!=null&&contentsList.size()!=0){

                if(db.getContents()!=null&&db.getContents().size()!=0){
                    System.out.println("Data Base Service"+db.getContents().size());


                    for (Contents content:contentsList) {

                        if(db.getContentById(content.getContentId())!=null){

                            db.updateContents(content);
                            System.out.println("Data Base Update Service");

                        }else{
                            db.addContents(content);
                            System.out.println("Data Base add Service");

                        }

                    }

                }else {

                    for (Contents content:contentsList) {
                        db.addContents(content);
                    }

                }


            }

        }
        return null;
    }


}
