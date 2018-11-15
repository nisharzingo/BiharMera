package tv.merabihar.app.merabihar.UI.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.Influencer.Income;

public class TimeWatchedScreen extends AppCompatActivity {

    TextView mDailyAvgWatchedTime, mPastWeekWatchedTime, mYesterdayWatchedTime, mTodayWatchedTime ;
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


        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Time watched");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whenever back arrow click
                TimeWatchedScreen.this.finish();
            }
        });

    }
}
