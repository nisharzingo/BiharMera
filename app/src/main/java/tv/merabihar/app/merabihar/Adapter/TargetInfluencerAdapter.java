package tv.merabihar.app.merabihar.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Lato_Regular;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Roboto_Regular;
import tv.merabihar.app.merabihar.Model.Goals;
import tv.merabihar.app.merabihar.Model.InterestProfileMapping;
import tv.merabihar.app.merabihar.Model.SubscribedGoals;
import tv.merabihar.app.merabihar.Model.TargetDes;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.InterestProfileAPI;
import tv.merabihar.app.merabihar.WebAPI.SubscribedGoalsAPI;

/**
 * Created by ZingoHotels Tech on 05-11-2018.
 */

public class TargetInfluencerAdapter extends RecyclerView.Adapter<TargetInfluencerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Goals> list;
    private ArrayList<Integer> ids;
    public TargetInfluencerAdapter(Context context,ArrayList<Goals> list,ArrayList<Integer> ids) {

        this.context = context;
        this.list = list;
        this.ids = ids;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_target_des, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Goals targetDes = list.get(position);

        if(targetDes!=null){

            holder.mTitle.setText(""+targetDes.getGoalName());
            holder.mDesc.setText(""+targetDes.getDescription());

            if(ids!=null&&ids.size()!=0){
                boolean value = false;
                for(int j=0;j<ids.size();j++){

                    if(ids.get(j)==targetDes.getGoalId()){

                        value = true;
                    }

                }

                if(value){
                    holder.mActivateOffer.setText("Activated");
                    holder.mActivateOffer.setEnabled(false);
                }

            }else{
                holder.mActivateOffer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        SubscribedGoals sg = new SubscribedGoals();
                        sg.setGoalId(targetDes.getGoalId());
                        sg.setStatus("Activated");
                        sg.setProfileId(PreferenceHandler.getInstance(context).getUserId());

                        if(targetDes.getGoalId()==1){

                            sg.setStartDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));

                            Calendar c = Calendar.getInstance();
                            c.setTime(new Date()); // Now use today date.
                            c.add(Calendar.DATE, 150); // Adding 5 days
                            sg.setEndDate(new SimpleDateFormat("MM/dd/yyyy").format(c.getTime()));
                            sg.setActiveDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
                            sg.setExtraDescription("0");
                            sg.setRewardsEarned("0");
                            profileSubScribed(sg,holder.mActivateOffer);

                        }else if(targetDes.getGoalId()==3){

                            sg.setStartDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));

                            Calendar c = Calendar.getInstance();
                            c.setTime(new Date()); // Now use today date.
                            c.add(Calendar.DATE, 7); // Adding 5 days
                            sg.setEndDate(new SimpleDateFormat("MM/dd/yyyy").format(c.getTime()));
                            sg.setActiveDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
                            sg.setExtraDescription("0");
                            sg.setRewardsEarned("0");
                            profileSubScribed(sg,holder.mActivateOffer);

                        }else if(targetDes.getGoalId()==2){

                            sg.setStartDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));

                            Calendar c = Calendar.getInstance();
                            c.setTime(new Date()); // Now use today date.
                            c.add(Calendar.DATE, 7); // Adding 5 days
                            sg.setEndDate(new SimpleDateFormat("MM/dd/yyyy").format(c.getTime()));
                            sg.setActiveDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
                            sg.setExtraDescription("0");
                            sg.setRewardsEarned("0");
                            profileSubScribed(sg,holder.mActivateOffer);


                        }




                    }
                });
            }



        }
    }

    @Override
    public int getItemCount() {

        return list.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder  {

        MyTextView_Lato_Regular mTitle,mDesc;
        AppCompatButton mActivateOffer;


        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            mTitle = (MyTextView_Lato_Regular) itemView.findViewById(R.id.target_title);
            mDesc = (MyTextView_Lato_Regular) itemView.findViewById(R.id.target_des);
            mActivateOffer = (AppCompatButton) itemView.findViewById(R.id.activated_offer);


        }


    }

    private void profileSubScribed(final SubscribedGoals sg, final AppCompatButton tv) {

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                SubscribedGoalsAPI mapApi = Util.getClient().create(SubscribedGoalsAPI.class);
                Call<SubscribedGoals> response = mapApi.postSubscribedGoals(sg);
                response.enqueue(new Callback<SubscribedGoals>() {
                    @Override
                    public void onResponse(Call<SubscribedGoals> call, Response<SubscribedGoals> response) {

                        System.out.println(response.code());
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }


                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                            tv.setText("Activated");
                            tv.setEnabled(true);

                        }
                        else
                        {
                            tv.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<SubscribedGoals> call, Throwable t) {

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                        tv.setEnabled(true);
                    }
                });
            }
        });
    }


}
