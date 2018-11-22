package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Lato_Regular;
import tv.merabihar.app.merabihar.Model.Goals;
import tv.merabihar.app.merabihar.Model.SubscribedGoals;
import tv.merabihar.app.merabihar.Model.TargetDes;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.Influencer.InfluencerProgramDetailScreen;
import tv.merabihar.app.merabihar.UI.Activity.Influencer.InviteScreen;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.VideoCategoryScreens;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.GoalAPI;

public class ActiveTargetFragmentsAdapter extends RecyclerView.Adapter<ActiveTargetFragmentsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<SubscribedGoals> list;
    public ActiveTargetFragmentsAdapter(Context context,ArrayList<SubscribedGoals> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.active_fragement_adapter, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final SubscribedGoals targetDes = list.get(position);

        if(targetDes!=null){

            getGoals(targetDes.getGoalId(),holder.offerTitle);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String expdate = targetDes.getEndDate();
            Date past = null;



            if(expdate.contains("T")){

                String[] tDates = expdate.split("T");
                try {
                    past = dateFormat.parse(tDates[0]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            /*if ( new Date().getTime() > past.getTime()) {

                holder.mOfferExp.setVisibility(View.VISIBLE);
                holder.exp_offer.setText("Goal Expired");
                holder.mOnOffer.setVisibility(View.GONE);
            }else{
                holder.mOfferExp.setVisibility(View.GONE);
                holder.mOnOffer.setVisibility(View.VISIBLE);
            }*/

            holder.mMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent i = new Intent(context, InfluencerProgramDetailScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Goals",targetDes);
                    i.putExtras(bundle);
                    context.startActivity(i);
                }
            });

            holder.activate_offer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(targetDes.getGoalId()==1){

                        Intent i = new Intent(context, InviteScreen.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Goals",targetDes);
                        i.putExtras(bundle);
                        context.startActivity(i);
                    }else if(targetDes.getGoalId()==3){

                        Intent i = new Intent(context, VideoCategoryScreens.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Goals",targetDes);
                        i.putExtras(bundle);
                        context.startActivity(i);
                    }


                }
            });
            if(targetDes.getGoalId()==1){


                holder.activate_offer.setText("Refer Friend");

                String activeDate = targetDes.getActiveDate();

                if(activeDate.contains("T")){

                    String active[] = activeDate.split("T");
                    try {


                       // int value = (int)duration(active[0],new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                        if(targetDes.getStatus().equalsIgnoreCase("Activated")){

                            String rewards = targetDes.getRewardsEarned();
                            if(rewards.contains(",")){

                                String rew[] = rewards.split(",");

                                if(rew[0].equals("1")&&Integer.parseInt(rew[1])>=100){

                                    holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                }else if(rew[0].equals("1")&&Integer.parseInt(rew[1])<100){

                                    holder.mCircle1.setImageResource(R.drawable.oval_cross);
                                    holder.mCircleView1.setBackgroundColor(Color.parseColor("#FF0000"));

                                }

                                if (rew[0].equals("2") && Integer.parseInt(rew[1]) >= 300) {

                                    holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                    holder.mCircle2.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                } else if (rew[0].equals("2") && Integer.parseInt(rew[1]) < 300) {


                                    holder.mCircle2.setImageResource(R.drawable.oval_cross);
                                    holder.mCircleView2.setBackgroundColor(Color.parseColor("#FF0000"));

                                }

                                if (rew[0].equals(3) && Integer.parseInt(rew[1]) >= 900) {

                                    holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                    holder.mCircle2.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                    holder.mCircle3.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                                } else if (rew[0].equals("3") && Integer.parseInt(rew[1]) < 900) {

                                    holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                    holder.mCircle2.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                    holder.mCircle3.setImageResource(R.drawable.oval_cross);
                                    holder.mCircleView3.setBackgroundColor(Color.parseColor("#FF0000"));

                                }

                                if (rew[0].equals("4") && Integer.parseInt(rew[1]) >= 2700) {

                                    holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                    holder.mCircle2.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                    holder.mCircle3.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                                    holder.mCircle4.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));

                                } else if (rew[0].equals("4") && Integer.parseInt(rew[1]) < 2700) {
                                    holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                    holder.mCircle2.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                    holder.mCircle3.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));



                                    holder.mCircle4.setImageResource(R.drawable.oval_cross);
                                    holder.mCircleView4.setBackgroundColor(Color.parseColor("#FF0000"));

                                }

                                if (rew[0].equals("5") && Integer.parseInt(rew[1]) >= 5000) {

                                    holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                    holder.mCircle2.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                    holder.mCircle3.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                                    holder.mCircle4.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));

                                    holder.mCircle5.setImageResource(R.drawable.ovel_tick);


                                } else if (rew[0].equals("5") && Integer.parseInt(rew[1]) < 5000) {

                                    holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                    holder.mCircle2.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                    holder.mCircle3.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                                    holder.mCircle4.setImageResource(R.drawable.ovel_tick);
                                    holder.mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));

                                    holder.mCircle5.setImageResource(R.drawable.oval_cross);

                                }
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }else if(targetDes.getGoalId()==2){

                holder.activate_offer.setVisibility(View.GONE);

                String endDate = targetDes.getEndDate();

                if(endDate!=null&&!endDate.isEmpty()){

                    if(endDate.contains("T")){

                        String dats[] = endDate.split("T");
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dats[0]);

                            if(new Date().getTime()-date.getTime()>0){


                            }else{
                                if(targetDes.getStatus().equalsIgnoreCase("Activated")) {

                                    String rewards = targetDes.getRewardsEarned();

                                    int value = Integer.parseInt(rewards);

                                    if (value <=1) {
                                        holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));
                                    } else if (value <=2) {
                                        holder.mCircle2.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));
                                    } else if (value <=3) {
                                        holder.mCircle3.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));
                                    } else if (value <=4) {
                                        holder.mCircle4.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));
                                    } else if (value <=5) {
                                        holder.mCircle5.setImageResource(R.drawable.ovel_tick);
                                    }

                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }else if(targetDes.getGoalId()==3){

                holder.activate_offer.setText("Watch Video");

                String endDate = targetDes.getEndDate();

                if(endDate!=null&&!endDate.isEmpty()){

                    if(endDate.contains("T")){

                        String dats[] = endDate.split("T");
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dats[0]);

                            if(new Date().getTime()-date.getTime()>0){


                            }else{
                                if(targetDes.getStatus().equalsIgnoreCase("Activated")) {

                                    String rewards = targetDes.getRewardsEarned();
                                    int penaltyAmount = Integer.parseInt(targetDes.getExtraDescription());

                                    int value = Integer.parseInt(rewards);

                                    if (value >= (10800+penaltyAmount) && value < (21600+penaltyAmount) ) {
                                        holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));
                                    } else if (value >= (21600+penaltyAmount) && value < (32400+penaltyAmount) ) {

                                        holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle2.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));
                                    } else if (value >= (32400+penaltyAmount) && value < (43200+penaltyAmount) ) {

                                        holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle2.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle3.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));
                                    } else if (value >= (43200+penaltyAmount) && value < (54000+penaltyAmount) ) {
                                        holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle2.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle3.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle4.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));
                                    } else if (value >= (54000+penaltyAmount)) {

                                        holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle2.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle3.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle4.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle5.setImageResource(R.drawable.ovel_tick);
                                    }

                                }else if(targetDes.getStatus().equalsIgnoreCase("Completed")) {

                                    String rewards = targetDes.getRewardsEarned();
                                    int penaltyAmount = Integer.parseInt(targetDes.getExtraDescription());

                                    int value = Integer.parseInt(rewards);

                                    if (value >= (10800+penaltyAmount) && value < (21600+penaltyAmount) ) {
                                        holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));
                                    } else if (value >= (21600+penaltyAmount) && value < (32400+penaltyAmount) ) {

                                        holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle2.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));
                                    } else if (value >= (32400+penaltyAmount) && value < (43200+penaltyAmount) ) {

                                        holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle2.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle3.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));
                                    } else if (value >= (43200+penaltyAmount) && value < (54000+penaltyAmount) ) {
                                        holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle2.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle3.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle4.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));
                                    } else if (value >= (54000+penaltyAmount)) {

                                        holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle2.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle3.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle4.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle5.setImageResource(R.drawable.ovel_tick);
                                    }

                                }else if(targetDes.getStatus().equalsIgnoreCase("Penalty")){

                                    String rewards = targetDes.getRewardsEarned();
                                    int penaltyAmount = Integer.parseInt(targetDes.getExtraDescription());

                                    int value = Integer.parseInt(rewards);

                                    if (value >= (10800+penaltyAmount) && value < (21600+penaltyAmount) ) {
                                        holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));
                                    } else if (value >= (21600+penaltyAmount) && value < (32400+penaltyAmount) ) {

                                        holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle2.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));
                                    } else if (value >= (32400+penaltyAmount) && value < (43200+penaltyAmount) ) {

                                        holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle2.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle3.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));
                                    } else if (value >= (43200+penaltyAmount) && value < (54000+penaltyAmount) ) {
                                        holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle2.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle3.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle4.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));
                                    } else if (value >= (54000+penaltyAmount)) {

                                        holder.mCircle1.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView1.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle2.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView2.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle3.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView3.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle4.setImageResource(R.drawable.ovel_tick);
                                        holder.mCircleView4.setBackgroundColor(Color.parseColor("#176e0b"));

                                        holder.mCircle5.setImageResource(R.drawable.ovel_tick);
                                    }

                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }


            }
        }

    }

    @Override
    public int getItemCount() {

        return list.size();

    }



    class ViewHolder extends RecyclerView.ViewHolder {

        TextView offerTitle, offerDetails;
        Button activate_offer,exp_offer;
        CircleImageView offerLogo;
        LinearLayout mMain;
        ImageView mCircle1,mCircle2,mCircle3,mCircle4,mCircle5;
        View mCircleView1,mCircleView2,mCircleView3,mCircleView4;
        LinearLayout mOnOffer,mOfferExp;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            offerDetails = itemView.findViewById(R.id.offer_details);
            mMain = itemView.findViewById(R.id.layout_main);
            offerTitle = itemView.findViewById(R.id.offer_title);
            activate_offer = itemView.findViewById(R.id.activate_offer);
            exp_offer = itemView.findViewById(R.id.offer_earn);

            offerLogo = itemView.findViewById(R.id.offer_logo);
            mCircle1 = itemView.findViewById(R.id.month1_pb_circle);
            mCircle2 = itemView.findViewById(R.id.month2_pb_circle);
            mCircle3 = itemView.findViewById(R.id.month3_pb_circle);
            mCircle4 = itemView.findViewById(R.id.month4_pb);
            mCircle5 = itemView.findViewById(R.id.month5_pb_circle);

            mCircleView1 = itemView.findViewById(R.id.month1_pb_line);
            mCircleView2 = itemView.findViewById(R.id.month2_pb_line);
            mCircleView3 = itemView.findViewById(R.id.month3_pb_line);
            mCircleView4 = itemView.findViewById(R.id.month4_pb_line);

            mOnOffer = itemView.findViewById(R.id.on_offer_period);
            mOfferExp = itemView.findViewById(R.id.on_ex_period);



        }

    }

    public void getGoals(final int ids,final TextView textView)
    {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final GoalAPI categoryAPI = Util.getClient().create(GoalAPI.class);
                Call<Goals> getCat = categoryAPI.getGoalsById(ids);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<Goals>() {

                    @Override
                    public void onResponse(Call<Goals> call, Response<Goals> response) {


                        Goals goals = response.body();

                        if(response.code() == 200 && goals!= null)
                        {

                            textView.setText(""+goals.getGoalName());



                        }else{



                        }
                    }

                    @Override
                    public void onFailure(Call<Goals> call, Throwable t) {


//                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public long duration(String fromm,String too) throws Exception{

        String from = fromm+" 00:00:00";
        String to = too+" 00:00:00";

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1 = null;
        Date d2 = null;
        try {

            if(from.contains("-"))
            {
                d1 = format1.parse(from);
                d2 = format1.parse(to);
            }
            else
            {
                d1 = format.parse(from);
                d2 = format.parse(to);
            }
            long diff = d2.getTime() - d1.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            return diffDays;
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }

    }

}
