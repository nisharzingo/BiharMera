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

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

import tv.merabihar.app.merabihar.Adapter.FollowFragmentCategoriesAdapter;
import tv.merabihar.app.merabihar.Adapter.FollowFragmentContentAdapter;
import tv.merabihar.app.merabihar.Model.Category;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowFragments extends Fragment {

    Context context;
    ArrayList<Contents> contentList;
    ArrayList<Category> categoryList;

    View mFragmentView;
    RecyclerView categoryRecyclerView, contentRecyclerView ;


    public FollowFragments() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context; // to retrieve the context of the  parent class
        contentList = new ArrayList<>();
        categoryList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mFragmentView =  inflater.inflate(R.layout.fragment_follow_fragments, container, false);

        categoryRecyclerView = mFragmentView.findViewById(R.id.follow_frag_categories_recycler_view);
        contentRecyclerView = mFragmentView.findViewById(R.id.follow_frag_content_recycler_view);

        LinearLayoutManager horizontalLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager verticalLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        // category recyclerview will be horizontal

         // Dummy for testing
        for(int i=0 ; i<5 ; i++){
            categoryList.add(new Category());
          }

        FollowFragmentCategoriesAdapter followCategoriesAdapter = new FollowFragmentCategoriesAdapter(context, categoryList);
        categoryRecyclerView.setLayoutManager(horizontalLinearLayoutManager);
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setAdapter(followCategoriesAdapter);

        // content recyclerview will be vertical

        // Dummy for testing
        for(int i=0 ; i<5 ; i++){
            contentList.add(new Contents());
        }

        FollowFragmentContentAdapter followFragmentContentAdapter = new FollowFragmentContentAdapter(context, contentList);
        contentRecyclerView.setLayoutManager(verticalLinearLayoutManager);
        contentRecyclerView.setHasFixedSize(true);
        contentRecyclerView.setAdapter(followFragmentContentAdapter);
        return mFragmentView;
    }

}
