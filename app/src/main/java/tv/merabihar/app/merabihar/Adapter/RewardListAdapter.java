package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;

public class RewardListAdapter extends RecyclerView.Adapter<RewardListAdapter.MyAdapter> {

    ArrayList<UserProfile> userList;
    Context context;

    RewardListAdapter(Context context, ArrayList<UserProfile> userList ){
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reward_list_single_layout, parent, false);
            MyAdapter viewHolder = new MyAdapter(v);
            return viewHolder;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {

        holder.totalEarn.setText("100");
        holder.totalInvite.setText("15");
        holder.nickName.setText("NickName");
        holder.top50.setText("20");

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyAdapter extends RecyclerView.ViewHolder{

        TextView nickName, top50, totalInvite, totalEarn ;

        public MyAdapter(View itemView) {
            super(itemView);

            top50 = itemView.findViewById(R.id.top_user_value);
            nickName = itemView.findViewById(R.id.top_nick_name);
            totalInvite = itemView.findViewById(R.id.total_invite_value);
            totalEarn = itemView.findViewById(R.id.total_earning_value);


        }




    }
}
