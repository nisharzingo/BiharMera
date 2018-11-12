package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import tv.merabihar.app.merabihar.Model.Category;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;

public class VideoCategoryScreenAdapter extends RecyclerView.Adapter<VideoCategoryScreenAdapter.MyViewHolder> {

    Context context;
    ArrayList<Category> interests;

    public VideoCategoryScreenAdapter(Context context , ArrayList<Category> interests)
    {
        this.context = context;
        this.interests = interests;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.video_category_single_layout, viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        Category mCategory =  interests.get(position);

        ArrayList<Contents> mContentList;

        // Setting title for each category
        holder.mCatTitle.setText("Trending Now");

        // Get the content list from API
        mContentList = new ArrayList<Contents>();
        for(int i=0; i<5; i++){
            mContentList.add(new Contents());
        }

        RecyclerView mChildRecyclerView = holder.mChildRecyclerView;

        VideoCategoryItemAdapter childAdapter = new VideoCategoryItemAdapter(context, mContentList);
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mChildRecyclerView.setLayoutManager(manager);
        mChildRecyclerView.setHasFixedSize(true);
        mChildRecyclerView.setAdapter(childAdapter);


        /*when clicked on category title open category related to that*/
        holder.mCatTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, position+"", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return interests.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        RecyclerView mChildRecyclerView;
        TextView mCatTitle;

        MyViewHolder(View itemView) {
            super(itemView);
            mChildRecyclerView = itemView.findViewById(R.id.video_category_recyclerview_child);
            mCatTitle = itemView.findViewById(R.id.cat_title_name);
        }

    }
}
