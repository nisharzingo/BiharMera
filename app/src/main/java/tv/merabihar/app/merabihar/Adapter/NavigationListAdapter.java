package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tv.merabihar.app.merabihar.Model.NavBarItems;
import tv.merabihar.app.merabihar.R;

/**
 * Created by ZingoHotels Tech on 05-11-2018.
 */

public class NavigationListAdapter  extends BaseAdapter {

    private Context context;
    private ArrayList<NavBarItems> mList = new ArrayList<>();

    public NavigationListAdapter(Context context, ArrayList<NavBarItems> mList)
    {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int pos) {
        return mList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {

        if(view == null)
        {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.navbar_list_item_layout,viewGroup,false);
        }

        TextView mTitle = (TextView) view.findViewById(R.id.title);

        mTitle.setText(mList.get(pos).getTitle().toString());
        return view;
    }
}
