package tv.merabihar.app.merabihar.UI.MainTabHostScreens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import tv.merabihar.app.merabihar.Adapter.FollowFragmentContentAdapter;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;

public class TabSearchActivity2 extends AppCompatActivity {

    RecyclerView contentsView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_search2);

        /*dummy contents for testing*/

        ArrayList<ArrayList<Contents>> contentList = new ArrayList<>();
        ArrayList<Contents> c = new ArrayList<>();
        c.add(new Contents());
        for(int i=0; i<5 ; i++){
            contentList.add(c);
        }

        contentsView = findViewById(R.id.category_layout);
        LinearLayoutManager verticalLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        FollowFragmentContentAdapter followFragmentContentAdapter = new FollowFragmentContentAdapter(this, contentList);
        contentsView.setLayoutManager(verticalLinearLayoutManager);
        contentsView.setHasFixedSize(true);
        contentsView.setAdapter(followFragmentContentAdapter);


    }










}
