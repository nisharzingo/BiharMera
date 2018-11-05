package tv.merabihar.app.merabihar.UI.Activity.Influencer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tv.merabihar.app.merabihar.R;

public class InfluencerProgramDetailScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_influencer_program_detail_screen);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
