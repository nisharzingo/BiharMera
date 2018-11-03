package tv.merabihar.app.merabihar.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Roboto_Regular;
import tv.merabihar.app.merabihar.Model.ProfileFollowMapping;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.ContentImageDetailScreen;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ProfileFollowAPI;

/**
 * Created by ZingoHotels Tech on 03-11-2018.
 */

public class FollowRequestAdapter   extends RecyclerView.Adapter<FollowRequestAdapter.ViewHolder> {
    private Context context;
    private ArrayList<UserProfile> list;
    public FollowRequestAdapter(Context context,ArrayList<UserProfile> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_follow_people_list, parent, false);
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

            holder.mFollowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String follow = holder.mFollowButton.getText().toString();
                    int mappingId = Integer.parseInt(holder.mMapId.getText().toString());

                    if(follow.equalsIgnoreCase("Follow")){

                        ProfileFollowMapping pm = new ProfileFollowMapping();
                        pm.setFollowerId(profile.getProfileId());
                        pm.setProfileId(PreferenceHandler.getInstance(context).getUserId());
                        profileFollow(pm,holder.mFollowButton,holder.mMapId);

                    }else if(follow.equalsIgnoreCase("Unfollow")){


                        if(mappingId!=0){

                            deleteFollow(mappingId,holder.mFollowButton,holder.mMapId);

                        }else{
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
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


        CircleImageView mProfilePhoto;
        MyTextView_Roboto_Regular mProfileName,mMapId;
        TextView mFollowButton;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            mProfilePhoto = (CircleImageView) itemView.findViewById(R.id.profile_photo);
            mProfileName = (MyTextView_Roboto_Regular) itemView.findViewById(R.id.profile_name);
            mMapId = (MyTextView_Roboto_Regular) itemView.findViewById(R.id.map_id);
            mFollowButton = (TextView) itemView.findViewById(R.id.follow_options);


        }


    }

    private void profileFollow(final ProfileFollowMapping intrst,final TextView follow,final MyTextView_Roboto_Regular mappingId ) {


        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ProfileFollowAPI mapApi = Util.getClient().create(ProfileFollowAPI.class);
                Call<ProfileFollowMapping> response = mapApi.postInterest(intrst);
                response.enqueue(new Callback<ProfileFollowMapping>() {
                    @Override
                    public void onResponse(Call<ProfileFollowMapping> call, Response<ProfileFollowMapping> response) {

                        System.out.println(response.code());

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {


                            follow.setText("Unfollow");
                            mappingId.setText(""+response.body().getFollowId());


                        }
                        else
                        {

                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileFollowMapping> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    private void deleteFollow(final int mapId,final TextView follow,final MyTextView_Roboto_Regular mappingId ) {


        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ProfileFollowAPI mapApi = Util.getClient().create(ProfileFollowAPI.class);
                Call<ProfileFollowMapping> response = mapApi.deleteIntrs(mapId);
                response.enqueue(new Callback<ProfileFollowMapping>() {
                    @Override
                    public void onResponse(Call<ProfileFollowMapping> call, Response<ProfileFollowMapping> response) {

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
                    public void onFailure(Call<ProfileFollowMapping> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }


}
