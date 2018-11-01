package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexzh.circleimageview.CircleImageView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Roboto_Regular;
import tv.merabihar.app.merabihar.Model.Category;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.Model.Interest;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.CategoryContentList;
import tv.merabihar.app.merabihar.UI.Activity.ContentDetailScreen;

/**
 * Created by ZingoHotels Tech on 01-11-2018.
 */

public class CategoryGridAdapter extends BaseAdapter {



    Context context;
    ArrayList<Category> interests;

    public CategoryGridAdapter(Context context , ArrayList<Category> interests)//, ArrayList<SelectingRoomModel> rooms)
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.category_grid_view,parent,false);
        }

        MyTextView_Roboto_Regular interstname = convertView.findViewById(R.id.category_name);
        ImageView categoryPic = convertView.findViewById(R.id.category_card_image);
        CardView interstLay = convertView.findViewById(R.id.category_card);
        interstname.setText(interests.get(position).getCategoriesName());

        String img = interests.get(position).getCategoriesImage();

            if(img!=null&&!img.isEmpty()){
                Picasso.with(context).load(img).placeholder(R.drawable.no_image).
                        error(R.drawable.no_image).into(categoryPic);
            }else{
                categoryPic.setImageResource(R.drawable.no_image);
            }


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, CategoryContentList.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Category",interests.get(position));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        return convertView;
    }
}
