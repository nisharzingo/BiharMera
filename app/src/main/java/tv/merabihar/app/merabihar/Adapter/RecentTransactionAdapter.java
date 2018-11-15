package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import tv.merabihar.app.merabihar.R;

/**
 * Created by ZingoHotels Tech on 02-11-2018.
 */

public class RecentTransactionAdapter  extends RecyclerView.Adapter<RecentTransactionAdapter.ViewHolder>{

    private Context context;
    ArrayList transactionList;

    public RecentTransactionAdapter(Context context, ArrayList transactionList){
        this.context = context;
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_transaction_single_layout, parent, false);
            return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int currentPos = position;

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);

           TextView earnedMoney =  itemView.findViewById(R.id.reward_money);
           TextView earnedDate = itemView.findViewById(R.id.reward_earned_date);

        }
    }
}
