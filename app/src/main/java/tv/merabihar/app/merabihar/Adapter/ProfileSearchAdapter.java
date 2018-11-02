package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.InterestContentListScreen;
import tv.merabihar.app.merabihar.UI.Activity.ProfileScreen;

/**
 * Created by ZingoHotels Tech on 01-11-2018.
 */

public class ProfileSearchAdapter extends RecyclerView.Adapter<ProfileSearchAdapter.ViewHolder> {
    private Context context;
    private ArrayList<UserProfile> list;
    public ProfileSearchAdapter(Context context,ArrayList<UserProfile> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.profile_search_adapter, parent, false);
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

            holder.mName.setText(""+profile.getFullName());
            String base=profile.getProfilePhoto();
            if(base != null && !base.isEmpty()){
                Picasso.with(context).load(base).placeholder(R.drawable.profile_image).
                        error(R.drawable.profile_image).into(holder.mPhoto);

            }else{
                holder.mPhoto.setImageResource(R.drawable.profile_image);
            }

            holder.mProfileLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, ProfileScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Profile",profile);
                    intent.putExtras(bundle);
                    context.startActivity(intent);

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
        LinearLayout mProfileLay;
        CircleImageView mPhoto;
//

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            mName = (TextView)itemView.findViewById(R.id.people_name);
            mProfileLay = (LinearLayout) itemView.findViewById(R.id.profile_search_layout);
            mPhoto = (CircleImageView)itemView.findViewById(R.id.people_profile_photo);


        }


    }


}
