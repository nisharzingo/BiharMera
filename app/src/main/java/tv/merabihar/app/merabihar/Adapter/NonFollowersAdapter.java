package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Roboto_Regular;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;

/**
 * Created by ZingoHotels Tech on 03-11-2018.
 */

public class NonFollowersAdapter  extends RecyclerView.Adapter<NonFollowersAdapter.ViewHolder> {
    private Context context;
    private ArrayList<UserProfile> list;
    public NonFollowersAdapter(Context context,ArrayList<UserProfile> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_non_follower, parent, false);
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
        MyTextView_Roboto_Regular mProfileName;
        TextView mFollowButton;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            mProfilePhoto = (CircleImageView) itemView.findViewById(R.id.profile_photo);
            mProfileName = (MyTextView_Roboto_Regular) itemView.findViewById(R.id.profile_name);
            mFollowButton = (TextView) itemView.findViewById(R.id.follow_options);


        }


    }


}
