package tv.merabihar.app.merabihar.UI.MainTabHostScreens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tuyenmonkey.mkloader.model.Line;

import java.util.ArrayList;

import tv.merabihar.app.merabihar.Adapter.FollowFragmentContentAdapter;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;

public class TabVideoNewDesign2 extends AppCompatActivity {

    RecyclerView video_list_recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_video_new_design2);


        /*dummy contents for testing*/

        ArrayList<ArrayList<Contents>> contentList = new ArrayList<>();
        ArrayList<Contents> c = new ArrayList<>();
        c.add(new Contents());
        for(int i=0; i<5 ; i++){
            contentList.add(c);
        }


        video_list_recyclerView = findViewById(R.id.video_list_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        FollowFragmentContentAdapter followFragmentContentAdapter = new FollowFragmentContentAdapter(this, contentList);
        video_list_recyclerView.setLayoutManager(linearLayoutManager);
        video_list_recyclerView.setHasFixedSize(true);
        video_list_recyclerView.setAdapter(followFragmentContentAdapter);


    }
}
