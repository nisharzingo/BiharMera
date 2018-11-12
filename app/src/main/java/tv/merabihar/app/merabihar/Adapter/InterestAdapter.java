package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import tv.merabihar.app.merabihar.Model.Interest;
import tv.merabihar.app.merabihar.R;

/**
 * Created by ZingoHotels Tech on 12-11-2018.
 */

public class InterestAdapter extends BaseAdapter {



    Context context;
    ArrayList<Interest> interests;

    public InterestAdapter(Context context , ArrayList<Interest> interests)//, ArrayList<SelectingRoomModel> rooms)
    {
        this.context = context;
        this.interests = interests;
    }
    @Override
    public int getCount() {
        //System.out.println("class SelectRoomGridViewAdapter = "+rooms.size());
        return interests.size();
        //return 20;//rooms.size();

    }

    @Override
    public Object getItem(int position) {
        return position;//rooms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.interest_gridview_layout,parent,false);
        }

        TextView interstname = convertView.findViewById(R.id.interest_name);
        LinearLayout interstLay = convertView.findViewById(R.id.main_grid_interest);
        interstname.setText(interests.get(position).getInterestName());


        return convertView;
    }
}
