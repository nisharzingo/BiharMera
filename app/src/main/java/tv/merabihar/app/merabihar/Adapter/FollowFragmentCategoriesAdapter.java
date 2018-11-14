package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import tv.merabihar.app.merabihar.Model.Category;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.CategoryContentList;

public class FollowFragmentCategoriesAdapter extends RecyclerView.Adapter<FollowFragmentCategoriesAdapter.MyViewHolder> {

    Context context;
    ArrayList<Category> mCategoriesList;

    public FollowFragmentCategoriesAdapter(Context context , ArrayList<Category> mCategoriesList)
    {
        this.context = context;
        this.mCategoriesList = mCategoriesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_follow_categories_single_layout,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final  Category mCategory =  mCategoriesList.get(position);
        ImageView catPoster = holder.categoryPoster;
        TextView catTitle = holder.categoryTitle;

        // load image from api

        if(mCategory!=null){

            String title =mCategory.getCategoriesName();
            String urlString = mCategory.getCategoriesImage();

            if(urlString!=null){
                catPoster.setImageURI(Uri.parse(urlString));
            }


            catTitle.setText(title);
        /*When clicked on the content image*/
            catPoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CategoryContentList.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Category",mCategory);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

        }

    }


    @Override
    public int getItemCount() {
        return mCategoriesList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView categoryPoster;
        TextView categoryTitle;

        MyViewHolder(View itemView) {
            super(itemView);
            categoryPoster = itemView.findViewById(R.id.follow_cate_imageview);
            categoryTitle = itemView.findViewById(R.id.follow_cate_title);
        }

    }

}