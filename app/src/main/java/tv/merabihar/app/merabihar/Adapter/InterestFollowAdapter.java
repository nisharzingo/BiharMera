package tv.merabihar.app.merabihar.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Roboto_Regular;
import tv.merabihar.app.merabihar.Model.Interest;
import tv.merabihar.app.merabihar.Model.InterestProfileMapping;
import tv.merabihar.app.merabihar.Model.ProfileCategoryMapping;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.InterestProfileAPI;
import tv.merabihar.app.merabihar.WebAPI.ProfileCategoryAPI;

/**
 * Created by ZingoHotels Tech on 03-11-2018.
 */

public class InterestFollowAdapter   extends RecyclerView.Adapter<InterestFollowAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Interest> list;
    public InterestFollowAdapter(Context context,ArrayList<Interest> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_interest_map, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Interest profile = list.get(position);

        if(profile!=null){


            holder.mName.setText(""+profile.getInterestName());

            holder.mFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(Util.isNetworkAvailable(context)){

                        int mappingId = Integer.parseInt(holder.mMapId.getText().toString());

                        if(holder.mFollow.getText().toString().equalsIgnoreCase("Follow")){
                            holder.mFollow.setEnabled(false);
                            InterestProfileMapping pm = new InterestProfileMapping();
                            pm.setZingoInterestId(profile.getZingoInterestId());
                            pm.setProfileId(PreferenceHandler.getInstance(context).getUserId());
                            profileInterest(pm,holder.mFollow,holder.mMapId);
                        }else{

                            if(mappingId!=0){

                                deleteFollow(mappingId,holder.mFollow,holder.mMapId);

                            }else{
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }else{
                        Toast.makeText(context, "No internet connection ", Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder  {

        TextView mName;
        AppCompatButton mFollow;
        MyTextView_Roboto_Regular mMapId;



//

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            mName = (TextView)itemView.findViewById(R.id.interest_name);
            mFollow = (AppCompatButton) itemView.findViewById(R.id.follow_interest);
            mMapId = (MyTextView_Roboto_Regular) itemView.findViewById(R.id.map_id);



        }


    }

    private void profileInterest(final InterestProfileMapping intrst,final AppCompatButton tv,final MyTextView_Roboto_Regular mappingId) {

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                InterestProfileAPI mapApi = Util.getClient().create(InterestProfileAPI.class);
                Call<InterestProfileMapping> response = mapApi.postInterest(intrst);
                response.enqueue(new Callback<InterestProfileMapping>() {
                    @Override
                    public void onResponse(Call<InterestProfileMapping> call, Response<InterestProfileMapping> response) {

                        System.out.println(response.code());

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                            tv.setText("Following");
                            mappingId.setText(""+response.body().getMappingId());
                            tv.setEnabled(true);

                        }
                        else
                        {
                            tv.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<InterestProfileMapping> call, Throwable t) {

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

//                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                        tv.setEnabled(true);
                    }
                });
            }
        });
    }

    private void deleteFollow(final int mapId,final AppCompatButton follow,final MyTextView_Roboto_Regular mappingId ) {


        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                InterestProfileAPI mapApi = Util.getClient().create(InterestProfileAPI.class);
                Call<InterestProfileMapping> response = mapApi.deleteIntrs(mapId);
                response.enqueue(new Callback<InterestProfileMapping>() {
                    @Override
                    public void onResponse(Call<InterestProfileMapping> call, Response<InterestProfileMapping> response) {

                        System.out.println(response.code());

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {


                            follow.setText("Follow");
                            mappingId.setText("0");


                        }
                        else
                        {
                            Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<InterestProfileMapping> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
//                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

}
