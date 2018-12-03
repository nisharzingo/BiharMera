package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Roboto_Regular;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.WatchedHistroyScreen;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ProfileFollowAPI;

/**
 * Created by ZingoHotels Tech on 05-11-2018.
 */

public class ReferalPeopleListAdapter extends RecyclerView.Adapter<ReferalPeopleListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<UserProfile> list;
    String type;
    public ReferalPeopleListAdapter(Context context,ArrayList<UserProfile> list,String type) {

        this.context = context;
        this.list = list;
        this.type = type;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reward_list_single_layout, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final UserProfile profile = list.get(position);

        if(profile!=null){


            holder.top_user_value.setText(""+(position+1));
            holder.top_nick_name.setText(""+profile.getFullName());

            int coinsUsed = profile.getUsedAmount();
            int wallet = profile.getWalletBalance();
            double coinsValue =profile.getReferralAmount();
            double rupees = profile.getReferralAmountForOtherProfile();

            double  balance = coinsUsed+coinsValue;
            double amount = ((balance*1.0)/100.0);

            TextView earn_textview = holder.total_earning_value;
            getDirectRefer("MBR"+profile.getProfileId(),holder.total_invite_value, earn_textview);

            holder.mFriendMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent wath = new Intent(context, WatchedHistroyScreen.class);
                    wath.putExtra("ProfileId",profile.getProfileId());
                    wath.putExtra("FriendType",type);
                    wath.putExtra("ReferalCode",profile.getReferralCodeUsed());
                    context.startActivity(wath);
                }
            });

           /* String date = profile.getSignUpDate();

            if(date!=null&&!date.isEmpty()&&date.contains("T")){

                String spilt[] = date.split("T");
                try {
                    Date time = new SimpleDateFormat("yyyy-MM-dd").parse(spilt[0]);

                    String sign = new SimpleDateFormat("MMM dd,yyyy").format(time);
                    holder.mSignUpDate.setText(""+sign);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }





            String base=profile.getProfilePhoto();
            if(base != null && !base.isEmpty()){
                Picasso.with(context).load(base).placeholder(R.drawable.profile_image).
                        error(R.drawable.profile_image).into(holder.mProfilePhoto);

            }else{
                holder.mProfilePhoto.setImageResource(R.drawable.profile_image);
            }
*/

        }

    }

    @Override
    public int getItemCount() {

        return list.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder  {


        /*CircleImageView mProfilePhoto;
        MyTextView_Roboto_Regular mProfileName,mSignUpDate;*/
        LinearLayout mFriendMain;
        TextView top_user_value,top_nick_name,total_invite_value,total_earning_value;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            mFriendMain = (LinearLayout) itemView.findViewById(R.id.friend_main);
            top_user_value = (TextView) itemView.findViewById(R.id.top_user_value);
            top_nick_name = (TextView) itemView.findViewById(R.id.top_nick_name);
            total_invite_value = (TextView) itemView.findViewById(R.id.total_invite_value);
            total_earning_value = (TextView) itemView.findViewById(R.id.total_earning_value);


        }


    }

    private void getDirectRefer(final String code, final TextView tv, final TextView earn_textview){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getDirectReferedProfile(code);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();


                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {
                                //Collections.shuffle(responseProfile);

                               int size =  response.body().size();

                                tv.setText(""+size);

                                earn_textview.setText( String.valueOf(((size*0.5)+0.5)) );


                            }
                            else
                            {

                                tv.setText(""+0);
                                earn_textview.setText( String.valueOf(0.5) );

                            }
                        }
                        else
                        {



                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {



                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }


}
