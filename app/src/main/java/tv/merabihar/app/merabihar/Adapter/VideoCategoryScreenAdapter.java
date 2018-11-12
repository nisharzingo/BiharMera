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
import tv.merabihar.app.merabihar.Model.CategoryAndContentList;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;

public class VideoCategoryScreenAdapter extends RecyclerView.Adapter<VideoCategoryScreenAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<CategoryAndContentList> list;

    public VideoCategoryScreenAdapter(Context context , ArrayList<CategoryAndContentList> interests)
    {
        this.context = context;
        this.list = interests;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.video_category_single_layout, viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final CategoryAndContentList category = list.get(position);

        if(category!=null){

            holder.mCatTitle.setText("Stories in "+category.getCategories().getCategoriesName());
            //getContents(category.getContentList(),holder.mConteList);

            ArrayList<Contents> contentsList = new ArrayList<>();
            ArrayList<Contents> mContentList = category.getContentList();;

            // Setting title for each category


            if(mContentList != null && mContentList.size() != 0)
            {

                // Get the content list from API


                for (Contents content:mContentList) {

                    if(content.getContentType().equalsIgnoreCase("Video")){
                        contentsList.add(content);
                    }


                }


                RecyclerView mChildRecyclerView = holder.mChildRecyclerView;

                if(contentsList!=null&&contentsList.size()!=0){
                    VideoCategoryItemAdapter childAdapter = new VideoCategoryItemAdapter(context, contentsList);
                    LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                    mChildRecyclerView.setLayoutManager(manager);
                    mChildRecyclerView.setHasFixedSize(true);
                    mChildRecyclerView.setAdapter(childAdapter);
                }



        /*when clicked on category title open category related to that*/
                holder.mCatTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, position+"", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        }





    }

    @Override
    public int getItemCount() {
        return list.size();
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
