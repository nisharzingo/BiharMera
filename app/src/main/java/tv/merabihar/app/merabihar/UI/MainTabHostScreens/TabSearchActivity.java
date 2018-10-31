package tv.merabihar.app.merabihar.UI.MainTabHostScreens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tv.merabihar.app.merabihar.R;

public class TabSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_tab_search);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
