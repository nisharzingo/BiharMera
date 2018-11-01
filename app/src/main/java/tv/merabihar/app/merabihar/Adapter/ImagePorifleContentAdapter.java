package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;

/**
 * Created by ZingoHotels Tech on 01-11-2018.
 */

public class ImagePorifleContentAdapter extends RecyclerView.Adapter<ImagePorifleContentAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Contents> list;

    public ImagePorifleContentAdapter(Context context, ArrayList<Contents> accountModels) {
        this.context = context;
        this.list = accountModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_pic_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Contents contents = list.get(position);

        if(contents!=null){
            if(contents.getContentType().equalsIgnoreCase("Video")){

                //url = contents.getContentURL();


                //holder.mIcon.setVisibility(View.VISIBLE);
                if(contents.getContentURL()!=null&&!contents.getContentURL().isEmpty()){
                    String img = "https://img.youtube.com/vi/"+contents.getContentURL()+"/0.jpg";
                    if(img!=null&&!img.isEmpty()){
                        Picasso.with(context).load(img).placeholder(R.drawable.no_image).
                                error(R.drawable.no_image).into(holder.accountpic);
                    }
                }else{
                    holder.accountpic.setImageResource(R.drawable.no_image);
                }





            }else{


                //holder.mIcon.setVisibility(View.GONE);
                if(contents.getContentImage() != null && contents.getContentImage().size()!=0)
                {

                    String img = contents.getContentImage().get(0).getImages();

                    if(img!=null&&!img.isEmpty()){
                        Picasso.with(context).load(img).placeholder(R.drawable.no_image).
                                error(R.drawable.no_image).into(holder.accountpic);
                    }else{
                        holder.accountpic.setImageResource(R.drawable.no_image);
                    }



                }else{
                    holder.accountpic.setImageResource(R.drawable.no_image);
                }

            }

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView accountpic;
        public ViewHolder(View itemView) {
            super(itemView);
            accountpic = itemView.findViewById(R.id.accountpic);
        }
    }
}
