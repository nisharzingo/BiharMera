package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tv.merabihar.app.merabihar.CustomFonts.MyTextView_Lato_Regular;
import tv.merabihar.app.merabihar.Model.NewsCategory;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.NewsListCategoryScreen;

public class NewsCategoryNameAdapter extends RecyclerView.Adapter<NewsCategoryNameAdapter.ViewHolder> {
    private Context context;
    private ArrayList<NewsCategory> list;
    public NewsCategoryNameAdapter(Context context,ArrayList<NewsCategory> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_news_name_adapter, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final NewsCategory content = list.get(position);

        if(content!=null){

            if(content.getTitle()!=null){

                holder.mInterestName.setText(""+content.getTitle());
            }






            holder.mInterestLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, NewsListCategoryScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Category",content.getTitle());
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }
            });



        }

    }

    @Override
    public int getItemCount() {

        return list.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder  {


        TextView mInterestName;
        LinearLayout mInterestLay;





        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            mInterestName = (TextView) itemView.findViewById(R.id.category_name);

            mInterestLay = (LinearLayout) itemView.findViewById(R.id.category_lyt);


        }

    }


    // loading the cropped image from youtube
    private void loadCroppedImage(String urlString, final ImageView imageView) {
        Picasso.with(context).load(urlString).into(new com.squareup.picasso.Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                // Cropping the image
                Bitmap customBitMap =  Bitmap.createBitmap(bitmap, 0, 45, bitmap.getWidth(), bitmap.getHeight()-90);

                Glide.with(context)
                        .load(customBitMap)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.no_image)
                                .error(R.drawable.no_image))
                        .into(imageView);

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }



}
