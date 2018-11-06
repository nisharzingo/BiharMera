package tv.merabihar.app.merabihar.UI.Activity.Influencer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import tv.merabihar.app.merabihar.Adapter.ActiveTargetFragmentsAdapter;
import tv.merabihar.app.merabihar.Adapter.TargetInfluencerAdapter;
import tv.merabihar.app.merabihar.Model.TargetDes;
import tv.merabihar.app.merabihar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveTargetFragment extends Fragment {


    RecyclerView recyclerView;
    private ProgressBar mProgressBar;

    ArrayList<TargetDes> targetDesArrayList;

    TargetDes targetDes;

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

            ActiveTargetFragmentsAdapter adapter = new ActiveTargetFragmentsAdapter(getActivity(),targetDesArrayList);
            recyclerView.setAdapter(adapter);
            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
