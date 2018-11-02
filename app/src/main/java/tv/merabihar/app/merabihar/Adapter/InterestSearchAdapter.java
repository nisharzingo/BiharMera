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

import java.util.ArrayList;

import tv.merabihar.app.merabihar.Model.Interest;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.CategoryContentList;
import tv.merabihar.app.merabihar.UI.Activity.InterestContentListScreen;
import tv.merabihar.app.merabihar.UI.Activity.InterestListScreen;

/**
 * Created by ZingoHotels Tech on 01-11-2018.
 */

public class InterestSearchAdapter extends RecyclerView.Adapter<InterestSearchAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Interest> list;
    public InterestSearchAdapter(Context context,ArrayList<Interest> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.interest_search_adapter, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Interest profile = list.get(position);

        if(profile!=null){

            holder.mName.setText(""+profile.getInterestName());

            holder.mProfileLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, InterestContentListScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Interest",profile);
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

//

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            mName = (TextView)itemView.findViewById(R.id.interest_name);
            mProfileLay = (LinearLayout) itemView.findViewById(R.id.profile_blog);



        }


    }


}
