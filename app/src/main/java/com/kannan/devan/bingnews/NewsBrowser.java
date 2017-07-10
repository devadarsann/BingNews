package com.kannan.devan.bingnews;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class NewsBrowser extends AppCompatActivity {
    WebView mWebView;
    ProgressBar mProgressBar;
    Toolbar mToolbar;
    String pageTitle;

    @Override
    public void onBackPressed() {
        if (mWebView.copyBackForwardList().getCurrentIndex()>0){
            mWebView.goBack();
        }
        else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_news,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_browser);
        mToolbar= (Toolbar) findViewById(R.id.linkbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mWebView= (WebView) findViewById(R.id.newsBrowse);
        mProgressBar= (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setMax(100);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pageTitle=view.getTitle();
                mToolbar.setSubtitle(url);
                mToolbar.setTitle(pageTitle);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mProgressBar.setProgress(newProgress,true);
                }
                else {
                    mProgressBar.setProgress(newProgress);
                }
                if (newProgress==100){
                    mProgressBar.setVisibility(View.GONE);
                }
                mToolbar.setTitle(getIntent().getExtras().getString("url"));
            }
        });
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.loadUrl(getIntent().getExtras().getString("url"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent mIntent=new Intent(NewsBrowser.this,NewsReadActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mIntent);
        }

        return super.onOptionsItemSelected(item);
    }


}
