package tv.merabihar.app.merabihar.UI.MainTabHostScreens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import tv.merabihar.app.merabihar.Adapter.VideoCategoryScreenAdapter;
import tv.merabihar.app.merabihar.Model.Category;
import tv.merabihar.app.merabihar.R;

public class VideoCategoryScreens extends AppCompatActivity {


    ArrayList<Category> interests;
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_category_screens);

        mRecyclerView = findViewById(R.id.video_category_recyclerview_parent);

        interests = new ArrayList<>();

        /*DUMMY DATA */
        Category ct1 = new Category();
        new Category().setCategoriesName("POPULAR");
        Category ct2 = new Category();
        new Category().setCategoriesName("TRENDING");
        interests.add(ct1);
        interests.add(ct2);

        VideoCategoryScreenAdapter adapter = new VideoCategoryScreenAdapter( this,interests);
        LinearLayoutManager manager = new LinearLayoutManager(VideoCategoryScreens.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);

    }


}
