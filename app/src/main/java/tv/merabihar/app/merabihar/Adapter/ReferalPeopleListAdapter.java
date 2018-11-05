package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Roboto_Regular;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;

/**
 * Created by ZingoHotels Tech on 05-11-2018.
 */

public class ReferalPeopleListAdapter extends RecyclerView.Adapter<ReferalPeopleListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<UserProfile> list;
    public ReferalPeopleListAdapter(Context context,ArrayList<UserProfile> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_referal_profile, parent, false);
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


            holder.mProfileName.setText(""+profile.getFullName());

            String date = profile.getSignUpDate();

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


        }

    }

    @Override
    public int getItemCount() {

        return list.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder  {


        CircleImageView mProfilePhoto;
        MyTextView_Roboto_Regular mProfileName,mSignUpDate;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            mProfilePhoto = (CircleImageView) itemView.findViewById(R.id.profile_photo_referal);
            mProfileName = (MyTextView_Roboto_Regular) itemView.findViewById(R.id.profile_name_referal);
            mSignUpDate = (MyTextView_Roboto_Regular) itemView.findViewById(R.id.signUp_date_referal);


        }


    }


}
