package tv.merabihar.app.merabihar.UI.Activity.Influencer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import tv.merabihar.app.merabihar.Adapter.RecentTransactionAdapter;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.SettingScreen;

public class Records extends AppCompatActivity {

    Toolbar mRecordsToolbar;
    RecyclerView recent_transition_rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        mRecordsToolbar = findViewById(R.id.records_toolbar);
        recent_transition_rv = findViewById(R.id.recent_transition_list);

        try{

            setSupportActionBar(mRecordsToolbar);
            getSupportActionBar().setTitle("Exchange Record");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            mRecordsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // perform whatever you want on back arrow click
                    Intent settingIntent = new Intent(Records.this, SettingScreen.class);
                    settingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(settingIntent);
                }
            });



            // api call to check recent transition
             checkRecentTransaction();






        }catch (Exception e){
            e.printStackTrace();
        }



    }

    private void checkRecentTransaction() {

        boolean hasTransition = false;
        // if user has transaction records initialize recyclerview & show the transactions
        if(hasTransition){

            ArrayList transaction_list = new ArrayList<String>();

            recent_transition_rv.setVisibility(View.VISIBLE);
            RecentTransactionAdapter adapter = new RecentTransactionAdapter(this,transaction_list);




        }


        else {
           TextView empty_tran_txt =  findViewById(R.id.empty_transition_txt);
           empty_tran_txt.setVisibility(View.VISIBLE);
        }


    }
}
