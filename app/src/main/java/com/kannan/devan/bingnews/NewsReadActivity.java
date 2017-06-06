package com.kannan.devan.bingnews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class NewsReadActivity extends AppCompatActivity{

    TextView mTextView,mDescription,mProvider;
    ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_read);
        Toolbar mToolbar= (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (NullPointerException nEx){
            nEx.printStackTrace();
        }
        mTextView= (TextView) findViewById(R.id.article_heading);
        mDescription= (TextView) findViewById(R.id.description);
        mProvider= (TextView) findViewById(R.id.provider);
        mTextView.setText(getIntent().getExtras().getString("heading"));
        mDescription.setText(getIntent().getExtras().getString("description"));
        mProvider.setText(getIntent().getExtras().getString("provider"));
        mImageView= (ImageView) findViewById(R.id.imageView);
        Picasso.with(this)
                .load(getIntent().getExtras().getString("imageUrl"))
                .into(mImageView);
        //new NetworkConnector(NewsReadActivity.this,getApplicationContext()).execute(getIntent().getExtras().getString("readLink"));
        mProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newsreadIntent=new Intent(NewsReadActivity.this,NewsBrowser.class);
                newsreadIntent.putExtra("url",getIntent().getExtras().getString("readLink"));
                startActivity(newsreadIntent);
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent mIntent=new Intent(this,ArticelsActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mIntent);
        }

        return super.onOptionsItemSelected(item);
    }

}
