package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;

public class FollowFragmentContentAdapter extends RecyclerView.Adapter<FollowFragmentContentAdapter.MyViewHolder> {

    Context context;
    ArrayList<Contents> mContentList;



    public FollowFragmentContentAdapter(Context context , ArrayList<Contents> mContentList)
    {
        this.context = context;
        this.mContentList = mContentList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_follow_content_single_layout,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Contents content = mContentList.get(position);

        SimpleDraweeView iv1 = holder.iv1 ;
        SimpleDraweeView iv2 = holder.iv2 ;
        SimpleDraweeView iv3 = holder.iv3 ;  // large imageview
        SimpleDraweeView iv4 = holder.iv4 ;
        SimpleDraweeView iv5 = holder.iv5 ;
        SimpleDraweeView iv6 = holder.iv6 ;
        SimpleDraweeView iv7 = holder.iv7 ;
        SimpleDraweeView iv8 = holder.iv8 ;
        SimpleDraweeView iv9 = holder.iv9 ;


        // load image from api
        String urlString1 = "https://mykidslickthebowl.com/wp-content/uploads/2017/11/welcome-to-my-kids-lick-the-bowl-1-of-2017-1-720x722.jpg";
        String urlString2 = "https://expatliving.hk/wp-content/uploads/2017/11/Kids-clothes-Hong-Kong-seed.jpg";
        String urlString3 = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSyfjKao_dfG97UuqYvkwsPwfilQSbWW2wBTCxpDM2_aj3_-KcT";
        String urlString4 = "https://www.sciencenews.org/sites/default/files/2018/09/main/articles/092618_LS_screen-time_feat.jpg";
        String urlString5 = "https://mykidslickthebowl.com/wp-content/uploads/2017/11/welcome-to-my-kids-lick-the-bowl-1-of-2017-1-720x722.jpg";
        String urlString6 = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSyfjKao_dfG97UuqYvkwsPwfilQSbWW2wBTCxpDM2_aj3_-KcT";
        String urlString7 = "https://expatliving.hk/wp-content/uploads/2017/11/Kids-clothes-Hong-Kong-seed.jpg";
        String urlString8 = "https://www.sciencenews.org/sites/default/files/2018/09/main/articles/092618_LS_screen-time_feat.jpg";
        String urlString9 = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSyfjKao_dfG97UuqYvkwsPwfilQSbWW2wBTCxpDM2_aj3_-KcT";


        iv1.setImageURI(Uri.parse(urlString1));
        iv2.setImageURI(Uri.parse(urlString2));
        iv3.setImageURI(Uri.parse(urlString3));
        iv4.setImageURI(Uri.parse(urlString4));
        iv5.setImageURI(Uri.parse(urlString5));
        iv6.setImageURI(Uri.parse(urlString6));
        iv7.setImageURI(Uri.parse(urlString7));
        iv8.setImageURI(Uri.parse(urlString8));
        iv9.setImageURI(Uri.parse(urlString9));

        /*When clicked on the content image*/
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, position + "", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return mContentList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9 ;

        MyViewHolder(View itemView) {
            super(itemView);
            iv1 = itemView.findViewById(R.id.follow_frag_img_view1);
            iv2 = itemView.findViewById(R.id.follow_frag_img_view2);
            iv3 = itemView.findViewById(R.id.follow_frag_img_view3);
            iv4 = itemView.findViewById(R.id.follow_frag_img_view4);
            iv5 = itemView.findViewById(R.id.follow_frag_img_view5);
            iv6 = itemView.findViewById(R.id.follow_frag_img_view6);
            iv7 = itemView.findViewById(R.id.follow_frag_img_view7);
            iv8 = itemView.findViewById(R.id.follow_frag_img_view8);
            iv9 = itemView.findViewById(R.id.follow_frag_img_view9);
        }

    }

}