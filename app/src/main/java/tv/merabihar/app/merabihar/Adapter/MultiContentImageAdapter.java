package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tv.merabihar.app.merabihar.Model.ContentImages;
import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.YoutubePlayList.YouTubeListScreen;

/**
 * Created by ZingoHotels Tech on 02-11-2018.
 */

public class MultiContentImageAdapter  extends RecyclerView.Adapter<MultiContentImageAdapter.ViewHolder>{
    private Context context;
    private ArrayList<ArrayList<String> > mLists = new ArrayList<>();

    public MultiContentImageAdapter(Context context, ArrayList<ArrayList<String> > mLists) {
        this.context = context;
        this.mLists = mLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.insta_image_adapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int pos) {

        if(pos%2==0){
            holder.view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }else{
            holder.view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }


        if(mLists.get(pos)!=null&&mLists.get(pos).size()!=0){


            Picasso.with(context).load(mLists.get(pos).get(0)).placeholder(R.drawable.no_image).
                    error(R.drawable.no_image).into(holder.one);

            Picasso.with(context).load(mLists.get(pos).get(1)).placeholder(R.drawable.no_image).
                    error(R.drawable.no_image).into(holder.two);

            Picasso.with(context).load(mLists.get(pos).get(2)).placeholder(R.drawable.no_image).
                    error(R.drawable.no_image).into(holder.three);

            Picasso.with(context).load(mLists.get(pos).get(3)).placeholder(R.drawable.no_image).
                    error(R.drawable.no_image).into(holder.four);

            Picasso.with(context).load(mLists.get(pos).get(4)).placeholder(R.drawable.no_image).
                    error(R.drawable.no_image).into(holder.five);
        }

        holder.one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, YouTubeListScreen.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Position",((pos*5)+0));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, YouTubeListScreen.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Position",((pos*5)+1));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, YouTubeListScreen.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Position",((pos*5)+2));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, YouTubeListScreen.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Position",((pos*5)+3));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, YouTubeListScreen.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Position",((pos*5)+4));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView  one,two,three,four,five;
        VideoView videoView;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            one = (ImageView) itemView.findViewById(R.id.activity_image_one);
            two = (ImageView) itemView.findViewById(R.id.activity_image_two);
            three = (ImageView) itemView.findViewById(R.id.activity_image_three);
            four = (ImageView) itemView.findViewById(R.id.activity_image_four);
            five = (ImageView) itemView.findViewById(R.id.activity_image_five);
            videoView = (VideoView) itemView.findViewById(R.id.videoView);

            MediaController mediaController= new MediaController(context);
            mediaController.setAnchorView(videoView);


        }
    }
}
