package com.kannan.devan.bingnews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import jp.wasabeef.blurry.Blurry;

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
    public void onBindViewHolder(final NewsAdapter.Viewholder holder, int position) {
        mJsonData=JsonList.get(position);
        holder.category.setText(mJsonData.getCategory());
        holder.description.setText(mJsonData.getHeading());
        //holder.date.setText(mJsonData.getDate().toString());
        //Bitmap bitmap=getBitmApFromUrl(mJsonData.getImageUrl());
//        Glide.with(mContext).load(mJsonData.getImageUrl())
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .dontTransform()
//                .into(holder.mImage);
        Picasso.with(mContext)
                .load(mJsonData.getImageUrl())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Blurry.with(mContext)
                                .radius(1)
                                .from(bitmap)
                                .into(holder.mImage);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
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
        public TextView description,date,category;
        public ImageView mImage;
        public Viewholder(View itemView) {
            super(itemView);
            description= (TextView) itemView.findViewById(R.id.description);
            date= (TextView) itemView.findViewById(R.id.date);
            category= (TextView) itemView.findViewById(R.id.category);
            mImage= (ImageView) itemView.findViewById(R.id.image);
            mImage.setTransitionName("headerImage");
            Pair<View,String> pair=Pair.create((View)mImage,"holderImage");
            ActivityOptionsCompat activityOptionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext,pair);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
           JsonData mJSData=JsonList.get(getLayoutPosition());
            String readlink=mJSData.getReadlink();
            if (mNewsItemClickListener!=null){
                mNewsItemClickListener.OnItemClickListener(mJSData);
            }
        }
    }
}
