package com.kannan.devan.bingnews;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ResponseDataListener {

    List<JsonData> mJsonDataList;
    JsonData mJsonData;
    RecyclerView NewsView;
    public final String ARTICLES_DIR="/articles/";
    public final String ARTICLES="top_articles.json";
    public final String ENTERTAINTMENT="entertaintment.json";
    public final String BUSINESS="business.json";
    public final String HEALTH="health.json";
    public final String POLITICS="politics.json";
    public final String SCIANDTECH="scienceandtechnology.json";
    public final String SPORTS="sports.json";
    public final String WORLD="world.json";
    public final String SHARED_PREFS="com.kannan.newsbing.APPPREFS";
    public final String LASTREFRESHTIME="Lastrefreshed";
    public final String BING_API="https://api.cognitive.microsoft.com/bing/v5.0/news/";
    public final String CAT_ENTERTAINTMENT="?Category=Entertainment";
    public final String CAT_BUSINESS="?Category=Business";
    public final String CAT_HEALTH="?Category=Health";
    public final String CAT_POLITICS="?Category=Politics";
    public final String CAT_SCIANDTECH="?Category=ScienceAndTechnology";
    public final String CAT_SPORTS="?Category=Sports";
    public final String CAT_WORLD="?Category=World";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NewsView= (RecyclerView) findViewById(R.id.newsviewtmp);
        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(this);
        NewsView.setLayoutManager(mLayoutManager);
        Date now=new Date();
        Date LastUpdateTime=getLastRefreshTime(now);
        long timeDifference=now.getTime()-LastUpdateTime.getTime();
        long diffInSeconds=timeDifference/1000;
        long diffInMinutes=diffInSeconds/60;
        if (diffInMinutes>10) {
            //new NetworkConnector(this).execute(BING_API);
            UpdateLastRefreshTime(now);
        }
        else {
            try {
                ReadJsonData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_articels,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        String url="";
        switch (id){
            case R.id.app_bar_search:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private Date getLastRefreshTime(Date now) {
        SharedPreferences mSharedPrefs=getApplicationContext().getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        Date LastRefreshTime=new Date();
        String LastRefresh=mSharedPrefs.getString(LASTREFRESHTIME,"00/00/0000 00:00:00");
        java.text.SimpleDateFormat simpleDateFormat=new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            LastRefreshTime=simpleDateFormat.parse(LastRefresh);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return LastRefreshTime;
    }

    private void UpdateLastRefreshTime(Date now) {
        android.text.format.DateFormat mDateFormat=new android.text.format.DateFormat();
        String refreshTime= mDateFormat.format("dd/MM/yyyy HH:mm:ss",now).toString();
        SharedPreferences mSharedPrefs=getApplicationContext().getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor mPrefsEditor=mSharedPrefs.edit();
        mPrefsEditor.putString(LASTREFRESHTIME,refreshTime);
        mPrefsEditor.commit();
    }

    @Override
    public void Response(String response, int sectionId) {
        ParseJsonData(response);
    }

    private void ParseJsonData(String response) {
        try {
            JSONObject mJobject=new JSONObject(response);
            String jsonData= getJsonObjectValue(mJobject, "value");
//            Save Data to file
            SaveDataToFile(jsonData);
            ReadJsonData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ReadJsonData() throws JSONException {

        try {
            File jsonFile=new File(getApplicationContext().getFilesDir().getAbsolutePath()+"/"+ARTICLES);
            if (!jsonFile.canRead())
            {
                //new NetworkConnector(this).execute(BING_API);
            }
            else {
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
                    JSONObject job = new JSONObject(getJsonObjectValue(mOb, "image"));
                    JSONObject obj = new JSONObject(job.getString("thumbnail"));
                   mJsonData.setImageUrl(obj.getString("contentUrl"));
                    mJsonData.setWidth(obj.getInt("width"));
                    mJsonData.setHeight(obj.getInt("height"));
                    mJsonDataList.add(mJsonData);
                }
                NewsAdapter mAdapter = new NewsAdapter(mJsonDataList, this);
                NewsView.setAdapter(mAdapter);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    private String getJsonStringFromFile(File jsonFile) throws IOException {
        FileInputStream jsonDataStream=new FileInputStream(jsonFile);
        int fileSize=jsonDataStream.available();
        byte[] buffer=new byte[fileSize];
        jsonDataStream.read(buffer);
        jsonDataStream.close();
        return new String(buffer);
    }

    private void SaveDataToFile(String jsonData) {
        try{
            Writer jsdataWriter=null;
            String Path=getApplicationContext().getFilesDir().getAbsolutePath()+"/"+ARTICLES;
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

    private JSONArray getJsonArray(String jsonData) throws JSONException {
        return new JSONArray(jsonData);
    }

    private String getJsonObjectValue(JSONObject mJobject, String value) throws JSONException {
        return mJobject.getString(value);
    }
}
