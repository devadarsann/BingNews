package com.kannan.devan.bingnews;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticelsActivity extends AppCompatActivity {

    public final String SHARED_PREFS = "com.kannan.newsbing.APPPREFS";
    public final String LASTREFRESHTIME = "Lastrefreshed";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articels);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Date now = new Date();
    }

    private void UpdateLastRefreshTime(Date now) {
        android.text.format.DateFormat mDateFormat = new android.text.format.DateFormat();
        String refreshTime = mDateFormat.format("dd/MM/yyyy HH:mm:ss", now).toString();
        SharedPreferences mSharedPrefs = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor mPrefsEditor = mSharedPrefs.edit();
        mPrefsEditor.putString(LASTREFRESHTIME, refreshTime);
        mPrefsEditor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_articels, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.app_bar_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements ResponseDataListener, NewsItemClickListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        List<JsonData> mJsonDataList;
        JsonData mJsonData;
        RecyclerView NewsView;
        private static final String ARG_SECTION_NUMBER = "section_number";
        private final String ARTICLES = "/top_articles.json";
        private final String ENTERTAINTMENT = "/entertaintment.json";
        private final String BUSINESS = "/business.json";
        private final String HEALTH = "/health.json";
        private final String POLITICS = "/politics.json";
        private final String SCIANDTECH = "/scienceandtechnology.json";
        private final String SPORTS = "/sports.json";
        private final String WORLD = "/world.json";
        private final String SHARED_PREFS = "com.kannan.newsbing.APPPREFS";
        private final String BING_API = "https://api.cognitive.microsoft.com/bing/v5.0/news/";
        private final String CAT_ENTERTAINTMENT = "?Category=Entertainment";
        private final String CAT_BUSINESS = "?Category=Business";
        private final String CAT_HEALTH = "?Category=Health";
        private final String CAT_POLITICS = "?Category=Politics";
        private final String CAT_SCIANDTECH = "?Category=ScienceAndTechnology";
        private final String CAT_SPORTS = "?Category=Sports";
        private final String CAT_WORLD = "?Category=World";
        RequestQueue mRequestQueue;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_articels, container, false);
            int pager = getArguments().getInt(ARG_SECTION_NUMBER);
            String newsFile = getFileName(pager);
            NewsView= (RecyclerView) rootView.findViewById(R.id.newsview);
            RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getContext());
            NewsView.setLayoutManager(mLayoutManager);
            ReadJsonData(newsFile, pager);
            return rootView;
        }

        private void ReadJsonData(String newsFile, final int pager) {

            try {
                File jsonFile = new File(newsFile);
//                if (!jsonFile.canRead()) {
                    new NetworkConnector(this,getContext(),pager).execute(getNewsApi(pager), String.valueOf(pager));
//                    mRequestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
//                    JsonObjectRequest mJsonRequest=new JsonObjectRequest(Request.Method.GET, getNewsApi(pager), null, new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            SaveDataToFile(response.toString(), pager);
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                        }
//                    })
//                    {
//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            super.getHeaders();
//                            Map<String,String> params=new HashMap<>();
//                            params.put("Ocp-Apim-Subscription-Key","2b983fe0064b416b8b338a973ca81643");
//                            return params;
//                        }
//                    };
//                    mRequestQueue.add(mJsonRequest);

//                }
                    ReadDataFile(jsonFile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void ReadDataFile(File jsonFile) throws IOException, JSONException {
            try {
                String jsonData = getJsonStringFromFile(jsonFile);
                JSONArray mJarray = getJsonArray(jsonData);
                mJsonDataList = new ArrayList<>();
                for (int i = 0; i < mJarray.length(); i++) {
                    mJsonData = new JsonData();
                    JSONObject mOb = mJarray.getJSONObject(i);
                    mJsonData.setHeading(getJsonObjectValue(mOb, "name"));
                    if (mOb.has("category")) {
                        mJsonData.setCategory(getJsonObjectValue(mOb, "category"));
                    }
                    //JSONArray jarray=mOb.getJSONArray("image");
                    mJsonData.setReadlink(mOb.getString("url"));
                    mJsonData.setDescription(mOb.getString("description"));
                    if (mOb.has("image")) {
                        JSONObject job = new JSONObject(getJsonObjectValue(mOb, "image"));
                        JSONObject obj = new JSONObject(job.getString("thumbnail"));
                        mJsonData.setImageUrl(obj.getString("contentUrl"));
                        mJsonData.setWidth(obj.getInt("width"));
                        mJsonData.setHeight(obj.getInt("height"));
                    }
                    if (mOb.has("provider")) {
                        JSONArray providerArray = mOb.getJSONArray("provider");
                        JSONObject providerObject = providerArray.getJSONObject(0);
                        mJsonData.setProvider(providerObject.getString("name"));
                    }
//                    if (mOb.has("about")) {
//                        JSONObject aboutJson = mOb.getJSONArray("about").getJSONObject(0);
//                        mJsonData.setReadlink(aboutJson.getString("readLink"));
//                   }
                    mJsonDataList.add(mJsonData);
                }

                NewsAdapter mAdapter = new NewsAdapter(mJsonDataList, getContext());
                NewsView.setAdapter(mAdapter);
                mAdapter.setmNewsItemClickListener(this);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }

        private String getNewsApi(int pager) {
            switch (pager){
                case 1:
                    return BING_API;
                case 2:
                    return BING_API+CAT_ENTERTAINTMENT;
                case 3:
                    return BING_API+CAT_BUSINESS;
//                case 4:
//                    return BING_API+CAT_HEALTH;
                case 4:
                    return BING_API+CAT_POLITICS;
                case 5:
                    return BING_API+CAT_SCIANDTECH;
                case 6:
                    return BING_API+CAT_SPORTS;
                case 7:
                    return BING_API+CAT_WORLD;
                default:
                    return null;
            }

        }

        private String getJsonObjectValue(JSONObject mJobject, String value) throws JSONException {
            return mJobject.getString(value);
        }

        private JSONArray getJsonArray(String jsonData) throws JSONException {
            return new JSONArray(jsonData);
        }

        private String getFileName(int pager) {
            switch (pager) {
                case 1:
                    return getContext().getFilesDir().getAbsolutePath() + ARTICLES;
                case 2:
                    return getContext().getFilesDir().getAbsolutePath() + ENTERTAINTMENT;
                case 3:
                    return getContext().getFilesDir().getAbsolutePath() + BUSINESS;
//                case 4:
//                    return getContext().getFilesDir().getAbsolutePath() + HEALTH;
                case 4:
                    return getContext().getFilesDir().getAbsolutePath() + POLITICS;
                case 5:
                    return getContext().getFilesDir().getAbsolutePath() + SCIANDTECH;
                case 6:
                    return getContext().getFilesDir().getAbsolutePath() + SPORTS;
                case 7:
                    return getContext().getFilesDir().getAbsolutePath()+WORLD;
                default:
                    return null;
            }

        }

        @Override
        public void Response(String response, int sectionId) {
            SaveDataToFile(response,sectionId);
        }

        private void SaveDataToFile(String response, int sectionId) {
            try{
                JSONObject mJobject=new JSONObject(response);
                String jsonData= getJsonObjectValue(mJobject, "value");
                Writer jsdataWriter=null;
                String Path=getFileName(sectionId);
                File mFile=new File(Path);
                if (!mFile.canWrite()){
                    mFile.createNewFile();
                }
                jsdataWriter=new BufferedWriter(new FileWriter(mFile));
                jsdataWriter.write(jsonData);
                jsdataWriter.close();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }


        @Override
        public void OnItemClickListener(JsonData mJsonData) {
            Intent mIntent=new Intent(getContext(),NewsReadActivity.class);
            //mIntent.putExtra("readLink",ReadLink);
            mIntent.putExtra("readLink",mJsonData.getReadlink());
            mIntent.putExtra("description",mJsonData.getDescription());
            mIntent.putExtra("imageUrl",mJsonData.getImageUrl());
            mIntent.putExtra("provider",mJsonData.getProvider());
            mIntent.putExtra("heading",mJsonData.getHeading());
            startActivity(mIntent);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 8 total pages.
            return 7;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.top_articles);
                case 1:
                    return "Entertaintment";
                case 2:
                    return "Business";
                case 3:
                    return "Politics";
                case 4:
                    return "Science and Technology";
                case 5:
                    return "Sports";
                case 6:
                    return "World";
            }
            return null;
        }
    }


    private static String getJsonStringFromFile(File jsonFile) throws IOException {
        FileInputStream jsonDataStream = new FileInputStream(jsonFile);
        int fileSize = jsonDataStream.available();
        byte[] buffer = new byte[fileSize];
        jsonDataStream.read(buffer);
        jsonDataStream.close();
        return new String(buffer);
    }
}