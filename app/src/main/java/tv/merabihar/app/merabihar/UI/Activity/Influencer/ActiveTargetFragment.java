package tv.merabihar.app.merabihar.UI.Activity.Influencer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.ActiveTargetFragmentsAdapter;
import tv.merabihar.app.merabihar.Adapter.TargetInfluencerAdapter;
import tv.merabihar.app.merabihar.Model.SubscribedGoals;
import tv.merabihar.app.merabihar.Model.TargetDes;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.SubscribedGoalsAPI;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveTargetFragment extends Fragment {


    RecyclerView recyclerView;
    private ProgressBar mProgressBar;

    ArrayList<SubscribedGoals> targetDesArrayList;

    SubscribedGoals targetDes;

    public ActiveTargetFragment() {
        // Required empty public constructor
    }

    public static ActiveTargetFragment newInstance() {
        ActiveTargetFragment fragment = new ActiveTargetFragment();
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
           // View view = inflater.inflate(R.layout.active_fragement_adapter, container, false);
            View view = inflater.inflate(R.layout.fragment_active_target, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.active_target_recyclerview);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar_active_fragments);
            mProgressBar.setVisibility(View.GONE);

            targetDesArrayList = new ArrayList<>();

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


                        targetDesArrayList =response.body();
                        if(response.code() == 200 && targetDesArrayList!= null)
                        {


                            ActiveTargetFragmentsAdapter adapter = new ActiveTargetFragmentsAdapter(getActivity(),targetDesArrayList);
                            recyclerView.setAdapter(adapter);




                        }else{



                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<SubscribedGoals>> call, Throwable t) {




                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

}
