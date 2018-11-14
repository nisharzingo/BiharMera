package tv.merabihar.app.merabihar.UI.MainTabHostScreens.HomeFragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.CategoryGridAdapter;
import tv.merabihar.app.merabihar.Adapter.FollowFragmentCategoriesAdapter;
import tv.merabihar.app.merabihar.Adapter.FollowFragmentContentAdapter;
import tv.merabihar.app.merabihar.Model.Category;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.TabSearchActivity;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.TabVideoActivity;
import tv.merabihar.app.merabihar.Util.Constants;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.CategoryApi;
import tv.merabihar.app.merabihar.WebAPI.ContentAPI;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowFragments extends Fragment {

    Context context;

    ArrayList<Category> categoryList;

    View mFragmentView;
    RecyclerView categoryRecyclerView, contentRecyclerView ;
    LinearLayoutManager horizontalLinearLayoutManager,verticalLinearLayoutManager;



    public FollowFragments() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context; // to retrieve the context of the  parent class

        categoryList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        Fresco.initialize(getActivity());
        mFragmentView =  inflater.inflate(R.layout.fragment_follow_fragments, container, false);



        categoryRecyclerView = mFragmentView.findViewById(R.id.follow_frag_categories_recycler_view);
        contentRecyclerView = mFragmentView.findViewById(R.id.follow_frag_content_recycler_view);

        categoryRecyclerView.setNestedScrollingEnabled(false);
        contentRecyclerView.setNestedScrollingEnabled(false);

        horizontalLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        verticalLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);



        getCategories();
        loadFirstSetOfContents();



        // content recyclerview will be vertical

        // Dummy for testing



        return mFragmentView;
    }


    public void getCategories()
    {
/*        final ProgressDialog dialog = new ProgressDialog(TabSearchActivity.this);
        dialog.setMessage("Loading Categories");
        dialog.setCancelable(false);
        dialog.show();*/

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final CategoryApi categoryAPI = Util.getClient().create(CategoryApi.class);
                Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(Constants.CITY_ID);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Category>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/
                        if(response.code() == 200)
                        {

                            if(response.body() != null && response.body().size() != 0)
                            {

                                categoryList = response.body();
                                FollowFragmentCategoriesAdapter followCategoriesAdapter = new FollowFragmentCategoriesAdapter(context, response.body());
                                categoryRecyclerView.setLayoutManager(horizontalLinearLayoutManager);
                                categoryRecyclerView.setHasFixedSize(true);
                                categoryRecyclerView.setAdapter(followCategoriesAdapter);


                            }
                            else
                            {
                                categoryRecyclerView.setVisibility(View.GONE);

                            }
                        }else{
                            categoryRecyclerView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/
                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                        categoryRecyclerView.setVisibility(View.GONE);
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public void loadFirstSetOfContents()
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ContentAPI categoryAPI = Util.getClient().create(ContentAPI.class);
                Call<ArrayList<Contents>> getCat = categoryAPI.getContentByCityId(Constants.CITY_ID);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Contents>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {


                        System.out.println("Content inside"+response.code());
                        if(response.code() == 200 && response.body()!= null)
                        {
                            System.out.println("Content inside"+response.body().size());


                            ArrayList<ArrayList<Contents>> contentList = new ArrayList<>();
                            ArrayList<Contents> contents = new ArrayList<>();
                            if( response.body().size()!= 0){

                                System.out.println("Content inside");

                                int count = 0;

                                for (Contents  content:response.body()) {

                                    if(content.getContentType().equalsIgnoreCase("Image")){
                                        contents.add(content);
                                        count = count+1;
                                        if(count==9){
                                            contentList.add(contents);
                                            count=0;
                                            contents = new ArrayList<>();
                                        }
                                    }


                                }

                                if(contentList!=null&&contentList.size()!=0){
                                    FollowFragmentContentAdapter followFragmentContentAdapter = new FollowFragmentContentAdapter(context, contentList);
                                    contentRecyclerView.setLayoutManager(verticalLinearLayoutManager);
                                    contentRecyclerView.setHasFixedSize(true);
                                    contentRecyclerView.setAdapter(followFragmentContentAdapter);

                                }else{
                                    Toast.makeText(context, "No Contents", Toast.LENGTH_SHORT).show();
                                }

                            }else{

                            }



                        }else{


                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Contents>> call, Throwable t) {



                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

}
