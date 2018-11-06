package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Lato_Regular;
import tv.merabihar.app.merabihar.Model.TargetDes;
import tv.merabihar.app.merabihar.R;

public class ActiveTargetFragmentsAdapter extends RecyclerView.Adapter<ActiveTargetFragmentsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TargetDes> list;
    public ActiveTargetFragmentsAdapter(Context context,ArrayList<TargetDes> list) {

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

        final TargetDes targetDes = list.get(position);

    }

    @Override
    public int getItemCount() {

        return list.size();

    }



    class ViewHolder extends RecyclerView.ViewHolder {

        TextView offerTitle, offerDetails;
        Button activate_offer;
        CircleImageView offerLogo;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            offerDetails = itemView.findViewById(R.id.offer_details);
            offerTitle = itemView.findViewById(R.id.offer_title);
            activate_offer = itemView.findViewById(R.id.activate_offer);
            offerLogo = itemView.findViewById(R.id.offer_logo);


        }

    }
}
