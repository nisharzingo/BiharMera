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
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.ContentImageDetailScreen;

public class FollowFragmentContentAdapter extends RecyclerView.Adapter<FollowFragmentContentAdapter.MyViewHolder> {

    Context context;
    ArrayList<ArrayList<Contents>> mContentList;



    public FollowFragmentContentAdapter(Context context , ArrayList<ArrayList<Contents>> mContentList)
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

        final ArrayList<Contents> content = mContentList.get(position);



        SimpleDraweeView iv1 = holder.iv1 ;
        SimpleDraweeView iv2 = holder.iv2 ;
        SimpleDraweeView iv3 = holder.iv3 ;  // large imageview
        SimpleDraweeView iv4 = holder.iv4 ;
        SimpleDraweeView iv5 = holder.iv5 ;
        SimpleDraweeView iv6 = holder.iv6 ;
        SimpleDraweeView iv7 = holder.iv7 ;
        SimpleDraweeView iv8 = holder.iv8 ;
        SimpleDraweeView iv9 = holder.iv9 ;


        if(content!=null&&content.size()!=0&&content.size()==9){


            if(content.get(0).getContentImage()!=null&&content.get(0).getContentImage().size()!=0){

                String urlString1 = content.get(0).getContentImage().get(0).getImages();

                if(urlString1!=null){
                    iv1.setImageURI(Uri.parse(urlString1));
                }

            }

            if(content.get(1).getContentImage()!=null&&content.get(1).getContentImage().size()!=0){

                String urlString1 = content.get(1).getContentImage().get(0).getImages();

                if(urlString1!=null){
                    iv2.setImageURI(Uri.parse(urlString1));
                }

            }

            if(content.get(2).getContentImage()!=null&&content.get(2).getContentImage().size()!=0){

                String urlString1 = content.get(2).getContentImage().get(0).getImages();

                if(urlString1!=null){
                    iv3.setImageURI(Uri.parse(urlString1));
                }

            }

            if(content.get(3).getContentImage()!=null&&content.get(3).getContentImage().size()!=0){

                String urlString1 = content.get(3).getContentImage().get(0).getImages();

                if(urlString1!=null){
                    iv4.setImageURI(Uri.parse(urlString1));
                }

            }

            if(content.get(4).getContentImage()!=null&&content.get(4).getContentImage().size()!=0){

                String urlString1 = content.get(4).getContentImage().get(0).getImages();

                if(urlString1!=null){
                    iv5.setImageURI(Uri.parse(urlString1));
                }

            }

            if(content.get(5).getContentImage()!=null&&content.get(5).getContentImage().size()!=0){

                String urlString1 = content.get(5).getContentImage().get(0).getImages();

                if(urlString1!=null){
                    iv6.setImageURI(Uri.parse(urlString1));
                }

            }

            if(content.get(6).getContentImage()!=null&&content.get(6).getContentImage().size()!=0){

                String urlString1 = content.get(6).getContentImage().get(0).getImages();

                if(urlString1!=null){
                    iv7.setImageURI(Uri.parse(urlString1));
                }

            }

            if(content.get(7).getContentImage()!=null&&content.get(7).getContentImage().size()!=0){

                String urlString1 = content.get(7).getContentImage().get(0).getImages();

                if(urlString1!=null){
                    iv8.setImageURI(Uri.parse(urlString1));
                }

            }

            if(content.get(8).getContentImage()!=null&&content.get(8).getContentImage().size()!=0){

                String urlString1 = content.get(8).getContentImage().get(0).getImages();

                if(urlString1!=null){
                    iv9.setImageURI(Uri.parse(urlString1));
                }

            }




        }
        // load image from api

        /*When clicked on the content image*/
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContentImageDetailScreen.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Contents",content.get(0));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContentImageDetailScreen.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Contents",content.get(1));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContentImageDetailScreen.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Contents",content.get(2));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContentImageDetailScreen.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Contents",content.get(3));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContentImageDetailScreen.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Contents",content.get(4));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        iv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContentImageDetailScreen.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Contents",content.get(5));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        iv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContentImageDetailScreen.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Contents",content.get(6));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        iv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContentImageDetailScreen.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Contents",content.get(7));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        iv9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContentImageDetailScreen.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Contents",content.get(8));
                intent.putExtras(bundle);
                context.startActivity(intent);
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