package com.kannan.devan.bingnews;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.http.HTTP;

/**
 * Created by devan on 6/3/17.
 */

public class NetworkConnector extends AsyncTask<String,Void,String> {
    ResponseDataListener responseDataListener;
    String URL_API;
    Context mContext;
    ProgressDialog mProgressDialog;
    int SectionId;

    public NetworkConnector(ArticelsActivity.PlaceholderFragment mainActivity,Context context,int pageId) {
        responseDataListener=mainActivity;
        mContext=context;
        SectionId=pageId;
    }

    public NetworkConnector(SearchResultFragment searchResultFragment, Context context, int pageId) {
        responseDataListener=searchResultFragment;
        mContext = context;
        SectionId = pageId;
    }

//    public NetworkConnector(NewsReadActivity newsReadActivity, Context applicationContext) {
//        responseDataListener=newsReadActivity;
//        mContext=applicationContext;
//    }

    @Override
    protected String doInBackground(String... strings) {
        String resp="";
        try {
            URL_API=strings[0];
            String query=strings[1];
            Uri mUrlBuilder= Uri.parse(URL_API);
            if (SectionId==-1){
                mUrlBuilder = mUrlBuilder.buildUpon()
                        .appendQueryParameter("q",query)
                        .appendQueryParameter("offset","0")
                        .appendQueryParameter("safeSearch","Strict")
                        .build();
//                mHttpconnection.setRequestProperty("q",query);
//                mHttpconnection.setRequestProperty("count","20");
//                mHttpconnection.setRequestProperty("offset","0");
//                mHttpconnection.setRequestProperty("safeSearch","Strict");
            }
            URL mUrl=new URL(mUrlBuilder.toString()); //URL("https://api.cognitive.microsoft.com/bing/v5.0/news/");
            HttpURLConnection mHttpconnection= (HttpURLConnection) mUrl.openConnection();
            mHttpconnection.setRequestProperty("Ocp-Apim-Subscription-Key","2b983fe0064b416b8b338a973ca81643");

            int statusCode=mHttpconnection.getResponseCode();
            BufferedReader in;
            if (statusCode>=400){
                in = new BufferedReader(new InputStreamReader(mHttpconnection.getErrorStream()));
            }
            else {
                in = new BufferedReader(new InputStreamReader(mHttpconnection.getInputStream()));
            }
            String inputlIne;
            StringBuffer response=new StringBuffer();
            while ((inputlIne=in.readLine())!=null){
                response.append(inputlIne);
            }

            in.close();

            resp=response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog=new ProgressDialog(mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(true);
        //mProgressDialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mProgressDialog.cancel();
        responseDataListener.Response(s,SectionId);
    }

}
