package tv.merabihar.app.merabihar.UI.MainTabHostScreens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.VideoCategoryScreenAdapter;
import tv.merabihar.app.merabihar.Model.Category;
import tv.merabihar.app.merabihar.Model.CategoryAndContentList;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.Constants;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.CategoryApi;

public class VideoCategoryScreens extends AppCompatActivity {


    ArrayList<Category> interests;
    RecyclerView mRecyclerView;
    ProgressBar mProgressBar;

    ArrayList<Contents> contentsList;
    ArrayList<Category> categoryList;
    ArrayList<CategoryAndContentList> categoryAndContentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_category_screens);

        mRecyclerView = findViewById(R.id.video_category_recyclerview_parent);
        mProgressBar = findViewById(R.id.progressBar_content_video_category);
        mProgressBar.setVisibility(View.VISIBLE);

        interests = new ArrayList<>();

        getCategoryAndContent();



    }

    public void getCategoryAndContent()
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final CategoryApi categoryAPI = Util.getClient().create(CategoryApi.class);
                Call<ArrayList<CategoryAndContentList>> getCat = categoryAPI.getContentAndCategoryByCityId(Constants.CITY_ID);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<CategoryAndContentList>>() {

                    @Override
                    public void onResponse(Call<ArrayList<CategoryAndContentList>> call, Response<ArrayList<CategoryAndContentList>> response) {

                        mProgressBar.setVisibility(View.GONE);

                        if(response.code() == 200)
                        {

                            ArrayList<CategoryAndContentList> body = response.body();
                            categoryAndContentList = new ArrayList<>();
                            contentsList = new ArrayList<>();


                            for (CategoryAndContentList categoryContent: body) {

                                if(categoryContent.getContentList()!=null&&categoryContent.getContentList().size()!=0){

                                    boolean value = false;

                                    for (Contents content:categoryContent.getContentList()) {

                                        if(content.getContentType().equalsIgnoreCase("Video")){
                                            contentsList.add(content);
                                            value = true;
                                        }


                                    }

                                    if(value){
                                        categoryAndContentList.add(categoryContent);
                                    }
                                }

                            }

                            if(categoryAndContentList != null && categoryAndContentList.size() != 0)
                            {

                                Collections.shuffle(categoryAndContentList);
                                VideoCategoryScreenAdapter adapter = new VideoCategoryScreenAdapter( VideoCategoryScreens.this,categoryAndContentList);
                                LinearLayoutManager manager = new LinearLayoutManager(VideoCategoryScreens.this, LinearLayoutManager.VERTICAL, false);
                                mRecyclerView.setLayoutManager(manager);
                                mRecyclerView.setHasFixedSize(true);
                                mRecyclerView.setAdapter(adapter);








                            }
                            else
                            {

                                mRecyclerView.setVisibility(View.GONE);
                                mProgressBar.setVisibility(View.GONE);

                            }
                        }else{
                            mRecyclerView.setVisibility(View.GONE);
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<CategoryAndContentList>> call, Throwable t) {

                        mRecyclerView.setVisibility(View.GONE);
                        mProgressBar.setVisibility(View.GONE);

                        Toast.makeText(VideoCategoryScreens.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

}
