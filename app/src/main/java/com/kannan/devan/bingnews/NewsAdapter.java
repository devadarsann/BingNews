package com.kannan.devan.bingnews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by devan on 12/3/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.Viewholder> {
    List<JsonData> JsonList;
    JsonData mJsonData;
    Context mContext;

    public NewsItemClickListener getmNewsItemClickListener() {
        return mNewsItemClickListener;
    }

    public void setmNewsItemClickListener(NewsItemClickListener mNewsItemClickListener) {
        this.mNewsItemClickListener = mNewsItemClickListener;
    }

    NewsItemClickListener mNewsItemClickListener;

      public NewsAdapter(List<JsonData> mJsonDataList, Context mainActivity) {
      JsonList=mJsonDataList;
       mContext=mainActivity;
    }

    @Override
    public NewsAdapter.Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.newitem,parent,false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(final NewsAdapter.Viewholder holder, final int position) {
        mJsonData=JsonList.get(position);
        holder.category.setText(mJsonData.getCategory());
        holder.description.setText(mJsonData.getHeading());
        holder.elaboratedNews.setText(mJsonData.getDescription());
        holder.provider.setText(mJsonData.getProvider());
        //holder.date.setText(mJsonData.getDate().toString());
        //Bitmap bitmap=getBitmApFromUrl(mJsonData.getImageUrl());
        Glide.with(mContext).load(mJsonData.getImageUrl())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontTransform()
                .animate(android.R.animator.fade_in)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        holder.mImage.setImageBitmap(resource);
                        Palette.from(resource).generate(new AsyncJob(holder));
//                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
//                            @Override
//                            public void onGenerated(Palette palette) {
//                                Palette.Swatch vibrant=palette.getVibrantSwatch();
//                                    if (vibrant!=null){
//                                        holder.itemsView.setCardBackgroundColor(vibrant.getRgb());
//                                        holder.description.setTextColor(vibrant.getTitleTextColor());
//                                        holder.category.setTextColor(vibrant.getBodyTextColor());
//                                        holder.elaboratedNews.setTextColor(vibrant.getBodyTextColor());
//                                        holder.provider.setTextColor(vibrant.getBodyTextColor());
//                                    }
//                            }
//                        });
                    }
                });
//        holder.description.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.elaboratedNews.setVisibility(View.VISIBLE);
//            }
//        });
//        Picasso.with(mContext)
//                .load(mJsonData.getImageUrl())
//                .into(new Target() {
//                    @Override
//                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                        Blurry.with(mContext)
//                                .radius(1)
//                                .from(bitmap)
//                                .into(holder.mImage);
//                            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                                @Override
//                                public void onGenerated(Palette palette) {
//                                    Palette.Swatch vibrant=palette.getVibrantSwatch();
//                                    if (vibrant!=null){
//                                        holder.itemsView.setCardBackgroundColor(vibrant.getRgb());
//                                    }
//                                }
//
//                            });
//
//                    }
//
//                    @Override
//                    public void onBitmapFailed(Drawable errorDrawable) {
//
//                    }
//
//                    @Override
//                    public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                    }
//                });
    }

    private Bitmap getBitmApFromUrl(String imageUrl) {
        Bitmap mMap=null;
        try {
            URL url=new URL(imageUrl);
            HttpURLConnection con= (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.connect();
            InputStream input=con.getInputStream();
            mMap=BitmapFactory.decodeStream(input);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mMap;
    }

    @Override
    public int getItemCount() {
        return JsonList.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView description,date,category,elaboratedNews,provider;
        public ImageView mImage;
        public LinearLayout extraContents;
        CardView itemsView;
        public Viewholder(View itemView) {
            super(itemView);
            description= (TextView) itemView.findViewById(R.id.article_heading);
            date= (TextView) itemView.findViewById(R.id.date);
            category= (TextView) itemView.findViewById(R.id.category);
            mImage= (ImageView) itemView.findViewById(R.id.image);
            mImage.setTransitionName("headerImage");
            elaboratedNews= (TextView) itemView.findViewById(R.id.description);
            extraContents= (LinearLayout) itemView.findViewById(R.id.extra_content);
            provider= (TextView) itemView.findViewById(R.id.provider);
            itemsView= (CardView) itemView.findViewById(R.id.cardItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
           JsonData mJSData=JsonList.get(getLayoutPosition());
            if (extraContents.getVisibility()==View.VISIBLE) {
                if (mNewsItemClickListener != null) {
                    mNewsItemClickListener.OnItemClickListener(mJSData, description);
                }
            }
            else {
                extraContents.setVisibility(View.VISIBLE);
            }

        }
    }
}
