package tv.merabihar.app.merabihar.UI.Activity.Influencer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import tv.merabihar.app.merabihar.Adapter.TargetInfluencerAdapter;
import tv.merabihar.app.merabihar.Model.TargetDes;
import tv.merabihar.app.merabihar.R;

/**
 * A simple {@link Fragment} subclass. fragment_influencer_target
 */
public class InfluencerTargetFragment extends Fragment {

    RecyclerView recyclerView;
    private ProgressBar mProgressBar;

    ArrayList<TargetDes> targetDesArrayList;

    TargetDes targetDes;

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

            targetDesArrayList = new ArrayList<>();

            targetDes = new TargetDes();
            targetDes.setTitle("Refer 5000 users - Earn Rs 2500");
            targetDes.setDesc("Get 50 coins after 1 user signup by using your refer code");

            targetDesArrayList.add(targetDes);

            targetDes = new TargetDes();
            targetDes.setTitle("Login 5 days in a Week - Earn Rs 500");
            targetDes.setDesc("Get 50 coins after 1 user signup by using your refer code");

            targetDesArrayList.add(targetDes);

            targetDes = new TargetDes();
            targetDes.setTitle("Refer 5000 users - Earn Rs 2500");
            targetDes.setDesc("Get 50 coins after 1 user signup by using your refer code");

            targetDesArrayList.add(targetDes);

            TargetInfluencerAdapter adapter = new TargetInfluencerAdapter(getActivity(),targetDesArrayList);
            recyclerView.setAdapter(adapter);



            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
