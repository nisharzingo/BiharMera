package tv.merabihar.app.merabihar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import tv.merabihar.app.merabihar.Model.Contents;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.UI.Activity.ContentDetailScreen;
import tv.merabihar.app.merabihar.UI.Activity.ContentImageDetailScreen;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;

/**
 * Created by ZingoHotels Tech on 19-11-2018.
 */

public class ContentSearchPaginationAdapter extends RecyclerView.Adapter {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private boolean isLoadingAdded = false;
    private Context context;
    ArrayList<ArrayList<Contents>> mContentList;

    int currentPage = 0,start = 0,end = 0;
    Timer timer;
    final long DELAY_MS = 5000;
    final long PERIOD_MS = 3500;

    String url,fileNames;


    public ContentSearchPaginationAdapter(Context context) {

        this.context = context;
        mContentList = new ArrayList<>();


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType)
        {
            case ITEM:
                viewHolder = getViewHolder(parent,inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }

        return viewHolder;

    }

    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent,LayoutInflater inflater)
    {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.fragment_follow_content_single_layout, parent, false);
        viewHolder = new BlogViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holders, final int pos) {

        final int profileId = PreferenceHandler.getInstance(context).getUserId();


        try{

            final ArrayList<Contents> content = mContentList.get(pos);

            if(mContentList.get(pos)!=null){

                switch (getItemViewType(pos)) {

                    case ITEM:
                        final BlogViewHolder holder = (BlogViewHolder) holders;

                        final ImageView iv1 = holder.iv1 ;
                        ImageView iv2 = holder.iv2 ;
                        ImageView iv3 = holder.iv3 ;  // large imageview
                        ImageView iv4 = holder.iv4 ;
                        ImageView iv5 = holder.iv5 ;
                        ImageView iv6 = holder.iv6 ;
                        ImageView iv7 = holder.iv7 ;
                        ImageView iv8 = holder.iv8 ;
                        ImageView iv9 = holder.iv9 ;


                        if(content!=null&&content.size()!=0&&content.size()==9){


                            if(content.get(0).getContentType().equalsIgnoreCase("Video")){

                                holder.ivp1.setVisibility(View.VISIBLE);

                                if(content.get(0).getContentURL()!=null&&!content.get(0).getContentURL().isEmpty()){
                                    final String img = "https://img.youtube.com/vi/"+content.get(0).getContentURL()+"/0.jpg";
                                    if(img!=null&&!img.isEmpty()){


                                        Picasso.with(context)
                                                .load(Uri.parse(img))
                                                .error(R.drawable.no_image)
                                                .placeholder(R.drawable.no_image)
                                                .into(holder.iv1);
                                    }else{
                                        holder.iv1.setImageResource(R.drawable.no_image);
                                    }
                                }

                            }else{
                                if(content.get(0).getContentImage()!=null&&content.get(0).getContentImage().size()!=0){

                                    String urlString1 = content.get(0).getContentImage().get(0).getImages();

                                    if(urlString1.contains(" ")){
                                        Picasso.with(context)
                                                .load(urlString1.replaceAll(" ","%20"))

                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image)
                                                .into(holder.iv1);
                                    }else{
                                        Picasso.with(context)
                                                .load(urlString1)

                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image)
                                                .into(holder.iv1);
                                    }




                                }
                            }


                            if(content.get(1).getContentType().equalsIgnoreCase("Video")){

                                holder.ivp2.setVisibility(View.VISIBLE);

                                if(content.get(1).getContentURL()!=null&&!content.get(1).getContentURL().isEmpty()){
                                    final String img = "https://img.youtube.com/vi/"+content.get(1).getContentURL()+"/0.jpg";
                                    if(img!=null&&!img.isEmpty()){


                                        Picasso.with(context)
                                                .load(Uri.parse(img))
                                                .error(R.drawable.no_image)
                                                .placeholder(R.drawable.no_image)
                                                .into(holder.iv2);
                                    }else{
                                        holder.iv2.setImageResource(R.drawable.no_image);
                                    }
                                }

                            }else{
                                if(content.get(1).getContentImage()!=null&&content.get(1).getContentImage().size()!=0){

                                    String urlString1 = content.get(1).getContentImage().get(0).getImages();

                                    if(urlString1.contains(" ")){
                                        Picasso.with(context)
                                                .load(urlString1.replaceAll(" ","%20"))

                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image)
                                                .into(holder.iv2);
                                    }else{
                                        Picasso.with(context)
                                                .load(urlString1)

                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image)
                                                .into(holder.iv2);
                                    }

                                }
                            }


                            if(content.get(2).getContentType().equalsIgnoreCase("Video")){

                                holder.ivp3.setVisibility(View.VISIBLE);

                                if(content.get(2).getContentURL()!=null&&!content.get(2).getContentURL().isEmpty()){
                                    final String img = "https://img.youtube.com/vi/"+content.get(2).getContentURL()+"/0.jpg";
                                    if(img!=null&&!img.isEmpty()){

                                        Picasso.with(context)
                                                .load(Uri.parse(img))
                                                .error(R.drawable.no_image)
                                                .placeholder(R.drawable.no_image)
                                                .into(holder.iv3);
                                    }else{
                                        holder.iv3.setImageResource(R.drawable.no_image);
                                    }
                                }

                            }else{
                                if(content.get(2).getContentImage()!=null&&content.get(2).getContentImage().size()!=0){

                                    String urlString1 = content.get(2).getContentImage().get(0).getImages();

                                    if(urlString1.contains(" ")){
                                        Picasso.with(context)
                                                .load(urlString1.replaceAll(" ","%20"))

                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image)
                                                .into(holder.iv3);
                                    }else{
                                        Picasso.with(context)
                                                .load(urlString1)

                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image)
                                                .into(holder.iv3);
                                    }

                                }
                            }


                            if(content.get(3).getContentType().equalsIgnoreCase("Video")){

                                holder.ivp4.setVisibility(View.VISIBLE);

                                if(content.get(3).getContentURL()!=null&&!content.get(3).getContentURL().isEmpty()){
                                    final String img = "https://img.youtube.com/vi/"+content.get(3).getContentURL()+"/0.jpg";
                                    if(img!=null&&!img.isEmpty()){

                                        Picasso.with(context)
                                                .load(Uri.parse(img))
                                                .error(R.drawable.no_image)
                                                .placeholder(R.drawable.no_image)
                                                .into(holder.iv4);
                                    }else{
                                        holder.iv4.setImageResource(R.drawable.no_image);
                                    }
                                }

                            }else{
                                if(content.get(3).getContentImage()!=null&&content.get(3).getContentImage().size()!=0){

                                    String urlString1 = content.get(3).getContentImage().get(0).getImages();

                                    if(urlString1.contains(" ")){
                                        Picasso.with(context)
                                                .load(urlString1.replaceAll(" ","%20"))

                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image)
                                                .into(holder.iv4);
                                    }else{
                                        Picasso.with(context)
                                                .load(urlString1)

                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image)
                                                .into(holder.iv4);
                                    }

                                }
                            }


                            if(content.get(4).getContentType().equalsIgnoreCase("Video")){

                                holder.ivp5.setVisibility(View.VISIBLE);

                                if(content.get(4).getContentURL()!=null&&!content.get(4).getContentURL().isEmpty()){
                                    final String img = "https://img.youtube.com/vi/"+content.get(4).getContentURL()+"/0.jpg";
                                    if(img!=null&&!img.isEmpty()){


                                        Picasso.with(context)
                                                .load(Uri.parse(img))
                                                .error(R.drawable.no_image)
                                                .placeholder(R.drawable.no_image)
                                                .into(holder.iv5);
                                    }else{
                                        holder.iv5.setImageResource(R.drawable.no_image);
                                    }
                                }

                            }else{
                                if(content.get(4).getContentImage()!=null&&content.get(4).getContentImage().size()!=0){

                                    String urlString1 = content.get(4).getContentImage().get(0).getImages();

                                    if(urlString1.contains(" ")){
                                        Picasso.with(context)
                                                .load(urlString1.replaceAll(" ","%20"))

                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image)
                                                .into(holder.iv5);
                                    }else{
                                        Picasso.with(context)
                                                .load(urlString1)

                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image)
                                                .into(holder.iv5);
                                    }

                                }
                            }


                            if(content.get(5).getContentType().equalsIgnoreCase("Video")){

                                holder.ivp6.setVisibility(View.VISIBLE);

                                if(content.get(5).getContentURL()!=null&&!content.get(5).getContentURL().isEmpty()){
                                    final String img = "https://img.youtube.com/vi/"+content.get(5).getContentURL()+"/0.jpg";
                                    if(img!=null&&!img.isEmpty()){


                                        Picasso.with(context)
                                                .load(Uri.parse(img))
                                                .error(R.drawable.no_image)
                                                .placeholder(R.drawable.no_image)
                                                .into(holder.iv6);
                                    }else{
                                        holder.iv6.setImageResource(R.drawable.no_image);
                                    }
                                }

                            }else{
                                if(content.get(5).getContentImage()!=null&&content.get(5).getContentImage().size()!=0){

                                    String urlString1 = content.get(5).getContentImage().get(0).getImages();

                                    if(urlString1.contains(" ")){
                                        Picasso.with(context)
                                                .load(urlString1.replaceAll(" ","%20"))

                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image)
                                                .into(holder.iv6);
                                    }else{
                                        Picasso.with(context)
                                                .load(urlString1)

                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image)
                                                .into(holder.iv6);
                                    }

                                }
                            }


                            if(content.get(6).getContentType().equalsIgnoreCase("Video")){

                                holder.ivp7.setVisibility(View.VISIBLE);

                                if(content.get(6).getContentURL()!=null&&!content.get(6).getContentURL().isEmpty()){
                                    final String img = "https://img.youtube.com/vi/"+content.get(6).getContentURL()+"/0.jpg";
                                    if(img!=null&&!img.isEmpty()){


                                        Picasso.with(context)
                                                .load(Uri.parse(img))
                                                .error(R.drawable.no_image)
                                                .placeholder(R.drawable.no_image)
                                                .into(holder.iv7);
                                    }else{

                                        holder.iv7.setImageResource(R.drawable.no_image);
                                    }
                                }

                            }else{
                                if(content.get(6).getContentImage()!=null&&content.get(6).getContentImage().size()!=0){

                                    String urlString1 = content.get(6).getContentImage().get(0).getImages();

                                    if(urlString1.contains(" ")){
                                        Picasso.with(context)
                                                .load(urlString1.replaceAll(" ","%20"))

                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image)
                                                .into(holder.iv7);
                                    }else{
                                        Picasso.with(context)
                                                .load(urlString1)

                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image)
                                                .into(holder.iv7);
                                    }

                                }
                            }


                            if(content.get(7).getContentType().equalsIgnoreCase("Video")){

                                holder.ivp8.setVisibility(View.VISIBLE);

                                if(content.get(7).getContentURL()!=null&&!content.get(7).getContentURL().isEmpty()){
                                    final String img = "https://img.youtube.com/vi/"+content.get(7).getContentURL()+"/0.jpg";
                                    if(img!=null&&!img.isEmpty()){


                                        Picasso.with(context)
                                                .load(Uri.parse(img))
                                                .error(R.drawable.no_image)
                                                .placeholder(R.drawable.no_image)
                                                .into(holder.iv8);
                                    }else{
                                        holder.iv8.setImageResource(R.drawable.no_image);
                                    }
                                }

                            }else{
                                if(content.get(7).getContentImage()!=null&&content.get(7).getContentImage().size()!=0){

                                    String urlString1 = content.get(7).getContentImage().get(0).getImages();

                                    if(urlString1.contains(" ")){
                                        Picasso.with(context)
                                                .load(urlString1.replaceAll(" ","%20"))

                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image)
                                                .into(holder.iv8);
                                    }else{
                                        Picasso.with(context)
                                                .load(urlString1)

                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image)
                                                .into(holder.iv8);
                                    }

                                }
                            }


                            if(content.get(8).getContentType().equalsIgnoreCase("Video")){

                                holder.ivp9.setVisibility(View.VISIBLE);

                                if(content.get(8).getContentURL()!=null&&!content.get(8).getContentURL().isEmpty()){
                                    final String img = "https://img.youtube.com/vi/"+content.get(8).getContentURL()+"/0.jpg";
                                    if(img!=null&&!img.isEmpty()){


                                        Picasso.with(context)
                                                .load(Uri.parse(img))
                                                .error(R.drawable.no_image)
                                                .placeholder(R.drawable.no_image)
                                                .into(holder.iv9);
                                    }else{
                                        holder.iv9.setImageResource(R.drawable.no_image);
                                    }
                                }

                            }else{

                                if(content.get(8).getContentImage()!=null&&content.get(8).getContentImage().size()!=0){

                                    String urlString1 = content.get(8).getContentImage().get(0).getImages();

                                    if(urlString1.contains(" ")){
                                        Picasso.with(context)
                                                .load(urlString1.replaceAll(" ","%20"))

                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image)
                                                .into(holder.iv9);
                                    }else{
                                        Picasso.with(context)
                                                .load(urlString1)

                                                .placeholder(R.drawable.no_image)
                                                .error(R.drawable.no_image)
                                                .into(holder.iv9);
                                    }

                                }
                            }





                        }
                        // load image from api

        /*When clicked on the content image*/
                        iv1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(content.get(0).getContentType().equalsIgnoreCase("Video")){
                                    Intent intent = new Intent(context, ContentDetailScreen.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Contents",content.get(0));
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }else{
                                    Intent intent = new Intent(context, ContentImageDetailScreen.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Contents",content.get(0));
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }

                            }
                        });

                        iv2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(content.get(1).getContentType().equalsIgnoreCase("Video")){
                                    Intent intent = new Intent(context, ContentDetailScreen.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Contents",content.get(1));
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }else{
                                    Intent intent = new Intent(context, ContentImageDetailScreen.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Contents",content.get(1));
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }

                            }
                        });

                        iv3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(content.get(2).getContentType().equalsIgnoreCase("Video")){
                                    Intent intent = new Intent(context, ContentDetailScreen.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Contents",content.get(2));
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }else{
                                    Intent intent = new Intent(context, ContentImageDetailScreen.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Contents",content.get(2));
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }

                            }
                        });

                        iv4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(content.get(3).getContentType().equalsIgnoreCase("Video")){
                                    Intent intent = new Intent(context, ContentDetailScreen.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Contents",content.get(3));
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }else{
                                    Intent intent = new Intent(context, ContentImageDetailScreen.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Contents",content.get(3));
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }


                            }
                        });

                        iv5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(content.get(4).getContentType().equalsIgnoreCase("Video")){
                                    Intent intent = new Intent(context, ContentDetailScreen.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Contents",content.get(4));
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }else{
                                    Intent intent = new Intent(context, ContentImageDetailScreen.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Contents",content.get(4));
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }

                            }
                        });

                        iv6.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(content.get(5).getContentType().equalsIgnoreCase("Video")){
                                    Intent intent = new Intent(context, ContentDetailScreen.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Contents",content.get(5));
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }else{
                                    Intent intent = new Intent(context, ContentImageDetailScreen.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Contents",content.get(5));
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }

                            }
                        });

                        iv7.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(content.get(6).getContentType().equalsIgnoreCase("Video")){
                                    Intent intent = new Intent(context, ContentDetailScreen.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Contents",content.get(6));
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }else{
                                    Intent intent = new Intent(context, ContentImageDetailScreen.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Contents",content.get(6));
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }

                            }
                        });

                        iv8.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(content.get(7).getContentType().equalsIgnoreCase("Video")){
                                    Intent intent = new Intent(context, ContentDetailScreen.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Contents",content.get(7));
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }else{
                                    Intent intent = new Intent(context, ContentImageDetailScreen.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Contents",content.get(7));
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }

                            }
                        });
                        iv9.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(content.get(8).getContentType().equalsIgnoreCase("Video")){
                                    Intent intent = new Intent(context, ContentDetailScreen.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Contents",content.get(8));
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }else{
                                    Intent intent = new Intent(context, ContentImageDetailScreen.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Contents",content.get(8));
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }


                            }
                        });

                        break;

                    case LOADING:
