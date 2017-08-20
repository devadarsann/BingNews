package com.kannan.devan.bingnews;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by devan on 4/8/17.
 */

public class SearchResultFragment extends Fragment implements ResponseDataListener, NewsItemClickListener {
    TextView mSearchTextView;
    RecyclerView mSearchResultView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_results,container,false);
        mSearchTextView = (TextView) rootView.findViewById(R.id.search_text);
        mSearchResultView = (RecyclerView) rootView.findViewById(R.id.searchresult);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mSearchResultView.setLayoutManager(mLayoutManager);

        String searchQuery = getArguments().getString("query");
        String searchText="Search results for \""+searchQuery+"\"";
        String searchAPI = getArguments().getString("API");
        mSearchTextView.setText(searchText);
        new NetworkConnector(this,getContext(),-1).execute(searchAPI,searchQuery);
        return rootView;
    }

    @Override
    public void Response(String response, int sectionId) {
        try {
            List<JsonData> mJsonArrayList = new ArrayList<>();
            JSONObject mJsonObject = getJsonObject(response);
            String jsonObject = getValue(mJsonObject,"value");
            JSONArray mJsonArray = getJsonArray(jsonObject);
            for (int i = 0;i < mJsonArray.length();i++){
                JsonData mJsonData=new JsonData();
                JSONObject mJobject=mJsonArray.getJSONObject(i);
                if (mJobject.has("name")) {
                    mJsonData.setHeading(mJobject.getString("name"));
                }
                if (mJobject.has("category")){
                    mJsonData.setCategory(mJobject.getString("category"));
                }
                if (mJobject.has("description")){
                    mJsonData.setDescription(mJobject.getString("description"));
                }
                if (mJobject.has("url")){
                    mJsonData.setReadlink(mJobject.getString("url"));
                }
                if (mJobject.has("image")){
                    String imageObj = mJobject.getString("image");
                    JSONObject imageObject = getJsonObject(imageObj);
                    JSONObject thumbnailObject = getJsonObject(imageObject.getString("thumbnail"));
                    if (thumbnailObject.has("contentUrl")){
                        mJsonData.setImageUrl(thumbnailObject.getString("contentUrl"));
                    }
                }
                mJsonArrayList.add(mJsonData);
            }

            NewsAdapter mSearchAdapter=new NewsAdapter(mJsonArrayList,getContext());
            mSearchAdapter.setmNewsItemClickListener(this);
            mSearchResultView.setAdapter(mSearchAdapter);
        }
        catch (Exception ex){
            Log.e("getJsonObject",ex.getMessage());
        }
    }

    @NonNull
    private JSONArray getJsonArray(String jsonObject) throws JSONException {
        return new JSONArray(jsonObject);
    }

    private String getValue(JSONObject mJsonObject, String key) throws JSONException {
        return mJsonObject.getString(key);
    }

    @NonNull
    private JSONObject getJsonObject(String response) throws JSONException {
        return new JSONObject(response);
    }

    @Override
    public void OnItemClickListener(JsonData mJsonData, View view) {
        OpenURLinCustomTab(mJsonData.getReadlink());
    }

    private void OpenURLinCustomTab(String readlink) {
        Uri mUri=Uri.parse(readlink);
        CustomTabsIntent.Builder cstIntentBuilder=new CustomTabsIntent.Builder();
        cstIntentBuilder.setToolbarColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        cstIntentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
        cstIntentBuilder.setShowTitle(true);
        cstIntentBuilder.setStartAnimations(getContext(),android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        cstIntentBuilder.setExitAnimations(getContext(),android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        CustomTabsIntent mCustomTabIntent=cstIntentBuilder.build();
        mCustomTabIntent.launchUrl(getContext(),mUri);
    }
}
