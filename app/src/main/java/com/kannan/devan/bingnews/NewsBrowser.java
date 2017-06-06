package com.kannan.devan.bingnews;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class NewsBrowser extends AppCompatActivity {
    WebView mWebView;
    ProgressBar mProgressBar;
    Toolbar mToolbar;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_browser);
        mToolbar= (Toolbar) findViewById(R.id.linkbar);
        setSupportActionBar(mToolbar);
        mWebView= (WebView) findViewById(R.id.newsBrowse);
        mProgressBar= (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setMax(100);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
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
            }
        });
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.loadUrl(getIntent().getExtras().getString("url"));
    }
}