//                Do nothing
                        break;
                }

            }



        }catch (Exception w){
            w.printStackTrace();
        }

    }

    @Override
    public int getItemViewType(int position) {
        System.out.println("inside getItemViewType = "+position);
        if(position == mContentList.size() - 1 && isLoadingAdded)
        {
            //System.out.println("inside getItemViewType LOADING = "+position);
            return LOADING;
        }
        else
        {
            //System.out.println("inside getItemViewType ITEM = "+position);
            return ITEM;
        }
        //return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @Override
    public int getItemCount() {
        return mContentList == null ? 0 : mContentList.size();
    }




    public class BlogViewHolder extends RecyclerView.ViewHolder {

        ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9,ivp1, ivp2, ivp3, ivp4, ivp5, ivp6, ivp7, ivp8, ivp9 ;

        public BlogViewHolder(View view) {
            super(view);
            iv1 = view.findViewById(R.id.follow_frag_img_view1);
            iv2 = view.findViewById(R.id.follow_frag_img_view2);
            iv3 = view.findViewById(R.id.follow_frag_img_view3);
            iv4 = view.findViewById(R.id.follow_frag_img_view4);
            iv5 = view.findViewById(R.id.follow_frag_img_view5);
            iv6 = view.findViewById(R.id.follow_frag_img_view6);
            iv7 = view.findViewById(R.id.follow_frag_img_view7);
            iv8 = view.findViewById(R.id.follow_frag_img_view8);
            iv9 = view.findViewById(R.id.follow_frag_img_view9);

            ivp1 = view.findViewById(R.id.videoicon1);
            ivp2 = view.findViewById(R.id.videoicon2);
            ivp3 = view.findViewById(R.id.videoicon3);
            ivp4 = view.findViewById(R.id.videoicon4);
            ivp5 = view.findViewById(R.id.videoicon5);
            ivp6 = view.findViewById(R.id.videoicon6);
            ivp7 = view.findViewById(R.id.videoicon7);
            ivp8 = view.findViewById(R.id.videoicon8);
            ivp9 = view.findViewById(R.id.videoicon9);




        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public void add(ArrayList<Contents> mc) {
        //System.out.println("BookingAndTraveller = "+mc.getRoomBooking().getTotalAmount());
        mContentList.add(mc);
        //System.out.println("BookingAndTraveller = "+list.size());
        notifyItemInserted(mContentList.size() - 1);
    }

    public void addAll(ArrayList<ArrayList<Contents>> mcList) {
        for (ArrayList<Contents> mc : mcList) {
            add(mc);
        }
    }
    public void adds(ArrayList<Contents> mc) {
        //System.out.println("BookingAndTraveller = "+mc.getRoomBooking().getTotalAmount());
        mContentList.add(0,mc);
        //System.out.println("BookingAndTraveller = "+list.size());
        notifyItemInserted(0);
    }

    public void addAlls(ArrayList<ArrayList<Contents>>mcList) {
        for (ArrayList<Contents> mc : mcList) {
            adds(mc);
        }
    }


    public void remove(ArrayList<Contents> city) {
        int position = mContentList.indexOf(city);
        if (position > -1) {
            mContentList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        //add(new Movie());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        if(mContentList != null && mContentList.size() !=0)
        {
            int position = mContentList.size() - 1;
            ArrayList<Contents> item = getItem(position);
            notifyItemChanged(position);

            /*if (item != null) {
                list.remove(position);
                notifyItemRemoved(position);
            }*/
        }
    }

    public ArrayList<Contents> getItem(int position) {
        return mContentList.get(position);
    }

    public ArrayList<ArrayList<Contents>> getAllBookings()
    {
        return mContentList;
    }

}
