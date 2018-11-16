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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Roboto_Regular;
import tv.merabihar.app.merabihar.Model.ProfileFollowMapping;
import tv.merabihar.app.merabihar.Model.TransactionHistroy;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ProfileFollowAPI;

/**
 * Created by ZingoHotels Tech on 16-11-2018.
 */

public class WatchHistroyAdapter extends RecyclerView.Adapter<WatchHistroyAdapter.ViewHolder> {
    private Context context;
    private ArrayList<TransactionHistroy> list;
    public WatchHistroyAdapter(Context context,ArrayList<TransactionHistroy> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rewards_descriptions_single_layout, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final TransactionHistroy tc = list.get(position);

        if(tc!=null){

            double valuea = (tc.getValue())*.20;
            holder.mCoins.setText(""+(int)valuea);

            String tdate = tc.getTransactionHistoryDate();

            if(tdate.contains("T")){

                String[] tDates = tdate.split("T");
                try {
                    Date past = new SimpleDateFormat("yyyy-MM-dd").parse(tDates[0]);
                    String dateValue = new SimpleDateFormat("MMM dd,yyyy").format(past);
                    holder.mDate.setText(""+dateValue);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }



    }

    @Override
    public int getItemCount() {

        return list.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder  {


       TextView mTitle,mDesc,mCoins,mDate;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            mTitle = (TextView) itemView.findViewById(R.id.target_title_name);
            mDesc = (TextView) itemView.findViewById(R.id.target_descr_name);
            mCoins = (TextView) itemView.findViewById(R.id.max_coins_txt);
            mDate = (TextView) itemView.findViewById(R.id.earned_date_txt);



        }


    }


}
