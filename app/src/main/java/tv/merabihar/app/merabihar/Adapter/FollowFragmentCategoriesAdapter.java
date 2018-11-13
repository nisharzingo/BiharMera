package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.net.Uri;
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
        String title = "Trending";
        String urlString = "data:image/jpeg;base64,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";
        catPoster.setImageURI(Uri.parse(urlString));

        catTitle.setText(title);
        /*When clicked on the content image*/
        catPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Toast.makeText(context, position + "", Toast.LENGTH_SHORT).show();
            }
        });
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