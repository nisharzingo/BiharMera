package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tv.merabihar.app.merabihar.Model.Interest;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.MainTabHostScreens.PostContent.PostContentScreen;

/**
 * Created by ZingoHotels Tech on 02-11-2018.
 */

public class AutocompleteCustomArrayAdapter  extends ArrayAdapter<Interest> {

    final String TAG = "AutocompleteCustomArrayAdapter.java";

    Context mContext;
    int layoutResourceId;
    String activity;
    ArrayList<Interest> data = null;

    public AutocompleteCustomArrayAdapter(Context mContext, int layoutResourceId, ArrayList<Interest> data) {

        super(mContext, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.activity = activity;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try{

            /*
             * The convertView argument is essentially a "ScrapView" as described is Lucas post
             * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
             * It will have a non-null value when ListView is asking you recycle the row layout.
             * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
             */
            if(convertView==null){

                LayoutInflater inflater = ((PostContentScreen) mContext).getLayoutInflater();
                convertView = inflater.inflate(layoutResourceId, parent, false);

            }

            // object item based on the position
            Interest objectItem = data.get(position);

            // get the TextView and then set the text (item name) and tag (item ID) values
            TextView textViewItem = (TextView) convertView.findViewById(R.id.interest_name);
            textViewItem.setText("#"+objectItem.getInterestName());

            // in case you want to add some style, you can do something like:
            //textViewItem.setBackgroundColor(Color.CYAN);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;

    }
}
