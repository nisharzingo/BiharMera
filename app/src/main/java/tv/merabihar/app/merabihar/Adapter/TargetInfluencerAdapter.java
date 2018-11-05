package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Lato_Regular;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Roboto_Regular;
import tv.merabihar.app.merabihar.Model.TargetDes;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;

/**
 * Created by ZingoHotels Tech on 05-11-2018.
 */

public class TargetInfluencerAdapter extends RecyclerView.Adapter<TargetInfluencerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<TargetDes> list;
    public TargetInfluencerAdapter(Context context,ArrayList<TargetDes> list) {

        this.context = context;
        this.list = list;

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

        final TargetDes targetDes = list.get(position);

        if(targetDes!=null){

            holder.mTitle.setText(""+targetDes.getTitle());
            holder.mDesc.setText(""+targetDes.getDesc());

        }



    }

    @Override
    public int getItemCount() {

        return list.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder  {

        MyTextView_Lato_Regular mTitle,mDesc;


        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            mTitle = (MyTextView_Lato_Regular) itemView.findViewById(R.id.target_title);
            mDesc = (MyTextView_Lato_Regular) itemView.findViewById(R.id.target_des);


        }


    }


}
