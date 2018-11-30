package tv.merabihar.app.merabihar.Rss;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tv.merabihar.app.merabihar.Adapter.InterestSearchAdapter;
import tv.merabihar.app.merabihar.Adapter.NewsSourceSearchAdapter;
import tv.merabihar.app.merabihar.Model.Interest;
import tv.merabihar.app.merabihar.Model.NewsSources;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.SearchScreens.Fragments.PeopleSearchFragment;
import tv.merabihar.app.merabihar.Util.Constants;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.InterestAPI;
import tv.merabihar.app.merabihar.WebAPI.NewsFeedAPI;

/**
 * A simple {@link Fragment} subclass.
 */
public class SourceSearchFragment extends Fragment {


    RecyclerView recyclerFilterSearch,recyclerSources;
    private ProgressBar mProgressBar;
    EditText mNewsSearchText;
    NestedScrollView mNewsScroll;
    LinearLayout mOn;

    ArrayList<NewsSource> newsSources;
    ArrayList<NewsSource> filterNewsSources;

    private static Retrofit retrofit = null;


    public SourceSearchFragment() {
        // Required empty public constructor
    }

    public static SourceSearchFragment newInstance() {
        SourceSearchFragment fragment = new SourceSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        try{
            View view = inflater.inflate(R.layout.fragment_source_search, container, false);

            recyclerFilterSearch= (RecyclerView) view.findViewById(R.id.filter_news_sources_list);
            recyclerSources = (RecyclerView) view.findViewById(R.id.news_sources_list);
            mNewsScroll = (NestedScrollView) view.findViewById(R.id.scroll_news);

            mOn = (LinearLayout) view.findViewById(R.id.on_layout);
            mNewsSearchText = (EditText) view.findViewById(R.id.news_search_editText);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

            mProgressBar.setVisibility(View.VISIBLE);
            filterNewsSources = new ArrayList<>();


            getSources();

            mNewsSearchText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    String text = mNewsSearchText.getText().toString();

                    if(text.isEmpty()||text.equalsIgnoreCase("")){

                        mNewsScroll.setVisibility(View.VISIBLE);
                        recyclerFilterSearch.setVisibility(View.GONE);

                    }else{
                        filterNewsSource(charSequence.toString().toLowerCase());

                    }




                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            return view;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        //return inflater.inflate(R.layout.fragment_source_search, container, false);
    }


    private void filterNewsSource(String s) {

        ArrayList<NewsSource> filteredList = new ArrayList<>();
        mNewsScroll.setVisibility(View.GONE);
        recyclerFilterSearch.setVisibility(View.VISIBLE);

        try{
            for(int i=0;i<newsSources.size();i++)
            {

                String fullName = "";


                if(newsSources.get(i).getName()!=null){
                    fullName= newsSources.get(i).getName().toLowerCase();
                }

                if(fullName.contains(s))
                {
                    filteredList.add(newsSources.get(i));
                }



            }

            NewsSourceSearchAdapter adapter = new NewsSourceSearchAdapter(getActivity(),filteredList);
            recyclerFilterSearch.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("OTA Error "+e.getMessage());

        }




    }

    public static Retrofit getClientNews() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.NEWSAPI_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        //System.out.println("Retrofit = "+retrofit.get);
        return retrofit;
    }
    public void getSources()
    {
        mProgressBar.setVisibility(View.VISIBLE);
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                final NewsFeedAPI categoryAPI = getClientNews().create(NewsFeedAPI.class);

                Call<NewsSources> getCat = categoryAPI.getSources();
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<NewsSources>() {

                    @Override
                    public void onResponse(Call<NewsSources> call, Response<NewsSources> response) {
                        mProgressBar.setVisibility(View.GONE);

                        if(response.code() == 200)
                        {
                            NewsSources newsSource = response.body();

                            if(newsSource!=null){

                                if(newsSource.getSources()!=null&&newsSource.getSources().size()!=0){

                                    newsSources = newsSource.getSources();
                                }

                                if(newsSources!=null&&newsSources.size()!=0){
                                    recyclerSources.setVisibility(View.VISIBLE);
                                    NewsSourceSearchAdapter adapter = new NewsSourceSearchAdapter(getActivity(),newsSources);
                                    recyclerSources.setAdapter(adapter);
                                }

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<NewsSources> call, Throwable t) {
                        mProgressBar.setVisibility(View.GONE);

//                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }


}
