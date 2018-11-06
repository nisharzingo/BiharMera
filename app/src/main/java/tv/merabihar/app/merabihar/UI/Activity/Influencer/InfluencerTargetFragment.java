package tv.merabihar.app.merabihar.UI.Activity.Influencer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.ContentRecyclerHorizontal;
import tv.merabihar.app.merabihar.Adapter.TargetInfluencerAdapter;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Model.Goals;
import tv.merabihar.app.merabihar.Model.SubscribedGoals;
import tv.merabihar.app.merabihar.Model.TargetDes;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ContentAPI;
import tv.merabihar.app.merabihar.WebAPI.GoalAPI;
import tv.merabihar.app.merabihar.WebAPI.SubscribedGoalsAPI;

/**
 * A simple {@link Fragment} subclass. fragment_influencer_target
 */
public class InfluencerTargetFragment extends Fragment {

    RecyclerView recyclerView;
    private ProgressBar mProgressBar;

    ArrayList<Goals> targetDesArrayList;
    ArrayList<Integer> targetDesArrayListId;



    public InfluencerTargetFragment() {
        // Required empty public constructor
    }

    public static InfluencerTargetFragment newInstance() {
        InfluencerTargetFragment fragment = new InfluencerTargetFragment();
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
        try{
            View view = inflater.inflate(R.layout.fragment_influencer_target, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.influnecer_targets);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            mProgressBar.setVisibility(View.GONE);


            getGoalsByProfileId(PreferenceHandler.getInstance(getActivity()).getUserId());
            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void getGoalsByProfileId(final int id)
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final SubscribedGoalsAPI categoryAPI = Util.getClient().create(SubscribedGoalsAPI.class);
                Call<ArrayList<SubscribedGoals>> getCat = categoryAPI.getSubscribedGoalsByProfileId(id);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<SubscribedGoals>>() {

                    @Override
                    public void onResponse(Call<ArrayList<SubscribedGoals>> call, Response<ArrayList<SubscribedGoals>> response) {



                        if(response.code() == 200 && response.body()!= null)
                        {
                            targetDesArrayListId = new ArrayList<>();

                            for (SubscribedGoals sg:response.body()) {

                                targetDesArrayListId.add(sg.getGoalId());


                            }

                            getGoals(targetDesArrayListId);


                        }else{

                            getGoals(targetDesArrayListId);

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<SubscribedGoals>> call, Throwable t) {


                        getGoals(targetDesArrayListId);

                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public void getGoals(final ArrayList<Integer> ids)
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final GoalAPI categoryAPI = Util.getClient().create(GoalAPI.class);
                Call<ArrayList<Goals>> getCat = categoryAPI.getGoals();
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Goals>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Goals>> call, Response<ArrayList<Goals>> response) {

                        targetDesArrayList =new ArrayList<>() ;
                        ArrayList<Goals> goals = response.body();

                        if(response.code() == 200 && goals!= null)
                        {

                            if(goals.size()!=0){

                                if(ids!=null&&ids.size()!=0){
                                    for(int i=0;i<goals.size();i++){

                                        boolean value = true;

                                        for(int j=0;j<ids.size();j++){

                                            if(ids.get(j)==goals.get(i).getGoalId()){

                                                value = false;
                                            }

                                        }

                                        if(value){
                                            targetDesArrayList.add(goals.get(i));
                                        }
                                    }

                                    if(targetDesArrayList!=null&&targetDesArrayList.size()!=0){

                                        TargetInfluencerAdapter adapter = new TargetInfluencerAdapter(getActivity(),targetDesArrayList);
                                        recyclerView.setAdapter(adapter);

                                    }

                                }else{
                                    TargetInfluencerAdapter adapter = new TargetInfluencerAdapter(getActivity(),response.body());
                                    recyclerView.setAdapter(adapter);
                                }


                            }


                        }else{



                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Goals>> call, Throwable t) {




                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }
}
